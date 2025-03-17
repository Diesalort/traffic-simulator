package simulator.model;

public enum Weather {
	SUNNY, CLOUDY, RAINY, WINDY, STORM;
	
	
	public String imageFile() {
		
		String imageFile = "";
		
		switch (this) {
		
		case SUNNY:
			imageFile = "sun.png";
			break;
		case CLOUDY:
			imageFile = "cloud.png";
			break;
		case RAINY:
			imageFile = "rain.png";
			break;
		case WINDY:
			imageFile = "wind.png";
			break;
		case STORM:
			imageFile = "storm.png";
			break;	
		}
		
		return imageFile;
	}
}
