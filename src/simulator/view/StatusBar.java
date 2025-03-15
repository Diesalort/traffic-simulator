package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

public class StatusBar extends JPanel implements TrafficSimObserver{

	private Controller _ctrl;
	private int _time;
	private String _message;
	
	private JLabel _timeLabel;
	private JSeparator _separator;
	private JLabel _eventLabel;
	
	StatusBar(Controller ctrl){
		
		_ctrl = ctrl;
		initGUI();
	}
	
	//TODO Seguro que se la puede dar la misma apariencia que la imagen sin usar setPreferredSize, maximum, minimum ni rigidArea
	private void initGUI() {
		
		this.setLayout(new BoxLayout (this, BoxLayout.LINE_AXIS));
		
		_timeLabel = new JLabel("Time: 70");
		_separator = new JSeparator(SwingConstants.VERTICAL);
		_separator.setPreferredSize(new Dimension(10, 20));
		_separator.setMaximumSize(new Dimension(10, 20));
		_separator.setMinimumSize(new Dimension(10, 20));
		_separator.setForeground(Color.gray);
		_eventLabel = new JLabel("Event added (Change CO2 class: [(v2,0)])");
		
		
		this.add(_timeLabel);
		this.add(Box.createRigidArea(new Dimension(115, 0)));
		this.add(_separator);
		this.add(_eventLabel);

	}
	
	
	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	
	

}
