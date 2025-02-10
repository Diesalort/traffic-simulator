package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewRoadEventBuilder extends Builder<Event> { //TODO repasar esta clase y la herencia de las clases hijas

	public NewRoadEventBuilder(String typeTag, String desc) {
		super(typeTag, desc);
	}
	
	@Override
	protected Event create_instance(JSONObject data) {

		int time = data.getInt("time");
		String id = data.getString("id");
		String src = data.getString("src");
		String dest = data.getString("dest");
		int length = data.getInt("length");
		int co2Limit = data.getInt("co2limit");
		int maxSpeed = data.getInt("maxspeed");
		
		String ws = data.getString("weather"); //Pongo String porque se han guardado como String	
		Weather w = Weather.valueOf(ws.toUpperCase()); //convertimos el String ws al enum Weather
		
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2Limit, maxSpeed, w);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		
		o.put("time", 1);
		o.put("id", "r1");
		o.put("src", "j1");
		o.put("dest", "j2");
		o.put("length", 10000);
		o.put("co2limit", 500);
		o.put("maxspeed", 120);
		o.put("weather", Weather.SUNNY.toString());
	}

}
