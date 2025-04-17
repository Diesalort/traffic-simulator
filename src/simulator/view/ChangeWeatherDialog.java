package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel _desc;
	private JLabel _road;
	private JComboBox<Road> _roadsComboBox;
	private DefaultComboBoxModel<Road> _roadsModel; // Modelo de roadsCombo para mostrar las carreteras de la simulación
													// adecuadamente
	private JLabel _weather;
	private JComboBox<Weather> _weatherComboBox;
	private JLabel _ticks;
	private JSpinner _ticksSpinner;

	private int _choice;

	ChangeWeatherDialog(Frame parent) {
		super(parent, "Change Road Weather", true);

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(460, 205));
		this.setResizable(false); // Para que no se pueda redimensionar

		initGUI();
	}

	private void initGUI() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// SUPERIOR
		JPanel superiorPanel = new JPanel(new BorderLayout());

		_desc = new JLabel(
				"<html><p>Schedule an event to change the weather of a road after a given number of simulation ticks from now.</p></html>");
		superiorPanel.add(_desc);

		// CENTRO
		JPanel centerPanel = new JPanel();
		_road = new JLabel("Road: ");
		_roadsModel = new DefaultComboBoxModel<Road>();
		_roadsComboBox = new JComboBox<>(_roadsModel);
		_roadsComboBox.setPreferredSize(new Dimension(85, 20));

		_weather = new JLabel("Weather: ");
		_weatherComboBox = new JComboBox<Weather>(Weather.values());
		_weatherComboBox.setPreferredSize(new Dimension(85, 20));

		_ticks = new JLabel("Ticks: ");
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		_ticksSpinner.setPreferredSize(new Dimension(60, 20));

		centerPanel.add(_road);
		centerPanel.add(_roadsComboBox);
		centerPanel.add(_weather);
		centerPanel.add(_weatherComboBox);
		centerPanel.add(_ticks);
		centerPanel.add(_ticksSpinner);

		// INFERIOR
		JPanel lowerPanel = new JPanel();

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_choice = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_choice = 1;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});

		lowerPanel.add(cancel);
		lowerPanel.add(ok);

		// Añadimos todo a mainPanel y creamos áreas rígidas
		mainPanel.add(superiorPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		mainPanel.add(centerPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));
		mainPanel.add(lowerPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

		this.pack();
	}

	void initializeDialog(List<Road> roads) { // Pasa la lista de roads al combobox de road cada vez que se utiliza el
												// diálogo, y pone valores por defecto

		_roadsModel.removeAllElements();
		_weatherComboBox.setSelectedIndex(0); // Para que aparezca inicializada la primera opción
		_ticksSpinner.setValue(1); // Para que aparezca incialmente 1 tick
		if (roads.size() != 0) {
			_roadsModel.addAll(roads);
			_roadsComboBox.setSelectedIndex(0); // Para que aparezca por defecto la primera road
		}
	}

	int getChoice() { // 0-Cancel, 1-OK
		return _choice;
	}

	Road getSelectedRoad() {

		return (Road) _roadsComboBox.getSelectedItem();
	}

	Weather getSelectedWeather() {

		return (Weather) _weatherComboBox.getSelectedItem();
	}

	int getSelectedTicks() {

		return (Integer) _ticksSpinner.getValue();
	}
}