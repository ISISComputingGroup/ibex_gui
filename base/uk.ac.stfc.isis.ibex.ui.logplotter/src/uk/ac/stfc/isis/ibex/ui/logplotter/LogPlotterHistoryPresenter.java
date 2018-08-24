
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

package uk.ac.stfc.isis.ibex.ui.logplotter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
import org.csstudio.trends.databrowser2.model.ArchiveRescale;
import org.csstudio.trends.databrowser2.model.AxisConfig;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.ModelItem;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.csstudio.ui.util.EmptyEditorInput;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;

/**
 * The class that is responsible for displaying the log plotter.
 */
public class LogPlotterHistoryPresenter implements PVHistoryPresenter {
    
	/**
	 * @return A stream of all the current DataBrowserEditors.
	 */
	public static Stream<DataBrowserEditor> getCurrentDataBrowsers() {
		return getCurrentEditors().map(e -> (DataBrowserEditor) e);
	}
	
	/**
	 * Returns a stream of all the current editors. Editors are taken from all windows instead of just the active window
	 * as the active window is null if called from a non UI-thread (ie when switching instruments). 
	 * 
	 * @return
	 *         A stream of all the current editors.
	 */
	private static Stream<IEditorPart> getCurrentEditors() {
	    
	    List<IEditorReference> editors = new ArrayList<IEditorReference>();
	    
	    Arrays.stream(PlatformUI.getWorkbench().getWorkbenchWindows())
	    .forEach(wd -> Arrays.stream(wd.getActivePage().getEditorReferences()).forEach(ed -> editors.add(ed)));
	    
	    Stream<IEditorReference> editorRefs = Arrays.stream(editors.toArray(new IEditorReference[editors.size()]));
	    
        return editorRefs.map(e -> e.getEditor(false))
                         .filter(e -> e instanceof DataBrowserEditor);
	}
	
	/**
	 * Closes all the current data browsers. The call to close is done for all windows instead of just the active window
     * as the active window is null if called from a non UI-thread (ie when switching instruments). 
	 */
	public static void closeCurrentDataBrowsers() {
	    Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                Arrays.stream(PlatformUI.getWorkbench().getWorkbenchWindows())
                .forEach(wd -> getCurrentEditors().forEach(db -> wd.getActivePage().closeEditor(db, false)));
            }
        });
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<String> getDataBrowserTitles() {
		return getCurrentDataBrowsers().map(e -> e.getTitle());
	}
	
	private void addPVToEditor(String pvAddress, final String displayName, DataBrowserEditor editor) {
		if (editor == null) {
	        return;
	    }
		Model model = editor.getModel();
		model.setSaveChanges(false);
		model.setArchiveRescale(ArchiveRescale.NONE);
		
		// Add received items
	    final double period = Preferences.getScanPeriod();
	    try {
	    	// Create axis
			AxisConfig axis = new AxisConfig(displayName);
			axis.setAutoScale(false);
			model.addAxis(axis);
	    	
			// Create trace
	    	final PVItem item = new PVItem(pvAddress, period);
	    	item.setDisplayName(displayName);
			item.useDefaultArchiveDataSources();

			// Add item to new axes
			item.setAxis(axis);
			model.addItem(item);
	    } catch (Exception ex) {
	        MessageDialog.openError(editor.getSite().getShell(),
	                Messages.Error,
	                NLS.bind(Messages.ErrorFmt, ex.getMessage()));
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void newDisplay(String pvAddress, final String displayName) {		
	    // Create new editor
	    final DataBrowserEditor editor = DataBrowserEditor.createInstance(new EmptyEditorInput() {
	    	@Override
	    	public String getName() {
	    		return displayName;
	    	}
	    });
	    
	    addPVToEditor(pvAddress, displayName, editor);
	    
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addToDisplay(String pvAddress, String display, String presenterName) {
		List<DataBrowserEditor> editors = getCurrentDataBrowsers().filter(e -> e.getTitle().equals(presenterName))
				  .collect(Collectors.toList());
		
		if (editors.size() == 0) {
			// Can't find the editor to add to, make a new one
			newDisplay(pvAddress, display);
		} else {
			DataBrowserEditor editor = editors.get(0);
			addPVToEditor(pvAddress, display, editor);
			// Recolour the axes so that they match the traces
			for (ModelItem trace : editor.getModel().getItems()) {
				trace.getAxis().setColor(trace.getColor());
			}
		}
	}

}
