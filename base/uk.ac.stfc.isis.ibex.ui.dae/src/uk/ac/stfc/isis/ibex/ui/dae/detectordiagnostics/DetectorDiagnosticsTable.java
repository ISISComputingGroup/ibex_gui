 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.DetectorDiagnosticsViewModel;
import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.SpectrumInformation;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * The Detector diagnostics table.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DetectorDiagnosticsTable extends DataboundTable<SpectrumInformation> {
    
    /**
     * Instantiates a new device screens table.
     *
     * @param parent the parent
     * @param style the style
     * @param tableStyle the table style
     */
    public DetectorDiagnosticsTable(Composite parent, int style, int tableStyle) {
        super(parent, style, SpectrumInformation.class, tableStyle);       
        initialise();        
    }
    
    private static final String DISPLAY_STRING_FOR_NULL_VALUE = "No data";
    
    @Override
    protected void addColumns() {
        createSpectrumNumberColumn();
        createCountRateColumn();
        createMaxSpecBinCountColumn();
        createIntegralColumn();
    }
    
    /**
     * Binds this table to the underlying data.
     */
    public void bind() {
        
        final DetectorDiagnosticsViewModel model = DetectorDiagnosticsViewModel.getInstance(); 
        
        (new DataBindingContext()).bindValue(WidgetProperties.enabled().observe(this), BeanProperties.value("diagnosticsEnabled").observe(model)); 
        
        model.addPropertyChangeListener("spectra", new PropertyChangeListener() {
            
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setRows(model.getSpectra());
            }
        });
        
        model.startObserving(); 
    } 

    private void createSpectrumNumberColumn() {
        TableViewerColumn number = createColumn("Spectrum number", 20);
        number.setLabelProvider(new DataboundCellLabelProvider<SpectrumInformation>(observeProperty("spectrumNumber")) {
            @Override
            protected String valueFromRow(SpectrumInformation row) {
                try {
                    return row.getSpectrumNumber().toString();
                } catch (NullPointerException e) {
                    return DISPLAY_STRING_FOR_NULL_VALUE;
                }
            }
        });
    }
    
    private void createCountRateColumn() {
        TableViewerColumn countRate = createColumn("Count rate", 20);
        countRate.setLabelProvider(new DataboundCellLabelProvider<SpectrumInformation>(observeProperty("countRate")) {
            @Override
            protected String valueFromRow(SpectrumInformation row) {
                try {
                    return row.getCountRate().toString();
                } catch (NullPointerException e) {
                    return DISPLAY_STRING_FOR_NULL_VALUE;
                }
            }
        });
    }
    
    private void createMaxSpecBinCountColumn() {
        TableViewerColumn maximum = createColumn("Maximum", 20);
        maximum.setLabelProvider(new DataboundCellLabelProvider<SpectrumInformation>(observeProperty("maxSpecBinCount")) {
            @Override
            protected String valueFromRow(SpectrumInformation row) {
                try {
                    return row.getMaxSpecBinCount().toString();
                } catch (NullPointerException e) {
                    return DISPLAY_STRING_FOR_NULL_VALUE;
                }
            }
        });
    }
    
    private void createIntegralColumn() {
        TableViewerColumn integral = createColumn("Integral", 20);
        integral.setLabelProvider(new DataboundCellLabelProvider<SpectrumInformation>(observeProperty("integral")) {
            @Override
            protected String valueFromRow(SpectrumInformation row) {
                try {
                    return row.getIntegral().toString();
                } catch (NullPointerException e) {
                    return DISPLAY_STRING_FOR_NULL_VALUE;
                }
            }
        });
    }
}
