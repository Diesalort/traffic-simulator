package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{


	private JToolBar toolbar;
	private Controller _ctrl;

	public ControlPanel(Controller ctrl) {

		_ctrl = ctrl;
		this.setLayout(new BorderLayout()); // Con borderLayout haremos que la toolBar ocupe todo el ancho de la parte superior
		initGUI();
	}

	private void initGUI() {

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.LINE_AXIS));		

		this.fileChooserConf();
		this.setContClassConf();
		this.changeRoadWeatherConf();
		this.runConf();
		this.stopConf();
		this.ticksConf();
		toolbar.add(Box.createHorizontalGlue());
		this.exitConf();

		this.add(toolbar, BorderLayout.CENTER);
	}

	private void fileChooserConf() {

		// FileChooser
		JButton fileChooserButton = createButton("resources/icons/open.png");
		toolbar.add(fileChooserButton); // Añadimos el button a la toolbar

		fileChooserButton.addActionListener(new ActionListener() { //TODO

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(new File("resources")); // Abre por defecto la carpeta resources

				int selection = fileChooser.showOpenDialog(fileChooserButton);

				if (selection == JFileChooser.APPROVE_OPTION) { // Aceptar

					File fichero = fileChooser.getSelectedFile();

					try(InputStream in = new BufferedInputStream(new FileInputStream(fichero));){

						// Si ponemos this, se hace --> ControlPanel.this._ctrl.reset();
						_ctrl.reset();
						_ctrl.loadEvents(in);


					} catch (FileNotFoundException fnf) {
						//TODO null? QUITAR ABBORT y poner ICONO 
						JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ABORT);
					} catch (Exception ex) {

						JOptionPane.showMessageDialog(null, "An error happenned: " + ex.getMessage(), "Error", JOptionPane.ABORT);
					}

				}
			}
		});
	}

	private void setContClassConf() {

		// SetContClass on vehicle TODO

		JButton setContClassButton = createButton("resources/icons/co2class.png");
		toolbar.add(setContClassButton); // Añadimos el button a la toolBar

		setContClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				//TODO getWindowAncestor??, o metodo ContainerPanel.this.getParent()
				ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog((JFrame) SwingUtilities.getWindowAncestor(ControlPanel.this), null);
				dialog.setVisible(true);

			}			

		});
	}



	private void changeRoadWeatherConf() {

		JButton roadWeatherButton = createButton("resources/icons/weather.png");
		toolbar.add(roadWeatherButton); // Añadimos el button a la toolBar

		roadWeatherButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER changeRoadWeather

			}

		});

	}

	private void runConf() {

		JButton runButton = createButton("resources/icons/run.png");
		toolbar.add(runButton); // Añadimos el button a la toolBar

		runButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER run

			}

		});
	}

	private void stopConf() {

		JButton stopButton = createButton("resources/icons/stop.png");
		toolbar.add(stopButton); // Añadimos el button a la toolBar

		stopButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER stop

			}

		});

	}

	private void ticksConf() {

		JLabel ticksLabel = new JLabel ("Ticks: ");
		JSpinner ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
		ticksSpinner.setMaximumSize(new Dimension(80, 40));
		ticksSpinner.setMinimumSize(new Dimension(80, 40));
		ticksSpinner.setPreferredSize(new Dimension(80, 40));

		toolbar.add(ticksLabel);
		toolbar.add(ticksSpinner);
	}

	private void exitConf() {

		JButton exitButton = createButton("resources/icons/exit.png");
		toolbar.add(exitButton); // Añadimos el button a la toolBar

		exitButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO en vez de ancestor, metodo getParent() ? (Ver ejemplos de JSpinner del campus)
				int n = JOptionPane.showOptionDialog(ControlPanel.this.getParent(), "Are sure you want to quit?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (n == 0) {
					System.exit(0);
				}

			}

		});
	}

	private JButton createButton(String iconPath) { //TODO Añadir al parametro un ActionListener para crearlos de forma generica con funciones auxiliares

		JButton button = new JButton();		
		button.setIcon(new ImageIcon(iconPath));
		return button;
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
