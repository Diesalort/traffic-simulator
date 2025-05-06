package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private List<Road> _roads;
	private String[] _colNames = { "Id", "Length", "Weather", "Max. Speed", "Speed Limit", "Total CO2", "CO2 Limit" };

	public RoadsTableModel(Controller ctrl) {
		_roads = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public String getColumnName(int col) {
		return _colNames[col];
	}

	@Override
	public int getColumnCount() {
		return _colNames.length;
	}

	@Override
	public int getRowCount() {
		return _roads == null ? 0 : _roads.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _roads.get(rowIndex).getId();
			break;
		case 1:
			s = _roads.get(rowIndex).getLength();
			break;
		case 2:
			s = _roads.get(rowIndex).getWeather();
			break;
		case 3:
			s = _roads.get(rowIndex).getMaxSpeed();
			break;
		case 4:
			s = _roads.get(rowIndex).getSpeedLimit();
			break;
		case 5:
			s = _roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = _roads.get(rowIndex).getContLimit();
		}

		return s;
	}

	private void update(RoadMap map) {

		SwingUtilities.invokeLater(new Runnable () {

			@Override
			public void run() {
				_roads = new ArrayList<>(map.getRoads());
				fireTableDataChanged();
			}
		});
		
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		_roads.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		update(map);
	}
}
