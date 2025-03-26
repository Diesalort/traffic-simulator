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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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

	private boolean _stopped;

	private RoadMap _map;
	private int _currTime;


	public ControlPanel(Controller ctrl) {

		_ctrl = ctrl;
		_ctrl.addObserver(this); // Añadimos ControlPanel como nuevo observador
		_stopped = true;
		this.setLayout(new BorderLayout()); // Con borderLayout haremos que la toolBar ocupe todo el ancho de la parte superior
		initGUI();
	}

	private void initGUI() {

		_toolbar = new JToolBar();

		this.fileChooserConf();
		_toolbar.addSeparator();;
		this.setContClassConf();
		this.changeRoadWeatherConf();
		_toolbar.addSeparator();
		this.runConf();
		this.stopConf();
		this.ticksConf();
		_toolbar.add(Box.createHorizontalGlue());
		_toolbar.addSeparator();
		this.exitConf();

		_toolbar.setEnabled(false);
		this.add(_toolbar, BorderLayout.PAGE_START);
	}

	private void fileChooserConf() {

		// FileChooser
		_fileChooserButton = createButton("resources/icons/open.png", "Choose a file as simulation");
		_toolbar.add(_fileChooserButton); // Añadimos el button a la toolbar

		_fileChooserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(new File("resources/examples")); // Abre por defecto la carpeta resources/examples

				int selection = fileChooser.showOpenDialog(ViewUtils.getWindow(ControlPanel.this));

				if (selection == JFileChooser.APPROVE_OPTION) { // Aceptar

					File fichero = fileChooser.getSelectedFile();

					try(InputStream in = new BufferedInputStream(new FileInputStream(fichero));){

						// Si ponemos this, se hace --> ControlPanel.this._ctrl.reset();
						_ctrl.reset();
						_ctrl.loadEvents(in);

					} catch (FileNotFoundException fnf) {
						ViewUtils.showErrorMsg(ViewUtils.getWindow(ControlPanel.this), "File not found");
					} catch (Exception ex) {
						ViewUtils.showErrorMsg(ViewUtils.getWindow(ControlPanel.this), "An error happenned: " + ex.getMessage());
					}
				}
			}
		});
	}

	private void setContClassConf() {

		_setContClassButton = createButton("resources/icons/co2class.png", "Change CO2 Class of a Vehicle");
		_toolbar.add(_setContClassButton); // Añadimos el button a la toolBar

		_setContClassButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ChangeCO2ClassDialog changeCO2Dialog = new ChangeCO2ClassDialog(ViewUtils.getWindow(ControlPanel.this), _ctrl, _map.getVehicles(), _currTime);
			}			
		});
	}



	private void changeRoadWeatherConf() {

		_roadWeatherButton = createButton("resources/icons/weather.png", "Change Weather of a Road");
		_toolbar.add(_roadWeatherButton); // Añadimos el button a la toolBar

		_roadWeatherButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {

				ChangeWeatherDialog changeWeatherDialog = new ChangeWeatherDialog (ViewUtils.getWindow(ControlPanel.this), _ctrl, _map.getRoads(), _currTime);
			}

		});

	}

	private void runConf() {

		_runButton = createButton("resources/icons/run.png", "Run the simulator");
		_toolbar.add(_runButton); // Añadimos el button a la toolBar

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

		_stopButton = createButton("resources/icons/stop.png", "Stop the simulator");
		_toolbar.add(_stopButton); // Añadimos el button a la toolBar

		_stopButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}

		});

	}

	private void ticksConf() {

		JLabel ticksLabel = new JLabel ("Ticks: ");
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 10000, 1));
		_ticksSpinner.setMaximumSize(new Dimension(80, 40));
		_ticksSpinner.setMinimumSize(new Dimension(20, 40));
		_ticksSpinner.setPreferredSize(new Dimension(80, 40));
		_ticksSpinner.setToolTipText("Simulation tick to run: 1-10000");

		_toolbar.add(ticksLabel);
		_toolbar.add(_ticksSpinner);
	}

	private void exitConf() {

		_exitButton = createButton("resources/icons/exit.png", "Exit the simulator");
		_toolbar.add(_exitButton); // Añadimos el button a la toolBar

		_exitButton.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {

				ViewUtils.quit(_exitButton);
			}
		});
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
				SwingUtilities.invokeLater(() -> run_sim(n - 1));
			} catch (Exception e) {
				// show error message
				ViewUtils.showErrorMsg(ViewUtils.getWindow(ControlPanel.this), "An error happenned: " + e.getMessage());
				_stopped = true;
				enableToolbar(true);
			}
		} else {
			_stopped = true;
			enableToolbar(true);
		}
	}

	private JButton createButton(String iconPath, String tooltipText) { //TODO Añadir al parametro un ActionListener para crearlos de forma generica con funciones auxiliares

		JButton button = new JButton();		
		button.setIcon(new ImageIcon(iconPath));
		button.setToolTipText(tooltipText);
		return button;
	}

	private void enableToolbar(boolean b) {

		_fileChooserButton.setEnabled(b);
		_setContClassButton.setEnabled(b);
		_roadWeatherButton.setEnabled(b);
		_runButton.setEnabled(b);
		_ticksSpinner.setEnabled(b);
		_exitButton.setEnabled(b);
	}


	private void update(RoadMap map, int time) {
		
		_map = map;
		_currTime = time;
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
