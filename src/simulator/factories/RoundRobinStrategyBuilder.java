package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss", "Create a RoundRobinStrategy object");
	}

	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {

		int timeSlot = data.optInt("timeslot", 1);

		return new RoundRobinStrategy(timeSlot);
	}

	@Override
	protected void fill_in_data(JSONObject o) {

		o.put("timeslot", "Consecutive ticks during which the road can have the green traffic light");
	}

}
