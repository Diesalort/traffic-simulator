package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {

		List<Vehicle> list = new ArrayList<>();

		if (!q.isEmpty()) // Si la carretera tiene al menos un vehículo...
			list.add(q.get(0));

		return list;
	}

}
