package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}


	@Override
	void reduceTotalContamination() {

		int x = 20; //Storm
		
		if (this._weather == Weather.SUNNY) {
			x = 2;
			
		} else if (this._weather == Weather.CLOUDY) {
			x = 3;		
			
		} else if (this._weather == Weather.RAINY) {
			x = 10;
			
		} else if (this._weather == Weather.WINDY) {
			x = 15;
		}
		
		
		this._totalCO2 = ((100 - x)*this._totalCO2)/100;
	}

	@Override
	void updateSpeedLimit() {

		if (this._totalCO2 > this._contLimit) {
			
			this._speedLimit = this._maxSpeed/2;
			
		} else {
			
			this._speedLimit = this._maxSpeed;
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {

		int velocidad = this._speedLimit;
		
		if (this._weather == Weather.STORM) {
			
			velocidad = (velocidad*8)/10;
			
		}
		
		return velocidad;
	}	
	
}
