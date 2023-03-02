//
///*
// * This file is part of the ISIS IBEX application.
// * Copyright (C) 2012-2019 Science & Technology Facilities Council.
// * All rights reserved.
// *
// * This program is distributed in the hope that it will be useful.
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution.
// * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
// * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
// * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
// *
// * You should have received a copy of the Eclipse Public License v1.0
// * along with this program; if not, you can obtain a copy from
// * https://www.eclipse.org/org/documents/epl-v10.php or 
// * http://opensource.org/licenses/eclipse-1.0.php
// */
//
///**
// * 
// */
//package uk.ac.stfc.isis.ibex.ui.reflectometry;
//
//import org.csstudio.opibuilder.util.MacrosInput;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.ui.IViewSite;
//import org.eclipse.ui.PartInitException;
//
//import uk.ac.stfc.isis.ibex.instrument.Instrument;
//import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
//import uk.ac.stfc.isis.ibex.opis.Opi;
//import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;
//
///**
// * Shows a stand-alone view for a reflectometry OPI.
// */
//public abstract class ReflectometryOpiTargetView extends OpiTargetView {
//	/**
//	 * Class ID.
//	 */
//	public static final String ID = "uk.ac.stfc.isis.ibex.ui.ReflectometryOpiTargetView";
//
//    /**
//     * File name of the reflectometry OPI.
//     * 
//     * @return the OPI file name
//     */
//	protected abstract String getOpiName();
//
//    /**
//     * The title of the OPI view.
//     * 
//     * @return the OPI title
//     */
//	protected abstract String getOpiTitle();
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected Path opi() throws OPIViewCreationException {
//		return Opi.getDefault().opiProvider().pathFromName(getOpiName());
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void init(IViewSite site) throws PartInitException {
//		super.init(site);
//		try {
//			initialiseOPI();
//		} catch (OPIViewCreationException e) {
//			throw new PartInitException(e.getMessage(), e);
//		}
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public MacrosInput macros() {
//		MacrosInput macros = emptyMacrosInput();
//		macros.put("NAME", getOpiTitle());
//		macros.put("P", Instrument.getInstance().currentInstrument().pvPrefix());
//		return macros;
//	}
//
//}
