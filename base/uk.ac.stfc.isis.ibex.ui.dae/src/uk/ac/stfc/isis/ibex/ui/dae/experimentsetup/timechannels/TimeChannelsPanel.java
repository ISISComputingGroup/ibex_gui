
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.CalculationMethod;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeUnit;
import uk.ac.stfc.isis.ibex.model.DeferredPropertyChangeListener;
import uk.ac.stfc.isis.ibex.ui.UIUtils;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.DaeExperimentSetupCombo;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.DaeExperimentSetupRadioButton;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.PanelViewModel;

/**
 * Panel containing all controls for time channel settings of an experiment.
 */
public class TimeChannelsPanel extends Composite {
    private Composite tcbSettingsSwitchPanel;
    private Composite tcbFilePanel;
    private Composite timeRegimesPanel;
	private List<TimeRegimeView> timeRegimeViews = new ArrayList<TimeRegimeView>();
	private ArrayList<DaeExperimentSetupCombo> combos = new ArrayList<DaeExperimentSetupCombo>();
	
	private TimeChannelsViewModel viewModel;
	private DaeExperimentSetupCombo timeUnit;
    private DataBindingContext bindingContext;
    StackLayout stack;
    
    private ArrayList<Boolean> calcMethodBtnsRB = new ArrayList<Boolean>();
    
    private DaeExperimentSetupCombo timeChannelFileSelector;
    private Label timeChannelFileRB;
    private DaeExperimentSetupRadioButton radioSpecifyParameters;
    private DaeExperimentSetupRadioButton radioUseTCBFile;
    private ArrayList<DaeExperimentSetupRadioButton> calcMethodBtns = new ArrayList<DaeExperimentSetupRadioButton>();
    private Composite timeChannelFilePanel;
    private Composite timeUnitPanel;
    private final Map<String, DeferredPropertyChangeListener> viewModelListeners = new HashMap<>();

    private static final int TOP_MARGIN_WIDTH = 5;
    private static final int HORIZONTAL_SPACING = 100;

    private PanelViewModel panelViewModel;
    /**
     * Constructor for the time channel settings panel.
     * 
     * @param parent the parent composite.
     * @param style the SWT style.
     * @param panelViewModel The viewModel that helps manipulate the panels.
     */
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public TimeChannelsPanel(Composite parent, int style, PanelViewModel panelViewModel) {

		super(parent, style);
		this.panelViewModel = panelViewModel;
        GridLayout glParent = new GridLayout(1, false);
        glParent.verticalSpacing = 15;
        glParent.marginTop = 15;
        glParent.marginLeft = 5;
        setLayout(glParent);

        Label lblSelectionMethod = new Label(this, SWT.NONE);
        lblSelectionMethod.setText("Select Calculation Method: ");
        addMethodSelectionPanel(this);

        Group tcbSettings = new Group(this, SWT.NONE);
        tcbSettings.setText("Time Channel Settings");
        tcbSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tcbSettings.setLayout(new GridLayout(1, false));
        addTimeUnitPanel(tcbSettings);

        tcbSettingsSwitchPanel = new Composite(tcbSettings, SWT.NONE);
        tcbSettingsSwitchPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack = new StackLayout();
        tcbSettingsSwitchPanel.setLayout(stack);

        tcbFilePanel = new Composite(tcbSettingsSwitchPanel, SWT.NONE);
        tcbFilePanel.setLayout(new GridLayout(1, false));
        addTimeChannelFilePanel(tcbFilePanel);

        timeRegimesPanel = new Composite(tcbSettingsSwitchPanel, SWT.FILL);
        timeRegimesPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        timeRegimesPanel.setLayout(new GridLayout(3, false));
        
        this.addDisposeListener(evt -> removeListeners());
        
        fillWidgetLists();
	}

    /**
     * Binds model parameters to GUI elements.
     * 
     * @param viewModel The time channel settings model
     */
	public void setModel(final TimeChannelsViewModel viewModel) {
        this.viewModel = viewModel;

        bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(timeUnit),
                BeanProperties.value("timeUnit").observe(viewModel));
        bindingContext.bindList(WidgetProperties.items().observe(timeChannelFileSelector),
                BeanProperties.list("timeChannelFileList").observe(viewModel));
        bindingContext.bindValue(WidgetProperties.text().observe(timeChannelFileRB),
                BeanProperties.value("timeChannelFile").observe(viewModel));
        
        viewModelListeners.put("timeChannelFile", new DeferredPropertyChangeListener(new PropertyChangeListener() {        
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Display.getDefault().asyncExec(() -> timeChannelFileSelector.setCachedValue(timeChannelFileRB.getText()));
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS));
        
        viewModelListeners.put("timeRegimes", new DeferredPropertyChangeListener(new PropertyChangeListener() {        
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTimeRegimes();
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS));
        
        viewModelListeners.put("calculationMethod",  new DeferredPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setCalculationMethod(viewModel.getCalculationMethod());
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS));
        
        updateTimeRegimes();
        setCalculationMethod(viewModel.getCalculationMethod());
	}
	
    /**
     * Updates time regime tables.
     */
	private void updateTimeRegimes() {
		Display.getDefault().asyncExec(() -> {
			clearExistingTimeRegimeViews();
	
			GridData timeRegimeGridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			int count = 1;
			String name = "";
			
			for (TimeRegime timeRegime : viewModel.timeRegimes()) {
			    name =  String.format("Time regime %d", count);
	            TimeRegimeView view = new TimeRegimeView(timeRegimesPanel, SWT.NONE, panelViewModel, name);
				view.setLayoutData(timeRegimeGridData);					
				
				view.setModel(timeRegime);
				view.setTitle(String.format("Time regime %d", count));
				
				count++;
				timeRegimeViews.add(view);
			}
			
	        timeRegimesPanel.requestLayout();
	        UIUtils.recursiveSetEnabled(timeRegimesPanel, timeRegimesPanel.getEnabled());
		});
	}
	
    /**
     * Clear all existing time regime tables.
     */
	private void clearExistingTimeRegimeViews() {
		for (TimeRegimeView view : timeRegimeViews) {
		    view.removeListener();
			view.dispose();
		}
		timeRegimeViews.clear();
	}
	
    /**
     * Sets new calculation method and updates GUI elements accordingly.
     * 
     * @param method the new calculation method
     */
    private void setCalculationMethod(final CalculationMethod method) {
        viewModel.setCalculationMethod(method);
        updateCalculationMethod();
    }
    
    private void updateCalculationMethod() {
    	Display.getDefault().asyncExec(() -> {
			
			final CalculationMethod method = viewModel.getCalculationMethod();
			
            radioSpecifyParameters.setSelection(method == CalculationMethod.SPECIFY_PARAMETERS);
            radioUseTCBFile.setSelection(method == CalculationMethod.USE_TCB_FILE);
            
	        stack.topControl = method == CalculationMethod.USE_TCB_FILE ? tcbFilePanel : timeRegimesPanel;
	        
            tcbSettingsSwitchPanel.layout();
            if (method == CalculationMethod.SPECIFY_PARAMETERS) {
                for (TimeRegimeView view : timeRegimeViews) {
                    view.createInitialCachedValues();
                }
            } else {
                timeChannelFileSelector.setCachedValue(timeChannelFileRB.getText());
            }
    	});
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private void addMethodSelectionPanel(Composite parent) {

        Composite methodSelectionPanel = new Composite(parent, SWT.NONE);
        methodSelectionPanel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
        methodSelectionPanel.setLayout(new GridLayout(1, false));
        GridLayout glCalcMethod = new GridLayout(1, false);
        glCalcMethod.horizontalSpacing = HORIZONTAL_SPACING;
        glCalcMethod.marginTop = TOP_MARGIN_WIDTH;

        radioSpecifyParameters = new DaeExperimentSetupRadioButton(methodSelectionPanel, SWT.RADIO, panelViewModel, "timeChannelsBtns");
        radioSpecifyParameters.setText(CalculationMethod.SPECIFY_PARAMETERS.toString());
        radioUseTCBFile = new DaeExperimentSetupRadioButton(methodSelectionPanel, SWT.RADIO, panelViewModel, "timeChannelsBtns");
        radioUseTCBFile.setText(CalculationMethod.USE_TCB_FILE.toString());
    }

    private void addTimeUnitPanel(Composite parent) {

        timeUnitPanel = new Composite(parent, SWT.NONE);
        timeUnitPanel.setLayout(new GridLayout(2, false));
        timeUnitPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        timeUnitPanel.setBackgroundMode(SWT.INHERIT_DEFAULT);

        Label lblTimeUnit = new Label(timeUnitPanel, SWT.NONE);
        lblTimeUnit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTimeUnit.setText("Time Unit:");

        timeUnit = new DaeExperimentSetupCombo(timeUnitPanel, SWT.DROP_DOWN | SWT.READ_ONLY, panelViewModel, "timeUnit");
        timeUnit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        timeUnit.setItems(TimeUnit.allToString().toArray(new String[0]));
        
       
    }

    @SuppressWarnings({ "checkstyle:magicnumber", })
    private void addTimeChannelFilePanel(Composite parent) {

        timeChannelFilePanel = new Composite(parent, SWT.NONE);
        timeChannelFilePanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1));
        GridLayout glTcbFilePanel = new GridLayout(3, false);
        glTcbFilePanel.horizontalSpacing = 20;
        timeChannelFilePanel.setLayout(glTcbFilePanel);
        timeChannelFilePanel.setBackgroundMode(SWT.INHERIT_DEFAULT);

        Label lblTimeChannel = new Label(timeChannelFilePanel, SWT.NONE);
        lblTimeChannel.setText("Time Channel File:");

        Label lblTimeChannelRB = new Label(timeChannelFilePanel, SWT.NONE);
        lblTimeChannelRB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTimeChannelRB.setText("Current:");

        timeChannelFileRB = new Label(timeChannelFilePanel, SWT.NONE);
        timeChannelFileRB.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        timeChannelFileRB.setFont(JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT));

        new Label(timeChannelFilePanel, SWT.NONE);

        Label lblTimeChannelChange = new Label(timeChannelFilePanel, SWT.NONE);
        lblTimeChannelChange.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblTimeChannelChange.setText("Change:");

        timeChannelFileSelector = new DaeExperimentSetupCombo(timeChannelFilePanel, SWT.DROP_DOWN | SWT.READ_ONLY, 
                panelViewModel, "timeChannelFileSelector");
        timeChannelFileSelector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        
        addNewTimeChannelFileListener();
    }
    
    /**
     * Adds a listener to set a new calculation method.
     */
    private void addCalculationMethodListener() {
        SelectionListener listener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (radioUseTCBFile.getSelection()) {
                    timeChannelFileSelector.setCachedValue(timeChannelFileRB.getText());
                    setCalculationMethod(CalculationMethod.USE_TCB_FILE);
                } else {
                    setCalculationMethod(CalculationMethod.SPECIFY_PARAMETERS);
                }
            }
        };
        
        radioSpecifyParameters.addSelectionListener(listener);
        radioUseTCBFile.addSelectionListener(listener);
    }
    /**
     * Adds a listener to set a new time channel file.
     */
    private void addNewTimeChannelFileListener() {
        timeChannelFileSelector.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.setNewTimeChannelFile(timeChannelFileSelector.getText());
            }
        });
    }
    
    /**
     * Fills the lists of widgets.
     */
    private void fillWidgetLists() {
        if (!calcMethodBtns.isEmpty()) {
            calcMethodBtns.clear();
        }
        calcMethodBtns.add(radioUseTCBFile);
        calcMethodBtns.add(radioSpecifyParameters);
        panelViewModel.setBtnsListToRadioButtons(calcMethodBtns);
        
        if (!combos.isEmpty()) {
            combos.clear();
        }
        combos.add(timeUnit);
        combos.add(timeChannelFileSelector);
    }
    
    /**
     * Creates a cache of the applied values for the different widgets.
     */
    public void createInitialCachedValues() {
       panelViewModel.createInitialComboCachedValues(combos);
       
       final CalculationMethod method = viewModel.getCalculationMethod();
       if (!calcMethodBtnsRB.isEmpty()) {
           calcMethodBtnsRB.clear();
       }
       calcMethodBtnsRB.add(method == CalculationMethod.USE_TCB_FILE);
       radioUseTCBFile.setSelection(method == CalculationMethod.USE_TCB_FILE);
       calcMethodBtnsRB.add(method == CalculationMethod.SPECIFY_PARAMETERS);
       radioSpecifyParameters.setSelection(method == CalculationMethod.SPECIFY_PARAMETERS);
       panelViewModel.createInitialRadioButtonsCachedValues(calcMethodBtns, calcMethodBtnsRB);
       if (method == CalculationMethod.SPECIFY_PARAMETERS) {
           for (TimeRegimeView view : timeRegimeViews) {
               view.createInitialCachedValues();
           }
       }
    }
    
    
    /**
     * Resets the cache of the applied values for the different widgets.
     */
    public void resetCachedValue() {
       panelViewModel.resetComboCachedValues(combos);
       
       final CalculationMethod method = viewModel.getCalculationMethod();
       if (!calcMethodBtnsRB.isEmpty()) {
           calcMethodBtnsRB.clear();
       }
       calcMethodBtnsRB.add(method == CalculationMethod.USE_TCB_FILE);
       calcMethodBtnsRB.add(method == CalculationMethod.SPECIFY_PARAMETERS);
       panelViewModel.resetRadioButtonsCachedValues(calcMethodBtns, calcMethodBtnsRB);
       if (!timeRegimeViews.isEmpty()) {
           for (TimeRegimeView view : timeRegimeViews) {
               view.resetCachedValues();
           }
       }
    }
    
    /**
     * Goes over every widget and adds a label to a widget if its value is different from the one applied on the instrument.
     */
    public void ifWidgetValueDifferentFromCachedValueThenChangeLabel() {
        panelViewModel.ifRadioButtonValuesDifferentFromCachedValuesThenChangeLabel(calcMethodBtns);
        panelViewModel.ifComboValuesDifferentFromCachedValueThenChangeLabel(combos);
        for (TimeRegimeView view : timeRegimeViews) {
            view.ifTableValueDifferentFromCachedValueThenChangeLabel();
        }
    }
    
    /**
     * Adds the various listeners to the viewModel.
     */
    private void addViewModelListeners() {
        for (Entry<String, DeferredPropertyChangeListener> listener : viewModelListeners.entrySet()) {
            this.viewModel.addPropertyChangeListener(listener.getKey(), listener.getValue());
        }
    }
    
    /**
     * Adds all the listeners used in the panel.
     */
    public void addListeners() {
        addViewModelListeners();
        addCalculationMethodListener();
        addNewTimeChannelFileListener();
    }
    
    /**
     * Removes listeners from the viewModel.
     */
    public void removeViewModelListeners() {
        for (Entry<String, DeferredPropertyChangeListener> listener : viewModelListeners.entrySet()) {
            this.viewModel.removePropertyChangeListener(listener.getKey(), listener.getValue());
            listener.getValue().close();
        }
        viewModelListeners.clear();
    }
    
    /**
     * Removes the listeners out dated when changes were applied.
     */
    public void removeListeners() {
        removeViewModelListeners();
        panelViewModel.removesCombosListeners(combos);
        panelViewModel.removesRadioButtonsListener(calcMethodBtns);
        if (!timeRegimeViews.isEmpty()) {
            for (TimeRegimeView view : timeRegimeViews) {
                view.removeListener();
            }
        }
    }
    
    /**
     * Removes old listeners and adds new ones. The new listeners will contain the last applied values of the given widgets.
     */
    public void updateListeners() {
        resetCachedValue();
        addListeners();
    }
}
