package edu.cornell.gobii.gdi.forms;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Combo;

public class DlgWizTemplate extends Dialog {

	protected int result;
	protected String templName;
	protected Shell shell;
	private Button btnOk;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DlgWizTemplate(Shell parent) {
		super(parent);
		setText("Template Name Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public int open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setSize(450, 125);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblTemplateName = new Label(shell, SWT.NONE);
		lblTemplateName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTemplateName.setText("Template Name:");
		
		Combo cbName = new Combo(shell, SWT.NONE);
		cbName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		WizardUtils.getTemplateFiles(null, cbName);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite.heightHint = 35;
		composite.setLayoutData(gd_composite);
		
		btnOk = new Button(composite, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				templName = cbName.getText();
				result = Window.OK;
				shell.close();
			}
		});
		btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnOk.setText("OK");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = Window.CANCEL;
				shell.close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCancel.setText("Cancel");

	}

	public String getTemplName() {
		return templName;
	}

	public void setTemplName(String templName) {
		this.templName = templName;
	}

}
