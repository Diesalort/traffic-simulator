package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> { //TODO no pasa test 12!!
	  private Map<String, Builder<T>> _builders;
	  private List<JSONObject> _builders_info;

	  public BuilderBasedFactory() {
	    this._builders = new HashMap<>();
	    this._builders_info = new LinkedList<>();
	  }

	  public BuilderBasedFactory(List<Builder<T>> builders) {
	    this();

	    for (Builder<T> b : builders) {
	    	
	    	this.add_builder(b);
	    }
	    
	  }

	  public void add_builder(Builder<T> b) {
		  
		  String tag = b.get_type_tag();
		  this._builders.put(tag, b);
		  
		  JSONObject info = b.get_info();
		  this._builders_info.add(info);
	  }

	  @Override
	  public T create_instance(JSONObject info) {
	    if (info == null) {
	      throw new IllegalArgumentException("’info’ cannot be null");
	    }

	    String type = info.getString("type");
	    Builder<T> b = this._builders.get(type);
	    
	    if (b != null) {
	    	
	    	JSONObject data = info.has("data") ? info.getJSONObject("data") : new JSONObject();
	    	T instance = b.create_instance(data);
	    	
	    	if (instance != null)
	    		return instance;
	    }
	    
	    throw new IllegalArgumentException("Unrecognized ‘info’:" + info.toString());
	  }

	  
	  @Override
	  public List<JSONObject> get_info() {
	    return Collections.unmodifiableList(_builders_info);
	  }
	  
	}
