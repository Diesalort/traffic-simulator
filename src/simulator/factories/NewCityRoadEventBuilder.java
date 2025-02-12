package simulator.factories;

import simulator.model.NewCityRoadEvent;
import simulator.model.NewRoadEvent;
import simulator.model.Weather;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewCityRoadEventBuilder() {
		super("new_city_road", "Create a NewCityRoadEvent object");
	}

	@Override
	protected NewRoadEvent newInstance(int time, String id, String src, String dest, int length, int co2Limit, int maxSpeed, Weather w) {
		
		return new NewCityRoadEvent(time, id, src, dest, length, co2Limit, maxSpeed, w);
	}
}
