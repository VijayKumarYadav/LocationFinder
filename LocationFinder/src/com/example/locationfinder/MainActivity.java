package com.example.locationfinder;

import com.example.locationfinder.LocationFinder.OnCompletionListener;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity implements OnCompletionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// usage
		LocationFinder locFinder = new LocationFinder(this);
		locFinder.setListener(this);
		locFinder.getCurrentLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void FindingLocationComplete(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void FindingLocationTimeOut() {
		// TODO Auto-generated method stub
		
	}

}
