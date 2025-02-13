package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {
	
	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		  super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);	  
	}

	@Override
	void execute(RoadMap map) {

		Junction srcJunc = map.getJunction(this._srcJunc);
		if (srcJunc == null)
			throw new IllegalArgumentException("The source junction is not on the road map");
		
		Junction destJunc = map.getJunction(this._destJunc);
		if (destJunc == null)
			throw new IllegalArgumentException("The destiny junction is not on the road map");
		
		InterCityRoad r = new InterCityRoad(_id, srcJunc, destJunc, _maxSpeed, _co2Limit, _length, _weather);
		
		map.addRoad(r);
	}
	

}
