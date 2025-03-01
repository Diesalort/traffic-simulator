package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather", "A new SetWeatherEvent");
	}

	@Override
	protected Event create_instance(JSONObject data) {

		int time = data.getInt("time");
		JSONArray info = data.getJSONArray("info");

		List<Pair<String, Weather>> ws = new ArrayList<>();

		for (int i = 0; i < info.length(); i++) {

			JSONObject jo = info.getJSONObject(i);
			String road = jo.getString("road");
			String wStr = jo.getString("weather");

			ws.add(new Pair<>(road, Weather.valueOf(wStr.toUpperCase()))); // Añadimos el nuevo par a la lista
		}

		return new SetWeatherEvent(time, ws);
	}

	@Override
	protected void fill_in_data(JSONObject o) {

		o.put("time", "The time at which the event is executed");
		o.put("info", "A list of roads with their IDs and their weathers");
	}
}
