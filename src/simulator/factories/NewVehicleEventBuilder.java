package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;
import simulator.model.Weather;

public class NewVehicleEventBuilder extends Builder<Event> { //TODO NO PASA EL TEST, (puede que el fallo no esté en esta clase)

	public NewVehicleEventBuilder() {
		super("new_vehicle", "Create a NewVehicleEvent object");
	}

	@Override
	protected Event create_instance(JSONObject data) {

		int time = data.getInt("time");
		String id = data.getString("id");
		int maxSpeed = data.getInt("maxspeed");
		int contClass = data.getInt("class");
		
		JSONArray itineraryJa = data.getJSONArray("itinerary");
		List<String> itineraryStr = new ArrayList<>();
		
		for (int i = 0; i < itineraryJa.length(); i++) { //TODO esta bien hecho??
			
			String jStr = itineraryJa.getString(i);
			itineraryStr.add(jStr);
		}
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itineraryStr);
	}

	@Override
	protected void fill_in_data(JSONObject o) {
		
		o.put("time", 1);
		o.put("id", "v1");
		o.put("maxspeed", 100);
		o.put("class", 3);
		o.put("itinerary", "[\"j3\", \"j1\", ...]"); //TODO JSONArray?
	}
}
