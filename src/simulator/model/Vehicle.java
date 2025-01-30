package simulator.model;

import java.util.ArrayList;
import java.util.Collections;

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
	private int _totalDistance;

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
		this._totalDistance = 0;
	}

	@Override
	public JSONObject report() {
		// TODO Auto-generated method stub
		return null;
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
	
	void advance(int currTime) {
		
		if (this._status == VehicleStatus.TRAVELING) {
			
			int locAnterior = this._location;
			
			//Actualiza location
			if (this._location + this._speed < this._road.getLongitud()) { //TODO
				
				this._location += this._speed;
				
			} else {
				
				this._location = this._road.getLongitud(); 
			}
			
			//Actualiza contaminación
			int contProducida = this._contClass * (this._location - locAnterior);
			this._totalCO2 += contProducida;
			
			//TODO
			// añade contProducida al grado de contaminación de la carretera actual, invocando al método correspondiente de la clase Road.
			
			
			if (this._location >= this._road.getLongitud()) { //TODO
				
				//el vehı́culo entra en la cola del cruce correspondiente (llamando a un método de la clase Junction).
				//Recuerda que debes modificar el estado del vehı́culo.
			}
		}
		
		
	}
	
	
	void moveToNextRoad() throws IllegalArgumentException{ //TODO
		
		if (this._status != VehicleStatus.PENDING || this._status != VehicleStatus.WAITING) {
			
			throw new IllegalArgumentException("El estado del vehículo no es ni Pending ni Waiting");
		}
	}
	
	
	public JSONObject report() { //TODO
		
		
	}
	
	int getLocation(){
		
		return this._location;		
	}
	
	int getSpeed(){
		
		return this._speed;
	}
	
	int getMaxSpeed(){
		
		return this._maxSpeed;
	}

	int getContClass() {
		
		return this._contClass;
	}
	
	VehicleStatus getStatus(){
		
		return this._status;
	}
	
	int getTotalCO2(){
		
		return this._totalCO2;
	}
	
	 List<Junction> getItinerary(){
		 
		 //TODO Hay que devolver una copia!!		 
	 }
	
	 Road getRoad(){
		 
		 //TODO Hay que devolver una copia!!
	 }
}
