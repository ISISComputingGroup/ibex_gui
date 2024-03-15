package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

import uk.ac.stfc.isis.ibex.model.Action;
import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * Builder to create perspective buttons.
 */
public class IBEXButtonBuilder {
	private String text;
	private String tooltip;
	private Integer buttonStyle;
	private RowData rowData;
	private GridData gridData;
	private Composite parent;
	private Integer width;
	private Integer height;
	private Image image;
	private Action action;
	private Listener listener;
	private String link;
	private String description;
	private Boolean isHelpButton;

	// files/text used many times within perspectives.
	private static final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.widgets";
	private static final String HELP_ICON2 = "/icons/helpIcon.png";
	private static final String TOOLTIP_TEXT = "Open user manual link in browser for help with '%s': \n%s";

	/**
	 *  An expanding grid.
	 */
	public static GridData expandingGrid = new GridData(SWT.FILL, SWT.FILL, true, true);
	
	/**
	 * default grid.
	 */
	public static GridData defaultGrid = new GridData();
	
	/**
	 *  A compact grid.
	 */
	public static GridData compactGrid = new GridData(SWT.FILL, SWT.FILL, false, false);
	
	/**
	 *  A centre aligned grid.
	 */
	public static GridData centerGrid = new GridData(SWT.CENTER, SWT.CENTER, false, true);
	
	/**
	 *  default row data.
	 */
	public static RowData defaultRow = new RowData();

	/**
	 * Constructor with minimum paramaters required for button creation.
	 *
	 * @param parent the composite to add the button to
	 * @param style  the SWT style ENUM to assign the button
	 */
	public IBEXButtonBuilder(Composite parent, Integer style) {
		this.parent = parent;
		this.buttonStyle = style;
	}

	/**
	 * Set the tooltip for the button.
	 * 
	 * @param tooltip
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder tooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	/**
	 * sets a custom RowData layout.
	 * 
	 * @param layoutData
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder customLayoutData(RowData layoutData) {
		this.rowData = layoutData;
		return this;
	}

	/**
	 * sets a custom GridData layout.
	 * 
	 * @param layoutData
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder customLayoutData(GridData layoutData) {
		this.gridData = layoutData;
		return this;
	}

	/**
	 * sets the link for the button.
	 * 
	 * @param link
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder link(String link) {
		this.link = link;
		return this;
	}

	/**
	 * sets the description for the button.
	 * 
	 * @param description
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * sets the action the button should perform.
	 * 
	 * @param action
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder action(Action action) {
		this.action = action;
		return this;
	}

	/**
	 * sets the listener for the button.
	 * 
	 * @param listener
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder listener(Listener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * sets the text for the button.
	 * 
	 * @param text
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder text(String text) {
		this.text = text;
		return this;
	}

	/**
	 * sets the image for the button.
	 * 
	 * @param image
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder image(Image image) {
		this.image = image;
		return this;
	}

	/**
	 * sets the width for the button.
	 * 
	 * @param width
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder width(Integer width) {
		this.width = width;
		return this;
	}

	/**
	 * sets the height for the button.
	 * 
	 * @param height
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder height(Integer height) {
		this.height = height;
		return this;
	}

	/**
	 * sets the help button for the button.
	 * 
	 * @param isHelpButton
	 * @return IBEXButtonBuilder
	 */
	public IBEXButtonBuilder helpButton(Boolean isHelpButton) {
		this.isHelpButton = isHelpButton;
		return this;
	}

	/**
	 * builds the button with the set parameters.
	 * 
	 * @return Button
	 */
	public Button build() {
		Button button = new Button(parent, buttonStyle);

		// at start so if the user wants to override the default help button settings
		// they can do
		if (isHelpButton != null) {
			button.setToolTipText(String.format(TOOLTIP_TEXT, description, link));
			button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
			button.setImage(ResourceManager.getPluginImage(SYMBOLIC_PATH, HELP_ICON2));
		}

		if (action != null) {
			DataBindingContext bindingContext = new DataBindingContext();
			bindingContext.bindValue(WidgetProperties.enabled().observe(button),
					BeanProperties.value("canExecute").observe(action));

			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					action.execute();
				}
			});
		}

		button.setText(text);
		button.setToolTipText(tooltip);
		if (gridData != null) {
			gridData.widthHint = width;
			gridData.heightHint = height;
			button.setLayoutData(gridData);
		}
		if (rowData != null) {
			rowData.width = width;
			rowData.height = height;
			button.setLayoutData(rowData);
		}
		button.setImage(image);

		if (link != null) {
			button.addListener(SWT.Selection, e -> {
				try {
					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(link));
				} catch (PartInitException | MalformedURLException ex) {
					LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()),
							"Failed to open URL in browser: " + link, ex);
				}
			});
		}
		if (listener != null) {
			button.addListener(SWT.Selection, listener);
		}

		return button;
	}

}