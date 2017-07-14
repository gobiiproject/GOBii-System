package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.services.Controller;
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

public class FrmAbout extends Composite {
	protected Shell shell;
	protected String selectedName;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Label lblUi;
	private Label lblServerVersion;
	private Label lblUiVersion;
	private Label lblServerversion;
	
	private String loaderUiVersion;
	private Group group;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @param loaderUiVersion 
	 */
	public FrmAbout(Shell shell, Composite parent, int style, String loaderUiVersion) {

		super(parent, style);
		this.shell = shell;
		this.loaderUiVersion = loaderUiVersion;
		
		setLayout(new GridLayout(1, false));
		
		group = new Group(this, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_group.heightHint = 54;
		group.setLayoutData(gd_group);
		formToolkit.adapt(group);
		formToolkit.paintBordersFor(group);
		
		lblUi = new Label(group, SWT.NONE);
		formToolkit.adapt(lblUi, true, true);
		lblUi.setText("UI Version:");
		
		lblUiVersion = new Label(group, SWT.NONE);
		formToolkit.adapt(lblUiVersion, true, true);
		
		lblServerVersion = new Label(group, SWT.NONE);
		formToolkit.adapt(lblServerVersion, true, true);
		lblServerVersion.setText("Server Version:");
		
		lblServerversion = new Label(group, SWT.NONE);
		formToolkit.adapt(lblServerversion, true, true);
		
		Browser browser = new Browser(this, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		browser.setUrl("http://cbsugobii05.tc.cornell.edu:6084/display/GDocs/About+GOBII");
		createContent();
	}
	
	protected void createContent(){
		lblUiVersion.setText(loaderUiVersion);
		lblServerversion.setText(Controller.getGobiiVersion());
	}
}