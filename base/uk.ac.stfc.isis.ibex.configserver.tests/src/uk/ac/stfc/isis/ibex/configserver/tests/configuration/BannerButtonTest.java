/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
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

package uk.ac.stfc.isis.ibex.configserver.tests.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.function.Function;

import org.junit.Test;
import org.junit.Before;

import uk.ac.stfc.isis.ibex.configserver.configuration.BannerButton;
import uk.ac.stfc.isis.ibex.configserver.configuration.CustomBannerData;
import uk.ac.stfc.isis.ibex.configserver.json.JsonConverters;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

import org.eclipse.swt.graphics.RGB;

/**
 * Unit tests for the BannerButton class
 */
public class BannerButtonTest {
	
	private String pvName = "MYPV";
    private String bannerJsonLocal = String.format(
    		"{\"buttons\": [{\"index\": 0, \"fontSize\": \"16\", \"pv\": \"%s\", \"name\": \"Button\", \"textColour\": \"#ae98fa\","
    		+ " \"buttonColour\": \"#ff0000\", \"local\": \"true\", \"width\": \"150\", \"pvValue\": \"1\", \"height\": \"50\"}],\"items\": []}", 
    		pvName);
    private String bannerJsonNotLocal = String.format(
    		"{\"buttons\": [{\"index\": 0, \"fontSize\": \"16\", \"pv\": \"%s\", \"name\": \"Button\", \"textColour\": \"#ee88ee\","
    		+ " \"buttonColour\": \"#ff00ff\", \"local\": \"false\", \"width\": \"150\", \"pvValue\": \"1\", \"height\": \"50\"}],\"items\": []}", 
    		pvName);
    private Function<String, CustomBannerData> conv = new JsonConverters().toBannerDescription();
    private CustomBannerData bannerLocal;
    private CustomBannerData bannerNotLocal;

	
	@Before
	public void setUp() throws ConversionException {

		try {
			// Build banner and thus banner buttons from JSON
			bannerLocal = conv.apply(bannerJsonLocal);
		    bannerNotLocal = conv.apply(bannerJsonNotLocal);
		    
		} catch(ConversionException e) {
			fail("Check banner JSON data , should not fail to convert");
		}
	}
	
	@Test
	public void WHEN_pv_is_local_THEN_returns_without_prefix() {
		System.out.println(Instrument.getInstance());
		for (BannerButton button : bannerLocal.buttons) {
            assertEquals("Returned pv name should just be the pv no prefix", button.pv(), InstrumentUtils.addPrefix(pvName));
        }
	}
	
	@Test
	public void WHEN_pv_is_not_local_THEN_returns_without_prefix() {
		for (BannerButton button : bannerNotLocal.buttons) {
            assertEquals("Returned pv name should just be the pv no prefix", button.pv(), pvName);
        }
	}
	
	@Test
	public void WHEN_button_text_color_hex_THEN_convert_to_rgb() {
		for (BannerButton button : bannerLocal.buttons) {
            assertEquals("Returned colour should be rgb version of the hex button colour", button.buttonColour(), new RGB(255, 0, 0));
            assertEquals("Returned colour should be rgb verion of hex text colour", button.textColour(), new RGB(174, 152, 250));
        }
		for (BannerButton button : bannerNotLocal.buttons) {
			assertEquals("Returned colour should be rgb version of the hex button colour", button.buttonColour(), new RGB(255, 0, 255));
            assertEquals("Returned colour should be rgb verion of hex text colour", button.textColour(), new RGB(238, 136, 238));
        }
	}

}
