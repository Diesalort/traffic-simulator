package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {

	private String _id;
	private int _maxSpeed;
	private int _contClass;
	private List<String> _itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		  super(time);
		  
			if (maxSpeed <= 0) {
				
				throw new IllegalArgumentException("La velocidad máxima debe ser positiva");
				
			} else if (contClass < 0 || contClass > 10) {
				
				throw new IllegalArgumentException("contClass solo puede tomar valores de 0 a 10 (ambos inclusive)");
				
			} else if (itinerary.size() < 2) {
				
				throw new IllegalArgumentException("El itinerario debe tener, como mínimo, 2 cruces");
			}

		  this._id = id;
		  this._maxSpeed = maxSpeed;
		  this._contClass = contClass;
		  this._itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) {

		List<Junction> itinerary = new ArrayList<>();;
						
		for (String jId : _itinerary) {
			
			Junction j = map.getJunction(jId);
			
			if (j == null)
				throw new IllegalArgumentException("El cruce " + jId + " no está en el mapa de carreteras");
			
			itinerary.add(j);			
		}
		
		Vehicle v = new Vehicle (_id, _maxSpeed, _contClass, itinerary);
		
		map.addVehicle(v);
		v.moveToNextRoad();
	}
	
}
