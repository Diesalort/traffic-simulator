package simulator.model;

public class NewJunctionEvent extends Event {

	private String _id;
	private LightSwitchingStrategy _lss;
	private DequeuingStrategy _dqs;

	private int _xCoor;
	private int _yCoor;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy,
			int xCoor, int yCoor) {
		super(time);

		if (lsStrategy == null || dqStrategy == null) {

			throw new IllegalArgumentException("Strategys cannot be null");

		} else if (xCoor < 0 || yCoor < 0) {

			throw new IllegalArgumentException("Coordinates cannot be null");
		}

		this._id = id;
		this._lss = lsStrategy;
		this._dqs = dqStrategy;
		this._xCoor = xCoor;
		this._yCoor = yCoor;

	}

	@Override
	void execute(RoadMap map) {

		Junction j = new Junction(_id, _lss, _dqs, _xCoor, _yCoor);
		map.addJunction(j);
	}
}
