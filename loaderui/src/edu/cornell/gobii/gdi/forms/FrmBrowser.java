package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

public class FrmBrowser extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmBrowser(Composite parent, int style, String url) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Browser browser = new Browser(this, SWT.NONE);
		browser.setUrl(url);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
