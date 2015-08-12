
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

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
	private final List<NewGroup> groups = new ArrayList<>();
	
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
		//Leave text blank
		showBanner("");

	}

	private void configureMenu() {
		GroupsMenu menu = new GroupsMenu(this);
		Menu contextMenu = menu.get();
		mainComposite.setMenu(contextMenu);
		for (NewGroup group : groups) {
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
		for (NewGroup group : groups) {
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
					// Leave text blank
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
		for (NewGroup group : groups) {
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
	
	private NewGroup groupWidget(DisplayGroup group) {
		NewGroup groupWidget = new NewGroup(mainComposite, SWT.NONE, group);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd.heightHint = NewGroup.BLOCK_HEIGHT;
		gd.minimumHeight = gd.heightHint;
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
		
		for (NewGroup group : groups) {
			group.dispose();
		}
		
		groups.clear();
	}
}
