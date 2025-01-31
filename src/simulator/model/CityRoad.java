package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) throws IllegalArgumentException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	public CityRoad(CityRoad cr) {
		super(cr);
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
		
		v.setSpeed(((11-v.getContClass())*this._speedLimit)/11);
		
		return 0; //TODO: ¿QUÉ DEVUELVE ESTE MÉTODO? (o no hay que hacer setSpeed, y simplemente calcular la velocidad?)
	}

	@Override
	Road copy() {

		return new CityRoad(this);
	}

	
}
