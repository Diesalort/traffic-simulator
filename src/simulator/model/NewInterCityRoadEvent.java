package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	protected Road newInstance(Junction srcJunc, Junction destJunc) {

		return new InterCityRoad(_id, srcJunc, destJunc, _maxSpeed, _co2Limit, _length, _weather);
	}
	
	@Override
	public String toString() {
		
		return "New InterCityRoad '" + _id + "'";
	}
}
