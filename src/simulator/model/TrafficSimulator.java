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
			throw new IllegalArgumentException("El tiempo del evento es anterior al actual");
		
		this._events.add(e); //TODO creo que se mantiene el orden sin hacer nada más
	}
	
	public void advance() {
		
		this._time++;
		
		//TODO: ejecuta todos los eventos cuyo tiempo sea el tiempo actual de la simulación y los elimina de la lista.
		while (!this._events.isEmpty() && this._events.peek().getTime() == this._time) {
			
			Event e = this._events.poll(); //Obtenemos y elminamos el evento de la cola
			
			e.execute(this._roadMap);
		}
		
		//Advance junctions
		List<Junction> junctions = this._roadMap.getJunctions();
		for (Junction j : junctions) {
			
			j.advance(_time);
		}
		
		//Advance roads
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
