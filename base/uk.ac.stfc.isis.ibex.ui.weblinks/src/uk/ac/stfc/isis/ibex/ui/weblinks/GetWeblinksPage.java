
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
package uk.ac.stfc.isis.ibex.ui.weblinks;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class has some static methods to retrive the SECI Links webpage, find
 * the hyperlinks and return them.
 */
public final class GetWeblinksPage {

    private static final String SECI_LINKS_URL = "http://dataweb.isis.rl.ac.uk/SeciLinks/default.htm";
    private static final String REGEX_FOR_HTML_LINKS = "<a href=(.*?)</a>";

    /**
     * Utility class, now allowed to instantiate.
     */
    private GetWeblinksPage() {
    }

    /**
     * Gets the web links from the SECI Links URL.
     * 
     * @return A list of strings containing weblinks, in standard HTML format
     */
    public static List<String> getWebLinks() {
        String html = getWebLinkHtml();

        List<String> webLinksList = new ArrayList<>();

        Pattern pattern = Pattern.compile(REGEX_FOR_HTML_LINKS);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            webLinksList.add(matcher.group(0));
        }
        
        return webLinksList;
    }

    /**
     * Gets the raw HTML content from the SECI Links URL.
     * 
     * @return A string containing the HTML content.
     */
    private static String getWebLinkHtml() {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(SECI_LINKS_URL).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return content;
    }

}
