package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> _inRoads;
	private Map<Junction, Road> _outRoadByJunction; 
	private List<List<Vehicle>> _queues;
	private	Map<Road, List<Vehicle>> _queueByRoad;
	
	private int _green; 
	private int _lastSwitchingTime;
	private LightSwitchingStrategy _lss; 
	private DequeuingStrategy _dqs;

	private int _x;
	private int _y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws IllegalArgumentException {
		  super(id);
		  
		  if (lsStrategy == null || dqStrategy == null) {
			  
			  throw new IllegalArgumentException("Las estrategias no pueden ser nulas");
			  
		  } else if (xCoor < 0 || yCoor < 0) {
			  
			  throw new IllegalArgumentException("Las coordenadas no pueden ser negativas");
		  }
		  
		  this._inRoads = new ArrayList<>();
		  this._outRoadByJunction = new HashMap<Junction, Road>();
		  this._queues = new ArrayList<>();
		  this._queueByRoad = new HashMap<Road, List<Vehicle>>();
		  
		  this._green = -1;
		  this._lastSwitchingTime = 0;
		  this._lss = lsStrategy;
		  this._dqs = dqStrategy;
		  this._x = xCoor;
		  this._y = yCoor;
		}
	
	
	//TODO necesario equals y hashcode?
	@Override
	public int hashCode() {
		return Objects.hash(this._id);
	}


	@Override
	public boolean equals(Object obj) {

		return this == obj || obj != null && obj instanceof Junction && ((Junction)obj)._id.equals(this._id); 
	}



	void addIncommingRoad(Road r) {
		
		if (r.getDest().equals(this)) { //TODO usar == ?
			
			this._inRoads.add(r);

			List<Vehicle> queue = new LinkedList<>();
			this._queues.add(queue);
			this._queueByRoad.put(r, queue);
			
		} else throw new IllegalArgumentException("El cruce no es el destino de la carretera dada");
		
		
	}
	
	void addOutGoingRoad(Road r) {
		
		Junction j = r.getDest();

		if (this._outRoadByJunction.containsKey(j)) {
			
			throw new IllegalArgumentException("Ya existe una carretera que tiene ese cruce como destino");
			
		} else if (!r.getSrc().equals(this)) { //TODO usar != ?
			
			throw new IllegalArgumentException("La carretera dada no es una carretera saliente de este cruce");
		}
		
		this._outRoadByJunction.put(j,  r);	
	}
	
	void enter(Vehicle v) {
		
		Road r = v.getRoad();
		List<Vehicle> queue = _queueByRoad.get(r);
        queue.add(v);

	}
	
	Road roadTo(Junction j) {

		return this._outRoadByJunction.get(j);		
	}
	
	@Override
	void advance(int currTime) {
		
		if (this._green != -1) {
			
			Road greenLightRoad = this._inRoads.get(_green);
			List<Vehicle> vehiclesInGreenRoad = this._queueByRoad.get(greenLightRoad);
			List<Vehicle> vehiclesToMove = this._dqs.dequeue(vehiclesInGreenRoad);
			
			for (Vehicle v : vehiclesToMove) {
				
				v.moveToNextRoad();
				vehiclesInGreenRoad.remove(v);
			}
		}
		
		int newGreenRoad = this._lss.chooseNextGreen(this._inRoads , this._queues, this._green, this._lastSwitchingTime, currTime);
		
		if (newGreenRoad != this._green) {
			
			this._green = newGreenRoad;
			this._lastSwitchingTime = currTime;
		}
	}

	@Override
	public JSONObject report() {

		JSONObject jo = new JSONObject();
		
		jo.put("id", this._id);
		
		String id = "none";
		
		if (this._green != -1) {
			
			Road greenRoad = this._inRoads.get(_green);
			id = greenRoad.getId();
		}
		
		jo.put("green", id);

		JSONArray jaQueues = new JSONArray(); //Array de colas
		
		for (Road r : this._inRoads) { //recorremos las carreteras entrantes
			
			JSONObject joQi = new JSONObject();
			
			joQi.put("road", r.getId());
			
			List<Vehicle> roadQueue = this._queueByRoad.get(r);
			
			JSONArray jaVehicles = new JSONArray();
			
			for (Vehicle v : roadQueue) { //recorremos la cola (vehículos) de cada carretera
				
				jaVehicles.put(v.getId());
			}
			
			joQi.put("vehicles", jaVehicles);
			
			jaQueues.put(joQi);
		}
		
		jo.put("queues", jaQueues);
		
		return jo;
	}
}
