package com.sunlightlabs.congress.java.service;

public class ApplicationFacade {
	public static final LegislatorService defaultLegislatorService = new SunlightLegislatorService();
	public static final LegislatorService drumboneLegislatorService = new DrumboneLegislatorService();

	public static final BillService defaultBillService = new DrumboneBillService();

	private ApplicationFacade() {}
}
