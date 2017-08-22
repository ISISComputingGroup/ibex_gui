package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

public class PerspectiveInfo {
	
	private final String name;
	private final String partID;
	private final boolean isSelected;
	private final String pluginID;
	private final String iconPath;
	
	public PerspectiveInfo(String name, String partID, boolean isSelected, String pluginID, String iconPath) {
		this.name = name;
		this.partID = partID;
		this.isSelected = isSelected;
		this.pluginID = pluginID;
		this.iconPath = iconPath;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPartID() {
		return partID;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public String getPlugin() {
		return pluginID;
	}
	
	public String getIconPath() {
		return iconPath;
	}

}
