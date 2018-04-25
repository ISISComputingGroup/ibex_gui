
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
	private Stream<DataBrowserEditor> getCurrentDataBrowsers() {
		Stream<IEditorReference> editorRefs = Arrays.stream(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences());
		
		return editorRefs.map(e -> e.getEditor(false))
						 .filter(e -> e instanceof DataBrowserEditor)
						 .map(e -> (DataBrowserEditor) e);
	}
	
	@Override
<<<<<<< HEAD
	public List<String> getCurrentPresenters() {
		return getCurrentDataBrowsers().map(e -> e.getTitle())
									   .collect(Collectors.toList());
=======
	public ArrayList<String> getCurrentDisplays() {
		ArrayList<String> editorNames = new ArrayList<>();
		for (DataBrowserEditor editor : getCurrentDataBrowsers()) {
			editorNames.add(editor.getTitle());
		}
		return editorNames;
>>>>>>> master
	}
	
	private void addPVToEditor(String pvAddress, final String displayName, DataBrowserEditor editor) {
		if (editor == null) {
	        return;
	    }
		Model model = editor.getModel();
		model.setSaveChanges(false);
		model.setArchiveRescale(ArchiveRescale.STAGGER);
		
		// Add received items
	    final double period = Preferences.getScanPeriod();
	    try {
<<<<<<< HEAD
	    	// Create axis
			AxisConfig axis = new AxisConfig(displayName);
			axis.setAutoScale(true);
			model.addAxis(axis);
	    	
			// Create trace
	    	final PVItem item = new PVItem(pvAddress, period);
	    	item.setDisplayName(displayName);
			item.useDefaultArchiveDataSources();
=======
	    	
	    	AxisConfig axis = model.addAxis(displayName);
			axis.setAutoScale(false);
			
	    	final PVItem item = new PVItemWithUnits(displayName, pvAddress, period, axis);
			item.useDefaultArchiveDataSources();
			
			// Add item to new axes
>>>>>>> master
			item.setAxis(axis);
			model.addItem(item);
	    } catch (Exception ex) {
	        MessageDialog.openError(editor.getSite().getShell(),
	                Messages.Error,
	                NLS.bind(Messages.ErrorFmt, ex.getMessage()));
	    }
	}
	
	@Override
<<<<<<< HEAD
	public void newPresenter(String pvAddress, final String displayName) {		
=======
	public void newDisplay(String pvAddress, final String displayName) {	
		UI.getDefault().switchPerspective(Perspective.ID);
		
>>>>>>> master
	    // Create new editor
	    final DataBrowserEditor editor = DataBrowserEditor.createInstance(new EmptyEditorInput() {
	    	@Override
	    	public String getName() {
	    		return displayName;
	    	}
	    });
	    
	    addPVToEditor(pvAddress, displayName, editor);
	}

	@Override
<<<<<<< HEAD
	public void addToPresenter(String pvAddress, String display, String presenterName) {		
		List<DataBrowserEditor> editors = getCurrentDataBrowsers().filter(e -> e.getTitle().equals(presenterName))
																  .collect(Collectors.toList());
=======
	public void addToDisplay(String pvAddress, String display, String presenterName) {
		UI.getDefault().switchPerspective(Perspective.ID);
>>>>>>> master
		
		if (editors.size() == 0) {
			// Can't find the editor to add to, make a new one
<<<<<<< HEAD
			newPresenter(pvAddress, display);
		} else {
			DataBrowserEditor editor = editors.get(0);
=======
			newDisplay(pvAddress, display);
		} else {	
>>>>>>> master
			addPVToEditor(pvAddress, display, editor);
			
			// Recolour the axes so that they match the traces
			for (ModelItem trace : editor.getModel().getItems()) {
				trace.getAxis().setColor(trace.getColor());
			}
		}
	}

}
