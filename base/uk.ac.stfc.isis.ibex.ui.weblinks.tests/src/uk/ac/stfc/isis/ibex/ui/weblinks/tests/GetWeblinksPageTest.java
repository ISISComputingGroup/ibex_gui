 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.weblinks.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.ui.weblinks.GetWeblinksPage;

/**
 *
 */
public class GetWeblinksPageTest {
    private static final String NEWLINE = "\n";
    private static final String SECTION1 = "Section 1";
    private static final String SECTION2 = "Section 2";
    private static final String LINK1 = "Link 1";
    private static final String LINK2 = "Link 2";
    private static final String LINK3 = "Link 3";
    private static final String ADDRESS1 = "<a href=\"http://www.address1.com\" target=\"_blank\">" + LINK1 + "</a>";
    private static final String ADDRESS2 = "<a href=\"http://www.address2.com\" target=\"_blank\">" + LINK2 + "</a>";
    private static final String ADDRESS3 = "<a href=\"http://www.address3.com\" target=\"_blank\">" + LINK3 + "</a>";
    String html;
    @Before
    public void setUp() {
        html = "<h3>" + SECTION1 + "</h3>" + NEWLINE
                + "<p>" + NEWLINE
                + ADDRESS1 + NEWLINE
                + "</p>" + NEWLINE
                + "<p>" + NEWLINE
                + ADDRESS2 + NEWLINE
                + "</p>" + NEWLINE
                + "<h3>" + SECTION2 + "</h3>" + NEWLINE
                + "<p>" + NEWLINE
                + ADDRESS3 + NEWLINE
                + "</p>";
    }

    @Test
    public void GIVEN_raw_html_WHEN_parsing_section_titles_THEN_returned_list_contains_correct_titles() {
        // Arrange
        List<String> expected = new ArrayList<String>(Arrays.asList(SECTION1, SECTION2));
        
        // Act
        List<String> actual = GetWeblinksPage.getSections(html);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_section_title_WHEN_parsing_weblinks_THEN_returned_list_contains_weblinks_in_section() {
        // Arrange
        List<String> expected = new ArrayList<String>(Arrays.asList(ADDRESS1, ADDRESS2));

        // Act
        List<String> actual = GetWeblinksPage.getWebLinks(SECTION1, html);

        // Assert
        assertEquals(expected, actual);
    }

}
