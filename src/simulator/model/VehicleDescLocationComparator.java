package simulator.model;

import java.util.Comparator;

public class VehicleDescLocationComparator implements Comparator<Vehicle> {

	@Override
	public int compare(Vehicle v1, Vehicle v2) {

		if (v1.getLocation() < v2.getLocation())
			return 1;
		else if (v1.getLocation() > v2.getLocation())
			return -1;
		else
			return 0;
	}

}
