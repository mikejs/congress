package com.sunlightlabs.congress.java.service;

import java.util.ArrayList;

import com.sunlightlabs.congress.java.Bill;
import com.sunlightlabs.congress.java.CongressException;

public interface BillService {
	Bill billFor(String url) throws CongressException;

	ArrayList<Bill> billsFor(String url) throws CongressException;

	Bill find(String id, String sections) throws CongressException;

	ArrayList<Bill> recentlyIntroduced(int n, int p) throws CongressException;

	ArrayList<Bill> recentLaws(int n, int p) throws CongressException;

	ArrayList<Bill> recentlySponsored(int n, String sponsor_id, int p) throws CongressException;

	ArrayList<Bill> latestVotes(int n, int p) throws CongressException;
}
