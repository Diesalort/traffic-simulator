package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {

	protected Junction _source;
	protected Junction _destination;
	protected int _length;
	protected int _maxSpeed;
	protected int _speedLimit;
	protected int _contLimit;
	protected Weather _weather;
	protected int _totalCO2;
	protected List<Vehicle> _vehicles;

	private final VehicleDescLocationComparator _locComp;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);

		if (maxSpeed <= 0)
			throw new IllegalArgumentException("maxSpeed must be positive");

		if (contLimit < 0)
			throw new IllegalArgumentException("Contamination limit cannot be negative");

		if (length <= 0)
			throw new IllegalArgumentException("Road's length must be positive");

		if (srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("Junctions or time are null");

		this._source = srcJunc;
		this._destination = destJunc;

		this._source.addOutGoingRoad(this);
		this._destination.addIncommingRoad(this);

		this._length = length;
		this._maxSpeed = maxSpeed;
		this._speedLimit = maxSpeed;
		this._contLimit = contLimit;
		this._weather = weather;
		this._totalCO2 = 0;
		this._vehicles = new ArrayList<Vehicle>();
		this._locComp = new VehicleDescLocationComparator();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this._id);
	}


	@Override
	public boolean equals(Object obj) {

		return this == obj || obj != null && obj instanceof Road && ((Road)obj)._id.equals(this._id); 
	}
	
	void enter(Vehicle v) {

		if (v.getLocation() != 0 || v.getSpeed() != 0)
			throw new IllegalArgumentException("Vehicle's location and/or speed is different than 0");

		this._vehicles.add(v);
		Collections.sort(this._vehicles, this._locComp);

	}

	void exit(Vehicle v) {

		this._vehicles.remove(v);
	}

	void setWeather(Weather w) {

		if (w == null)
			throw new IllegalArgumentException("Weather es nulo");

		this._weather = w;
	}

	void addContamination(int c) {

		if (c < 0)
			throw new IllegalArgumentException("Contamination cannot be negative");

		this._totalCO2 += c;
	}

	abstract void reduceTotalContamination();

	abstract void updateSpeedLimit();

	abstract int calculateVehicleSpeed(Vehicle v);

	@Override
	void advance(int currTime) {

		this.reduceTotalContamination();
		this.updateSpeedLimit();

		for (Vehicle v : this._vehicles) {

			v.setSpeed(this.calculateVehicleSpeed(v));
			v.advance(currTime);
		}

		Collections.sort(this._vehicles, this._locComp);
	}

	@Override
	public JSONObject report() {

		JSONObject jo = new JSONObject();

		jo.put("id", this._id);
		jo.put("speedlimit", this._speedLimit);
		jo.put("weather", this._weather.toString());
		jo.put("co2", this._totalCO2);

		JSONArray ja = new JSONArray();

		for (Vehicle v : this._vehicles) {

			ja.put(v.getId());
		}

		jo.put("vehicles", ja);

		return jo;
	}

	int getLength() {

		return this._length;
	}

	Junction getDest() {

		return this._destination;
	}

	Junction getSrc() {

		return this._source;
	}

	Weather getWeather() {

		return this._weather;
	}

	int getContLimit() {

		return _contLimit;
	}

	int getMaxSpeed() {

		return _maxSpeed;
	}

	int getTotalCO2() {

		return this._totalCO2;
	}

	int getSpeedLimit() {

		return this._speedLimit;
	}

	List<Vehicle> getVehicles() {

		return Collections.unmodifiableList(this._vehicles);
	}

}
