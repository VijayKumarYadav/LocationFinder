/**
 * 
 */
package com.example.locationfinder;

import java.util.Calendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author VijayK  GetLastLocation.java
 */

public class LocationFinder {

	Activity activity;
	private LocationManager locManager;
	private ProgressDialog dialog;
	private LocationListener locListener = new MyLocationListener();

	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	boolean timeOut = false;

	/** The completion listener. */
	private OnCompletionListener completionListener;

	/**
	 * The listener interface for receiving onCompletion events. The class that
	 * is interested in processing a onCompletion event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addOnCompletionListener<code> method. When
	 * the onCompletion event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnCompletionEvent
	 */
	public interface OnCompletionListener {
		/**
		 * Finding Location Complete
		 * 
		 * @param location
		 */
		public void FindingLocationComplete(Location location);

		/**
		 * Finding Location Complete
		 * 
		 * @param location
		 */
		public void FindingLocationTimeOut();
	}

	/**
	 * Sets the listener.
	 * 
	 * @param listener
	 *            the new listener
	 */
	public void setListener(OnCompletionListener listener) {
		this.completionListener = listener;
	}

	public LocationFinder(Activity _activity) {
		this.activity = _activity;
		locManager = (LocationManager) activity
		.getSystemService(Context.LOCATION_SERVICE);
	}

	public void getCurrentLocation() {
		dialog = ProgressDialog.show(activity, "", activity.getResources()
				.getString(R.string.waitLocation), true);
		dialog.setOnDismissListener(dismissListener);
		dialog.setCancelable(true);
		try {
			gps_enabled = locManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			// do something
		}
		if (gps_enabled) {
			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					5000, 10, locListener);
		}else {
			Toast.makeText(activity, activity.getResources().getString(
					R.string.enable_location_service), Toast.LENGTH_LONG).show();
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
		}

		try {
			final Handler mHandler = new Handler(Looper.getMainLooper());
			new Thread() {
				public void run() {
					Long t = Calendar.getInstance().getTimeInMillis();
					timeOut = true;
					while (Calendar.getInstance().getTimeInMillis() - t < 120000) {
						// If The user finish the activity
						if (activity.isFinishing()) {
							locManager.removeUpdates(locListener);
							if (dialog != null && dialog.isShowing())
								dialog.dismiss();
							timeOut = false;
						}
					}
					;
					// if the user doesnt finish the activity and time out.
					if (timeOut)
						mHandler.post(postOperationResult());
				}
			}.start();
		} catch (Throwable e) {
			// do something
		}
	}

	protected Runnable postOperationResult() {

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				locManager.removeUpdates(locListener);
				if (!activity.isFinishing())
					/*
					 * Commons.showErroralert(activity, activity.getResources()
					 * .getString(R.string.cant_connect_gps));
					 */
					//completionListener.FindingLocationTimeOut();
					startNetworkProvider();
			}
		};
		return runnable;
	}

	class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the
				// battery power.
				locManager.removeUpdates(locListener);
				timeOut = false;
				if (dialog != null && dialog.isShowing())
					dialog.dismiss();
				completionListener.FindingLocationComplete(location);
			} 
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}

	OnDismissListener dismissListener = new OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {
			locManager.removeUpdates(locListener);
			timeOut = false;
		}
	};

	public void startNetworkProvider() {
		try {
			network_enabled = locManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			// do something
		}
		if (network_enabled) {
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					5000, 10, locListener);
		}else {
			completionListener.FindingLocationTimeOut();
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
		}

	}
	
	public void removeUpdates() {
		locManager.removeUpdates(locListener);
		timeOut = false;
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();

	}
}