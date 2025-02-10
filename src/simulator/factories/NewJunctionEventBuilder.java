package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	
	private Factory<LightSwitchingStrategy> _lssFactory;
	private Factory<DequeuingStrategy> _dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction", "Create a NewJunctionEvent object");
		this._lssFactory = lssFactory;
		this._dqsFactory = dqsFactory;
	}

	@Override
	protected Event create_instance(JSONObject data) { //TODO
		
		int time = data.getInt("time");
		String id = data.getString("id");
		JSONArray coor = data.getJSONArray("coor");
		JSONObject ls_strategyJSON = data.getJSONObject("ls_strategy");
		JSONObject dq_strategyJSON = data.getJSONObject("dq_strategy");
		
		LightSwitchingStrategy lss = this._lssFactory.create_instance(ls_strategyJSON);
		DequeuingStrategy dqs = this._dqsFactory.create_instance(dq_strategyJSON);
		
		return new NewJunctionEvent(time, id,  lss, dqs, coor.getInt(0), coor.getInt(1));
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		
		o.put("time", 1);
		o.put("id", "j1");
		o.put("coor", "[100,200]"); //TODO JSONArray?
		o.put("ls_strategy", new JSONObject()); //TODO new JSONObject.put("type", ....) ??
		o.put("dq_strategy", new JSONObject());
	}
}

