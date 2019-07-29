
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

package uk.ac.stfc.isis.ibex.ui.banner.views;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.banner.Banner;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerButton;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerCustom;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.ui.banner.controls.ControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.BannerItemModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.CurrentConfigModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.CustomControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Control;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Indicator;

/**
 * View of the spangle banner containing various instrument status messages.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class BannerView {

    private static final Font ALARM_FONT = SWTResourceManager.getFont("Arial", 10, SWT.BOLD);

    /**
     * View ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.banner.views.BannerView";

    private final Banner banner = Banner.getInstance();

    private Composite bannerItemPanel;
    private GridLayout glBannerItemPanel;
    
    /**
     * Create the controls for the part.
     * 
     * @param parent parent panel to add controls to
     */
    @PostConstruct
    public void createPartControl(Composite parent) {

        GridLayout glParent = new GridLayout(10, false);
        glParent.marginRight = 2;
        glParent.horizontalSpacing = 8;
        glParent.verticalSpacing = 0;
        glParent.marginWidth = 0;
        glParent.marginHeight = 0;
        parent.setLayout(glParent);

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd.heightHint = 35;
        bannerItemPanel = new Composite(parent, SWT.RIGHT_TO_LEFT);
        bannerItemPanel.setLayout(glBannerItemPanel = new GridLayout(1, false));
        bannerItemPanel.setLayoutData(gd);
        glBannerItemPanel.marginHeight = 0;
        glBannerItemPanel.horizontalSpacing = 15;

        banner.observables().bannerDescription.addObserver(modelAdapter);

    }

    /**
     * Converts a collection of banner item objects (the banner description)
     * into models readable by indicator widgets in the UI.
     * 
     * @param items the banner items
     * @return the indicator models
     */
    private Collection<IndicatorModel> convertBannerItems(Collection<BannerItem> items) {
        Collection<IndicatorModel> convertedItems = new ArrayList<IndicatorModel>();
        if (!(items == null)) {
            for (BannerItem item : items) {
                convertedItems.add(new BannerItemModel(item));
            }
        }
        return convertedItems;
    }
    
    /**
     * Converts a collection of banner button objects into models
     * 
     * @param items the banner buttons
     * @return the control models
     */
    private Collection<ControlModel> convertBannerButtons(Collection<BannerButton> buttons) {
        Collection<ControlModel> convertedItems = new ArrayList<ControlModel>();
        if (!(buttons == null)) {
            for (BannerButton button : buttons) {
                convertedItems.add(new CustomControlModel(button));
            }
        }
        return convertedItems;
    }

    /**
     * Adds an indicator widget for each banner item.
     * 
     * @param indicatorModels the banner item indicator models observed by the widget
     */
    private void setBanner(final Collection<IndicatorModel> indicatorModels, final Collection<ControlModel> controlModels) {
        disposeBanner();
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                glBannerItemPanel.numColumns = indicatorModels.size() + controlModels.size() + 3;

                // The models all contain a unique index which depends on the order the
                // items and buttons were listed in the XML.
                // The indexes are then used to display the banner elements in the intended order.
                for (int i = 0; i < indicatorModels.size() + controlModels.size(); i++) {
                    for (IndicatorModel model : indicatorModels) {
                        if (model.index() == i) {
                            Indicator bannerItem = new Indicator(bannerItemPanel, SWT.LEFT_TO_RIGHT, model, ALARM_FONT);
                            GridData gdBannerItem = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
                            gdBannerItem.widthHint = model.width();
                            bannerItem.setLayoutData(gdBannerItem);
                        }
                    }
                    for (ControlModel model : controlModels) {
                        if (model.index() == i) {
                            Control bannerButton = new Control(bannerItemPanel, SWT.NONE, model);
                            GridData gdBannerControl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
                            gdBannerControl.widthHint = model.width();
                            gdBannerControl.heightHint = model.height();
                            bannerButton.setLayoutData(gdBannerControl);
                        }
                    }
                }
                
                Indicator currentConfig = new Indicator(bannerItemPanel, SWT.LEFT_TO_RIGHT, new CurrentConfigModel(), ALARM_FONT);
                GridData gdCurrentConfig = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
                gdCurrentConfig.widthHint = 360;
                currentConfig.setLayoutData(gdCurrentConfig);

                bannerItemPanel.layout(true);
            }
        });
    }

    /**
     * Removes all indicators for instrument-specific properties from the
     * spangle banner.
     */
    private void disposeBanner() {
    	// As this involves disposing items, do it using a sync exec to avoid "widget is disposed" issues.
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                for (org.eclipse.swt.widgets.Control item : bannerItemPanel.getChildren()) {
                    item.dispose();
                }
            }
        });
    }

    /**
     * Observes the banner model and forwards changes to the UI.
     */
    private final BaseObserver<BannerCustom> modelAdapter = new BaseObserver<BannerCustom>() {
        @Override
        public void onValue(BannerCustom value) {
            setBanner(convertBannerItems(value.items), convertBannerButtons(value.buttons));
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
            if (!isConnected) {
                disposeBanner();
            }
        }
    };

}
