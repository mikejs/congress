package com.sunlightlabs.android.congress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.sunlightlabs.android.congress.utils.Utils;
import com.sunlightlabs.congress.java.CongressException;
import com.sunlightlabs.congress.java.Legislator;
import com.sunlightlabs.congress.java.service.ApplicationFacade;
import com.sunlightlabs.congress.java.service.LegislatorService;

public class LegislatorLoader extends Activity {
	private LoadLegislatorTask loadLegislatorTask = null;
	
	private LegislatorService legislatorService = ApplicationFacade.defaultLegislatorService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras.containsKey("legislatorService")) {
			Object service = extras.get("legislatorService");
			if (service instanceof LegislatorService)
				legislatorService = (LegislatorService) service;
		}

		Utils.setupSunlight(this);
		
		String legislator_id = getIntent().getStringExtra("legislator_id");
        
        loadLegislatorTask = (LoadLegislatorTask) getLastNonConfigurationInstance();
        if (loadLegislatorTask != null)
        	loadLegislatorTask.onScreenLoad(this);
        else
        	loadLegislatorTask = (LoadLegislatorTask) new LoadLegislatorTask(this).execute(legislator_id);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return loadLegislatorTask;
	}
	
	public void onLoadLegislator(Legislator legislator) {
		if (legislator != null)
			startActivity(Utils.legislatorIntent(this, legislator));
		else
			Utils.alert(LegislatorLoader.this, R.string.error_connection);
		
		finish();
	}
	
	private class LoadLegislatorTask extends AsyncTask<String,Void,Legislator> {
		public LegislatorLoader context;
		private ProgressDialog dialog = null;
    	
    	public LoadLegislatorTask(LegislatorLoader context) {
    		super();
    		this.context = context;
    	}
		
    	@Override
    	protected void onPreExecute() {
    		loadingDialog();
    	}
    	
    	public void onScreenLoad(LegislatorLoader context) {
    		this.context = context;
    		loadingDialog();
    	}
    	
    	@Override
    	protected Legislator doInBackground(String... ids) {
    		try {
				return legislatorService.find(ids[0]);
			} catch(CongressException exception) {
				return null;
			}
    	}
    	
    	@Override
    	protected void onPostExecute(Legislator legislator) {
    		if (dialog != null && dialog.isShowing())
        		dialog.dismiss();
    		
    		context.onLoadLegislator(legislator);
    		context.loadLegislatorTask = null;
    	}
    	
    	private void loadingDialog() {
    		dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading legislator...");
            
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancel(true);
					context.finish();
				}
			});
            
    		dialog.show();
    	}
    }
}
