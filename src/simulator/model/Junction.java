package simulator.model;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Junction extends SimulatedObject {

	//TODO
	
	private List<Road> _inRoads;
	private Map<Junction, Road> _outRoadByJunction; 
	private List<List<Vehicle>> _queues;
	private int _greenLightIndex; 
	private int _lastSwitchingTime; 
	private LightSwitchingStrategy _lss; 
	private DequeuingStrategy _dqs;
	//private	Map<Road, List<Vehicle>> _queueByRoad;

	private int _x;
	private int _y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws IllegalArgumentException {
		  super(id);
		  
		  if (lsStrategy == null || dqStrategy == null) {
			  
			  throw new IllegalArgumentException("Las estrategias no pueden ser nulas");
			  
		  } else if(xCoor < 0 || yCoor < 0) {
			  
			  throw new IllegalArgumentException("Las coordenadas no pueden ser negativas");
		  }
		  
		  //TODO INICIALIZAR todo BIEN. Hace falta copia??
		  this._lss = lsStrategy;
		  this._dqs = dqStrategy;
		  this._x = xCoor;
		  this._y = yCoor;
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
