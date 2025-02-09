package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 2;
		
		if (this._weather == Weather.WINDY || this._weather == Weather.STORM) {
			
			x = 10;	
		}
		
		this._totalCO2 -= x;
		
		if (this._totalCO2 < 0) this._totalCO2 = 0;
	}

	@Override
	void updateSpeedLimit() {
		//La velocidad límite no cambia, es siempre la máxima
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		
		return ((11-v.getContClass())*this._speedLimit)/11;
	}	
}
