package uk.ac.stfc.isis.ibex.ui.nicos;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ScriptStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.widgets.NumberedStyledText;

/**
 * The view of the current script.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosCurrentScriptContainer {

    private StyledText txtCurrentScript;
    private final NicosModel model;
    private final DataBindingContext bindingContext;
	private ScriptStatusViewModel scriptStatusViewModel;

	/**
	 * Constructor.
	 */
	public NicosCurrentScriptContainer() {
		model = Nicos.getDefault().getModel();
		bindingContext = new DataBindingContext();
		scriptStatusViewModel = new ScriptStatusViewModel(model);
	}
	
    private static final Color NEUTRAL = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	
    private PropertyChangeListener highlightListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    int currentLine = model.getLineNumber();
                    txtCurrentScript.setLineBackground(0, txtCurrentScript.getLineCount(), NEUTRAL);

                    if (currentLine > 0 && currentLine <= txtCurrentScript.getLineCount()) {
                        txtCurrentScript.setLineBackground(model.getLineNumber() - 1, 1,
                                scriptStatusViewModel.getHighlightColour());
                    	txtCurrentScript.setSelection(txtCurrentScript.getOffsetAtLine(currentLine - 1));
                    }
                }
            });
        }
    };
	
    /**
     * Create the view.
     * @param parent injected by eclipse
     */
	@PostConstruct
    public void createCurrentScriptContainer(Composite parent) {
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        parent.setLayout(new GridLayout(1, false));
           
        Composite currentScriptExecutingContainer = new Composite(parent, SWT.NONE);
        currentScriptExecutingContainer.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        currentScriptExecutingContainer.setLayout(new GridLayout(3, false));

        Label lblCombinedScriptInfo = new Label(currentScriptExecutingContainer, SWT.NONE);
        lblCombinedScriptInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lblCombinedScriptInfo), 
        		BeanProperties.value("combinedScriptInfo").observe(scriptStatusViewModel));
        
        txtCurrentScript = new NumberedStyledText(parent, SWT.V_SCROLL | SWT.BORDER);
        txtCurrentScript.setEditable(false);
        txtCurrentScript.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        bindingContext.bindValue(WidgetProperties.text().observe(txtCurrentScript),
                BeanProperties.value("currentlyExecutingScript").observe(model));
 
        model.addPropertyChangeListener("lineNumber", highlightListener);
        model.addPropertyChangeListener("currentlyExecutingScript", highlightListener);
        scriptStatusViewModel.addPropertyChangeListener("highlightColour", highlightListener);

        NicosControlButtonPanel controlPanel =
                new NicosControlButtonPanel(parent, SWT.NONE, scriptStatusViewModel);
        controlPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
}
