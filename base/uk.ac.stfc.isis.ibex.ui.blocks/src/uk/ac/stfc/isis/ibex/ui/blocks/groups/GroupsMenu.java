package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

public class GroupsMenu {

	private final MenuManager manager = new MenuManager();
	private final GroupsPanel groups;
	
	private final IAction showAllBlocks = new Action("Show hidden blocks") {
		@Override
		public void run() {
			groups.setShowHiddenBlocks(true);
		}
	};
	
	private final IAction hideBlocks = new Action("Don't show hidden blocks") {
		@Override
		public void run() {
			groups.setShowHiddenBlocks(false);
		}
	};

	
	public GroupsMenu(final GroupsPanel groups) {
		this.groups = groups;
		
		manager.addMenuListener(new IMenuListener() {		
			@Override
			public void menuAboutToShow(IMenuManager manager) {						
				showAllBlocks.setEnabled(!groups.showHiddenBlocks());				
				hideBlocks.setEnabled(!showAllBlocks.isEnabled());
			}
		});
		
		manager.add(showAllBlocks);
		manager.add(hideBlocks);
	}
	
	public Menu get() {
		return manager.createContextMenu(groups);
	}
}

//	final IAction displayBlockHistory = new Action("Display history") {
//		@Override
//		public void run() {
//			//BlockViewModel selectedBlock = selectedRows().get(0);
//			//selectedBlock.displayHistory();
//		}
//	};
//	
//	final IAction displayProperties = new Action("Properties") {
//		@Override
//		public void run() {
//			//BlockViewModel selectedBlock = firstSelectedRow();
//			//selectedBlock.displayProperties();
//		}
//	};

