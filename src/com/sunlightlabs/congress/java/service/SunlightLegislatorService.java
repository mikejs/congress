package com.sunlightlabs.congress.java.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sunlightlabs.congress.java.CongressException;
import com.sunlightlabs.congress.java.Legislator;
import com.sunlightlabs.congress.java.Sunlight;

public class SunlightLegislatorService implements LegislatorService {

	public Legislator newLegislator(JSONObject json) throws CongressException {
		Legislator legislator = new Legislator();

		try {
			legislator.bioguide_id = json.isNull("bioguide_id") ? null : json.getString("bioguide_id");
			legislator.govtrack_id = json.isNull("govtrack_id") ? null : json.getString("govtrack_id");
	
			legislator.first_name = json.isNull("firstname") ? null : json.getString("firstname");
			legislator.last_name = json.isNull("lastname") ? null : json.getString("lastname");
			legislator.nickname = json.isNull("nickname") ? null : json.getString("nickname");
			legislator.name_suffix = json.isNull("name_suffix") ? null : json.getString("name_suffix");
			legislator.title = json.isNull("title") ? null : json.getString("title");
			legislator.party = json.isNull("party") ? null : json.getString("party");
			legislator.state = json.isNull("state") ? null : json.getString("state");
			legislator.district = json.isNull("district") ? null : json.getString("district");
	
			legislator.gender = json.isNull("gender") ? null : json.getString("gender");
			legislator.congress_office = json.isNull("congress_office") ? null : json
					.getString("congress_office");
			legislator.website = json.isNull("website") ? null : json.getString("website");
			legislator.phone = json.isNull("phone") ? null : json.getString("phone");
			legislator.youtube_url = json.isNull("youtube_url") ? null : json.getString("youtube_url");
			legislator.twitter_id = json.isNull("twitter_id") ? null : json.getString("twitter_id");
		} catch(JSONException e) {
			throw new CongressException(e, "Could not parse Legislator json from Sunlight.");
		}

		return legislator;
	}

	public ArrayList<Legislator> allWhere(String key, String value) throws CongressException {
		return legislatorsFor(Sunlight.url("legislators.getList", key + "=" + value));
	}

	public ArrayList<Legislator> allForZipCode(String zip) throws CongressException {
		return legislatorsFor(Sunlight.url("legislators.allForZip", "zip=" + zip));
	}

	public ArrayList<Legislator> allForLatLong(double latitude, double longitude)
			throws CongressException {
		return legislatorsFor(Sunlight.url("legislators.allForLatLong", "latitude=" + latitude
				+ "&longitude=" + longitude));
	}

	public Legislator find(String bioguide_id) throws CongressException {
		return legislatorFor(Sunlight.url("legislators.get", "bioguide_id=" + bioguide_id));
	}

	public Legislator legislatorFor(String url) throws CongressException {
		String rawJSON = Sunlight.fetchJSON(url);
		try {
			return newLegislator(new JSONObject(rawJSON).getJSONObject("response").getJSONObject(
					"legislator"));
		} catch (JSONException e) {
			throw new CongressException(e, "Problem parsing the JSON from " + url);
		}
	}

	public ArrayList<Legislator> legislatorsFor(String url) throws CongressException {
		String rawJSON = Sunlight.fetchJSON(url);
		ArrayList<Legislator> legislators = new ArrayList<Legislator>();
		try {
			JSONArray results = new JSONObject(rawJSON).getJSONObject("response").getJSONArray(
					"legislators");

			int length = results.length();
			for (int i = 0; i < length; i++)
				legislators
						.add(newLegislator(results.getJSONObject(i).getJSONObject("legislator")));

		} catch (JSONException e) {
			throw new CongressException(e, "Problem parsing the JSON from " + url);
		}

		return legislators;
	}
}
