package uk.ac.stfc.isis.ibex.ui.dashboard.widgets;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public enum RunState {

	PROCESSING (RunStateColour.YELLOW),
	RUNNING (RunStateColour.LIGHT_GREEN),
	SETUP (RunStateColour.LIGHT_BLUE),
	PAUSED (RunStateColour.RED),
	WAITING (RunStateColour.GOLDEN_ROD),
	VETOING (RunStateColour.GOLDEN_ROD),	
	ENDING (RunStateColour.BLUE),
	
	PAUSING (RunStateColour.DARK_RED),
	BEGINNING (RunStateColour.GREEN),
	ABORTING (RunStateColour.BLUE),
	RESUMING (RunStateColour.GREEN),
	
	UPDATING (RunStateColour.YELLOW),
	STORING (RunStateColour.YELLOW),	
	SAVING (RunStateColour.YELLOW),
	
	UNKNOWN (RunStateColour.YELLOW);
	
	private final String name;
	private final Color color;

	private RunState(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	private RunState(Color color) {
		this.name = this.toString();
		this.color = color;
	}
	
	private RunState(int r, int g, int b) {
		this(SWTResourceManager.getColor(r, g, b));
	}
	
	public String getName() {
		return name;
	}
	
	public Color color() {
		return color;
	}
}
