package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {

		int x = 20; // Storm

		Weather w = this.getWeather();
		if (w == Weather.SUNNY) {
			x = 2;

		} else if (w == Weather.CLOUDY) {
			x = 3;

		} else if (w == Weather.RAINY) {
			x = 10;

		} else if (w == Weather.WINDY) {
			x = 15;
		}

		this._totalCO2 = ((100 - x) * this._totalCO2) / 100;
	}

	@Override
	void updateSpeedLimit() {

		if (this.getTotalCO2() > this.getContLimit()) {

			this._speedLimit = this.getMaxSpeed() / 2;

		} else {

			this._speedLimit = this.getMaxSpeed();
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {

		int velocidad = this._speedLimit;

		if (this.getWeather() == Weather.STORM) {

			velocidad = (velocidad * 8) / 10;

		}

		return velocidad;
	}

}
