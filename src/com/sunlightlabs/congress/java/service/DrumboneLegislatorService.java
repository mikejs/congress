package com.sunlightlabs.congress.java.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.sunlightlabs.congress.java.CongressException;
import com.sunlightlabs.congress.java.Legislator;

public class DrumboneLegislatorService extends SunlightLegislatorService implements
		LegislatorService {

	@Override
	public Legislator newLegislator(JSONObject json) throws CongressException {
		Legislator legislator = super.newLegislator(json);
		try {
			legislator.first_name = json.isNull("first_name") ? null : json.getString("first_name");
			legislator.last_name = json.isNull("last_name") ? null : json.getString("last_name");
		} catch (JSONException e) {
			throw new CongressException(e, "Could not parse Legislator json from Drumbone.");
		}
		return legislator;
	}

}
