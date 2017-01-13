package edu.cornell.gobii.gdi.wizards.datasets;

import org.eclipse.jface.wizard.Wizard;

public class WzDatasets extends Wizard {

	public WzDatasets() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		addPage(new PageDataLoader());
	}

	@Override
	public boolean performFinish() {
		return false;
	}

}
