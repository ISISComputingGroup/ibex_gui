package uk.ac.stfc.isis.ibex.ui.databrowser;

import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;

public class DataBrowserHistoryPresenter implements PVHistoryPresenter {

	private final DataBrowserDisplay display = new DataBrowserDisplay();
	
	@Override
	public void displayHistory(String pvAddress) {
		display.displayPVHistory(pvAddress);
	}

}
