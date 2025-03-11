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
	private List<List<Vehicle>> _queues; // Lista de colas para las carreteras entrantes
	private Map<Road, List<Vehicle>> _queueByRoad;

	private int _greenLightIndex;
	private int _lastSwitchingTime;
	private LightSwitchingStrategy _lss;
	private DequeuingStrategy _dqs;

	private int _xCoor;
	private int _yCoor;

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);

		if (lsStrategy == null || dqStrategy == null) {

			throw new IllegalArgumentException("Strategies cannot be null");

		} else if (xCoor < 0 || yCoor < 0) {

			throw new IllegalArgumentException("Coordinates [" + xCoor + "," + yCoor + "] cannot be negative");
		}

		this._inRoads = new ArrayList<Road>();
		this._outRoadByJunction = new HashMap<Junction, Road>();
		this._queues = new ArrayList<List<Vehicle>>();
		this._queueByRoad = new HashMap<Road, List<Vehicle>>();

		this._greenLightIndex = -1;
		this._lastSwitchingTime = 0;
		this._lss = lsStrategy;
		this._dqs = dqStrategy;
		this._xCoor = xCoor;
		this._yCoor = yCoor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this._id);
	}

	@Override
	public boolean equals(Object obj) {

		return this == obj || obj != null && obj instanceof Junction && ((Junction) obj)._id.equals(this._id);
	}

	void addIncommingRoad(Road r) {

		if (!r.getDest().equals(this))
			throw new IllegalArgumentException(
					"The junction: " + _id + " is not the destination of the given road: " + r.getId());

		this._inRoads.add(r);
		List<Vehicle> queue = new LinkedList<>();
		this._queues.add(queue);
		this._queueByRoad.put(r, queue);
	}

	void addOutGoingRoad(Road r) {

		Junction j = r.getDest();

		if (this._outRoadByJunction.containsKey(j)) { // Ninguna otra carretera debe ir desde this al cruce j

			throw new IllegalArgumentException(
					"There is already a road that has junction " + _id + " as its destination");

		} else if (!r.getSrc().equals(this)) { // Si la carretera no es saliente del cruce actual...

			throw new IllegalArgumentException(
					"The given road: " + r.getId() + " is not an outgoing road from this junction: " + _id);
		}

		this._outRoadByJunction.put(j, r);
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

		if (this._greenLightIndex != -1) { // Si algún semáforo está en verde...

			Road greenLightRoad = this._inRoads.get(_greenLightIndex);
			List<Vehicle> vehiclesInGreenRoad = this._queueByRoad.get(greenLightRoad);
			List<Vehicle> vehiclesToMove = this._dqs.dequeue(vehiclesInGreenRoad);

			for (Vehicle v : vehiclesToMove) {

				v.moveToNextRoad();
				vehiclesInGreenRoad.remove(v);
			}
		}

		int newGreenRoad = this._lss.chooseNextGreen(this._inRoads, this._queues, this._greenLightIndex,
				this._lastSwitchingTime, currTime);

		if (newGreenRoad != this._greenLightIndex) {

			this._greenLightIndex = newGreenRoad;
			this._lastSwitchingTime = currTime;
		}

	}

	@Override
	public JSONObject report() {

		JSONObject jo = new JSONObject();

		jo.put("id", this._id);

		String greenRoadId = "none";

		if (this._greenLightIndex != -1) {

			Road greenRoad = this._inRoads.get(_greenLightIndex);
			greenRoadId = greenRoad.getId();
		}

		jo.put("green", greenRoadId);

		JSONArray jaQueues = new JSONArray(); // JArray de colas [Q1,Q2,....]

		for (Road r : this._inRoads) { // recorremos las carreteras entrantes

			JSONObject joQi = new JSONObject();

			joQi.put("road", r.getId());

			List<Vehicle> roadQueue = this._queueByRoad.get(r);

			JSONArray jaVehicles = new JSONArray();

			for (Vehicle v : roadQueue) { // recorremos la cola (vehículos) de cada carretera

				jaVehicles.put(v.getId());
			}

			joQi.put("vehicles", jaVehicles);

			jaQueues.put(joQi);
		}

		jo.put("queues", jaQueues);

		return jo;
	}
}
