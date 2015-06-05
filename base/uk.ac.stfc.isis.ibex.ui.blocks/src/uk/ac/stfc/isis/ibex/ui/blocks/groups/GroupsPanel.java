package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;

public class GroupsPanel extends Composite {
			
	private final Display display = Display.getCurrent();
	private final List<Group> groups = new ArrayList<>();
	
	private Composite mainComposite;
	private ScrolledComposite scrolledComposite;
	private CLabel banner;
	
	private boolean showHiddenBlocks = false;
	
	public GroupsPanel(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL);
		mainComposite = new Composite(scrolledComposite, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		scrolledComposite.setContent(mainComposite);
		
		configureMenu();
		showBanner("");

	}

	private void configureMenu() {
		GroupsMenu menu = new GroupsMenu(this);
		Menu contextMenu = menu.get();
		mainComposite.setMenu(contextMenu);
		for (Group group : groups) {
			group.setMenu(contextMenu);
		}
		scrolledComposite.setMenu(contextMenu);
	}

	public boolean showHiddenBlocks() {
		return showHiddenBlocks;
	}
	
	public void setShowHiddenBlocks(boolean showHidden) {
		if (showHiddenBlocks == showHidden) {
			return;
		}
		
		showHiddenBlocks = showHidden;
		for (Group group : groups) {
			group.showHiddenBlocks(showHidden);
		}

		layoutGroups();	
	}
		
	public synchronized void updateGroups(final Collection<DisplayGroup> groups) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				clear();
				if (groups.isEmpty()) {
					showBanner("");
					return;
				}
				addGroups(groups);
				setRows();
				layoutGroups();
			}
		});
	}

	private void showBanner(String text) {
		banner = new CLabel(scrolledComposite, SWT.NONE);
		banner.setLeftMargin(5);
		scrolledComposite.setContent(banner);
		banner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		banner.setText(text);
		banner.pack();
	}

	private void layoutGroups() {
		scrolledComposite.setContent(mainComposite);
		mainComposite.pack(true);
		for (Group group : groups) {
			group.pack(true);
		}

		mainComposite.layout(true, true);
	}
	
	private void addGroups(Collection<DisplayGroup> configGroups) {
		for (DisplayGroup group : configGroups) {
			groups.add(groupWidget(group));
		}
	}
	
	private void setRows() {
		mainComposite.setLayout(new GridLayout(groups.size(), false));
	}
	
	private Group groupWidget(DisplayGroup group) {
		Group groupWidget = new Group(mainComposite, SWT.NONE, group);
		GridData gd = new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1);
		gd.widthHint = Group.BLOCK_WIDTH;
		gd.minimumWidth = gd.widthHint;
		groupWidget.setLayoutData(gd);
		groupWidget.pack();
		
		groups.add(groupWidget);
		groupWidget.showHiddenBlocks(showHiddenBlocks);
		
		return groupWidget;
	}
	
	private void clear() {
		if (banner != null) {
			banner.dispose();
		}
		
		for (Group group : groups) {
			group.dispose();
		}
		
		groups.clear();
	}
}
