package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {

	private List<Junction> _itinerary;
	private int _junctionIndex; // índice de itinerary en el que se encuentra el vehículo
	private int _maxSpeed;
	private int _speed; // Velocidad actual
	private VehicleStatus _status;
	private Road _road;
	private int _location;
	private int _contClass; // Grado de contaminación
	private int _totalCO2;
	private int _distance;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);

		if (maxSpeed <= 0) {

			throw new IllegalArgumentException("maxSpeed must be positive");

		} else if (contClass < 0 || contClass > 10) {

			throw new IllegalArgumentException(
					"Contamination class can only take values ​​between 0 and 10 (both inclusive)");

		} else if (itinerary.size() < 2) {

			throw new IllegalArgumentException("The itinerary must have, at least, 2 junctions");
		}

		this._itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		this._maxSpeed = maxSpeed;
		this._speed = 0;
		this._status = VehicleStatus.PENDING;
		this._road = null;
		this._location = 0;
		this._contClass = contClass;
		this._totalCO2 = 0;
		this._distance = 0;
	}

	void setSpeed(int s) {

		if (s < 0)
			throw new IllegalArgumentException("Speed must be positive");

		if (this._status == VehicleStatus.TRAVELING) {

			this._speed = s < this._maxSpeed ? s : this._maxSpeed;
					
		} else { // Si no está viajando, ponemos su velocidad a 0
			
			this._speed = 0;
		}
	}

	void setContaminationClass(int c) {

		if (c < 0 || c > 10)
			throw new IllegalArgumentException("Contamination class mmust be between 0 and 10 (both inclusive)");

		this._contClass = c;
	}

	@Override
	void advance(int currTime) {

		if (this._status == VehicleStatus.TRAVELING) {

			int locAnterior = this._location;

			// Actualiza location
			if (this._location + this._speed < this._road.getLength()) {

				this._location += this._speed;

			} else {

				this._location = this._road.getLength();
			}

			// Actualizamos distancia recorrida
			this._distance += this._location - locAnterior;

			// Actualiza contaminación
			int contProducida = (this._location - locAnterior) * this._contClass;
			this._totalCO2 += contProducida;
			this._road.addContamination(contProducida);

			// Si location >= roadLength, el vehículo entra en la cola del cruce correspondiente
			if (this._location >= this._road.getLength()) { // El vehı́culo entra en la cola del cruce correspondiente

				Junction actualJunction = this._itinerary.get(this._junctionIndex);
				actualJunction.enter(this);
				this._status = VehicleStatus.WAITING;
				this._speed = 0;
			}
			
		} else { // Si su estado no es Travelling, ponemos su velocidad a 0
			
			this._speed = 0;
		}
	}

	void moveToNextRoad() {

		if (this._status != VehicleStatus.PENDING && this._status != VehicleStatus.WAITING) {

			throw new IllegalArgumentException("The vehicle's status is not Pending or Waiting");
		}

		if (this._road != null || this._junctionIndex > 0) {

			this._road.exit(this);
		}

		if (this._junctionIndex == this._itinerary.size() - 1) { // Ha completado el itinerario

			this._status = VehicleStatus.ARRIVED;
			this._road = null;
			this._speed = 0;
			this._location = 0;

		} else {

			Junction actualJunction = this._itinerary.get(_junctionIndex);
			Junction nextJunction = this._itinerary.get(_junctionIndex + 1);

			this._junctionIndex++;
			this._road = actualJunction.roadTo(nextJunction); // Nueva carretera del vehículo
			this._location = 0;
			this._speed = 0;
			this._road.enter(this);

			this._status = VehicleStatus.TRAVELING;
		}
	}

	@Override
	public JSONObject report() {

		JSONObject jo = new JSONObject();

		jo.put("id", this._id);
		jo.put("speed", this._speed);
		jo.put("distance", this._distance);
		jo.put("co2", this._totalCO2);
		jo.put("class", this._contClass);
		jo.put("status", this._status.toString());

		if (this._status != VehicleStatus.PENDING && this._status != VehicleStatus.ARRIVED) {

			jo.put("road", this._road.getId());
			jo.put("location", this._location);
		}

		return jo;
	}

	public int getLocation() {

		return this._location;
	}

	public int getSpeed() {

		return this._speed;
	}

	public int getMaxSpeed() {

		return this._maxSpeed;
	}

	public int getContClass() {

		return this._contClass;
	}

	public VehicleStatus getStatus() {

		return this._status;
	}

	public int getTotalCO2() {

		return this._totalCO2;
	}

	public List<Junction> getItinerary() {

		return this._itinerary;
	}

	public Road getRoad() {

		return this._road;
	}

}
