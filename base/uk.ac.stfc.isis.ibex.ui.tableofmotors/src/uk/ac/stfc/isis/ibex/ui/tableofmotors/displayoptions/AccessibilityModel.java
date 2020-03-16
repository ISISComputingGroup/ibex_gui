package uk.ac.stfc.isis.ibex.ui.tableofmotors.displayoptions;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class AccessibilityModel extends ModelObject {
	
	private static final AccessibilityModel INSTANCE = new AccessibilityModel();
	
	private boolean accessibilityEnabled = false;
	
	public static AccessibilityModel getInstance() {
		return INSTANCE;
	}
	
	private AccessibilityModel() {
		
	}
	
	public void setAccessibilityEnabled(boolean enabled) {
		firePropertyChange("accessibilityEnabled", accessibilityEnabled, accessibilityEnabled = enabled);
	}
	
	public boolean getAccessibilityEnabled() {
		return accessibilityEnabled;
	}

}
