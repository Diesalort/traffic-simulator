package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy{
	
	private int _timeSlot;
	
	
	RoundRobinStrategy(int timeSlot) throws IllegalArgumentException {
		
		if (timeSlot <= 0) throw new IllegalArgumentException("timeSlot debe ser positivo");
		
		this._timeSlot = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		
		if (roads.isEmpty()) return -1;
	
		if (currGreen == -1) return 0;
		
		if (currTime-lastSwitchingTime < this._timeSlot) return currGreen;
		
		return (currGreen + 1) % roads.size();
	}

	
	
}
