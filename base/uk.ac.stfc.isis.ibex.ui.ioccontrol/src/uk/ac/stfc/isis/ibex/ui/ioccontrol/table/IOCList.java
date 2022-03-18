package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.configserver.IocState;
/**
 * 
 * Extension of Arraylist to be able to hold the label to use on the tree, usually the description of the first
 * element but sometimes Running or In Config.
 *
 */
@SuppressWarnings("serial")
public class IOCList extends ArrayList<IocState> {
	/**
	 *  The name of the list of Iocs for use in the Tree.
	 */
	public String name;
	
	/**
	 * Constructor to create List with name set.
	 * @param name - name to set on the list.
	 */
	public IOCList(String name) {
		this.name = name;
	}
}
