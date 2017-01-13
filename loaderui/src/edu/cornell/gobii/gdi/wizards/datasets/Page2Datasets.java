package edu.cornell.gobii.gdi.wizards.datasets;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;

public class Page2Datasets extends WizardPage {
	private Table tbCodes;

	/**
	 * Create the wizard.
	 */
	public Page2Datasets() {
		super("wizardPage");
		setTitle("Wizard :: Dataset Information");
		setDescription("");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Button chkEncode = new Button(container, SWT.CHECK);
		chkEncode.setText("Encode Genotype Matrix (UIPAC standard)");
		
		tbCodes = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		tbCodes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbCodes.setHeaderVisible(true);
		tbCodes.setLinesVisible(true);
		
		TableColumn tblclmnCalls = new TableColumn(tbCodes, SWT.NONE);
		tblclmnCalls.setWidth(222);
		tblclmnCalls.setText("Calls");
		
		TableColumn tblclmnCodes = new TableColumn(tbCodes, SWT.NONE);
		tblclmnCodes.setWidth(169);
		tblclmnCodes.setText("Codes");
	}

}
