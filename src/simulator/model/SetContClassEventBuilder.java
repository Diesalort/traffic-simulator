package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.factories.Builder;
import simulator.misc.Pair;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class", "Create a SetContClassEvent object");
	}

	@Override
	protected Event create_instance(JSONObject data) {

		int time = data.getInt("time");
		JSONArray info = data.getJSONArray("info");
		
		List<Pair<String,Integer>> cs = new ArrayList<>();
		
		for (int i = 0; i < info.length(); i++) { //TODO esta bien hecho??
			
			JSONObject jo = info.getJSONObject(i);
			String vehicle = jo.getString("vehicle");
			int contClass = jo.getInt("class");
			
			cs.add(new Pair<>(vehicle, contClass));
		}
		
		return new SetContClassEvent(time, cs);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		
		o.put("time", 10);
		o.put("info", "[ { \"vehicle\" : v1, \"class\": 3 },\r\n" //TODO
				+ "      { \"vehicle\" : v4, \"class\": 2 },\r\n"
				+ "      ...\r\n"
				+ "    ]");	

	}

}
