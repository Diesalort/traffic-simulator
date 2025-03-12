package simulator.view;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog { //TODO

	private JComboBox<String> comboBox;
	private JLabel vehicle;
	private JLabel co2Class;
	private JLabel ticks;
	private String desc;
	private int indexComboBox;
	
	private JPanel panelIzquierdo;
	
	

	public ChangeCO2ClassDialog (Frame parent, ArrayList<Vehicle> vehicles){
		super(parent,true);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.initGUI();
	}
	
	private void initGUI(ArrayList<Vehicle> vehicles) {
		
	     JPanel mainPanel = new JPanel();
	     mainPanel.setLayout(null); //TODO layout?
		 
	     this.comboBox = new JComboBox<String>();
			for (int i=0; i < vehicles.size(); i++){
				this.comboBox.addItem(vehicles.get(i).getId());
			}
		
			
		
	}
	
	
}
