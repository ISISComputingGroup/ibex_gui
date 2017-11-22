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

package uk.ac.stfc.isis.ibex.databases;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The databases plugin class.
 */
public class Databases extends AbstractUIPlugin {
    private static Databases instance;
    private static BundleContext context;

    /**
     * @return the plugin instance
     */
    public static Databases getInstance() {
        return instance;
    }

    /**
     * @return get the default instance
     */
    public static Databases getDefault() {
        return instance;
    }

    /**
     * Constructor.
     */
    public Databases() {
        super();
        instance = this;
    }

    static BundleContext getContext() {
        return context;
    }

    /**
     * Start the plugin.
     * 
     * @param bundleContext the associated context
     * @throws Exception occurs if the plugin cannot be started
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Databases.context = bundleContext;
    }

    /**
     * Stop the plugin.
     * 
     * @param bundleContext the associated context
     * @throws Exception occurs if the plugin cannot be started
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Databases.context = null;
    }
}
