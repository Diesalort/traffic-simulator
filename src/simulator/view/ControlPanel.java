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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private JToolBar _toolbar;
	private Controller _ctrl;

	private JButton _fileChooserButton;
	private JButton _setContClassButton;
	private JButton _roadWeatherButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JSpinner _ticksSpinner;
	private JButton _exitButton;
	private JSpinner _delaySpinner;

	private volatile Thread _thread;

	private RoadMap _map;
	private int _currTime;

	// Dialogos
	private ChangeCO2ClassDialog _changeCO2Dialog;
	private ChangeWeatherDialog _changeWeatherDialog;

	private static final int OK = 1;

	public ControlPanel(Controller ctrl) {

		_ctrl = ctrl;
		_ctrl.addObserver(this); // Añadimos ControlPanel como nuevo observador
		this.setLayout(new BorderLayout()); // Con borderLayout haremos que la toolBar ocupe todo el ancho de la parte
		// superior
		initGUI();
	}

	private void initGUI() {

		_toolbar = new JToolBar();

		this.fileChooserConf();
		_toolbar.addSeparator();
		this.setContClassConf();
		this.changeRoadWeatherConf();
		_toolbar.addSeparator();
		this.runConf();
		this.stopConf();
		this.ticksConf();
		this.delayConf();
		_toolbar.add(Box.createHorizontalGlue());
		_toolbar.addSeparator();
		this.exitConf();

		this.add(_toolbar, BorderLayout.PAGE_START);
	}

	private void fileChooserConf() {

		// FileChooser
		ActionListener fileChooserListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(new File("resources/examples")); // Abre por defecto la
				// carpeta
				// resources/examples

				int selection = fileChooser.showOpenDialog(ViewUtils.getWindow(ControlPanel.this));

				if (selection == JFileChooser.APPROVE_OPTION) { // Aceptar

					File fichero = fileChooser.getSelectedFile();

					try (InputStream in = new BufferedInputStream(new FileInputStream(fichero));) {

						// Si ponemos this, se hace --> ControlPanel.this._ctrl.reset();
						_ctrl.reset();
						_ctrl.loadEvents(in);

					} catch (FileNotFoundException fnf) {
						ViewUtils.showErrorMsg(ViewUtils.getWindow(ControlPanel.this), "File not found");
					} catch (Exception ex) {
						ViewUtils.showErrorMsg(ViewUtils.getWindow(ControlPanel.this),
								"An error happenned: " + ex.getMessage());
					}
				}
			}
		};

		_fileChooserButton = createButton("resources/icons/open.png", "Choose a file as simulation",
				fileChooserListener);
		_toolbar.add(_fileChooserButton); // Añadimos el button a la toolbar
	}

	private void setContClassConf() {

		ActionListener setContClassListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (_changeCO2Dialog == null)
					_changeCO2Dialog = new ChangeCO2ClassDialog(ViewUtils.getWindow(ControlPanel.this));

				_changeCO2Dialog.initializeDialog(_map.getVehicles()); // Le pasamos al jdialog la lista de vehiculos
				// actual
				showDialog(_changeCO2Dialog); // Mostramos diálogo

				if (_changeCO2Dialog.getChoice() == OK)
					addSetContClassEvent();
			}
		};

		_setContClassButton = createButton("resources/icons/co2class.png", "Change CO2 Class of a Vehicle",
				setContClassListener);
		_toolbar.add(_setContClassButton); // Añadimos el button a la toolBar
	}

	private void addSetContClassEvent() {

		Vehicle v = _changeCO2Dialog.getSelectedVehicle();

		if (v == null) { // Si el vehículo seleccionado es nulo...
			JOptionPane.showMessageDialog(getParent(), "A valid simulation must be running to add an event", "Error",
					JOptionPane.ERROR_MESSAGE);

		} else { // Añadimos el evento

			int contClass = _changeCO2Dialog.getSelectedContClass();
			int ticks = _changeCO2Dialog.getSelectedTicks();

			List<Pair<String, Integer>> cs = new ArrayList<>();
			cs.add(new Pair<>(v.getId(), contClass));

			_ctrl.addEvent(new SetContClassEvent(_currTime + ticks, cs)); // Debemos sumar el currTime + los ticks
			// seleccionados
		}
	}

	private void changeRoadWeatherConf() {

		ActionListener roadWeatherListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (_changeWeatherDialog == null)
					_changeWeatherDialog = new ChangeWeatherDialog(ViewUtils.getWindow(ControlPanel.this));

				_changeWeatherDialog.initializeDialog(_map.getRoads()); // Le pasamos al jdialog la lista de carreteras
				// actual
				showDialog(_changeWeatherDialog); // Mostramos diálogo

				if (_changeWeatherDialog.getChoice() == OK)
					addSetWeatherEvent();

			}
		};

		_roadWeatherButton = createButton("resources/icons/weather.png", "Change Weather of a Road",
				roadWeatherListener);
		_toolbar.add(_roadWeatherButton); // Añadimos el button a la toolBar
	}

	private void addSetWeatherEvent() {

		Road r = _changeWeatherDialog.getSelectedRoad();

		if (r == null) { // Si la carretera seleccionada es nula...
			JOptionPane.showMessageDialog(getParent(), "A valid simulation must be running to add an event", "Error",
					JOptionPane.ERROR_MESSAGE);

		} else { // Añadimos el evento

			Weather weather = (Weather) _changeWeatherDialog.getSelectedWeather();
			int ticks = (Integer) _changeWeatherDialog.getSelectedTicks();

			List<Pair<String, Weather>> ws = new ArrayList<>();
			ws.add(new Pair<>(r.getId(), weather));

			_ctrl.addEvent(new SetWeatherEvent(_currTime + ticks, ws)); // Debemos sumar el currTime + los ticks
			// seleccionados
		}
	}

	private void runConf() {

		ActionListener runListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ticks = (Integer) _ticksSpinner.getValue();
				_thread = new Thread(new Runnable() {

					@Override
					public void run() {
						enableToolbar(false);
						long delay = ((Number) _delaySpinner.getValue()).longValue();
						run_sim(ticks, delay);
					}				
				});
				
				_thread.start();
			}

		};

		_runButton = createButton("resources/icons/run.png", "Run the simulator", runListener);
		_toolbar.add(_runButton); // Añadimos el button a la toolBar
	}

	private void stopConf() {

		ActionListener stopListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_thread != null) {
					_thread.interrupt();
				}
			}

		};

		_stopButton = createButton("resources/icons/stop.png", "Stop the simulator", stopListener);
		_toolbar.add(_stopButton); // Añadimos el button a la toolBar
	}

	private void ticksConf() {

		JLabel ticksLabel = new JLabel("Ticks: ");
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 10000, 1));
		_ticksSpinner.setMaximumSize(new Dimension(80, 40));
		_ticksSpinner.setMinimumSize(new Dimension(20, 40));
		_ticksSpinner.setPreferredSize(new Dimension(80, 40));
		_ticksSpinner.setToolTipText("Simulation tick to run: 1-10000");

		_toolbar.add(ticksLabel);
		_toolbar.add(_ticksSpinner);
	}

	private void delayConf() {

		JLabel delayLabel = new JLabel("Delay: ");
		_delaySpinner = new JSpinner(new SpinnerNumberModel(10, 0, 1000, 1));
		_delaySpinner.setMaximumSize(new Dimension(80, 40));
		_delaySpinner.setMinimumSize(new Dimension(20, 40));
		_delaySpinner.setPreferredSize(new Dimension(80, 40));
		_delaySpinner.setToolTipText("Delay between simulation steps: 1-1000");

		_toolbar.add(delayLabel);
		_toolbar.add(_delaySpinner);
	}

	private void exitConf() {

		ActionListener exitListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ViewUtils.quit(_exitButton);
			}
		};

		_exitButton = createButton("resources/icons/exit.png", "Exit the simulator", exitListener);
		_toolbar.add(_exitButton); // Añadimos el button a la toolBar

	}

	private void run_sim(int n, long delay) {

		while ( n > 0 && (!Thread.currentThread().isInterrupted()) ) {

			try {
				_ctrl.run(1);
				Thread.sleep(delay);
				n--;

			} catch (InterruptedException e) {				
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				// show error message
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						ViewUtils.showErrorMsg(ViewUtils.getWindow(ControlPanel.this), "An error happenned: " + e.getMessage());
						enableToolbar(true);
					}					
				});
				return; // Salimos del método si ocurre error
			}
		}
		
		// Activamos toolbar y ponemos _thread a null
		SwingUtilities.invokeLater(() -> {
			enableToolbar(true);
		});
		_thread = null;
	}

	private JButton createButton(String iconPath, String tooltipText, ActionListener listener) {

		JButton button = new JButton();
		button.setIcon(new ImageIcon(iconPath));
		button.setToolTipText(tooltipText);
		button.addActionListener(listener);
		return button;
	}

	private void enableToolbar(boolean b) {

		_fileChooserButton.setEnabled(b);
		_setContClassButton.setEnabled(b);
		_roadWeatherButton.setEnabled(b);
		_runButton.setEnabled(b);
		_ticksSpinner.setEnabled(b);
		_delaySpinner.setEnabled(b);
		_exitButton.setEnabled(b);
	}

	private void update(RoadMap map, int time) {

		SwingUtilities.invokeLater(new Runnable () {

			@Override
			public void run() {
				_map = map;
				_currTime = time;
			}
		});
	}

	private void showDialog(JDialog d) {

		JFrame parent = (JFrame) ViewUtils.getWindow(ControlPanel.this);
		d.setLocation(parent.getX() + (parent.getWidth() - d.getWidth()) / 2,
				parent.getY() + (parent.getHeight() - d.getHeight()) / 2); // Para que aparezca centrado con respecto a
		// mainWindow
		d.setVisible(true);
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		update(map, time);
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		update(map, time);
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		update(map, time);
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		update(map, time);
	}

}