package simulator.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{

	private Controller _ctrl;
	
	public ControlPanel(Controller ctrl) {
		
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		
		JPanel mainPanel = new JPanel(new FlowLayout());
		//this.setContentPane(mainPanel);

		JButton fileChooserButton = new JButton();
		fileChooserButton.setIcon(null/*TODO*/);
		
		fileChooserButton.addActionListener(new ActionListener() { //TODO

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
				
			}
			
		});
		
		
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
