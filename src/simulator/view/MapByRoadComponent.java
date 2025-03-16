package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private static final Color _ROAD_AND_ID = Color.BLACK;

	private RoadMap _map;
	private Image _car;

	public MapByRoadComponent(Controller ctrl) {

		initGUI();
		this.setPreferredSize(new Dimension (300, 200));
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize(); TODO necesario
			drawMap(g);
		}
	}	

	private void drawMap(Graphics g) {

		List<Road> roads = _map.getRoads();

		for (int i = 0; i < roads.size(); i++) {

			Road r = roads.get(i);

			int x1 = 50;
			int x2 = getWidth()-100;
			int y = (i+1)*50;			

			drawRoad(g, r, x1, x2, y);
			drawJunctions(g, r, x1, x2, y);
			drawVehicles(g, r, x1, x2, y);
			drawWeather(g, r, x2, y);
			drawContamination(g, r, x2, y);
		}
	}

	private void drawRoad(Graphics g, Road r, int x1, int x2, int y) {

		// Road
		g.setColor(_ROAD_AND_ID);
		g.drawLine(x1, y, x2, y);
		// Road ID
		g.drawString(r.getId(), x1 - 30, y);
	}

	private void drawJunctions(Graphics g, Road r, int x1, int x2, int y) {
		// Source junction
		g.setColor(_JUNCTION_COLOR); // Blue
		g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junction's identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getSrc().getId(), x1, y - 10);

		// Destiny junction
		Color destJuncColor = _RED_LIGHT_COLOR;

		// Junction's color
		int idx = r.getDest().getGreenLightIndex();
		if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) {
			destJuncColor = _GREEN_LIGHT_COLOR;
		}

		g.setColor(destJuncColor);
		g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

		// draw the junction's identifier at (x,y)
		g.setColor(_JUNCTION_LABEL_COLOR);
		g.drawString(r.getDest().getId(), x2, y - 10);
	}

	private void drawVehicles(Graphics g, Road r, int x1, int x2, int y) {
		List<Vehicle> vehicles = r.getVehicles();
		for (Vehicle v : vehicles) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation()/ (double) r.getLength()));
				g.drawImage(_car, x, y - 6, 12, 12, this); // Imagen car
			}
		}
	}

	private void drawWeather(Graphics g, Road r, int x2, int y) {

		Image weather = loadImage(r.getWeather().toString() + ".png"); //TODO RENOMBRAR IMAGENES?
		g.drawImage(weather, x2 + 12, y - 17, 32, 32, this); // Imagen weather
	}

	private void drawContamination(Graphics g, Road r, int x2, int y) {
		int C = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getContLimit()),1.0) / 0.19);
		Image contamination = loadImage("cont_" + C + ".png");
		g.drawImage(contamination, x2 + 48, y - 17, 32, 32, this); // Imagen contamination
	}
	
	
	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		update(map);		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		update(map);		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		update(map);
	}

}
