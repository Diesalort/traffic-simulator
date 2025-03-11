package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy {

	private int _timeSlot;

	public RoundRobinStrategy(int timeSlot) {

		if (timeSlot <= 0)
			throw new IllegalArgumentException("timeSlot (" + timeSlot + ") must be positive");

		this._timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {

		if (roads.isEmpty())
			return -1;

		if (currGreen == -1) // Todos los semáforos están en rojo
			return 0; // Se pone en verde el primer semáforo

		if (currTime - lastSwitchingTime < this._timeSlot) // Sigue el semáforo actual en verde
			return currGreen;

		return (currGreen + 1) % roads.size();
	}

}
