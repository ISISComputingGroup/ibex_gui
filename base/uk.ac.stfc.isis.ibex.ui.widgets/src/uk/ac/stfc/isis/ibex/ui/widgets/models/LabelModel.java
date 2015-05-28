package uk.ac.stfc.isis.ibex.ui.widgets.models;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public abstract class LabelModel extends ModelObject {
	public abstract String getText();
	
	public abstract String getDescription();
	
	public abstract Color getColor();
	
	public abstract Image getImage();
}
