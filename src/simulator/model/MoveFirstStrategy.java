package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {

		List<Vehicle> list = new ArrayList<>();
		
		list.add(q.get(0)); //TODO: Es necesario comprobar si la cola q esta vacía?? Es necesario hacer q.get(0).copy()??;
		
		return list;
	}

	
}
