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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class AbstractFrm extends Composite {
	protected Shell shell;
	protected Table tbList;
	protected Combo cbList;
	protected Composite cmpForm;
	protected StyledText memInfo;
	private Button btnExport;
	protected Button btnRefresh;
	protected String selectedName;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private SashForm sashForm;
	protected Label lblCbList;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AbstractFrm(Shell shell, Composite parent, int style) {
		super(parent, style);
		this.shell = shell;
		setLayout(new GridLayout(1, false));
		createList();
		createContent();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	protected void createList(){
		
		sashForm = new SashForm(this, SWT.SMOOTH);
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(sashForm);
		formToolkit.paintBordersFor(sashForm);
		Group group = new Group(sashForm, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		
		lblCbList = new Label(group, SWT.NONE);
		
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
				FormUtils.exportTableAsTxt(shell, tbList, "exportList");
			}
		});
		btnExport.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnExport.setText("Export");
		
		btnRefresh = new Button(group, SWT.NONE);
		
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRefresh.setText("Refresh");
		
		cmpForm = new Composite(sashForm, SWT.BORDER);
		cmpForm.setLayout(new GridLayout(1, false));
		
		memInfo = new StyledText(sashForm, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.WRAP);
		sashForm.setWeights(new int[] {143, 206, 118});
		
		
	}
	
	protected abstract void createContent();
}
