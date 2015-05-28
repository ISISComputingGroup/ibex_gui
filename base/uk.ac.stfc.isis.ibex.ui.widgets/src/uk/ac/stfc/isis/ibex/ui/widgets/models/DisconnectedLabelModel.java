package uk.ac.stfc.isis.ibex.ui.widgets.models;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class DisconnectedLabelModel extends LabelModel {

	@Override
	public String getText() {
		return "Disconnected";
	}

	@Override
	public String getDescription() {
		return "No data source is connected";
	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public Image getImage() {
		return null;
	}

}
