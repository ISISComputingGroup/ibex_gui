package uk.ac.stfc.isis.ibex.ui.blocks.groups;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayGroup;
import uk.ac.stfc.isis.ibex.ui.blocks.groups.BlocksTable;

public class Group extends Composite {

	public static final int BLOCK_WIDTH = 200;

	private static final Color WHITE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	private static final Color BACKGROUND = SWTResourceManager.getColor(244, 238, 225);
	private static final Font NAME_FONT = SWTResourceManager.getFont("Arial", 12, SWT.BOLD);
	private static final Font TABLE_FONT = SWTResourceManager.getFont("Arial", 10, SWT.NONE);
	
	private final Composite titleBar;
	private final BlocksTable blocks;
	
	public Group(Composite parent, int style, DisplayGroup group) {
		super(parent, style | SWT.BORDER);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		titleBar = new Composite(this, SWT.NONE);
		GridLayout gl_titleBar = new GridLayout(1, false);
		gl_titleBar.verticalSpacing = 0;
		gl_titleBar.marginWidth = 0;
		gl_titleBar.marginHeight = 0;
		gl_titleBar.horizontalSpacing = 0;
		titleBar.setLayout(gl_titleBar);
		GridData gd_titleBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_titleBar.heightHint = 25;
		gd_titleBar.minimumHeight = 30;
		titleBar.setLayoutData(gd_titleBar);
		titleBar.setBounds(0, 0, 64, 64);
		titleBar.setBackground(BACKGROUND);
		
		Label name = new Label(titleBar, SWT.NONE);
		GridData gd_name = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_name.horizontalIndent = 5;
		name.setLayoutData(gd_name);
		name.setText(group.name());
		name.setFont(NAME_FONT);
		name.setBackground(BACKGROUND);
		
		blocks = new BlocksTable(this, SWT.NONE, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.widthHint = BLOCK_WIDTH;
		gd.minimumWidth = BLOCK_WIDTH;		
		blocks.setLayoutData(gd);
		
		blocks.setRows(group.blocks());
		blocks.setBackground(WHITE);
		blocks.setFont(TABLE_FONT);
		
		blocks.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				blocks.deselectAll();
			}
		});
	}
	
	public void showHiddenBlocks(boolean showHidden) {
		blocks.showHiddenBlocks(showHidden);
	}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		titleBar.setMenu(menu);
		blocks.setMenu(menu);
	}
}
