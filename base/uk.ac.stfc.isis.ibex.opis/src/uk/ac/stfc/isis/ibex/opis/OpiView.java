///*
//* This file is part of the ISIS IBEX application.
//* Copyright (C) 2012-2015 Science & Technology Facilities Council.
//* All rights reserved.
//*
//* This program is distributed in the hope that it will be useful.
//* This program and the accompanying materials are made available under the
//* terms of the Eclipse Public License v1.0 which accompanies this distribution.
//* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
//* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
//* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
//*
//* You should have received a copy of the Eclipse Public License v1.0
//* along with this program; if not, you can obtain a copy from
//* https://www.eclipse.org/org/documents/epl-v10.php or 
//* http://opensource.org/licenses/eclipse-1.0.php
//*/
//
//package uk.ac.stfc.isis.ibex.opis;
//
//import java.util.LinkedHashMap;
//
//import org.apache.logging.log4j.Logger;
//import org.csstudio.opibuilder.runmode.OPIView;
//import org.csstudio.opibuilder.runmode.RunnerInput;
//import org.csstudio.opibuilder.util.MacrosInput;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.ui.PartInitException;
//
//import uk.ac.stfc.isis.ibex.logger.IsisLog;
//import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
//
///**
// * The Class OpiView to show an OPI.
// */
//public abstract class OpiView extends OPIView {
//
//    private static final Logger LOG = IsisLog.getLogger(OpiView.class);
//    
//    /**
//     * Default constructor, but also registers this view in the OPI view model so that it
//     * can be reloaded when the instrument changes.
//     */
//    public OpiView() {
//    	OpiViewModel.addView(this);
//    }
//    
//    /**
//     * Utility method for generating a blank set of macros.
//     * @return a new, empty set of macros
//     */
//    public MacrosInput emptyMacrosInput() {
//    	return new MacrosInput(new LinkedHashMap<String, String>(), true);
//    }
//
//    /**
//     * Initialise OPI from a path.
//     *
//     * @throws OPIViewCreationException the OPI view creation exception
//     */
//    public void initialiseOPI() throws OPIViewCreationException {
//        try {
//            final RunnerInput input = new RunnerInput(opi(), null, macros());
//            setOPIInput(input, false);
//        } catch (PartInitException e) {
//        	LoggerUtils.logErrorWithStackTrace(LOG, String.format("Failed to init OPI %s: %s", opi(), e.getMessage()), e);
//        }
//    }
//    
//    /**
//     * Macros that the OPI has.
//     *
//     * @return the macros for input to the OPI
//     */
//    protected abstract MacrosInput macros();
//
//    /**
//     * Get the OPI path.
//     *
//     * @return the path
//     * @throws OPIViewCreationException if the path can not be found or can not
//     *             be read
//     */
//    protected abstract Path opi() throws OPIViewCreationException;
//}