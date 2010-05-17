package com.sunlightlabs.congress.java;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bill {
	
	// basic
	public String id, code, type, state, chamber;
	public int session, number;
	public String short_title, official_title;
	public Date last_action_at, last_vote_at;
	
	public Date introduced_at, house_result_at, senate_result_at, passed_at;
	public Date vetoed_at, override_house_result_at, override_senate_result_at;
	public Date awaiting_signature_since, enacted_at;
	
	public boolean passed, vetoed, awaiting_signature, enacted;
	public String house_result, senate_result, override_house_result, override_senate_result;
	
	// sponsor
	public Legislator sponsor;
	
	// summary
	public String summary;
	
	// votes
	public String last_vote_result, last_vote_chamber;
				
	public static String formatCode(String code) {
		Pattern pattern = Pattern.compile("^([a-z]+)(\\d+)$");
		Matcher matcher = pattern.matcher(code);
		if (!matcher.matches())
			return code;
		
		String match = matcher.group(1);
		String number = matcher.group(2);
		if (match.equals("hr"))
			return "H.R. " + number;
		else if (match.equals("hres"))
			return "H. Res. " + number;
		else if (match.equals("hjres"))
			return "H. Joint Res. " + number;
		else if (match.equals("hcres"))
			return "H. Con. Res. " + number;
		else if (match.equals("s"))
			return "S. " + number;
		else if (match.equals("sres"))
			return "S. Res. " + number;
		else if (match.equals("sjres"))
			return "S. Joint Res. " + number;
		else if (match.equals("scres"))
			return "S. Con. Res. " + number;
		else
			return code;
	}
	
	// for when you need that extra space
	public static String formatCodeShort(String code) {
		Pattern pattern = Pattern.compile("^([a-z]+)(\\d+)$");
		Matcher matcher = pattern.matcher(code);
		if (!matcher.matches())
			return code;
		
		String match = matcher.group(1);
		String number = matcher.group(2);
		if (match.equals("hr"))
			return "H.R. " + number;
		else if (match.equals("hres"))
			return "H. Res. " + number;
		else if (match.equals("hjres"))
			return "H.J. Res. " + number;
		else if (match.equals("hcres"))
			return "H.C. Res. " + number;
		else if (match.equals("s"))
			return "S. " + number;
		else if (match.equals("sres"))
			return "S. Res. " + number;
		else if (match.equals("sjres"))
			return "S.J. Res. " + number;
		else if (match.equals("scres"))
			return "S.C. Res. " + number;
		else
			return code;
	}
	
	public static String govTrackType(String type) {
		if (type.equals("hr"))
			return "h";
		else if (type.equals("hres"))
			return "hr";
		else if (type.equals("hjres"))
			return "hj";
		else if (type.equals("hcres"))
			return "hc";
		else if (type.equals("s"))
			return "s";
		else if (type.equals("sres"))
			return "sr";
		else if (type.equals("sjres"))
			return "sj";
		else if (type.equals("scres"))
			return "sc";
		else
			return type;
	}
	
	public static String thomasUrl(Bill bill) {
		return "http://thomas.loc.gov/cgi-bin/query/z?c" + bill.session + ":" + bill.type + bill.number + ":";
	}
	
	public static String openCongressUrl(Bill bill) {
		return "http://www.opencongress.org/bill/" + bill.session + "-" + govTrackType(bill.type) + bill.number + "/show";
	}
	
	public static String govTrackUrl(Bill bill) {
		return "http://www.govtrack.us/congress/bill.xpd?bill=" + govTrackType(bill.type) + bill.session + "-" + bill.number;
	}
	
	public static String formatSummary(String summary, String short_title) {
		String formatted = summary;
		formatted = formatted.replaceFirst("^\\d+\\/\\d+\\/\\d+--.+?\\.\\s*", "");
		formatted = formatted.replaceFirst("(\\(This measure.+?\\))\n*\\s*", "");
		if (short_title != null)
			formatted = formatted.replaceFirst("^" + short_title + " - ", "");
		formatted = formatted.replaceAll("\n", "\n\n");
		formatted = formatted.replaceAll(" (\\(\\d\\))", "\n\n$1");
		formatted = formatted.replaceAll("( [^A-Z\\s]+\\.)\\s+", "$1\n\n");
		return formatted;
	}
	
	public static String displayTitle(Bill bill) {
		return (bill.short_title != null) ? bill.short_title : bill.official_title; 
	}

	
}