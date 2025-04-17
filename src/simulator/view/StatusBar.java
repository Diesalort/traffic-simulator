package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private JLabel _timeLabel;
	private JSeparator _separator;
	private JLabel _eventLabel;

	StatusBar(Controller ctrl) {
		_timeLabel = new JLabel();
		_eventLabel = new JLabel();
		ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {

		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		_timeLabel.setPreferredSize(new Dimension(70, 20));
		_timeLabel.setMaximumSize(new Dimension(70, 20));
		_timeLabel.setMinimumSize(new Dimension(70, 20));

		_eventLabel.setText("Welcome!");

		_separator = new JSeparator(SwingConstants.VERTICAL);
		_separator.setPreferredSize(new Dimension(10, 20));
		_separator.setMaximumSize(new Dimension(10, 20));
		_separator.setMinimumSize(new Dimension(10, 20));
		_separator.setForeground(Color.gray);

		this.add(_timeLabel);
		this.add(Box.createRigidArea(new Dimension(115, 0)));
		this.add(_separator);
		this.add(_eventLabel);
	}

	private void update(int time, String message) {
		_timeLabel.setText("Time: " + time);
		_eventLabel.setText(message);
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		update(time, "");
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		update(time, "Event added " + "(" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		update(time, ""); // time = 0
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		update(time, "");
	}
}
