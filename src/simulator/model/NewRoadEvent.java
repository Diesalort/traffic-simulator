package simulator.model;

public abstract class NewRoadEvent extends Event {

	protected String _id;
	private String _srcJunc;
	private String _destJunc;
	protected int _length;
	protected int _co2Limit;
	protected int _maxSpeed;
	protected Weather _weather;

	NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);

		if (maxSpeed <= 0)
			throw new IllegalArgumentException("maxSpeed must be positive");

		if (co2Limit < 0)
			throw new IllegalArgumentException("Contamination limit cannot be negative");

		if (length <= 0)
			throw new IllegalArgumentException("Road's length must be positive");

		if (srcJunc == null || destJunc == null || weather == null)
			throw new IllegalArgumentException("Junctions or time are null");

		this._id = id;
		this._srcJunc = srcJunc;
		this._destJunc = destJunc;
		this._length = length;
		this._co2Limit = co2Limit;
		this._maxSpeed = maxSpeed;
		this._weather = weather;
	}

	void execute(RoadMap map) {

		Junction srcJunc = map.getJunction(this._srcJunc);
		if (srcJunc == null)
			throw new IllegalArgumentException("The source junction is not on the road map");

		Junction destJunc = map.getJunction(this._destJunc);
		if (destJunc == null)
			throw new IllegalArgumentException("The destiny junction is not on the road map");

		map.addRoad(this.newInstance(srcJunc, destJunc));
	}

	protected abstract Road newInstance(Junction src, Junction dest);
}
