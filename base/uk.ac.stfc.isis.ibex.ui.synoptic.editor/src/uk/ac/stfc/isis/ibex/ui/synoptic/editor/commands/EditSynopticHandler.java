
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.AboutToShow;

import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;


import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticSelectionDialog;

/**
 * Handler for editing a synoptic.
 */
public class EditSynopticHandler extends SynopticEditorHandler {
    private static final String EDIT_MENU_TEXT = "Edit";
    private static final String READ_ONLY_TEXT = "View";
	private static final String TITLE = " Synoptic";
	
	/**
	 * Create a new handler for editing synoptics.
	 */
	public EditSynopticHandler() {
		SYNOPTIC.availableSynopticsInfo().subscribe(synopticObserver);
	}
	/**
	 * Run edit synoptic from the menu. 
	 * @param shell shell to open the dialogue in.
	 * @return nothing
	 * @throws ExecutionException if there is a error
	 */
	@Execute
	public Object execute(Shell shell) throws ExecutionException {		
        SynopticSelectionDialog dialog =
                new SynopticSelectionDialog(shell, TITLE, SYNOPTIC.availableEditableSynoptics());
		if (dialog.open() == Window.OK) {
			openDialog(shell, load(dialog.selectedSynoptic()), (writer.canWrite() ? EDIT_MENU_TEXT : READ_ONLY_TEXT) + TITLE, false);
		}
		return null;
	}
	
	private SynopticDescription load(SynopticInfo info) {
		UpdatedValue<SynopticDescription> instrumentDescription = new UpdatedObservableAdapter<>(SYNOPTIC.synoptic(info));					
		if (Awaited.returnedValue(instrumentDescription, 1)) {
			return instrumentDescription.getValue();
		}
		return null;
	}
	
	
    private Observer<Collection<SynopticInfo>> synopticObserver = new BaseObserver<Collection<SynopticInfo>>() {

        @Override
        public void onConnectionStatus(boolean isConnected) {
            canViewOrEditSynoptics(isConnected);
        }

        @Override
        public void onError(Exception e) {
            canViewOrEditSynoptics(false);
        }

        private void canViewOrEditSynoptics(boolean isConnected) {
            setCanExecute(isConnected);
        }
    };
	
    /**
     * Generate the menu item as the menu is about to be shown.
     * 
     * It must be dynamic because the menu has a different label depending on
     * the state of the config pv.
     * 
     * @param items
     *            menu items to add to
     */
    @AboutToShow
    public void aboutToShow(List<MMenuElement> items) {
        String menuText;
        if (writer.canWrite()) {
            menuText = EDIT_MENU_TEXT;
        } else {
            menuText = READ_ONLY_TEXT;
        }

        MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE.createDirectMenuItem();

        dynamicItem.setLabel(menuText);
        dynamicItem.setContributorURI(PLUGIN); // Plugin in which this menu item exists
        dynamicItem.setContributionURI(CLASS_URI);
        items.add(dynamicItem);
    }
}
