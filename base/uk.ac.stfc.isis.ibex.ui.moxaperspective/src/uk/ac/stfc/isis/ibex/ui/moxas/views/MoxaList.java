package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.util.ArrayList;

/**
 * 
 * Extension of Arraylist to be able to hold the label to use on the tree, usually the description of the first
 * element but sometimes Running or In Config.
 *
 */
@SuppressWarnings("serial")
public class MoxaList extends ArrayList<MoxaModelObject> {
	/**
	 *  The name of the list of Iocs for use in the Tree.
	 */
	public String name;
	
	/**
	 * Constructor to create List with name set.
	 * @param name - name to set on the list.
	 */
	public MoxaList(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object b) {
		if (b instanceof MoxaList) {
			if (this.name != MoxaList.class.cast(b).name) {
				return false;
			}
			return super.equals(b);
		}
		return false;
	}
	
	// Required for checkstyle (hashcode must be the same result if equals is true, this works here
	// because equals is only true if super.equals is true, so super.hashCode works.
	// hashCodes do not have to be distict on equals being false, so using super.hashCode should be fine.
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
