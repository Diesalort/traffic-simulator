package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap _roadMap;
	private Queue<Event> _events;
	private int _time;
	private List<TrafficSimObserver> _observers;
	
	public TrafficSimulator() {
		_roadMap = new RoadMap();
		_events = new PriorityQueue<>();
		_time = 0;
		_observers = new ArrayList<>();
	}

	public void addEvent(Event e) {

		if (e.getTime() <= this._time)
			throw new IllegalArgumentException("Event time (" + e.getTime() + ") is earlier than current time");

		this._events.add(e);
		
		for (TrafficSimObserver obs : this._observers) {
			obs.onEventAdded(_roadMap, _events, e, _time);			
		}
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
		
		for (TrafficSimObserver obs : this._observers) {
			
			obs.onAdvance(_roadMap, _events, _time);
		}
	}

	public void reset() {

		this._roadMap.reset();
		this._events.clear();
		this._time = 0;
		
		for (TrafficSimObserver obs : this._observers) {
			
			obs.onReset(_roadMap, _events, _time);
		}
	}

	public JSONObject report() {

		JSONObject jo = new JSONObject();

		jo.put("time", this._time);
		jo.put("state", this._roadMap.report());

		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		// TODO comprobar if (o != null && !_observers.contains(o)) ?? 
		this._observers.add(o);
		o.onRegister(_roadMap, _events, _time);	
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		this._observers.remove(o);
	}

}
