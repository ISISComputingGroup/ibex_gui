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

import uk.ac.stfc.isis.ibex.motor.Motors;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTable;

/** A view that shows a collection of motors. */
public class TableOfMotorsView {

	/** Pixel height of the minimalMotorView in table of motors. */
	private static final int MOTOR_HEIGHT = MotorsOverview.HEIGHT_DIMENSION + 2 * MotorsOverview.MARGIN;
	/** Pixel width of the minimalMotorView in table of motors. */
	private static final int MOTOR_WIDTH = MotorsOverview.WIDTH_DIMENSION + 2 * MotorsOverview.MARGIN;
	/** Pixel margin to add on the right and bottom of the table. */
	private static final int TABLE_MARGIN = MotorsOverview.MARGIN;

	/** List of the tab titles as defined in plugin.xml for this class. */
	private static final List<String> TAB_TITLES = Arrays.asList("Main Motors (Controllers 1 - 8)",
												   "Additional Motors (Controllers 9 - 16)",
												   "Additional Motors (Controllers 17 - 24)");

	/** The ID of the table of motors shown in this view */
	private int motorTableID;

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

		// TODO: we need to calculate the actual height according to how many rows each controller will take up
		scrolledComposite.setMinWidth(8 * MOTOR_WIDTH + TABLE_MARGIN);
		scrolledComposite.setExpandVertical(true);
		
        motorsOverview =
                new MotorsOverview(scrolledComposite, SWT.NONE);

		GridData gdOverview = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		motorsOverview.setLayoutData(gdOverview);
        
        scrolledComposite.setContent(motorsOverview);
        motorsOverview.setMotors(motorsTable);
        
	}

	/**
	 * Sets the MotorsTable for this view, overridden for other motor tabs.
	 */
	@PersistState
	protected void setMotorsTable() {
		this.motorsTable = Motors.getInstance().getMotorsTablesList().get(motorTableID);
	}
}
