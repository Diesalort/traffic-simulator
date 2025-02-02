package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {

	private List<Junction> _itinerary;
	private int _maxSpeed;
	private int _speed;
	private VehicleStatus _status;
	private Road _road;
	private int _location;
	private int _contClass;
	private int _totalCO2;
	private int _distance;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws IllegalArgumentException {
		super(id);
		
		if (maxSpeed <= 0) {
			
			throw new IllegalArgumentException("La velocidad máxima debe ser positiva");
			
		} else if (contClass < 0 || contClass > 10) {
			
			throw new IllegalArgumentException("contClass solo puede tomar valores de 0 a 10 (ambos inclusive)");
			
		} else if (itinerary.size() < 2) {
			
			throw new IllegalArgumentException("itinerario debe tener, como mínimo, 2 cruces");
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

	//TODO REPASAR constructor de copia
	public Vehicle(Vehicle v) {
		super(v._id);
		
		this._itinerary = Collections.unmodifiableList(new ArrayList<>(v._itinerary));
		this._maxSpeed = v._maxSpeed;
		this._speed = v._speed;
		this._status = v._status;
		this._road = v._road.copy();
		this._location = v._location;
		this._contClass = v._contClass;
		this._totalCO2 = v._totalCO2;
		this._distance = v._distance;

	}


	void setSpeed(int s) throws IllegalArgumentException {
		
		if (s < 0) throw new IllegalArgumentException("La velocidad no puede ser negativa");
		
		if (s < this._maxSpeed) {
			
			this._speed = s;
			
		} else {
			
			this._speed = this._maxSpeed;
		}	
	}
	
	
	void setContaminationClass(int c) throws IllegalArgumentException {
		
		if (c < 0 || c > 10) throw new IllegalArgumentException("El grado de contaminacion debe estar comprendido entre 0 y 10 (incluidos)");
		
		this._contClass = c;
	}
	
	@Override
	void advance(int currTime) {
		
		if (this._status == VehicleStatus.TRAVELING) {
			
			int locAnterior = this._location;
			
			//Actualiza location
			if (this._location + this._speed < this._road.getLength()) { //TODO
				
				this._location += this._speed;
				
			} else {

				this._location = this._road.getLength(); 
			}
			
			//Actualiza contaminación
			int contProducida = this._contClass * (this._location - locAnterior);
			this._totalCO2 += contProducida;
			
			//TODO
			// añade contProducida al grado de contaminación de la carretera actual, invocando al método correspondiente de la clase Road.
			
			
			if (this._location >= this._road.getLength()) { //TODO
				
				//el vehı́culo entra en la cola del cruce correspondiente (llamando a un método de la clase Junction).
				//Recuerda que debes modificar el estado del vehı́culo.
			}
		} else {
			
			this._speed = 0;
		}
		
		
	}
	
	
	void moveToNextRoad() throws IllegalArgumentException{ //TODO
		
		if (this._status != VehicleStatus.PENDING || this._status != VehicleStatus.WAITING) {
			
			throw new IllegalArgumentException("El estado del vehículo no es ni Pending ni Waiting");
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
		jo.put("status", this._status);
		
		if (this._status != VehicleStatus.PENDING && this._status != VehicleStatus.ARRIVED) {
			
			jo.put("road", this._road);
			jo.put("location", this._location);
		}

		return jo;
	}
	
	public int getLocation(){
		
		return this._location;		
	}
	
	public int getSpeed(){
		
		return this._speed;
	}
	
	public int getMaxSpeed(){
		
		return this._maxSpeed;
	}

	public int getContClass() {
		
		return this._contClass;
	}
	
	public VehicleStatus getStatus(){
		
		return this._status;
	}
	
	public int getTotalCO2(){
		
		return this._totalCO2;
	}
	
	
	 //TODO Repasar los dos siguientes métodos y el proceso realizado para devolver una copia!!
	
	 public List<Junction> getItinerary(){
		 	 
		 return new ArrayList<>(this._itinerary); //TODO creo que habria que copiar cada cruce y añadirlo al arrayList
	 }
	
	 public Road getRoad(){ //¡Hay que devolver una copia!
		 
		 return this._road.copy();
	 }
	 
	 //TODO Repasar copy
	 Vehicle copy() {
		 
		 return new Vehicle(this);
	 }
	 
}
