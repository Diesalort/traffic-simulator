package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int _timeSlot;
	
	MostCrowdedStrategy(int timeSlot) throws IllegalArgumentException {
		
		if (timeSlot <= 0) throw new IllegalArgumentException("timeSlot debe ser positivo");
		
		this._timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {

		if (roads.isEmpty()) return -1;
		
		if (currGreen == -1) {
			
			int longMaxCola = -1;
			int indexMaxCola = 0;
			
			for (int i = 0; i < qs.size(); i++) {
				
				if (qs.get(i).size() > longMaxCola) {
					
					longMaxCola = qs.get(i).size();
					indexMaxCola = i;
				}
			}
			
			return indexMaxCola;			
		}
			
		
		if (currTime-lastSwitchingTime < this._timeSlot) return currGreen;
		
		//Búsqueda circular
		int startIndex = currGreen + 1;
		int indexAct = 0;
		
		int longMaxCola = -1;
		int indexMaxCola = 0;

		
		for (int i = 0; i < qs.size(); i++) {
			
			indexAct = (startIndex + i) % qs.size();
			
			if (qs.get(indexAct).size() > longMaxCola) {
				
				indexMaxCola = indexAct;
				longMaxCola = qs.get(indexAct).size();
			}
		}
				
		return indexMaxCola;
	}

	
}
