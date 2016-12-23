
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.targets;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.OpiView;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;

/**
 * A view that allows display of an OPI target. As the view is shared by
 * multiple perspectives, and views are singletons, the class is abstract so
 * that each perspective that wants to use it can own its own version.
 */
public abstract class OpiTargetView extends OpiView {

    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView"; //$NON-NLS-1$

    private static final Logger LOG = IsisLog.getLogger(OpiTargetView.class);

    /**
     * PV prefix required for adding new macros.
     */
	private final String pvPrefix = Instrument.getInstance().currentInstrument().pvPrefix();
	
    /**
     * Name of the opi.
     */
	private String opiName;

    /**
     * Sets the target opi.
     * 
     * @param target
     *            The OPI target to set to.
     * @throws OPIViewCreationException
     *             when the OPI can not be created
     */
    public void setOpi(OpiTarget target) throws OPIViewCreationException {
        this.opiName = target.opiName();
		
        addMacros(target);
		initialiseOPI();
	}
		
	@Override
    protected Path opi() throws OPIViewCreationException {
		Path path = Opi.getDefault().descriptionsProvider().pathFromName(opiName);
		
		if (path != null) {
			return path;
		}
		
		// This is for back-compatibility; previously the opi name in the synoptic was the path
		// At some point this can be removed.
        try {
            return Opi.getDefault().opiProvider().pathFromName(opiName);
        } catch (NullPointerException ex) {
            throw new OPIViewCreationException("OPI key or path can not be found.");
        }
	}
	
    /**
     * Add the macros to the OPI
     * 
     * @param target target of the OPI
     */
    private void addMacros(OpiTarget target) {
		MacrosInput input = macros();

        input.put("NAME", target.name());
        input.put("OPINAME", this.opiName);
		input.put("P", pvPrefix);
        for (Map.Entry<String, String> macro : target.properties().entrySet()) {
			input.put(macro.getKey(), macro.getValue());
		}
	}

    private static List<IViewPart> openOPIs = new ArrayList<>();;
    private static List<IPerspectiveDescriptor> openOPIsWorkbenchPage = new ArrayList<>();;

    /**
     * Closes any OPIs that have been opened.
     */
    public static void closeAllOPIs() {
        ListIterator<IPerspectiveDescriptor> wbiter = openOPIsWorkbenchPage.listIterator();
        for (ListIterator<IViewPart> iter = openOPIs.listIterator(); iter.hasNext();) {
            final IViewPart vp = iter.next();
            final IPerspectiveDescriptor descriptor = wbiter.next();
            // Must run on the GUI thread
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().setPerspective(descriptor);
                    IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    wp.hideView(vp);
                }
            });
        }
        openOPIs.clear();
        openOPIsWorkbenchPage.clear();
    }

    /**
     * Display an OPI using a target in the tab view.
     *
     * @param opiTarget
     *            the target OPI to display
     * @param id
     *            The view in which to display the OPI.
     * @throws OPIViewCreationException
     *             when opi can not be created
     */
    public static void displayOpi(OpiTarget opiTarget, String id) throws OPIViewCreationException {
        IWorkbenchPage workbenchPage = null;
        IViewPart view = null;
        try {
            workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            view = workbenchPage.showView(id, opiTarget.name(), IWorkbenchPage.VIEW_ACTIVATE);
            OpiTargetView viewAsOPITarget = (OpiTargetView) view;
            viewAsOPITarget.setOpi(opiTarget);
            openOPIs.add(view);
            openOPIsWorkbenchPage.add(workbenchPage.getPerspective());
        } catch (PartInitException e) {
            LOG.catching(e);
        } catch (OPIViewCreationException e) {
            workbenchPage.hideView(view);
            throw e;
        }
    }
}
