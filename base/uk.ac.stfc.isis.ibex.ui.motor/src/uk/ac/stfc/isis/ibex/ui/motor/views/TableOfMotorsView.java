package uk.ac.stfc.isis.ibex.ui.motor.views;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import uk.ac.stfc.isis.ibex.motor.Motors;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

/** A view that shows a collection of motors. */
public class TableOfMotorsView {

	/** Pixel height of the minimalMotorView in table of motors. */
	private static final int MOTOR_HEIGHT = 79;
	/** Pixel width of the minimalMotorView in table of motors. */
	private static final int MOTOR_WIDTH = 90;
	/** Pixel margin to add on the right and bottom of the table. */
	private static final int TABLE_MARGIN = 20;

	/** List of the tab titles as defined in plugin.xml for this class. */
	private static final List<String> TAB_TITLES = Arrays.asList("Main Motors (Controllers 1 - 8)",
												   "Additional Motors (Controllers 9 - 16)",
												   "Additional Motors (Controllers 17 - 24)");

	/** The ID of the table of motors shown in this view */
	private int motorTableID;

    /**
     * List the offset for the index of the first controller in the table. Used
     * to set labels down left hand side.
     */
    private static final List<Integer> TAB_CONTROLLER_OFFSETS = Arrays.asList(0, 8, 16);



    /** The MotorsTable used for this particular table of motors view. */
    protected MotorsTable motorsTable;

    /** The MotorsOverview used by this view. **/
    private MotorsOverview motorsOverview;

	/**
	 * Constructor.
	 *
	 * @param part The part of the E4 application model containing this view.
	 */
    @Inject
	public TableOfMotorsView(MPart part) {
    	motorTableID = TAB_TITLES.indexOf(part.getLabel());
	}

    /**
     * Initialises the view.
     *
     * @param parent The parent composite
     */
	@PostConstruct
	public void createPartControl(Composite parent) {
        setMotorsTable();

		GridLayout glParent = new GridLayout(2, false);
		glParent.verticalSpacing = 0;
		glParent.marginWidth = 0;
		glParent.marginHeight = 0;
		glParent.horizontalSpacing = 0;
		parent.setLayout(glParent);

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1));

		int numCrates = motorsTable.getNumCrates();
		scrolledComposite.setMinHeight(numCrates * MOTOR_HEIGHT + TABLE_MARGIN);
		scrolledComposite.setExpandHorizontal(true);

		int numMotors = motorsTable.getNumMotors();
		scrolledComposite.setMinWidth(numMotors * MOTOR_WIDTH + TABLE_MARGIN);
		scrolledComposite.setExpandVertical(true);

        motorsOverview =
                new MotorsOverview(scrolledComposite, SWT.NONE);

		GridData gdOverview = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		motorsOverview.setLayoutData(gdOverview);

		scrolledComposite.setContent(motorsOverview);

		//motorsOverview.addMouseListener(motorSelection);

		Label spacer = new Label(parent, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        motorsOverview.setMotors(motorsTable, getTableControllerOffset());
	}

	/**
	 * Sets the MotorsTable for this view, overridden for other motor tabs.
	 */
	@PersistState
	protected void setMotorsTable() {
		this.motorsTable = Motors.getInstance().getMotorsTablesList().get(motorTableID);
	}

    /**
     * @return The controller number offset for this particular tab
     */
    protected int getTableControllerOffset() {
        return TAB_CONTROLLER_OFFSETS.get(motorTableID);
    }
}
