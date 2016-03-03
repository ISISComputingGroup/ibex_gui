package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetDescriptionWidget extends Composite {

	private Text txtDescription;

	public TargetDescriptionWidget(Composite parent, final SynopticViewModel instrument) {
		super(parent, SWT.NONE);

		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				ComponentDescription component = instrument.getFirstSelectedComponent();

				if (component != null) {
                    // For back-compatibility reasons the name actually might be
                    // the path
					if (component.target() != null && component.target().name() != null) {
                        String name = Opi.getDefault().descriptionsProvider().guessOpiName(component.target().name());
                        OpiDescription opi = Opi.getDefault().descriptionsProvider().getDescription(name);
						if (opi != null) {
							txtDescription.setText(generateDescription(opi));
							return;
						}
					}
				}
				txtDescription.setText("");
			}
		});

		createControls(this);
	}

	private void createControls(Composite parent) {
		setLayout(new FillLayout());

        txtDescription = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
	}

	private String generateDescription(OpiDescription opi) {
		StringBuilder sb = new StringBuilder();
		sb.append(opi.getDescription());

		if (opi.getMacros().size() > 0) {

			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
			sb.append("Macros:");
			sb.append(System.lineSeparator());
			
			for (Iterator<MacroInfo> iter = opi.getMacros().iterator(); iter.hasNext();) {
				MacroInfo element = iter.next();
				sb.append("* " + element.getName());
				sb.append(" - ");
				sb.append(element.getDescription());
				sb.append(System.lineSeparator());
			}
		}

		return sb.toString();
	}

}
