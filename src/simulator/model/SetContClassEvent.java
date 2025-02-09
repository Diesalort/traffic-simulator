package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {

	private List<Pair<String,Integer>> _cs;
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs)  {
		  super(time);
		  
		  if (cs == null)
			  throw new IllegalArgumentException("La lista cs es nula");
		  
		this._cs = cs;  
	}

	@Override
	void execute(RoadMap map) {

		for (Pair<String, Integer> c : this._cs) {
			
			Vehicle v = map.getVehicle(c.getFirst());
			
			if (v == null)
				throw new IllegalArgumentException("El vehículo con id " + c.getFirst() + " no existe en el mapa de carreteras");
			
			v.setContaminationClass(c.getSecond());
		}
	}

}
