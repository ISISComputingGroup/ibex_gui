package uk.ac.stfc.isis.ibex.ui.moxaperspective.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;


/**
 * The Start/Stop IOC view model.
 */
public class MoxasViewModel extends ModelObject {
	
	private static final String RUNNING_DESCRIPTION = "Running";
	private static final String INCONFIG_DESCRIPTION = "In Config";
	private HashMap<String, MoxaList> moxaPorts = new HashMap<String, MoxaList>();
	
	/**
	 * Represents an IOC in the tree view using its description and name.
	 * If the item is a description, 'ioc' will be empty.
	 */
//	public static record Item(Optional<String> description, Optional<String> ioc) { }

	public HashMap<String, MoxaList> getMoxaPorts() {
		HashMap<String, MoxaList> map = new HashMap<String, MoxaList>();
		
		String nameAndIp = "130.246.55.58 MOXA_ARGUS_UPSTAIRS";
		
		MoxaList list1 = new MoxaList(nameAndIp);
		list1.add(new MoxaModelObject("1", "COM5"));
		list1.add(new MoxaModelObject("2", "COM6"));
		list1.add(new MoxaModelObject("3", "COM7"));
		
		map.put(nameAndIp, list1);
		return map;
		//return moxaPorts;
	};
	
//	private IocState ioc;
//	private final IocControl control;
//	private HashMap<String, IOCList> availableIocs = new HashMap<String, IOCList>();
//	
//	private boolean startEnabled = false;
//	private boolean stopEnabled = false;
//	private boolean restartEnabled = false;
//	
//	private HashSet<String> expanded = new HashSet<String>(Arrays.asList(RUNNING_DESCRIPTION, INCONFIG_DESCRIPTION));
//	private Item selected = new Item(Optional.empty(), Optional.empty());
//	private Item top = new Item(Optional.empty(), Optional.empty());
//	
//	
//	private PropertyChangeListener availableIocsListener;
//	private PropertyChangeListener enabledListener = new PropertyChangeListener() {
//        @Override
//        public void propertyChange(PropertyChangeEvent event) {
//        	boolean isRunning = (boolean) event.getNewValue();
//        	firePropertyChange("startEnabled", startEnabled, startEnabled = control.startIoc().getCanSend() && !isRunning);
//        	firePropertyChange("stopEnabled", stopEnabled, stopEnabled = control.stopIoc().getCanSend() && isRunning);
//        	firePropertyChange("restartEnabled", restartEnabled, restartEnabled = control.restartIoc().getCanSend() && isRunning);
//        }
//    };
//    
//	private PropertyChangeListener canSendStartListener = new PropertyChangeListener() {	
//		@Override
//		public void propertyChange(PropertyChangeEvent event) {
//			if (ioc != null) {
//				boolean canSend = (boolean) event.getNewValue();
//				firePropertyChange("startEnabled", startEnabled, startEnabled = canSend && !ioc.getIsRunning());
//			} else {
//				firePropertyChange("startEnabled", startEnabled, startEnabled = false);
//			}
//		}
//	};
//	
//	private PropertyChangeListener canSendStopListener = new PropertyChangeListener() {
//		@Override
//		public void propertyChange(PropertyChangeEvent event) {
//			if (ioc != null) {
//				boolean canSend = (boolean) event.getNewValue();
//				firePropertyChange("stopEnabled", stopEnabled, stopEnabled = canSend && ioc.getIsRunning());
//			} else {
//				firePropertyChange("stopEnabled", stopEnabled, stopEnabled = false);
//			}
//		}
//	};
//	
//	private PropertyChangeListener canSendRestartListener = new PropertyChangeListener() {
//		@Override
//		public void propertyChange(PropertyChangeEvent event) {
//			if (ioc != null) {
//				boolean canSend = (boolean) event.getNewValue();
//				firePropertyChange("restartEnabled", restartEnabled, restartEnabled = canSend && !ioc.getIsRunning());
//			} else {
//				firePropertyChange("restartEnabled", restartEnabled, restartEnabled = false);
//			}
//		}
//	};
//	
//	/**
//	 * Constructor. Creates a new instance of the IocControlViewModel object.
//	 * @param control The IOC control class.
//	 */
//	public IocControlViewModel(IocControl control) {
//		this.control = control;
//        availableIocs = createHashMap();
//
//        availableIocsListener = new PropertyChangeListener() {	
//    		@Override
//    		public void propertyChange(PropertyChangeEvent evt) {
//    			firePropertyChange("availableIocs", availableIocs, availableIocs = createHashMap());
//    		}
//    	};
//        
//        control.iocs().addPropertyChangeListener(availableIocsListener);
//        control.startIoc().addPropertyChangeListener("canSend", canSendStartListener);
//        control.stopIoc().addPropertyChangeListener("canSend", canSendStopListener);
//        control.restartIoc().addPropertyChangeListener("canSend", canSendRestartListener);
//	}
//	
//	private HashMap<String, IOCList> createHashMap() {
//		HashMap<String, IOCList> iocHashMap = new HashMap<String, IOCList>();
//		
//		Collection<IocState> rows = control.iocs().getValue();
//		if (rows == null) {
//			return iocHashMap;
//		}
//		
//    	String description = "";
//    	iocHashMap.put(RUNNING_DESCRIPTION, new IOCList(RUNNING_DESCRIPTION));
//    	iocHashMap.put(INCONFIG_DESCRIPTION, new IOCList(INCONFIG_DESCRIPTION));
//    	
//    	for (IocState ioc : rows) {
//    		description = ioc.getDescription();
//    		if (!iocHashMap.containsKey(description)) {
//    			iocHashMap.put(description, new IOCList(description));
//    		}
//    		iocHashMap.get(description).add(ioc);
//    		if (ioc.getIsRunning()) {
//    			iocHashMap.get(RUNNING_DESCRIPTION).add(ioc);
//    		}
//    		if (ioc.getInCurrentConfig()) {
//    			iocHashMap.get(INCONFIG_DESCRIPTION).add(ioc);
//    		}
//    	}
//    	
//		return iocHashMap;
//	}
//	
//	/**
//	 * The map has a custom comparator that puts the "Running" and "In Config" IOCs on the top of the tree view.
//	 * @return Sorted map with the key being a description and the value being a list of IOCs.
//	 */
//	public HashMap<String, IOCList> getAvailableIocs() {
//		return availableIocs;
//	}
//	
//	/**
//	 * Sets the currently selected IOC and adds/fires property change listeners.
//	 * Controls if the Start/Stop/Restarts buttons are enabled.
//	 * @param ioc The selected IOC.
//	 */
//	public void setIoc(final IocState ioc) {
//		if (this.ioc != null) {
//			this.ioc.removePropertyChangeListener(enabledListener);
//		}
//		this.ioc = ioc;
//		
//		if (this.ioc == null) {
//			firePropertyChange("startEnabled", startEnabled, startEnabled = false);
//        	firePropertyChange("stopEnabled", stopEnabled, stopEnabled = false);
//        	firePropertyChange("restartEnabled", restartEnabled, restartEnabled = false);
//		} else {
//			this.ioc.addPropertyChangeListener("isRunning", enabledListener);
//			
//			boolean isRunning = this.ioc.getIsRunning();
//        	firePropertyChange("startEnabled", startEnabled, startEnabled = control.startIoc().getCanSend() && !isRunning);
//        	firePropertyChange("stopEnabled", stopEnabled, stopEnabled = control.stopIoc().getCanSend() && isRunning);
//        	firePropertyChange("restartEnabled", restartEnabled, restartEnabled = control.restartIoc().getCanSend() && isRunning);
//		}
//	}
	
//	/**
//	 * Start the currently selected IOC.
//	 */
//	public void startIoc() {
//		if (ioc != null) {
//            control.startIoc().uncheckedSend(Arrays.asList(ioc.getName()));
//		}
//	}
//	
//	/**
//	 * Stop the currently selected IOC.
//	 */
//	public void stopIoc() {
//		if (ioc != null) {
//		    control.stopIoc().uncheckedSend(Arrays.asList(ioc.getName()));
//		}
//	}
//	
//	/**
//	 * Restart the currently selected IOC.
//	 */
//	public void restartIoc() {
//		if (ioc != null) {
//		    control.restartIoc().uncheckedSend(Arrays.asList(ioc.getName()));
//		}
//	}
//	
//	/**
//	 * @return True if the currently selected IOC can be started.
//	 */
//	public boolean getStartEnabled() {
//		return startEnabled;
//	}
//	
//	/**
//	 * @return True if the currently selected IOC can be stopped.
//	 */
//	public boolean getStopEnabled() {
//		return stopEnabled;
//	}
//	
//	/**
//	 * @return True if the currently selected IOC can be restarted.
//	 */
//	public boolean getRestartEnabled() {
//		return restartEnabled;
//	}
//	
//	/**
//	 * Set the currently selected tree item.
//	 * @param description The IOCs description.
//	 * @param ioc The IOC name.
//	 */
//	public void setSelected(String description, String ioc) {
//		selected = new Item(Optional.ofNullable(description), Optional.ofNullable(ioc));
//	}
//	
//	/**
//	 * @return Item representing the previously selected Description or IOC.
//	 */
//	public Item getSelected() {
//		return selected;
//	}
//	
//	/**
//	 * Set the current top tree item. This is used for restoring the scroll position of the IOC table.
//	 * @param description The IOCs description.
//	 * @param ioc The IOC name.
//	 */
//	public void setTop(String description, String ioc) {
//		top = new Item(Optional.ofNullable(description), Optional.ofNullable(ioc));
//	}
//	
//	/**
//	 * This is used for restoring the scroll position of the IOC table.
//	 * @return Item representing the previous Description or IOC that was on top.
//	 */
//	public Item getTop() {
//		return top;
//	}
//	
//	/**
//	 * Add a currently expanded tree item. This can only be a description.
//	 * @param description The IOCs description.
//	 */
//	public void addExpanded(final String description) {
//		expanded.add(description);
//	}
//	
//	/**
//	 * Remove from expanded as the tree item was collapsed in the view. This can only be a description.
//	 * @param description The IOCs description.
//	 */
//	public void removeExpanded(final String description) {
//		expanded.remove(description);
//	}
//	
//	/**
//	 * @return The IOCLists that were previously expanded.
//	 */
//	public List<IOCList> getExpanded() {
//		return expanded.stream()
//					   .map(desc -> availableIocs.get(desc))
//					   .collect(Collectors.toList());
//	}
//	
//	/**
//	 * Fire the view state restore properties.
//	 */
//	public void refresh() {
//		firePropertyChange("expanded", null, getExpanded());
//		firePropertyChange("selected", null, getSelected());
//		firePropertyChange("top", null, getTop());
//	}
//	
//	/**
//	 * Remove all listeners that are not automatically disposed.
//	 */
//	public void removeListeners() {
//		control.iocs().removePropertyChangeListener(availableIocsListener);
//		control.startIoc().removePropertyChangeListener("canSend", canSendStartListener);
//        control.stopIoc().removePropertyChangeListener("canSend", canSendStopListener);
//        control.restartIoc().removePropertyChangeListener("canSend", canSendRestartListener);
//	}
}
