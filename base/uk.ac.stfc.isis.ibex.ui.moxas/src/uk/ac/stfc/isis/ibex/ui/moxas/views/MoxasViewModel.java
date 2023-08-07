package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The Moxa mappings view model.
 */
public class MoxasViewModel extends ModelObject {

	private HashMap<String, MoxaList> moxaPorts = new HashMap<String, MoxaList>();
	private final Configurations control;

	/**
	 * Represents all moxa physical port to COM port mappings.
	 * 
	 * @return a map of Moxa port to serial port mappings by Moxa device
	 */
	public HashMap<String, MoxaList> getMoxaPorts() {
		HashMap<String, MoxaList> map = new HashMap<String, MoxaList>();

		HashMap<String, ArrayList<ArrayList<String>>> ret = control.moxaMappings().getValue();
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

	/**
	 * The constructor of the viewmodel.
	 * 
	 * @param control The configserver singleton instance holding configurations
	 *                info
	 */
	public MoxasViewModel(Configurations control) {
		this.control = control;
		moxaPorts = getMoxaPorts();

		moxasListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("moxaMappings", moxaPorts, moxaPorts = getMoxaPorts());
			}
		};

		control.moxaMappings().addPropertyChangeListener(moxasListener);
	}

	/**
	 * Removes listeners on Moxa mappings.
	 */
	public void removeListeners() {
		control.moxaMappings().removePropertyChangeListener(moxasListener);
	}
}
