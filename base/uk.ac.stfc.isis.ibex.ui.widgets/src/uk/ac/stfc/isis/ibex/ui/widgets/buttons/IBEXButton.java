package uk.ac.stfc.isis.ibex.ui.widgets.buttons;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.model.Action;

/**
 * Builder to create perspective buttons.
 */
public class IBEXButton {

	private Button button;

	/**
	 * Path to this.
	 */
	protected static final String SYMBOLIC_PATH = "uk.ac.stfc.isis.ibex.ui.widgets";

	/**
	 * An expanding grid.
	 */
	public static GridData expandingGrid = new GridData(SWT.FILL, SWT.FILL, true, true);

	/**
	 * default grid.
	 */
	public static GridData defaultGrid = new GridData();

	/**
	 * A compact grid.
	 */
	public static GridData compactGrid = new GridData(SWT.FILL, SWT.FILL, false, false);

	/**
	 * A centre aligned grid.
	 */
	public static GridData centerGrid = new GridData(SWT.CENTER, SWT.CENTER, false, true);

	/**
	 * default row data.
	 */
	public static RowData defaultRow = new RowData();

	/**
	 * Constructor with minimum parameters required for button creation.
	 *
	 * @param parent the composite to add the button to
	 * @param style  the SWT style ENUM to assign the button
	 */
	public IBEXButton(Composite parent, int style) {
		this.button = new Button(parent, style);
	}

	/**
	 * Creates a button and adds a selection listener to it.
	 * 
	 * @param parent
	 * @param style
	 * @param selectionListener
	 */
	public IBEXButton(Composite parent, int style, Listener selectionListener) {
		this(parent, style);

		this.listener(SWT.Selection, selectionListener);
	}

	/**
	 * Creates a button and binds a {@link uk.ac.stfc.isis.ibex.model.Action} to it.
	 * 
	 * @param parent
	 * @param style
	 * @param action
	 */
	public IBEXButton(Composite parent, int style, Action action) {
		this(parent, style);

		this.action(action);
	}

	/**
	 * Set the tooltip for the button.
	 * 
	 * @param tooltip
	 * @return IBEXButton
	 */
	public IBEXButton tooltip(String tooltip) {
		this.button.setToolTipText(tooltip);
		return this;
	}

	/**
	 * Sets a custom layout data on the button.
	 * 
	 * @param layoutData
	 * @return IBEXButton
	 */
	public IBEXButton layoutData(Object layoutData) {
		this.button.setLayoutData(layoutData);
		return this;
	}

	/**
	 * Sets up a selection event listener to open the link in the browser.
	 * 
	 * @param url the URL link to open
	 * @return IBEXButton
	 */
	public IBEXButton link(String url) {
		this.button.addListener(SWT.Selection, e -> {
			try {
				PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(url));
			} catch (PartInitException | MalformedURLException ex) {
				LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()),
						"Failed to open URL in browser: " + url, ex);
			}
		});

		return this;
	}

	/**
	 * Binds a uk.ac.stfc.isis.ibex.model.Action to the button.
	 * 
	 * Also takes care of enabling/disabling the button based on actions'canExecute property.
	 * 
	 * @param action
	 * @return IBEXButton
	 */
	public IBEXButton action(Action action) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(this.button),
				BeanProperties.value("canExecute").observe(action));

		this.listener(SWT.Selection, event -> {
			action.execute();
		});

		return this;
	}

	/**
	 * Adds a listener for the specific evetType to the button.
	 * 
	 * @param eventType
	 * @param listener
	 * @return IBEXButton
	 */
	public IBEXButton listener(int eventType, Listener listener) {
		this.button.addListener(eventType, listener);
		return this;
	}

	/**
	 * Sets the text for the button.
	 * 
	 * @param text
	 * @return IBEXButton
	 */
	public IBEXButton text(String text) {
		this.button.setText(text);
		return this;
	}

	/**
	 * Sets the image for the button.
	 * 
	 * @param image
	 * @return IBEXButton
	 */
	public IBEXButton image(Image image) {
		this.button.setImage(image);
		return this;
	}

	/**
	 * Sets the selected property of the button.
	 * 
	 * @param selected
	 * @return IBEXButton
	 */
	public IBEXButton selected(boolean selected) {
		this.button.setSelection(selected);
		return this;
	}
	
	/**
	 * Sets the font property of the text within the button.
	 * 
	 * @param font
	 * @return IBEXButton
	 */
	public IBEXButton font(Font font) {
		this.button.setFont(font);
		return this;
	}
	
	/**
	 * Sets the visible property of the button.
	 * 
	 * @param visible
	 * @return IBEXButton
	 */
	public IBEXButton visible(boolean visible) {
		this.button.setVisible(visible);
		return this;
	}
	
	/**
	 * Return the enabled property of the button.
	 * 
	 * @return Button
	 */
	public IBEXButton enabled(boolean enabled) {
		this.button.setEnabled(enabled);
		return this;
	}

	/**
	 * Return the SWT button.
	 * 
	 * @return Button
	 */
	public Button get() {
		return this.button;
	}

}