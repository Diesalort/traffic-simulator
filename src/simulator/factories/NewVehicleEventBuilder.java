package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle", "A new vehicle");
	}

	@Override
	protected Event create_instance(JSONObject data) {

		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");

		JSONArray itineraryJa = data.getJSONArray("itinerary");
		List<String> itineraryStr = new ArrayList<>();

		for (int i = 0; i < itineraryJa.length(); i++) {

			String junctionId = itineraryJa.getString(i);
			itineraryStr.add(junctionId);
		}

		return new NewVehicleEvent(time, id, maxSpeed, contClass, itineraryStr);
	}

	@Override
	protected void fill_in_data(JSONObject o) {

		o.put("time", "The time at which the event is executed");
		o.put("id", "The vehicle's ID");
		o.put("maxspeed", "The vehicle's max speed");
		o.put("class", "The vehicle's contamination class");
		o.put("itinerary", "The IDs of the junctions which the vehicle must pass");
	}
}
