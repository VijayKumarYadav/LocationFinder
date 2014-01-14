LocationFinder
==============

Can be used to find current location. 

Usage//

LocationFinder locFinder = new LocationFinder(this);
locFinder.setListener(this);
locFinder.getCurrentLocation();


Location will be return in 

@Override
	public void FindingLocationComplete(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void FindingLocationTimeOut() {
		// TODO Auto-generated method stub
		
	}
