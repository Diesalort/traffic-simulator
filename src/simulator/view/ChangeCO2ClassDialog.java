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
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JLabel _desc;
	private JLabel _vehicle;
	private JComboBox<Vehicle> _vehiclesComboBox;
	private DefaultComboBoxModel<Vehicle> _vehiclesModel; // Modelo de vehiclesCombo para mostrar los vehículos de la
															// simulación adecuadamente
	private JLabel _CO2Class;
	private JComboBox<Integer> _CO2ClassComboBox;
	private JLabel _ticks;
	private JSpinner _ticksSpinner;

	private int _choice;

	ChangeCO2ClassDialog(Frame parent) {
		super(parent, "Change CO2 Class", true);

		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(460, 205));
		this.setResizable(false); // Para que no se pueda redimensionar

		initGUI();
	}

	private void initGUI() {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);
		;

		// SUPERIOR
		JPanel superiorPanel = new JPanel(new BorderLayout());

		_desc = new JLabel(
				"<html><p>Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.</p></html>");
		superiorPanel.add(_desc);

		// CENTRO
		JPanel centerPanel = new JPanel();
		_vehicle = new JLabel("Vehicle: ");
		_vehiclesModel = new DefaultComboBoxModel<Vehicle>();
		_vehiclesComboBox = new JComboBox<>(_vehiclesModel);
		_vehiclesComboBox.setPreferredSize(new Dimension(85, 20));

		_CO2Class = new JLabel("CO2 Class: ");
		DefaultComboBoxModel<Integer> range = new DefaultComboBoxModel<>();
		for (int i = 0; i <= 10; i++)
			range.addElement(i);

		_CO2ClassComboBox = new JComboBox<Integer>(range);
		_CO2ClassComboBox.setPreferredSize(new Dimension(70, 20));

		_ticks = new JLabel("Ticks: ");
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		_ticksSpinner.setPreferredSize(new Dimension(60, 20));

		centerPanel.add(_vehicle);
		centerPanel.add(_vehiclesComboBox);
		centerPanel.add(_CO2Class);
		centerPanel.add(_CO2ClassComboBox);
		centerPanel.add(_ticks);
		centerPanel.add(_ticksSpinner);

		// INFERIOR
		JPanel lowerPanel = new JPanel();

		JButton cancel = new JButton("Cancel");

		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_choice = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_choice = 1;
				ChangeCO2ClassDialog.this.setVisible(false);
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

	void initializeDialog(List<Vehicle> vehicles) { // Pasa la lista de vehículos al combobox de vehicles cada vez que
													// se utiliza el diálogo, y pone valores por defecto

		_vehiclesModel.removeAllElements();
		_CO2ClassComboBox.setSelectedIndex(0); // Para que aparezca 0 por defecto
		_ticksSpinner.setValue(1); // Para que aparezca el tick 1 por defecto
		if (vehicles.size() != 0) {
			_vehiclesModel.addAll(vehicles);
			_vehiclesComboBox.setSelectedIndex(0); // Para dejar el primer vehículo seleccionado por defecto
		}
	}

	int getChoice() { // 0-Cancel, 1-OK
		return _choice;
	}

	Vehicle getSelectedVehicle() {

		return (Vehicle) _vehiclesComboBox.getSelectedItem();
	}

	int getSelectedContClass() {

		return (Integer) _CO2ClassComboBox.getSelectedItem();
	}

	int getSelectedTicks() {

		return (Integer) _ticksSpinner.getValue();
	}

}