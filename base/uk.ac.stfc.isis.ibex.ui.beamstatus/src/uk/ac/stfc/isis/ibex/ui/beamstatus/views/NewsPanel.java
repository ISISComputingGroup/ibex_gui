
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class NewsPanel extends Composite {

    private static final String MCR_NEWS_PAGE_URL = "http://www.isis.stfc.ac.uk/files/mcr-news/mcrnews.txt";

    public static final String ID = "uk.ac.stfc.isis.ibex.ui.beamstatus.views.newsPanel"; //$NON-NLS-1$

    private static final long WEB_PAGE_REFRESH_PERIOD = 30000; // milliseconds

    private Browser newsBrowser;

    public NewsPanel(Composite parent, int style) {
		super(parent, style);
        newsBrowser = new Browser(this, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
        newsBrowser.setJavascriptEnabled(false);
        newsBrowser.setUrl(MCR_NEWS_PAGE_URL);
		startTimer();
	}

    private void startTimer() {
        Timer timer = new Timer();
        long delay = WEB_PAGE_REFRESH_PERIOD;
        timer.scheduleAtFixedRate(updateBrowser(newsBrowser), delay, WEB_PAGE_REFRESH_PERIOD);
    }

    private TimerTask updateBrowser(final Browser browser) {
        return new TimerTask() {
            @Override
            public void run() {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        browser.refresh();
                    }
                });
            }
        };
    }

}
