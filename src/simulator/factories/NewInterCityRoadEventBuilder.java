package simulator.factories;

import simulator.model.NewInterCityRoadEvent;
import simulator.model.NewRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder {

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road", "A new interCityRoad");
	}

	@Override
	protected NewRoadEvent newInstance(int time, String id, String src, String dest, int length, int co2Limit,
			int maxSpeed, Weather w) {

		return new NewInterCityRoadEvent(time, id, src, dest, length, co2Limit, maxSpeed, w);
	}

}
