package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.util.List;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A view containing all matplotlib secondary figures.
 */
public class MatplotlibAllSecondaryFiguresView extends MatplotlibAllFiguresView {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UpdatedValue<List<Integer>> getFigures() {
		return Activator.getSecondaryFigures();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UpdatedValue<String> getConnectionUrl() {
		return Activator.getSecondaryUrl();
	}
}
