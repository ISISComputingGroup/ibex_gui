
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.Collection;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import uk.ac.stfc.isis.ibex.configserver.Configurations;

/**
 * Class to add a recently used configurations menu item.
 *
 */
public class RecentItems extends ContributionItem {
	
	private static Configurations configs;

	public RecentItems() {
		configs = Configurations.getInstance();
	}

	public RecentItems(String id) {
		super(id);
	}

	/**
     * Create the menu.
     */
	@Override
	public void fill(Menu menu, int index) {
		// Create the parent for the list
		MenuItem parent = new MenuItem(menu, SWT.CASCADE, index);
		parent.setText("Recent Configurations");
		
		// Create menu items for the list
		Collection<String> items = configs.recent();
		if (items.size() > 0) {
			Menu child = new Menu(parent);
			for (String config : items) {
				MenuItem item = new MenuItem(child, SWT.PUSH);
				item.setText(config);
				final String target = config;
				item.addSelectionListener(new SelectionAdapter() {
					@Override
                    public void widgetSelected(SelectionEvent e) {
						//what to do when menu is subsequently selected.
                        configs.server().load().uncheckedWrite(target);
					}	
				});
			}
			parent.setMenu(child);
		} else {
			// No items yet
			parent.setEnabled(false);
		}
	}
	
	/**
     * Say the list is dynamic, or fill will only be called once.
     */
	@Override
	public boolean isDynamic() {
		return true;
	}
}
