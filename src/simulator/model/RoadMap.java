package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {

	private List<Junction> _junctions;
	private List<Road> _roads;
	private List<Vehicle> _vehicles;
	
	private Map<String,Junction> _junctionsMap;
	private Map<String, Road> _roadsMap;
	private Map<String, Vehicle> _vehiclesMap;
	
	
	RoadMap(){
		
		_junctions = new ArrayList<>();
		_roads = new ArrayList<>();
		_vehicles = new ArrayList<>();

		_junctionsMap = new HashMap<>();
		_roadsMap = new HashMap<>();
		_vehiclesMap = new HashMap<>();
	}
	
	void addJunction(Junction j) {
		
		if (_junctionsMap.containsKey(j.getId()))
			throw new IllegalArgumentException ("Ya existe un cruce con el identificador: " + j.getId());
		
		_junctions.add(j);
		_junctionsMap.put(j.getId(), j);
	}
	
	void addRoad(Road r) {
		
		if (_roadsMap.containsKey(r.getId()))
			throw new IllegalArgumentException ("Ya existe una carretera con el identificador: " + r.getId());
		
		Junction jSrc = r.getSrc();
		Junction jDest = r.getDest();
		
		if (!_junctionsMap.containsKey(jSrc.getId()) || !_junctionsMap.containsKey(jDest.getId()))
			throw new IllegalArgumentException ("Los cruces que conectan la carretera no se encuentran en el mapa de cruces");
		
		_roads.add(r);
		_roadsMap.put(r.getId(), r);
	}
	
	void addVehicle(Vehicle v) { //TODO optimizar
		
		if (_vehiclesMap.containsKey(v.getId()))
			throw new IllegalArgumentException ("Ya existe un vehículo con el identificador: " + v.getId());

		//TODO
		List<Junction> itinerary = v.getItinerary();
		
		//Cruces por los que deberá ir pasando el vehículo
		Junction j1;
		Junction j2;
		
		boolean exist = false;
		
		for (int i = 0; i < itinerary.size() - 1; i++) {
			
			j1 = itinerary.get(i);
			j2 = itinerary.get(i+1);
			
			for (Road r : _roads) {

				if (r.getSrc().equals(j1) && r.getDest().equals(j2)) { //Si existe una carretera que una los cruces j1 y j2, seguimos buscando
					
					exist = true;
				}
				
			}
			
			//Si no se ha encontrado una carretera en roadList que una j1 y j2, lanzamos excepción
			if (!exist)
				throw new IllegalArgumentException("El itinerario del vehículo no es válido");
			
			exist = false;
		}
		
		_vehicles.add(v);
		_vehiclesMap.put(v.getId(), v);		
	}
	
	
	public Junction getJunction(String id) {
		
		return _junctionsMap.get(id);
	}
	
	
	public Road getRoad(String id) {
		
		return _roadsMap.get(id);
	}
	
	
	public Vehicle getVehicle(String id) {
		
		return _vehiclesMap.get(id);
	}
	
	
	public List<Junction> getJunctions(){
		
		return Collections.unmodifiableList(this._junctions);		
	}
	
	
	public List<Road> getRoads(){
		
		return Collections.unmodifiableList(this._roads);		
	}
	
	
	public List<Vehicle> getVehicles(){
		
		return Collections.unmodifiableList(this._vehicles);		
	}
	
	
	void reset() {
		
		this._junctions.clear();
		this._roads.clear();
		this._vehicles.clear();
		
		this._junctionsMap.clear();
		this._roadsMap.clear();
		this._vehiclesMap.clear();
	}
	
	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		//Junctions
		JSONArray jaJunctions = new JSONArray();
		for (Junction j : this._junctions) {
			
			jaJunctions.put(j.report());
		}
		
		//Roads
		JSONArray jaRoads = new JSONArray();
		for (Road r : this._roads) {
			
			jaRoads.put(r.report());
		}
		
		//Vehicles
		JSONArray jaVehicles = new JSONArray();
		for (Vehicle v : this._vehicles) {
			
			jaVehicles.put(v.report());
		}
		
		
		jo.put("junctions", jaJunctions);
		jo.put("roads", jaRoads);
		jo.put("vehicles", jaVehicles);

		return jo;
	}
	
}
