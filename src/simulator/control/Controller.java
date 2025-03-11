package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator _sim;
	private Factory<Event> _eventsFactory;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {

		if (sim == null || eventsFactory == null)
			throw new IllegalArgumentException("sim and eventsFactory cannot be null");

		this._sim = sim;
		this._eventsFactory = eventsFactory;
	}

	public void loadEvents(InputStream in) {

		JSONObject jo = new JSONObject(new JSONTokener(in));

		if (!jo.has("events") || jo.keySet().size() != 1)
			throw new IllegalArgumentException("Invalid JSON object");

		JSONArray jArrayEvents = jo.getJSONArray("events");

		for (int i = 0; i < jArrayEvents.length(); i++) {

			JSONObject jEvent = jArrayEvents.getJSONObject(i);
			Event e = this._eventsFactory.create_instance(jEvent);

			this._sim.addEvent(e);
		}
	}

	public void run(int n, OutputStream out) {

		PrintStream p = new PrintStream(out);

		p.print("{  \"states\": [");

		// loop for the first n-1 states (to print comma after each state)
		for (int i = 0; i < n - 1; i++) {
			_sim.advance();
			p.print(_sim.report());
			p.println(",");
		}

		// last step, only if 'n > 0'
		if (n > 0) {
			_sim.advance();
			p.print(_sim.report());
		}

		p.print("] }");
	}

	public void reset() {

		this._sim.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		
		this._sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		
		this._sim.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		
		this._sim.addEvent(e);
	}
	
	public void run(int n) {
		
		for (int i = 0; i < n; i++)
			this._sim.advance();
	}
}
