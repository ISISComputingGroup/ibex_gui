package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.core.databinding.conversion.Converter;


/**
 * Takes the JSON list string and converts it into a nicely formatted list of names
 *
 */
public class UsersConverter extends Converter {
	
    public UsersConverter(Object fromType, Object toType) {
		super(fromType, toType);
	}

	@Override
	public Object convert(Object arg0) {
		String raw = "";
		
		List<String> names = new ArrayList<String>();
		
		try {
			names = new Gson().fromJson(arg0.toString(), new TypeToken<List<String>>() {}.getType());
		}
		catch (Exception err) {
			//It was not valid json, so just set users to nothing
		}
		
		if (names != null && names.size() > 0) {
			for (int i = 0; i < names.size(); ++i) {
				if (i == 0) {
					raw += names.get(i);
				}
				else if (i == names.size() - 1) {
					raw += " and " + names.get(i);
				}
				else {
					raw += ", " + names.get(i);
				}	
			}
		}
		
		return raw.trim();
	}

}
