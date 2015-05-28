package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;

public class BlocksMenu extends MenuManager {
		
	private final BlocksTable table;
	
	private final IAction displayHistory;
	private final PVHistoryPresenter pvHistoryPresenter = Presenter.getInstance().pvHistoryPresenter();
	
	public BlocksMenu(final BlocksTable table) {
		this.table = table;
		
		displayHistory = new Action("Display block history") {
			@Override
			public void run() {
				DisplayBlock block = table.firstSelectedRow();
				pvHistoryPresenter.displayHistory(block.blockServerAlias());
			}
		};
		
		add(displayHistory);
	}
	
	public Menu createContextMenu() {
		return super.createContextMenu(table);
	}
}
