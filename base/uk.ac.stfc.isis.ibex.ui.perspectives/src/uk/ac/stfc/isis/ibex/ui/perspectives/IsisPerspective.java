package uk.ac.stfc.isis.ibex.ui.perspectives;

import org.eclipse.swt.graphics.Image;

public interface IsisPerspective extends Comparable<IsisPerspective> {
	String ID();
	
	String name();
	
	Image image();
}
