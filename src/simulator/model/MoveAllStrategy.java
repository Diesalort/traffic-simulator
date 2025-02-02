package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {

		//TODO: es necesaria la copia profunda?

		/*
		List<Vehicle> list = new ArrayList<>();
	
		for (Vehicle v : q) {
			
			list.add(v.copy());
		}*/
		
		List<Vehicle> list = new ArrayList<>(q);
		
		return list;
	}

}
