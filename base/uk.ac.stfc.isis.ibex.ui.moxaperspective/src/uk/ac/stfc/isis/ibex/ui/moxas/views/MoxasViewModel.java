package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.stfc.isis.ibex.configserver.MoxaMappings;
import uk.ac.stfc.isis.ibex.model.ModelObject;


/**
 * The Moxa mappings view model.
 */
public class MoxasViewModel extends ModelObject {
	
	private HashMap<String, MoxaList> moxaPorts = new HashMap<String, MoxaList>();
	private final MoxaMappings control; 
	
	/**
	 * represents all moxa physical port to COM port mappings. 
	 */

	public HashMap<String, MoxaList> getMoxaPorts() {
		HashMap<String, MoxaList> map = new HashMap<String, MoxaList>();
		
		HashMap<String, ArrayList<ArrayList<String>>> ret = control.getMappings().getValue();
		if (ret != null) {
		ret.forEach((key, value) -> {
			MoxaList list = new MoxaList(key);
			for (ArrayList<String> item : value) {
				list.add(new MoxaModelObject(item.get(0), item.get(1)));	
			}
			map.put(key, list);
		});
		}
		return map;
	};
	
	
	private PropertyChangeListener moxasListener;

	public MoxasViewModel(MoxaMappings control) {
		this.control = control;
        moxaPorts = getMoxaPorts();

        moxasListener = new PropertyChangeListener() {	
    		@Override
    		public void propertyChange(PropertyChangeEvent evt) {
    			firePropertyChange("moxaMappings", moxaPorts, moxaPorts = getMoxaPorts());
    		}
    	};
    	
    	control.getMappings().addPropertyChangeListener(moxasListener);
	}

	public void removeListeners() {
		control.getMappings().removePropertyChangeListener(moxasListener);
	}
}
