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
import simulator.model.SetContClassEvent;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	Controller _ctrl;
	
	JLabel desc;

	JLabel vehicle;
	JComboBox<String> vehiclesCombo;

	JLabel CO2Class;
	JComboBox<Integer> CO2ClassCombo;

	JLabel ticks;
	JSpinner ticksSpinner;

	ChangeCO2ClassDialog(Frame parent, Controller ctrl, List<Vehicle> vehicles){
		super(parent, true);
		_ctrl = ctrl;
		
		this.setTitle("Change CO2 Class");
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setResizable(false); // Para que no se pueda redimensionar
		
		List<String> vehiclesId = new ArrayList<>();
		for (Vehicle v : vehicles) {

			vehiclesId.add(v.getId());
		}

		initGUI(vehiclesId);		
	}

	private void initGUI(List<String> vehiclesId) {

		this.setLayout(new BorderLayout());

		desc = new JLabel();
		desc.setText("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now");

		JPanel centerPanel = new JPanel(new FlowLayout()); //TODO FlowLayout?
		vehicle = new JLabel ("Vehicle: ");
		vehiclesCombo = new JComboBox<String>(vehiclesId.toArray(new String[0])); // Convertimos la lista de ids de vehiculos a un array de string, y lo pasamos al jcombobox

		CO2Class = new JLabel("CO2 Class: ");
		CO2ClassCombo = new JComboBox<Integer>();
		for (int i = 0; i <= 10; i++) {

			CO2ClassCombo.addItem(i);
		}
		
		ticks = new JLabel("Ticks: ");
		ticksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

		centerPanel.add(vehicle);
		centerPanel.add(vehiclesCombo);
		centerPanel.add(CO2Class);
		centerPanel.add(CO2ClassCombo);
		centerPanel.add(ticks);
		centerPanel.add(ticksSpinner);
		
		JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton cancel = new JButton("Cancel");
		
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeCO2ClassDialog.this.setVisible(false);							
			}
		});
		
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				addEvent();
				ChangeCO2ClassDialog.this.setVisible(false);							
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
		
		String vehicleId = (String) vehiclesCombo.getSelectedItem();
		
		if (vehicleId == null) {
			this.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), "A simulation must be running to add an event", "Error", JOptionPane.ERROR_MESSAGE);
			
		} else {
			
			int contClass = (Integer) CO2ClassCombo.getSelectedItem();
			int ticks = (Integer) ticksSpinner.getValue();
			
			List<Pair<String, Integer>> cs = new ArrayList<>();
			cs.add(new Pair<>(vehicleId, contClass));
			
			_ctrl.addEvent(new SetContClassEvent(_ctrl.getCurrentTime() + ticks, cs)); // Debemos sumar el currentTime + los ticks seleccionados			
		}
	}
}
