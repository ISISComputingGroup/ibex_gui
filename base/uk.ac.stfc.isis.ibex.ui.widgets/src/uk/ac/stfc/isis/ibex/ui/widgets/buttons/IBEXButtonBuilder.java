package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import uk.ac.stfc.isis.ibex.model.Action;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

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

	private static final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.widgets";
	private static final String HELP_ICON2 = "/icons/helpIcon.png";
	private static final String TOOLTIP_TEXT = "Open user manual link in browser for help with '%s': \n%s";

	public static GridData expandingGrid = new GridData(SWT.FILL, SWT.FILL, true, true);
	public static GridData defaultGrid = new GridData();
	public static GridData compactGrid = new GridData(SWT.FILL, SWT.FILL, false, false);
	public static GridData fitGrid = new GridData(SWT.LEFT, SWT.FILL, false, false);
	public static GridData centerGrid = new GridData(SWT.CENTER, SWT.CENTER, false, true);
	public static GridData SquareImage = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
	public static RowData defaultRow = new RowData();

	public IBEXButtonBuilder(Composite parent, Integer style) {
		this.parent = parent;
		this.buttonStyle = style;
	}

	public IBEXButtonBuilder setLabel(String label) {
		this.text = label;
		return this;
	}

	public IBEXButtonBuilder setTooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public IBEXButtonBuilder setCustomLayoutData(RowData layoutData) {
		this.rowData = layoutData;
		return this;
	}

	public IBEXButtonBuilder setCustomLayoutData(GridData layoutData) {
		this.gridData = layoutData;
		return this;
	}

	public IBEXButtonBuilder setLink(String link) {
		this.link = link;
		return this;
	}

	public IBEXButtonBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public IBEXButtonBuilder setButtonType(Integer buttonStyle) {
		this.buttonStyle = buttonStyle;
		return this;
	}

	public IBEXButtonBuilder setAction(Action action) {
		this.action = action;
		return this;
	}

	public IBEXButtonBuilder setListener(Listener listener) {
		this.listener = listener;
		return this;
	}

	public IBEXButtonBuilder setText(String text) {
		this.text = text;
		return this;
	}

	public IBEXButtonBuilder setImage(Image image) {
		this.image = image;
		return this;
	}

	public IBEXButtonBuilder setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public IBEXButtonBuilder setHeight(Integer height) {
		this.height = height;
		return this;
	}

	public IBEXButtonBuilder setHelpButton(Boolean isHelpButton) {
		this.isHelpButton = isHelpButton;
		return this;
	}

	public IBEXButtonBuilder setParent(Composite parent) {
		this.parent = parent;
		return this;
	}

	public IBEXButtonBuilder setOpenBrowser(String link) {
		this.link = link;
		return this;
	}

	public Button build() {
		Button button = new Button(parent, buttonStyle);

		// at start so if the user wants to override the default help button settings
		// they can do
		if (isHelpButton != null) {
			button.setToolTipText(String.format(TOOLTIP_TEXT, description, link));
			button.setLayoutData(IBEXButtonBuilder.SquareImage);
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

		if (text != null) {
			button.setText(text);
		} 
		
		

		if (tooltip != null) {
			button.setToolTipText(tooltip);
		}

		if (gridData != null) {
			if (width != null) {
				gridData.widthHint = width;
			}

			if (height != null) {
				gridData.heightHint = height;
			}

			button.setLayoutData(gridData);
		}

		if (rowData != null) {
			if (width != null) {
				rowData.width = width;
			}

			if (height != null) {
				rowData.height = height;
			}

			button.setLayoutData(rowData);
		}

		if (image != null) {
			button.setImage(image);
		}

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