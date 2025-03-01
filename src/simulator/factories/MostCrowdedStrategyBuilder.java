package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy> {

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss", "Create a MostCrowdedStrategy object");
	}

	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {

		int timeSlot = data.optInt("timeslot", 1);

		return new MostCrowdedStrategy(timeSlot);
	}

	@Override
	protected void fill_in_data(JSONObject o) {

		o.put("timeslot", "Consecutive ticks during which the road can have the green traffic light");
	}

}
