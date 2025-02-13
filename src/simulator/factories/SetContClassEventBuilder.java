package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class", "A new SetContClassEvent");
	}

	@Override
	protected Event create_instance(JSONObject data) {

		int time = data.getInt("time");
		JSONArray info = data.getJSONArray("info");

		List<Pair<String, Integer>> cs = new ArrayList<>();

		for (int i = 0; i < info.length(); i++) { // TODO esta bien hecho??

			JSONObject jo = info.getJSONObject(i);
			String vehicle = jo.getString("vehicle");
			int contClass = jo.getInt("class");

			cs.add(new Pair<>(vehicle, contClass));
		}

		return new SetContClassEvent(time, cs);
	}

	@Override
	protected void fill_in_data(JSONObject o) {

		o.put("time", "The time at which the event is executed");
		o.put("info", "A list of vehicles with their IDs and their contamination classes");

	}

}
