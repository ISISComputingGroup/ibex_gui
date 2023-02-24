
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
import java.util.Collections;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.banner.Banner;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerButton;
import uk.ac.stfc.isis.ibex.configserver.configuration.BannerItem;
import uk.ac.stfc.isis.ibex.configserver.configuration.CustomBannerData;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.instrument.status.ServerStatusVariables;
import uk.ac.stfc.isis.ibex.ui.banner.models.BannerItemModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.CustomControlModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Control;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.Indicator;
import uk.ac.stfc.isis.ibex.ui.banner.widgets.StatusIndicatorPanel;

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

	private Collection<CustomControlModel> activeModels = Collections.emptyList();
    
    /**
     * Create the controls for the part.
     * 
     * @param parent parent panel to add controls to
     */
    @PostConstruct
    public void createPartControl(Composite parent) {

        GridLayout glParent = new GridLayout(10, false);
        glParent.marginRight = 0;
        glParent.horizontalSpacing = 0;
        glParent.verticalSpacing = 0;
        glParent.marginWidth = 0;
        glParent.marginHeight = 0;
        parent.setLayout(glParent);
        
        Composite serverStatusPanel = new Composite(parent, SWT.NONE);
        serverStatusPanel.setLayout(new GridLayout());
        GridData gdServerStatus = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        serverStatusPanel.setLayoutData(gdServerStatus);
        
        ServerStatusViewModel model = new ServerStatusViewModel(new ServerStatusVariables());

        StatusIndicatorPanel statusIndicator = new StatusIndicatorPanel(serverStatusPanel, SWT.NONE, model);
        statusIndicator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));

        GridData gdBannerItems = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gdBannerItems.heightHint = 35;
        bannerItemPanel = new Composite(parent, SWT.RIGHT_TO_LEFT);
        bannerItemPanel.setLayout(glBannerItemPanel = new GridLayout(1, false));
        bannerItemPanel.setLayoutData(gdBannerItems);
        glBannerItemPanel.marginHeight = 0;
        glBannerItemPanel.horizontalSpacing = 15;
        
        banner.observables().bannerDescription.subscribe(modelAdapter);

    }

    /**
     * Converts a collection of banner item objects (the banner description)
     * into models readable by indicator widgets in the UI.
     * 
     * @param items the banner items
     * @return the indicator models
     */
    private Collection<BannerItemModel> convertBannerItems(Collection<BannerItem> items) {
        Collection<BannerItemModel> convertedItems = new ArrayList<>();
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
     * @param buttons the banner buttons
     * @return the control models
     */
    private Collection<CustomControlModel> convertBannerButtons(Collection<BannerButton> buttons) {
        if (buttons != null) {
            return buttons.stream().map(CustomControlModel::new).collect(Collectors.toList());
        } else {
            return new ArrayList<CustomControlModel>();
        }
    }

    /**
     * Adds an indicator widget for each banner item.
     * 
     * @param indicatorModels the banner item indicator models observed by the widget
     * @param controlModels the banner button control models
     */
    private void setBanner(final Collection<BannerItemModel> indicatorModels, final Collection<CustomControlModel> controlModels) {
        disposeBanner();
        
        // Ensure that previous models get closed.
        activeModels.forEach(CustomControlModel::close);
        activeModels = controlModels;
        
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                glBannerItemPanel.numColumns = indicatorModels.size() + controlModels.size();

                // The models all contain a unique index which depends on the order the
                // items and buttons were listed in the XML.
                // The indexes are then used to display the banner elements in the intended order.
                for (int i = 0; i < indicatorModels.size() + controlModels.size(); i++) {
                    for (BannerItemModel model : indicatorModels) {
                        if (model.index() == i) {
                            drawIndicator(model);
                        }
                    }
                    for (CustomControlModel model : controlModels) {
                        if (model.index() == i) {
                            drawButton(model);
                        }
                    }
                }

                bannerItemPanel.layout(true);
            }
        });
    }
    
    /**
     * Draw an indicator using the model.
     * @param model the BannerItemModel to use to draw the indicator
     */
    private void drawIndicator(BannerItemModel model) {
        Indicator bannerItem = new Indicator(bannerItemPanel, SWT.LEFT_TO_RIGHT, model, ALARM_FONT);
        GridData gdBannerItem = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        gdBannerItem.widthHint = model.width();
        bannerItem.setLayoutData(gdBannerItem);
    }
    
    /**
     * Draw an butotn using the model.
     * @param model the ControlModel to use to draw the button
     */
    private void drawButton(CustomControlModel model) {
        Control bannerButton = new Control(bannerItemPanel, SWT.NONE, model);
        GridData gdBannerControl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gdBannerControl.widthHint = model.width();
        gdBannerControl.heightHint = model.height();
        bannerButton.setLayoutData(gdBannerControl);
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
    private final BaseObserver<CustomBannerData> modelAdapter = new BaseObserver<CustomBannerData>() {
        @Override
        public void onValue(CustomBannerData value) {
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
