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
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{


	private JToolBar toolBar;
	private Controller _ctrl;

	public ControlPanel(Controller ctrl) {

		_ctrl = ctrl;
		this.setLayout(new FlowLayout((FlowLayout.LEFT)));
		initGUI();
	}

	private void initGUI() {

		this.setLayout(new BorderLayout()); //TODO
		toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.add(toolBar, BorderLayout.PAGE_START);

		this.fileChooserConf();
		this.setContClassConf();
		this.changeRoadWeatherConf();
		this.runConf();
		this.stopConf();
		this.ticksConf();
		this.exitConf();
	}

	private void fileChooserConf() {

		// FileChooser
		JButton fileChooserButton = new JButton();
		fileChooserButton.setIcon(new ImageIcon("resources/icons/open.png"));
		toolBar.add(fileChooserButton); // Añadimos el button a la toolbar

		fileChooserButton.addActionListener(new ActionListener() { //TODO

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(new File("resources")); // Abre por defecto la carpeta resources

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
	}

	private void setContClassConf() {

		// SetContClass on vehicle TODO
		JButton setContClassButton = new JButton();
		setContClassButton.setIcon(new ImageIcon("resources/icons/co2class.png"));
		toolBar.add(setContClassButton); // Añadimos el button a la toolBar
		
		setContClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//TODO getWindowAncestor??, o pasar frame a constructor de controlPanel y guardarlo en un atributo, para pasarlo aqui?
				ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog((JFrame) SwingUtilities.getWindowAncestor(ControlPanel.this), null);
				dialog.setVisible(true);
				
			}			

		});
	}



	private void changeRoadWeatherConf() {
		
		JButton roadWeatherButton = new JButton();
		roadWeatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		toolBar.add(roadWeatherButton); // Añadimos el button a la toolBar

		roadWeatherButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER changeRoadWeather
				
			}
			
		});
		
	}
	
	private void runConf() {
		
		JButton runButton = new JButton();
		runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		toolBar.add(runButton); // Añadimos el button a la toolBar

		runButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER run
				
			}
			
		});
	}

	private void stopConf() {
		
		JButton stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		toolBar.add(stopButton); // Añadimos el button a la toolBar

		stopButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER stop
				
			}
			
		});
		
	}
	
	private void ticksConf() {
		
		//TODO
		
		
	}

	private void exitConf() {
		
		JButton exitButton = new JButton();
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		toolBar.add(exitButton); // Añadimos el button a la toolBar

		exitButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER exit
				
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
