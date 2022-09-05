/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package uk.ac.stfc.isis.ibex.ui.logplotter;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.csstudio.csdata.ProcessVariable;
import org.csstudio.csdata.TimestampedPV;
import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
import org.csstudio.trends.databrowser2.model.ArchiveDataSource;
import org.csstudio.trends.databrowser2.model.ChannelInfo;
import org.csstudio.trends.databrowser2.model.Model;
import org.csstudio.trends.databrowser2.model.PVItem;
import org.csstudio.trends.databrowser2.model.RequestType;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.csstudio.ui.util.AdapterUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import uk.ac.stfc.isis.ibex.ui.blocks.groups.BlocksMenu;


/** Command handler for opening Data Browser on the current selection. 
 *  Copied almost exactly from CS-Studio code but modified for IBEX.
 *  Linked from popup menu that is sensitive to {@link ProcessVariable}
 */
public class OpenLogPlotterPopup extends AbstractHandler {

    final double period = Preferences.getScanPeriod();
    private static final String LOG_PLOTTER_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";

    /** {@inheritDoc} */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
    	if (BlocksMenu.canAddPlot()) {
            // Get selection first because the ApplicationContext might change.
            ISelection selection = HandlerUtil.getActiveMenuSelection(event);
            if (selection == null) {
                // This works for double-clicks.
                selection = HandlerUtil.getCurrentSelection(event);
            }

            switchToLogPlotter();

            // Create new editor
            final DataBrowserEditor editor = DataBrowserEditor.createInstance();
            if (editor == null) {
                return null;
            }
            
            // Add received items
            final Model model = editor.getModel();
            model.setSaveChanges(false);
            try {
                if (selection instanceof IStructuredSelection
                    && ((IStructuredSelection) selection).getFirstElement() instanceof ChannelInfo) {   
                    // Received items are from search dialog
                    final Object[] channels = ((IStructuredSelection) selection).toArray();
                    for (Object channel : channels) {
                        final ChannelInfo info = (ChannelInfo) channel;
                        add(model, info.getProcessVariable(), info.getArchiveDataSource());
                    }
                } else {
                    // Add received PVs with default archive data sources
                    final List<TimestampedPV> timestampedPVs = Arrays
                            .asList(AdapterUtil.convert(selection,
                                    TimestampedPV.class));
                    if (!timestampedPVs.isEmpty()) {
                        // Add received items, tracking their start..end time
                        long start_ms = Long.MAX_VALUE,  end_ms = 0;
                        for (TimestampedPV timestampedPV : timestampedPVs) {
                            final long time = timestampedPV.getTime();
                            if (time < start_ms) {
                                start_ms = time;
                            }
                            if (time > end_ms) {
                                end_ms = time;
                            }
                            final PVItem item = new PVItem(timestampedPV.getName().trim(), period);
                            item.setAxis(model.addAxis());
                            item.useDefaultArchiveDataSources();
                            model.addItem(item);
                        }

                        final Instant start = Instant.ofEpochMilli(start_ms).minus(Duration.ofMinutes(30));
                        final Instant end = Instant.ofEpochMilli(end_ms).plus(Duration.ofMinutes(30));
                        model.enableScrolling(false);
                        model.setTimerange(start, end);
                    } else {
                        final ProcessVariable[] pvs = AdapterUtil.convert(
                                selection, ProcessVariable.class);
                        for (ProcessVariable pv : pvs) {
                            add(model, pv, null);
                        }
                    }
                }
            } catch (Exception ex) {
                MessageDialog.openError(editor.getSite().getShell(),
                        "Error", ex.getMessage());
            }
    	} else {
    		Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			messageBox.setText("Failed to open in Log Plotter");
			messageBox.setMessage("Make the Log Plotter perspective visible.");
			messageBox.open();
    	}
        return null;
    }

    /** Add item
     *  @param model Model to which to add the item
     *  @param pv PV to add
     *  @param archive Archive to use or <code>null</code>
     *  @throws Exception on error
     */
    private void add(final Model model, final ProcessVariable pv,
            final ArchiveDataSource archive) throws Exception {
        String pvName = pv.getName();
        if (!pvName.contains(".")) {
            pvName += ".VAL";
        }
        final PVItem item = new PVItem(pvName, period);
        if (archive == null) {
            item.useDefaultArchiveDataSources();            
        } else {
            item.addArchiveDataSource(archive);
        }
        item.setRequestType(RequestType.RAW);
        // Add item to new axis
        item.setAxis(model.addAxis());
        model.addItem(item);
    }
    
    /**
     * This is a bit of a hack to switch perspectives in a way that is compatible with both E4 and the E3 compatibility layer.
     * 
     * Can't use our normal perspective provider as that requires dependency injection, which we can't use with E3-style extension
     * points. Can't move away from E3 extension points as this is what the CSStudio view uses.
     */
    private static void switchToLogPlotter() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
        final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        page.setPerspective(registry.findPerspectiveWithId(LOG_PLOTTER_PERSPECTIVE_ID));
    }
}
