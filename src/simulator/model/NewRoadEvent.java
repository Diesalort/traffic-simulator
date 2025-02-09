package simulator.model;

public abstract class NewRoadEvent extends Event {

	protected String _id;
	protected String _srcJunc;
	protected String _destJunc;
	protected int _length;
	protected int _co2Limit;
	protected int _maxSpeed;
	protected Weather _weather;

	NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);

		if (maxSpeed <= 0)
			throw new IllegalArgumentException("La velocidad máxima debe ser positiva");

		if (co2Limit < 0)
			throw new IllegalArgumentException("El límite de contaminación no puede ser negativo");

		if (length <= 0)
			throw new IllegalArgumentException("La longitud de la carretera debe ser positiva");

		if (srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("El valor de los cruces o el tiempo es nulo");
		
		this._id = id;
		this._srcJunc = srcJunc;
		this._destJunc = destJunc;
		this._length = length;
		this._co2Limit = co2Limit;
		this._maxSpeed = maxSpeed;
		this._weather = weather;
	}

	// Método execute implementado en clases hijas
}
