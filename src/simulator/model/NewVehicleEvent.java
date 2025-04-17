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

			throw new IllegalArgumentException("maxSpeed must be positive");

		} else if (contClass < 0 || contClass > 10) {

			throw new IllegalArgumentException(
					"Contamination class (" + contClass + ") can only take values ​​between 0 and 10 (both inclusive)");

		} else if (itinerary.size() < 2) {

			throw new IllegalArgumentException(
					"The itinerary must have, at least, 2 junctions. Actual size: " + itinerary.size());
		}

		this._id = id;
		this._maxSpeed = maxSpeed;
		this._contClass = contClass;
		this._itinerary = itinerary;
	}

	@Override
	void execute(RoadMap map) {

		// Hay que convertir el itinerario List<String> a uno que sea del tipo
		// List<Junction>, para pasárselo al constructor
		List<Junction> itinerary = new ArrayList<>();

		for (String jId : _itinerary) {

			Junction j = map.getJunction(jId);

			if (j == null)
				throw new IllegalArgumentException("Junction with identifier " + jId + " it's not on the road map");

			itinerary.add(j);
		}

		Vehicle v = new Vehicle(_id, _maxSpeed, _contClass, itinerary);

		map.addVehicle(v);
		v.moveToNextRoad();
	}

	@Override
	public String toString() {

		return "New Vehicle '" + _id + "'";
	}

}
