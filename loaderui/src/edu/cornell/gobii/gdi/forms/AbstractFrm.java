package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.utils.FormUtils;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

public abstract class AbstractFrm extends Composite {
	protected Shell shell;
	protected Table tbList;
	protected Combo cbList;
	protected Composite cmpForm;
	protected StyledText memInfo;
	private Button btnExport;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractFrm(Shell shell, Composite parent, int style) {
		super(parent, style);
		this.shell = shell;
		setLayout(new GridLayout(3, false));
		createList();
		createContent();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	protected void createList(){
		Group group = new Group(this, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_group.heightHint = 438;
		gd_group.widthHint = 184;
		group.setLayoutData(gd_group);
		
		cbList = new Combo(group, SWT.NONE);
		cbList.setToolTipText("Select Project");
		cbList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tbList = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		tbList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbList.setHeaderVisible(true);
		tbList.setLinesVisible(true);
		tbList.addListener(SWT.MouseHover, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = tbList.getItem(pt);
				if (item == null)
	                return;
				tbList.setToolTipText(item.getText());
			}
		});
		
		btnExport = new Button(group, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FormUtils.exportTableAsTxt(shell, tbList);
			}
		});
		btnExport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnExport.setText("Export");
		
		cmpForm = new Composite(this, SWT.NONE);
		cmpForm.setLayout(new GridLayout(1, false));
		GridData gd_cmpForm = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_cmpForm.widthHint = 285;
		cmpForm.setLayoutData(gd_cmpForm);
		
		memInfo = new StyledText(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.WRAP);
		memInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
	}
	
	protected abstract void createContent();
	
}
