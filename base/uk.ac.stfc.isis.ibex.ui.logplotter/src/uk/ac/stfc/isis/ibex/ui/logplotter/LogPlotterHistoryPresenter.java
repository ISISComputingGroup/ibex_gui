//
///*
// * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
// * Science & Technology Facilities Council. All rights reserved.
// *
// * This program is distributed in the hope that it will be useful. This program
// * and the accompanying materials are made available under the terms of the
// * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
// * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
// * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
// * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
// * details.
// *
// * You should have received a copy of the Eclipse Public License v1.0 along with
// * this program; if not, you can obtain a copy from
// * https://www.eclipse.org/org/documents/epl-v10.php or
// * http://opensource.org/licenses/eclipse-1.0.php
// */
//
//package uk.ac.stfc.isis.ibex.ui.logplotter;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//import org.csstudio.trends.databrowser2.Messages;
//import org.csstudio.trends.databrowser2.editor.DataBrowserEditor;
//import org.csstudio.trends.databrowser2.model.ArchiveRescale;
//import org.csstudio.trends.databrowser2.model.AxisConfig;
//import org.csstudio.trends.databrowser2.model.Model;
//import org.csstudio.trends.databrowser2.model.ModelListenerAdapter;
//import org.csstudio.trends.databrowser2.model.PVItem;
//import org.csstudio.trends.databrowser2.model.RequestType;
//import org.csstudio.trends.databrowser2.preferences.Preferences;
//import org.csstudio.ui.util.EmptyEditorInput;
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.osgi.util.NLS;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.ui.IWorkbenchPage;
//import org.eclipse.ui.IWorkbenchWindow;
//import org.eclipse.ui.PlatformUI;
//
//import uk.ac.stfc.isis.ibex.ui.blocks.presentation.PVHistoryPresenter;
//
///**
// * The class that is responsible for displaying the log plotter.
// */
//public class LogPlotterHistoryPresenter implements PVHistoryPresenter {
//
//    /**
//     * Returns a stream of all the current data browsers. Editors are taken from
//     * all windows instead of just the active window as the active window is
//     * null if called from a non UI-thread (ie when switching instruments).
//     * 
//     * @return A stream of all the current data browser editors.
//     */
//    public static Stream<DataBrowserEditor> getCurrentDataBrowsers() {
//        // clone to avoid potential ConcurrentModificationException
//        return Arrays.stream(PlatformUI.getWorkbench().getWorkbenchWindows().clone())
//                .filter(window -> window != null).map(IWorkbenchWindow::getActivePage).filter(page -> page != null)
//                .map(IWorkbenchPage::getEditorReferences).flatMap(Arrays::stream)
//                .map(editorReference -> editorReference.getEditor(false))
//                .filter(editor -> editor instanceof DataBrowserEditor).map(editor -> (DataBrowserEditor) editor);
//    }
//
//    /**
//     * Closes all the current data browsers.
//     */
//    public static void closeAllDataBrowsers() {
//        getCurrentDataBrowsers().forEach(editor -> closeDataBrowser(editor));
//    }
//
//    /**
//     * Closes the given data browser editor. Runs asynchronously on the GUI
//     * thread.
//     * 
//     * @param dataBrowser
//     *            the editor to close
//     */
//    private static void closeDataBrowser(final DataBrowserEditor dataBrowser) {
//        Display.getDefault().asyncExec(
//                () -> dataBrowser.getEditorSite().getWorkbenchWindow().getActivePage().closeEditor(dataBrowser, false));
//    }
//
//    private Stream<AxisConfig> getAxes(DataBrowserEditor editor) {
//        // Axes are returned as an iterable so must use spliterator to create
//        // stream.
//        return StreamSupport.stream(editor.getModel().getAxes().spliterator(), false);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public HashMap<String, ArrayList<String>> getPlotsAndAxes() {
//        HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
//        getCurrentDataBrowsers().forEach(e -> map.put(e.getTitle(),
//                new ArrayList<String>(getAxes(e).map(a -> a.getName()).collect(Collectors.toList()))));
//        return map;
//    }
//
//    /**
//     * Adds a PV to an editor.
//     * 
//     * @param pvAddress
//     *            The PV to plot the history of
//     * @param displayName
//     *            The user-friendly name for the plot and the axis to add
//     * @param editor
//     *            The editor to add the PV to
//     * @param axisName
//     *            The name of the axis to add the PV to, if empty creates a new
//     *            axis
//     */
//    private void addPVToEditor(String pvAddress, final String displayName, DataBrowserEditor editor,
//            Optional<String> axisName) {
//        Model model = editor.getModel();
//        model.setSaveChanges(false);
//        model.setArchiveRescale(ArchiveRescale.NONE);
//
//        // Add received items
//        final double period = Preferences.getScanPeriod();
//        try {
//            // Create trace
//            final PVItem item = new PVItem(pvAddress, period);
//            item.setDisplayName(displayName);
//            item.useDefaultArchiveDataSources();
//            item.setRequestType(RequestType.RAW);
//
//            AxisConfig axis;
//            if (axisName.isPresent()) {
//                // Extract the axis to add to from the list of axes
//                axis = getAxes(editor).filter(a -> a.getName() == axisName.get()).findFirst()
//                        .orElseGet(() -> createNewAxis(displayName, model));
//            } else {
//                axis = createNewAxis(displayName, model);
//            }
//
//            // Add item to the axis and model
//            item.setAxis(axis);
//            model.addItem(item);
//        } catch (Exception ex) {
//            MessageDialog.openError(editor.getSite().getShell(), Messages.Error,
//                    NLS.bind(Messages.ErrorFmt, ex.getMessage()));
//        }
//    }
//
//    /**
//     * Creates a new axis on the specified plot.
//     * 
//     * @param displayName
//     *            The name of the new axis.
//     * @param model
//     *            The model to add the axis to.
//     * @return The created axis.
//     */
//    private AxisConfig createNewAxis(final String displayName, Model model) {
//        AxisConfig axis;
//        // If an existing axis has not been specified, create a new one
//        axis = new AxisConfig(displayName);
//        axis.setAutoScale(true);
//        model.addAxis(axis);
//
//        model.addListener(new ModelListenerAdapter() {
//            private int dataCount = 0;
//            private static final int DATA_COUNT_LIMIT = 10;
//            private boolean autoscale = true;
//
//            // This listener is triggered every time that a data point
//            // is added to the graph,
//            // either from archive data or from new data. After 10
//            // pieces of data are received,
//            // it turns off autoscale.
//            @Override
//            public void selectedSamplesChanged() {
//                if (dataCount < DATA_COUNT_LIMIT) {
//                    dataCount++;
//                } else if (autoscale) {
//                    Display.getDefault().asyncExec(() -> axis.setAutoScale(false));
//                    autoscale = false;
//                }
//            }
//
//        });
//        return axis;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void newDisplay(String pvAddress, final String displayName) {
//        // Create new editor
//        final DataBrowserEditor editor = DataBrowserEditor.createInstance(new EmptyEditorInput() {
//            @Override
//            public String getName() {
//                return displayName;
//            }
//        });
//
//        addPVToEditor(pvAddress, displayName, editor, Optional.empty());
//
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void addToDisplay(String pvAddress, String display, String presenterName, Optional<String> axisName) {
//        Optional<DataBrowserEditor> editorFromCurrent =
//                getCurrentDataBrowsers().filter(e -> e.getTitle().equals(presenterName)).findFirst();
//
//        if (editorFromCurrent.isPresent()) {
//            addPVToEditor(pvAddress, display, editorFromCurrent.get(), axisName);
//        } else {
//            // Can't find the editor to add to, make a new one
//            newDisplay(pvAddress, display);
//        }
//    }
//
//}
