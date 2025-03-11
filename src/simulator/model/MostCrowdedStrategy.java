package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int _timeSlot;

	public MostCrowdedStrategy(int timeSlot) {

		if (timeSlot <= 0)
			throw new IllegalArgumentException("timeSlot (" + timeSlot + ") must be positive");

		this._timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {

		if (roads.isEmpty())
			return -1;

		if (currTime - lastSwitchingTime < this._timeSlot)
			return currGreen;

		// Casos en los que hay que realizar búsqueda (currGreen == -1, o que no se haya
		// cumplido ninguno anterior y currGreen != -1)

		// Longitud e índice de la carretera con la cola más larga
		int longMaxCola = -1;
		int indexMaxCola = 0;
		int indexAct = 0; // El índice en el que nos encontramos durante la búsqueda

		// Búsqueda circular
		int startIndex = 0; // Índice desde el que empezamos a buscar

		if (currGreen != -1) {

			startIndex = currGreen + 1;
		}

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
