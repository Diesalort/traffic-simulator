package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

	private List<Pair<String, Weather>> _ws;

	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);

		if (ws == null)
			throw new IllegalArgumentException("SetWeather list cannot be null");

		this._ws = ws;
	}

	@Override
	void execute(RoadMap map) {

		for (Pair<String, Weather> w : this._ws) {

			Road r = map.getRoad(w.getFirst());

			if (r == null)
				throw new IllegalArgumentException(
						"The road with identifier " + w.getFirst() + " is not on the road map");

			r.setWeather(w.getSecond());
		}
	}

}
