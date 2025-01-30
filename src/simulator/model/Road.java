package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Road extends SimulatedObject {

	// TODO: protected xq van a heredar de esta clase nuevas clases
	protected Junction _srcJunc;
	protected Junction _destJunc;
	protected int _length;
	protected int _maxSpeed;
	protected int _speedLimit;
	protected int _contLimit;
	protected Weather _weather;
	protected int _totalCO2;
	protected List<Vehicle> _vehicles; // TODO: debe estar siempre ordenada por la localización de los vehı́culos
										// (orden descendente)

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
			throws IllegalArgumentException {
		super(id);

		if (maxSpeed <= 0)
			throw new IllegalArgumentException("La velocidad máxima debe ser positiva");

		if (contLimit < 0)
			throw new IllegalArgumentException("El límite de contaminación debe ser positivo");

		if (srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("El valor de los cruces o el tiempo es nulo");

		this._srcJunc = srcJunc;
		this._destJunc = destJunc;
		this._length = length;
		this._maxSpeed = maxSpeed;
		this._speedLimit = maxSpeed;
		this._contLimit = contLimit;
		this._weather = weather;
		this._totalCO2 = 0;
		this._vehicles = new ArrayList<>();

	}

	void enter(Vehicle v) throws IllegalArgumentException {

		if (v.getLocation() != 0 || v.getSpeed() != 0)
			throw new IllegalArgumentException("La localización del vehículo y/o la velocidad es distinta de 0");

		this._vehicles.add(v);

	}

	void exit(Vehicle v) {

		this._vehicles.remove(v); // TODO: creo que deberiamos implementar el equals
	}

	void setWeather(Weather w) {

		if (w == null)
			throw new IllegalArgumentException("Weather es nulo");

		this._weather = w;
	}

	void addContamination(int c) throws IllegalArgumentException {

		if (c < 0)
			throw new IllegalArgumentException("La contaminación dada es negativa");

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

		// TODO: ¡HAY QUE ORDENAR LA LISTA DE VEHICULOS AQUÍ!
	}

	@Override
	public JSONObject report() { // TODO

	}

	int getLength() {

		return this._length;
	}

	Junction getDest() {

		// TODO: ¡HAY QUE DEVOLVER COPIA!
	}

	Junction getSrc() {

		// TODO: ¡HAY QUE DEVOLVER COPIA!
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
