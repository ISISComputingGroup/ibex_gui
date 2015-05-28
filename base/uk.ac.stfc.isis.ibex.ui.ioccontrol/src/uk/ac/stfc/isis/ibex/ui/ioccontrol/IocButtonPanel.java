package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.NegatingConverter;

public class IocButtonPanel extends Composite {

	private final Button start;
	private final Button stop;
	private final Button restart;
	
	private DataBindingContext bindingContext; 
	private static final UpdateValueStrategy WITH_NEGATION = new UpdateValueStrategy();
	static {
		WITH_NEGATION.setConverter(new NegatingConverter());
	};
	
	private EditableIocState ioc;
	private final IocControl control;
	
	public IocButtonPanel(Composite parent, int style, final IocControl control) {
		super(parent, style);
		this.control = control;
		GridLayout gridLayout = new GridLayout(3, true);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		start = new Button(this, SWT.NONE);
		start.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		start.setText("Start");
		start.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ioc != null) {
					control.startIoc().send(Arrays.asList(ioc.getName()));
				}
			}
		});
		
		stop = new Button(this, SWT.NONE);
		stop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		stop.setText("Stop");
		stop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ioc != null) {
					control.stopIoc().send(Arrays.asList(ioc.getName()));
				}
			}
		});
		
		restart = new Button(this, SWT.NONE);
		restart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		restart.setText("Restart");
		restart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ioc != null) {
					control.restartIoc().send(Arrays.asList(ioc.getName()));
				}
			}
		});
	}

	public void setIoc(final EditableIocState ioc) {
		this.ioc = ioc;
		if (ioc == null) {
			start.setEnabled(false);
			stop.setEnabled(false);
			restart.setEnabled(false);
			bindingContext = null;
			return;
		}
		
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		bindingContext = new DataBindingContext();
		
		bindStart(ioc);
		bindStop(ioc);		
		bindRestart(ioc);
	}


	private void bindStart(final EditableIocState ioc) {
		IConverter startConverter = new Converter(Boolean.class, Boolean.class) {
			@Override
			public Object convert(Object arg0) {				
				return control.startIoc().getCanSend() && !ioc.getIsRunning();
			}

		};
		
		UpdateValueStrategy startStrategy = new UpdateValueStrategy();
		startStrategy.setConverter(startConverter);
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(start), BeanProperties.value("canSend").observe(control.startIoc()), null, startStrategy);
		bindingContext.bindValue(WidgetProperties.enabled().observe(start), BeanProperties.value("isRunning").observe(ioc), null, startStrategy);
	}

	private void bindStop(final EditableIocState ioc) {
		IConverter stopConverter = new Converter(Boolean.class, Boolean.class) {
			@Override
			public Object convert(Object arg0) {				
				return control.stopIoc().getCanSend() && ioc.getIsRunning();
			}

		};
		
		UpdateValueStrategy stopStrategy = new UpdateValueStrategy();
		stopStrategy.setConverter(stopConverter);
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(stop), BeanProperties.value("canSend").observe(control.stopIoc()), null, stopStrategy);
		bindingContext.bindValue(WidgetProperties.enabled().observe(stop), BeanProperties.value("isRunning").observe(ioc), null, stopStrategy);
	}
	
	private void bindRestart(final EditableIocState ioc) {
		IConverter restartConverter = new Converter(Boolean.class, Boolean.class) {
			@Override
			public Object convert(Object arg0) {				
				return control.restartIoc().getCanSend() && ioc.getIsRunning();
			}

		};
		
		UpdateValueStrategy restartStrategy = new UpdateValueStrategy();
		restartStrategy.setConverter(restartConverter);
		
		bindingContext.bindValue(WidgetProperties.enabled().observe(restart), BeanProperties.value("canSend").observe(control.restartIoc()), null, restartStrategy);
		bindingContext.bindValue(WidgetProperties.enabled().observe(restart), BeanProperties.value("isRunning").observe(ioc), null, restartStrategy);
	}
}
