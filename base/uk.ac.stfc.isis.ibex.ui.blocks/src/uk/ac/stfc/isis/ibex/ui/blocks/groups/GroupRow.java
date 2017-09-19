package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;

public class GroupRow extends Composite {
	
    private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
    private static final int VALUE_WIDTH = 75;
    private static final int VALUE_HEIGHT = 17;
    private static final int VALUE_WIDTH_MARGIN = 4;
    private static final int VALUE_HEIGHT_MARGIN = 2;
    
    private Label lblName;
    private Label lblValue;
    private Label lblStatus;

	public GroupRow(Composite parent, int style, DisplayBlock block) {
		super(parent, style);
		
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        this.setBackground(WHITE);
        this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        lblName =
                labelMaker(this, SWT.NONE, block.getName() + ": ", block.getDescription(), null);
        lblName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

        // additional container to hold value label to draw a border around it
        Composite valueContainer = new Composite(this, SWT.CENTER);
        GridLayout valueContainerLayout = new GridLayout(1, false);
        valueContainerLayout.marginWidth = 2;
        valueContainerLayout.marginHeight = 2;
        valueContainerLayout.verticalSpacing = 0;
        valueContainerLayout.horizontalSpacing = 0;
        valueContainer.setLayout(valueContainerLayout);

        GridData gdValueContainer = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gdValueContainer.widthHint = VALUE_WIDTH + VALUE_WIDTH_MARGIN;
        gdValueContainer.heightHint = VALUE_HEIGHT + VALUE_HEIGHT_MARGIN;
        valueContainer.setLayoutData(gdValueContainer);

        lblValue =
                labelMaker(valueContainer, SWT.CENTER, block.getValue(), block.getDescription(),
                        null);
        GridData gdValue = new GridData(SWT.CENTER, SWT.NONE, false, false, 1, 1);
        gdValue.widthHint = VALUE_WIDTH;
        lblValue.setLayoutData(gdValue);
        lblValue.setVisible(true);
        valueContainer.pack();

        lblStatus = labelMaker(this, SWT.CENTER, "", "Run Control Status", null);
        FontDescriptor boldDescriptor = FontDescriptor.createFrom(lblStatus.getFont()).setStyle(SWT.BOLD);
        Font boldFont = boldDescriptor.createFont(lblStatus.getDisplay());
        lblStatus.setFont(boldFont);
        GridData gdStatus = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gdStatus.widthHint = VALUE_HEIGHT;
        lblStatus.setLayoutData(gdStatus);

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.text().observe(lblValue),
                BeanProperties.value("value").observe(block));

        bindingContext.bindValue(WidgetProperties.visible().observe(lblStatus),
                BeanProperties.value("enabled").observe(block));

        UpdateValueStrategy symbolStrategy = new UpdateValueStrategy();
        symbolStrategy.setConverter(new RunControlSymbolConverter());

        bindingContext.bindValue(WidgetProperties.text().observe(lblStatus),
                BeanProperties.value("runcontrolState").observe(block), null, symbolStrategy);

        UpdateValueStrategy fgColourStrategy = new UpdateValueStrategy();
        fgColourStrategy.setConverter(new RunControlForegroundColourConverter());

        bindingContext.bindValue(WidgetProperties.foreground().observe(lblStatus),
                BeanProperties.value("runcontrolState").observe(block), null, fgColourStrategy);

        UpdateValueStrategy bgColourStrategy = new UpdateValueStrategy();
        bgColourStrategy.setConverter(new RunControlBackgroundColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(lblStatus),
                BeanProperties.value("runcontrolState").observe(block), null, bgColourStrategy);

        UpdateValueStrategy borderStrategy = new UpdateValueStrategy();
        borderStrategy.setConverter(new BlockStatusBorderColourConverter());

        bindingContext.bindValue(WidgetProperties.background().observe(valueContainer),
                BeanProperties.value("blockState").observe(block), null, borderStrategy);
	}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		lblName.setMenu(menu);
		lblValue.setMenu(menu);
		lblStatus.setMenu(menu);
	}

    private Label labelMaker(Composite composite, int style, String text, String toolTip, Font font) {
        Label label = new Label(composite, style);
        if (text != null) {
            label.setText(text);
        }

        label.setBackground(WHITE);

        if (toolTip != null) {
            label.setToolTipText(toolTip);
        }

        if (font != null) {
            label.setFont(font);
        }

        return label;
    }

}
