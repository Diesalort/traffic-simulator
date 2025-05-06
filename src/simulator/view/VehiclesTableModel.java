package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private List<Vehicle> _vehicles;
	private String[] _colNames = { "Id", "Location", "Itinerary", "CO2 Class", "Max. Speed", "Speed", "Total CO2",
	"Distance" };

	public VehiclesTableModel(Controller ctrl) {
		_vehicles = new ArrayList<>();
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
		return _vehicles == null ? 0 : _vehicles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = _vehicles.get(rowIndex).getId();
			break;
		case 1:

			Vehicle v = _vehicles.get(rowIndex);
			switch (v.getStatus()) {

			case PENDING:
				s = "Pending";
				break;

			case TRAVELING:
				s = v.getRoad() + ":" + v.getLocation();
				break;

			case WAITING:
				s = "Waiting:" + v.getJunction();
				break;

			case ARRIVED:
				s = "Arrived";
				break;
			}
			break;
		case 2:
			s = _vehicles.get(rowIndex).getItinerary();
			break;
		case 3:
			s = _vehicles.get(rowIndex).getContClass();
			break;
		case 4:
			s = _vehicles.get(rowIndex).getMaxSpeed();
			break;
		case 5:
			s = _vehicles.get(rowIndex).getSpeed();
			break;
		case 6:
			s = _vehicles.get(rowIndex).getTotalCO2();
		case 7:
			s = _vehicles.get(rowIndex).getDistance();
		}

		return s;
	}

	private void update(RoadMap map) {

		SwingUtilities.invokeLater(new Runnable () {

			@Override
			public void run() {

				_vehicles = new ArrayList<>(map.getVehicles());
				fireTableDataChanged();}
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
		_vehicles.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		update(map);
	}
}
