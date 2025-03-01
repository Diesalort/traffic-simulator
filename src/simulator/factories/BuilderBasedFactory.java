package simulator.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _buildersInfo;

	public BuilderBasedFactory() {
		this._builders = new HashMap<>();
		this._buildersInfo = new LinkedList<>();
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
		this._buildersInfo.add(info);
	}

	@Override
	public T create_instance(JSONObject info) {
		if (info == null) {
			throw new IllegalArgumentException("'info' cannot be null");
		}

		String type = info.getString("type");
		Builder<T> builder = this._builders.get(type);

		if (builder != null) {

			JSONObject data = info.has("data") ? info.getJSONObject("data") : new JSONObject();
			T instance = builder.create_instance(data);

			if (instance != null)
				return instance;
		}

		throw new IllegalArgumentException("Unrecognized 'info': " + info.toString());
	}

	@Override
	public List<JSONObject> get_info() {
		return Collections.unmodifiableList(_buildersInfo);
	}

}
