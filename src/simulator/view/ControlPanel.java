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

	private JButton _fileChooserButton;
	private JButton _setContClassButton;
	private JButton _roadWeatherButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JSpinner _ticksSpinner;
	private JButton _exitButton;


	private boolean _stopped;


	public ControlPanel(Controller ctrl) {

		_ctrl = ctrl;
		_stopped = true;
		this.setLayout(new BorderLayout()); // Con borderLayout haremos que la toolBar ocupe todo el ancho de la parte superior
		initGUI();
	}

	private void initGUI() {

		toolbar = new JToolBar();

		this.fileChooserConf();
		toolbar.addSeparator();;
		this.setContClassConf();
		this.changeRoadWeatherConf();
		toolbar.addSeparator();
		this.runConf();
		this.stopConf();
		this.ticksConf();
		toolbar.add(Box.createHorizontalGlue());
		this.exitConf();

		toolbar.setEnabled(false);
		this.add(toolbar, BorderLayout.PAGE_START);
	}

	private void fileChooserConf() {

		// FileChooser
		_fileChooserButton = createButton("resources/icons/open.png");
		toolbar.add(_fileChooserButton); // Añadimos el button a la toolbar

		_fileChooserButton.addActionListener(new ActionListener() { //TODO

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(new File("resources")); // Abre por defecto la carpeta resources

				int selection = fileChooser.showOpenDialog(_fileChooserButton);

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

		_setContClassButton = createButton("resources/icons/co2class.png");
		toolbar.add(_setContClassButton); // Añadimos el button a la toolBar

		_setContClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				//TODO getWindowAncestor??, o metodo ContainerPanel.this.getParent()
				ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog((JFrame) SwingUtilities.getWindowAncestor(ControlPanel.this), null);
				dialog.setVisible(true);

			}			

		});
	}



	private void changeRoadWeatherConf() {

		_roadWeatherButton = createButton("resources/icons/weather.png");
		toolbar.add(_roadWeatherButton); // Añadimos el button a la toolBar

		_roadWeatherButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO LISTENER changeRoadWeather

			}

		});

	}

	private void runConf() {

		_runButton = createButton("resources/icons/run.png");
		toolbar.add(_runButton); // Añadimos el button a la toolBar

		_runButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ticks = (Integer) _ticksSpinner.getValue();
				_stopped = false;
				enableToolbar(false);
				run_sim(ticks);
			}

		});
	}

	private void stopConf() {

		_stopButton = createButton("resources/icons/stop.png");
		toolbar.add(_stopButton); // Añadimos el button a la toolBar

		_stopButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}

		});

	}

	private void ticksConf() {

		JLabel ticksLabel = new JLabel ("Ticks: ");
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
		_ticksSpinner.setMaximumSize(new Dimension(80, 40));
		_ticksSpinner.setMinimumSize(new Dimension(80, 40));
		_ticksSpinner.setPreferredSize(new Dimension(80, 40));

		toolbar.add(ticksLabel);
		toolbar.add(_ticksSpinner);
	}

	private void exitConf() {

		_exitButton = createButton("resources/icons/exit.png");
		toolbar.add(_exitButton); // Añadimos el button a la toolBar

		_exitButton.addActionListener(new ActionListener () {

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

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
				SwingUtilities.invokeLater(() -> run_sim(n - 1));
			} catch (Exception e) {
				// TODO show error message
				_stopped = true;
				enableToolbar(true);
			}
		} else {
			_stopped = true;
			enableToolbar(true);
		}
	}

	private JButton createButton(String iconPath) { //TODO Añadir al parametro un ActionListener para crearlos de forma generica con funciones auxiliares

		JButton button = new JButton();		
		button.setIcon(new ImageIcon(iconPath));
		return button;
	}

	private void enableToolbar(boolean b) { //TODO se hace asi o de otra forma?

		_fileChooserButton.setEnabled(b);
		_setContClassButton.setEnabled(b);
		_roadWeatherButton.setEnabled(b);
		_runButton.setEnabled(b);
		_ticksSpinner.setEnabled(b);
		_exitButton.setEnabled(b);
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
