
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
 * This class has some static methods to retrieve the IBEX Links webpage, find
 * sections and associated hyperlinks and return them.
 */
public final class GetWeblinksPage {

    private static final String IBEX_LINKS_URL = "http://dataweb.isis.rl.ac.uk/IbexLinks/default.htm";
    private static final String REGEX_FOR_HTML_LINKS = "<a href=(.*?)</a>";
    private static final String REGEX_FOR_HTML_TITLES = "<h[0-6]>(.*?)</h[0-6]>";
    // Match either HTML section between an h3 end- and start tag, or the
    // rest of the document if there is no further h3 tags.
    private static final String REGEX_FOR_HTML_SECTION = "((</h[0-6]>)(.*?)(<h[0-6]>)|(</h[0-6]>)(.+))";

    /**
     * Utility class, now allowed to instantiate.
     */
    private GetWeblinksPage() {
    }

    /**
     * Gets the section titles from the IBEX Links URL.
     * 
     * @return A list of strings containing section titles with HTML tags
     *         removed.
     */
    public static List<String> getSections() {
        return getSections(getWebLinkHtml());
    }

    /**
     * Gets the section titles from a passed string of HTML.
     * 
     * @param html
     *            The string of HTML to be scanned.
     * @return A list of strings containing section titles with HTML tags
     *         removed.
     */
    public static List<String> getSections(String html) {
        List<String> sectionList = new ArrayList<>();

        Pattern pattern = Pattern.compile(REGEX_FOR_HTML_TITLES);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String currentSection = removeTags(matcher.group(0));
            sectionList.add(currentSection);
        }
        return sectionList;
    }

    private static String removeTags(String s) {
        return s.replaceAll("\\<.*?>", "");
    }

    /**
     * Given a section title, gets the web links under that section from the
     * IBEX Links URL.
     * 
     * @param section
     *            The section containing the links.
     * @return A list of strings containing web links, in standard HTML format
     */
    public static List<String> getWebLinks(String section) {
        return getWebLinks(section, getWebLinkHtml());
    }

    /**
     * Given a section title, gets the web links under that section from the
     * passed HTML string.
     * 
     * @param section
     *            The section containing the links.
     * @param html
     *            The HTML data to be scanned.
     * @return A list of strings containing web links, in standard HTML format
     */
    public static List<String> getWebLinks(String section, String html) {

        List<String> webLinksList = new ArrayList<>();

        String sectionHtml = getSection(section, html);

        Pattern pattern = Pattern.compile(REGEX_FOR_HTML_LINKS);
        Matcher matcher = pattern.matcher(sectionHtml);
        while (matcher.find()) {
            webLinksList.add(matcher.group(0));
        }

        return webLinksList;
    }

    /**
     * Gets a substring of a given HTML string, containing only the content
     * under a specific section title.
     * 
     * @param section
     *            The title of the section.
     * @param html
     *            The HTML data to be scanned.
     * @return The HTML data under the section title as string.
     */
    private static String getSection(String section, String html) {
        String sectionHtml = "";

        Pattern pattern = Pattern.compile(section + REGEX_FOR_HTML_SECTION, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            sectionHtml = matcher.group(0);
        }
        return sectionHtml;
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
            connection = new URL(IBEX_LINKS_URL).openConnection();
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
