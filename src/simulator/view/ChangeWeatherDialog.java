package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel _desc;
	private JLabel _road;
	private JComboBox<Road> _roadsCombo;
	private JLabel _weather;
	private JComboBox<Weather> _weatherCombo;
	private JLabel _ticks;
	private JSpinner _ticksSpinner;

	private Controller _ctrl;
	private int _currTime;

	ChangeWeatherDialog (Frame parent, Controller ctrl, List<Road> roads, int time){
		super(parent, "Change Road Weather", true);

		_ctrl = ctrl;
		_currTime = time;

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(460, 205));
		this.setResizable(false); // Para que no se pueda redimensionar

		initGUI(roads);
		
		// Llamarlo después de this.pack()
		this.setLocation(parent.getX() + (parent.getWidth() - this.getWidth())/2, parent.getY() + (parent.getHeight() - this.getHeight())/2); // Para que aparezca centrado con respecto a mainWindow
		this.setVisible(true);
	}

	private void initGUI(List<Road> roads) {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// SUPERIOR
		JPanel superiorPanel = new JPanel(new BorderLayout());
		
		_desc = new JLabel ("<html><p>Schedule an event to change the weather of a road after a given number of simulation ticks from now</p></html>");
		superiorPanel.add(_desc);
		
		// CENTRO
		JPanel centerPanel = new JPanel(new FlowLayout());
		_road = new JLabel ("Road: ");
		_roadsCombo = new JComboBox<>(roads.toArray(new Road[0])); // Casteamos roads a un array y lo añadimos en el comboBox
		_roadsCombo.setPreferredSize(new Dimension(85, 20));
		
		_weather = new JLabel("Weather: ");
		_weatherCombo = new JComboBox<Weather>(Weather.values());
		_weatherCombo.setPreferredSize(new Dimension(85, 20));

		_ticks = new JLabel("Ticks: ");
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		_ticksSpinner.setPreferredSize(new Dimension(60, 20));
		
		centerPanel.add(_road);
		centerPanel.add(_roadsCombo);
		centerPanel.add(_weather);
		centerPanel.add(_weatherCombo);
		centerPanel.add(_ticks);
		centerPanel.add(_ticksSpinner);
		
		// INFERIOR
		JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeWeatherDialog.this.setVisible(false);							
			}
		});
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				addEvent();
				ChangeWeatherDialog.this.setVisible(false);							
			}	
		});
		
		lowerPanel.add(cancel);
		lowerPanel.add(ok);
		
		
		// Añadimos todo a mainPanel y creamos áreas rígidas
		mainPanel.add(superiorPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,15)));
		mainPanel.add(centerPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0,35)));
		mainPanel.add(lowerPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		this.add(mainPanel);
		this.pack();
	}

	private void addEvent() {

		Road road = (Road) _roadsCombo.getSelectedItem();

		if (road == null) {
			this.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), "A valid simulation must be running to add an event", "Error", JOptionPane.ERROR_MESSAGE);

		} else {

			Weather weather = (Weather) _weatherCombo.getSelectedItem();
			int ticks = (Integer) _ticksSpinner.getValue();

			List<Pair<String, Weather>> ws = new ArrayList<>();
			ws.add(new Pair<>(road.getId(), weather));

			_ctrl.addEvent(new SetWeatherEvent (_currTime + ticks, ws)); // Debemos sumar el currTime + los ticks seleccionados			
		}
	}



}
