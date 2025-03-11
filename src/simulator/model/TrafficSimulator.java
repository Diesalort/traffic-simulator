package simulator.model;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator {

	private RoadMap _roadMap;
	private Queue<Event> _events;
	private int _time;

	public TrafficSimulator() {
		_roadMap = new RoadMap();
		_events = new PriorityQueue<>();
		_time = 0;
	}

	public void addEvent(Event e) {

		if (e.getTime() <= this._time)
			throw new IllegalArgumentException("Event time (" + e.getTime() + ") is earlier than current time");

		this._events.add(e);
	}

	public void advance() {

		this._time++;

		// Ejecuta todos los eventos cuyo tiempo sea el tiempo actual de la simulación y
		// los elimina de la lista.
		// Con peek, obtenemos el primer evento de la cola
		while (!this._events.isEmpty() && this._events.peek().getTime() == this._time) {

			Event e = this._events.poll(); // Con poll, obtenemos y eliminamos el primer evento de la cola

			e.execute(this._roadMap);
		}

		// Advance junctions
		List<Junction> junctions = this._roadMap.getJunctions();
		for (Junction j : junctions) {

			j.advance(_time);
		}

		// Advance roads
		List<Road> roads = this._roadMap.getRoads();
		for (Road r : roads) {

			r.advance(_time);
		}
	}

	public void reset() {

		this._roadMap.reset();
		this._events.clear();
		this._time = 0;
	}

	public JSONObject report() {

		JSONObject jo = new JSONObject();

		jo.put("time", this._time);
		jo.put("state", this._roadMap.report());

		return jo;
	}

}
