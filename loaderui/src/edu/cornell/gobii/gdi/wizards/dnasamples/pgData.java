package edu.cornell.gobii.gdi.wizards.dnasamples;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.custom.SashForm;

public class pgData extends WizardPage {
	private Text text;

	/**
	 * Create the wizard.
	 */
	public pgData() {
		super("wizardPage");
		setMessage("Load file containing DNA sample and organism information");
		setTitle("Data");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new ColumnLayout());
		
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new RowLayout(SWT.HORIZONTAL));
		ColumnLayoutData cld_group = new ColumnLayoutData();
		cld_group.heightHint = 30;
		group.setLayoutData(cld_group);
		
		Button btnBrowse = new Button(group, SWT.NONE);
		btnBrowse.setText("Browse");
		
		text = new Text(group, SWT.BORDER);
		text.setLayoutData(new RowData(478, SWT.DEFAULT));
		
		Composite composite = new Composite(container, SWT.NONE);
		ColumnLayoutData cld_composite = new ColumnLayoutData();
		cld_composite.heightHint = 301;
		composite.setLayoutData(cld_composite);
	}

}
