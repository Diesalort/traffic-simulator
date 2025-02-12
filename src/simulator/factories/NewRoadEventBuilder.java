package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.NewRoadEvent;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> { //TODO repasar esta clase y la herencia de las clases hijas

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
		
		return this.newInstance(time, id, src, dest, length, co2Limit, maxSpeed, w);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		
		o.put("time", "The time at which the event is executed");
		o.put("id", "The road's ID");
		o.put("src", "The road's source junction");
		o.put("dest", "The road's destiny junction");
		o.put("length", "The road's length");
		o.put("co2limit", "The road's co2 limit");
		o.put("maxspeed", "The max speed allowed on the road");
		o.put("weather", "The actual road's weather");
	}
	
	//TODO repasar este metodo creado por mi
	protected abstract NewRoadEvent newInstance(int time, String id, String src, String dest, int length, int co2Limit, int maxSpeed, Weather w);
	

}
