package com.sunlightlabs.congress.java.service;

import java.util.ArrayList;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sunlightlabs.congress.java.Bill;
import com.sunlightlabs.congress.java.CongressException;
import com.sunlightlabs.congress.java.Drumbone;

public class DrumboneBillService implements BillService {
	private LegislatorService legislatorService = ApplicationFacade.drumboneLegislatorService;

	public Bill newBill(JSONObject json) throws CongressException {
		Bill bill = new Bill();
		String[] df = Drumbone.dateFormat;
		
		try { 
			bill.id = json.isNull("bill_id") ? null : json.getString("bill_id");
			bill.code = json.isNull("code") ? null : json.getString("code");
			bill.type = json.isNull("type") ? null : json.getString("type");
			bill.state = json.isNull("state") ? null : json.getString("state");
			bill.chamber = json.isNull("chamber") ? null : json.getString("chamber");
			bill.session = json.isNull("session") ? null : json.getInt("session");
			bill.number = json.isNull("number") ? null : json.getInt("number");
			
			bill.short_title = json.isNull("short_title") ? null : json.getString("short_title");
			bill.official_title = json.isNull("official_title") ? null : json.getString("official_title");
			bill.last_action_at = json.isNull("last_action_at") ? null : DateUtils.parseDate(json.getString("last_action_at"), df);
			bill.last_vote_at = json.isNull("last_vote_at") ? null : DateUtils.parseDate(json.getString("last_vote_at"), df);
			
			bill.introduced_at = json.isNull("introduced_at") ? null : DateUtils.parseDate(json.getString("introduced_at"), df);
			bill.house_result_at = json.isNull("house_result_at") ? null : DateUtils.parseDate(json.getString("house_result_at"), df);
			bill.senate_result_at = json.isNull("senate_result_at") ? null : DateUtils.parseDate(json.getString("senate_result_at"), df);
			bill.passed_at = json.isNull("passed_at") ? null : DateUtils.parseDate(json.getString("passed_at"), df);
			bill.vetoed_at = json.isNull("vetoed_at") ? null : DateUtils.parseDate(json.getString("vetoed_at"), df);
			bill.override_house_result_at = json.isNull("override_house_result_at") ? null : DateUtils.parseDate(json.getString("override_house_result_at"), df);
			bill.override_senate_result_at = json.isNull("override_senate_result_at") ? null : DateUtils.parseDate(json.getString("override_senate_result_at"), df);
			bill.awaiting_signature_since = json.isNull("awaiting_signature_since") ? null : DateUtils.parseDate(json.getString("awaiting_signature_since"), df);
			bill.enacted_at = json.isNull("enacted_at") ? null : DateUtils.parseDate(json.getString("enacted_at"), df);
			
			// timeline flags and values
			bill.house_result = json.isNull("house_result") ? null : json.getString("house_result");
			bill.senate_result = json.isNull("senate_result") ? null : json.getString("senate_result");
			bill.passed = json.isNull("passed") ? null : json.getBoolean("passed");
			bill.vetoed = json.isNull("vetoed") ? null : json.getBoolean("vetoed");
			bill.override_house_result = json.isNull("override_house_result") ? null : json.getString("override_house_result");
			bill.override_senate_result = json.isNull("override_senate_result") ? null : json.getString("override_senate_result");
			bill.awaiting_signature = json.isNull("awaiting_signature") ? null : json.getBoolean("awaiting_signature");
			bill.enacted = json.isNull("enacted") ? null : json.getBoolean("enacted");
			
			bill.sponsor = json.isNull("sponsor") ? null : legislatorService.newLegislator(json.getJSONObject("sponsor"));
			
			bill.summary = json.isNull("summary") ? null : json.getString("summary");

			if (!json.isNull("votes"))
				deserializeLatestVote(json.getJSONArray("votes"), bill);
		} catch (Exception e) {
			throw new CongressException(e, "Could not parse the Bill json from Drumbone.");
		}
		return bill;
	}
	
	private void deserializeLatestVote(JSONArray votes, Bill bill) throws DateParseException,
			JSONException {
		int length = votes.length();

		for (int i = 0; i < length; i++) {
			JSONObject vote = votes.getJSONObject(i);
			// find the last vote (same date and year as the 'last_vote_at')
			if (DateUtils.parseDate(vote.getString("voted_at"), Drumbone.dateFormat).equals(
					bill.last_vote_at)) {
				bill.last_vote_chamber = vote.getString("chamber");
				bill.last_vote_result = vote.getString("result");
				break;
			}
		}
	}

	public ArrayList<Bill> recentlyIntroduced(int n, int p) throws CongressException {
		return billsFor(Drumbone.url("bills",
				"order=introduced_at&sections=basic,sponsor&per_page=" + n + "&page=" + p));
	}

	public ArrayList<Bill> recentLaws(int n, int p) throws CongressException {
		return billsFor(Drumbone
				.url("bills", "order=enacted_at&enacted=true&sections=basic,sponsor&per_page=" + n
						+ "&page=" + p));
	}

	public ArrayList<Bill> recentlySponsored(int n, String sponsor_id, int p)
			throws CongressException {
		return billsFor(Drumbone.url("bills", "order=introduced_at&sponsor_id=" + sponsor_id
				+ "&sections=basic,sponsor&per_page=" + n + "&page=" + p));
	}

	public ArrayList<Bill> latestVotes(int n, int p) throws CongressException {
		return billsFor(Drumbone.url("bills",
				"order=last_vote_at&sections=basic,sponsor,votes&per_page=" + n + "&page=" + p));
	}

	public Bill find(String id, String sections) throws CongressException {
		return billFor(Drumbone.url("bill", "bill_id=" + id + "&sections=" + sections));
	}

	public Bill billFor(String url) throws CongressException {
		String rawJSON = Drumbone.fetchJSON(url);
		try {
			return newBill(new JSONObject(rawJSON).getJSONObject("bill"));
		} catch (Exception e) {
			throw new CongressException(e, "Problem parsing the JSON from " + url);
		}
	}

	public ArrayList<Bill> billsFor(String url) throws CongressException {
		String rawJSON = Drumbone.fetchJSON(url);
		ArrayList<Bill> bills = new ArrayList<Bill>();
		try {
			JSONArray results = new JSONObject(rawJSON).getJSONArray("bills");

			int length = results.length();
			for (int i = 0; i < length; i++)
				bills.add(newBill(results.getJSONObject(i)));

		} catch (Exception e) {
			Log.i("BillList", "==============Received exception2 " + e);
			throw new CongressException(e, "Problem parsing the JSON from " + url);
		}
		return bills;
	}
}
