package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

	private JLabel desc;
	private JLabel road;
	private JComboBox<String> roadsCombo;
	private JLabel weather;
	private JComboBox<Weather> weatherCombo;
	private JLabel ticks;
	private JSpinner ticksSpinner;

	private Controller _ctrl;
	private int _currTime;

	ChangeWeatherDialog (Frame parent, Controller ctrl, List<Road> roads, int time){
		super(parent, true);

		_ctrl = ctrl;
		_currTime = time;

		this.setTitle("Change Road Weather");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setResizable(false); // Para que no se pueda redimensionar

		List<String> roadsId = new ArrayList<>();
		for (Road r : roads) {

			roadsId.add(r.getId());
		}

		initGUI(roadsId);		
	}

	private void initGUI(List<String> roadsId) {

		this.setLayout(new BorderLayout());

		desc = new JLabel();
		desc.setText("Schedule an event to change the weather of a road after a given number of simulation ticks from now");

		JPanel centerPanel = new JPanel(new FlowLayout()); //TODO FlowLayout?
		road = new JLabel ("Road: ");
		roadsCombo = new JComboBox<String>(roadsId.toArray(new String[0])); // Convertimos la lista de ids de vehiculos a un array de string, y lo pasamos al jcombobox

		weather = new JLabel("Weather: ");
		weatherCombo = new JComboBox<Weather>(Weather.values());
		
		ticks = new JLabel("Ticks: ");
		ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

		centerPanel.add(road);
		centerPanel.add(roadsCombo);
		centerPanel.add(weather);
		centerPanel.add(weatherCombo);
		centerPanel.add(ticks);
		centerPanel.add(ticksSpinner);

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

		this.add(desc, BorderLayout.PAGE_START);
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(lowerPanel, BorderLayout.PAGE_END);
		this.pack();
	}

	private void addEvent() {

		String roadId = (String) roadsCombo.getSelectedItem();

		if (roadId == null) {
			this.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), "A simulation must be running to add an event", "Error", JOptionPane.ERROR_MESSAGE);

		} else {

			Weather weather = (Weather) weatherCombo.getSelectedItem();
			int ticks = (Integer) ticksSpinner.getValue();

			List<Pair<String, Weather>> ws = new ArrayList<>();
			ws.add(new Pair<>(roadId, weather));

			_ctrl.addEvent(new SetWeatherEvent (_currTime + ticks, ws)); // Debemos sumar el currTime + los ticks seleccionados			
		}
	}



}
