package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog { //TODO

	private JComboBox<String> comboBox;
	private String desc;
	private int indexComboBox;

	private JTextArea superiorTextArea;

	private JPanel mediumPanel;

	private JComboBox<String> vehiclesCombo; //TODO comboBox de vehicle o string??
	private JComboBox<Integer> co2ClassCombo;
	private JSpinner ticksSpinner;

	private JLabel vehicleLabel;
	private JLabel co2ClassLabel;
	private JLabel ticksLabel;


	private JPanel inferiorPanel;


	public ChangeCO2ClassDialog (Frame parent, ArrayList<Vehicle> vehicles){
		super(parent,true);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initGUI(vehicles);
	}

	private void initGUI(ArrayList<Vehicle> vehicles) {

		// TODO Usar borderLayout y dividir en north, center, south??
		// Haremos un mainpanel con BoxLayout vertical, en el que la primera fila contendrá un textArea y las demás un panel con un flowLayout
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		this.setContentPane(mainPanel);

		// Fila 1
		superiorTextArea = new JTextArea(2, 37); // Área de 2 filas y 25 columnas
		superiorTextArea.setText("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now");
		superiorTextArea.setLineWrap(true); // Saltos de línea automáticos
		superiorTextArea.setWrapStyleWord(true); // Para que no se corten las palabras
		superiorTextArea.setEditable(false);
		superiorTextArea.setPreferredSize(new Dimension(100, 100));
		mainPanel.add(superiorTextArea);

		// Fila 2
		JPanel mediumPanel = new JPanel(new FlowLayout());

		vehicleLabel = new JLabel("Vehicle:");
		mediumPanel.add(vehicleLabel);

		vehiclesCombo = new JComboBox<String>();
		// Inicializamos los valores del comboBox
		/*
		for (int i=0; i < vehicles.size(); i++){
			this.comboBox.addItem(vehicles.get(i).getId());
		}
		 */
		mediumPanel.add(vehiclesCombo);

		co2ClassLabel = new JLabel("CO2 Class:");

		co2ClassCombo = new JComboBox<Integer>();
		for (int i = 0; i <= 10; i++) {

			co2ClassCombo.addItem(i);
		}

		mediumPanel.add(co2ClassLabel);
		mediumPanel.add(co2ClassCombo);

		ticksLabel = new JLabel("Ticks:");

		ticksSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
		
		
		mediumPanel.add(ticksLabel);
		mediumPanel.add(ticksSpinner);

		mainPanel.add(mediumPanel);

		// Fila 3




		this.pack();
	}


}
