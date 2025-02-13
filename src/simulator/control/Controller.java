package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller { //TODO repasar

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
		
		//TODO Necesario comprobar que no sea vacío, o tenga cosas de más (jo.keySet().size() == 1), o que no está asociado a un JSONArray?
		if (!jo.has("events"))
			throw new IllegalArgumentException("Invalid JSON object");
		/*
		 * try{
		 * 
		 * 		JSONArray jArrayEvents = jo.getJSONArray("events");

		 * } catch(JSONException e){
		 * Excepcion: La key no existe o no tiene asociado un JSONArray
		 * }
		 * 
		 * */
		
		
		JSONArray jArrayEvents = jo.getJSONArray("events");
		
		for (int i = 0; i < jArrayEvents.length(); i++) {
			
			JSONObject jEvent = jArrayEvents.getJSONObject(i);
			Event e = this._eventsFactory.create_instance(jEvent);
			
			this._sim.addEvent(e); //TODO Necesario comprobar que no hay errores?, es decir, try-catch y lanzar otra excepcion?
		}
		
	}
	
	public void run(int n, OutputStream out) {
		
		PrintStream p = new PrintStream(out);

		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();
		
		for (int i = 0; i < n; i++) {
			
			this._sim.advance();
			ja.put(this._sim.report());
		}
		
		jo.put("states", ja);
		
		//TODO ver como hace print en guía de práctica
		p.println(jo.toString(3));
	}
	
	public void reset() {
		
		this._sim.reset();
	}
}
