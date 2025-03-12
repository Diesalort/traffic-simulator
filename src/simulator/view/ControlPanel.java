package simulator.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{

	// TODO Usar toolbar
	
	private Controller _ctrl;
	private JButton fileChooser;

	public ControlPanel(Controller ctrl) {

		_ctrl = ctrl;
		this.setLayout(new FlowLayout((FlowLayout.LEFT)));
		initGUI();
	}

	private void initGUI() {

		this.setLayout(new BorderLayout()); //TODO
		JToolBar toolBar = new JToolBar();
		this.add(toolBar, BorderLayout.PAGE_START);
		
		// FileChooser
		JButton fileChooserButton = new JButton();
		fileChooserButton.setIcon(new ImageIcon("resources/icons/open.png"));

		fileChooserButton.addActionListener(new ActionListener() { //TODO

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				int selection = fileChooser.showOpenDialog(fileChooserButton);

				if (selection == JFileChooser.APPROVE_OPTION) { // Aceptar

					File fichero = fileChooser.getSelectedFile();

					try(InputStream in = new BufferedInputStream(new FileInputStream(fichero));){

						// Si ponemos this -> ControlPanel.this._ctrl.reset();
						_ctrl.reset();
						_ctrl.loadEvents(in);


					} catch (FileNotFoundException fnf) {
						//TODO null? QUITAR ABBORT y poner ICONO 
						JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ABORT);
					} catch (Exception ex) {

						JOptionPane.showMessageDialog(null, "An error happenned: " + ex.getMessage(), "Error", JOptionPane.ABORT);
					}

				} else if(selection == JFileChooser.CANCEL_OPTION) { // Cancelar

					//TODO CANCELAR
					
				}

			}
		});
		
		// SetContClass on vehicle TODO
		JButton setContClassButton = new JButton();
		setContClassButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
		
		setContClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}			

		});

		
		
		//Cambio de las condiciones atmosféricas de una carretera co2class TODO
		
		
		
		
		
		


		
		// Añadimos los componentes
		toolBar.add(fileChooserButton);		
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
