package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;

public class Junction extends SimulatedObject {

	//TODO
	
	private List<Road> _inRoads;
	private Map<Junction, Road> _outRoadsMap; 
	private List<List<Vehicle>> _queues;
	private	Map<Road, List<Vehicle>> _roadQueueMap;
	
	private int currGreen; 
	private int _lastSwitchingTime;
	private LightSwitchingStrategy _lss; 
	private DequeuingStrategy _dqs;

	private int _x;
	private int _y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws IllegalArgumentException {
		  super(id);
		  
		  if (lsStrategy == null || dqStrategy == null) {
			  
			  throw new IllegalArgumentException("Las estrategias no pueden ser nulas");
			  
		  } else if(xCoor < 0 || yCoor < 0) {
			  
			  throw new IllegalArgumentException("Las coordenadas no pueden ser negativas");
		  }
		  
		  this._inRoads = new ArrayList<>();
		  this._outRoadsMap = new HashMap<Junction, Road>();
		  this._queues = new ArrayList<>();
		  this._roadQueueMap = new HashMap<Road, List<Vehicle>>();
		  
		  this.currGreen = -1;
		  this._lastSwitchingTime = 0;
		  this._lss = lsStrategy;
		  this._dqs = dqStrategy;
		  this._x = xCoor;
		  this._y = yCoor;
		}
	
	

	@Override
	public int hashCode() {
		return Objects.hash(this._id);
	}


	@Override
	public boolean equals(Object obj) {

		return this == obj || obj != null && obj instanceof Junction && ((Junction)obj)._id.equals(this._id); 
	}



	void addIncommingRoad(Road r) {
		
		if (r.getDest().equals(this)) {
			
			_inRoads.add(r);

			List<Vehicle> queue = new LinkedList<>();
			this._queues.add(queue);
			this._roadQueueMap.put(r, queue);
			
		} else throw new IllegalArgumentException("El cruce no es el destino de la carretera dada");
		
		
	}
	
	void addOutGoingRoad(Road r) {
		
		Junction j = r.getDest();

		if (this._outRoadsMap.containsKey(j)) {
			
			throw new IllegalArgumentException("Ya existe una carretera que tiene ese cruce como destino");
			
		} else if (!r.getSrc().equals(this)) {
			
			throw new IllegalArgumentException("La carretera dada no es una carretera saliente de este cruce");
		}
		
		this._outRoadsMap.put(j,  r);	
	}
	
	void enter(Vehicle v) {
		
		Road r = v.getRoad();
		List<Vehicle> queue = _roadQueueMap.get(r);
        queue.add(v);

	}
	
	Road roadTo(Junction j) {

		return this._outRoadsMap.get(j);		
	}
	
	@Override
	void advance(int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
	}
}
