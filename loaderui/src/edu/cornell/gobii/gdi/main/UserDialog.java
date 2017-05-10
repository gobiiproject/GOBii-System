package edu.cornell.gobii.gdi.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;

import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class UserDialog extends Dialog {
	private static Logger log = Logger.getLogger(UserDialog.class.getName());
	protected int result;
	protected Shell shell;
	private Combo cbCrop;
	private Button btnCheckSSH;
	private Text txtServer;
	private Text txtService;
	private Text text;
	private Text textUsername;
	private Text textPassword;
	private Text textEmail;
	private Button btnGetCrops;
	private Button btnConnect;
	private Button btnTest;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UserDialog(Shell parent) {
		super(parent);
		setText("New User Information");
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
		shell = new Shell(getParent(), SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setSize(319, 326);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		Rectangle screenSize = Display.getCurrent().getPrimaryMonitor().getBounds();
		shell.setLocation((screenSize.width - shell.getBounds().width) / 2, (screenSize.height - shell.getBounds().height) / 2);
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite_2 = new Composite(scrolledComposite, SWT.NONE);
		composite_2.setLayout(new GridLayout(3, false));

		Label lblWebService = new Label(composite_2, SWT.RIGHT);
		lblWebService.setSize(66, 15);
		lblWebService.setText("Web Service:");

		Composite composite_1 = new Composite(composite_2, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite_1.setSize(353, 34);
		composite_1.setLayout(new GridLayout(2, false));

		txtService = new Text(composite_1, SWT.BORDER);
		txtService.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				App.INSTANCE.setService(txtService.getText());
			}
		});
		txtService.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnGetCrops = new Button(composite_1, SWT.NONE);
		btnGetCrops.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Controller.userEmail = "";

				if(App.INSTANCE.getService() == null || App.INSTANCE.getService().isEmpty()) return;
				if(!App.INSTANCE.getService().endsWith("/")){
					String service = App.INSTANCE.getService() +"/";
					txtService.setText(service);
					App.INSTANCE.setService(service);
				}
				//				App.INSTANCE.setCrop(null);
				if(Controller.getCrops(log, true, btnCheckSSH.getSelection(),true)){
					enableUserLogin(true);
					FormUtils.cropSetToCombo(cbCrop);	
					MessageDialog.openInformation(getParent(), "Connection successful", "Request processed successfully!\n\nPlease select a crop.");
				}else{
					enableUserLogin(false);
					Utils.log(shell, null, log, "Error connecting to service.", null);

				}
			}
		});
		btnGetCrops.setText("Get Crops");
		new Label(composite_2, SWT.NONE);

		btnCheckSSH = new Button(composite_2, SWT.CHECK);
		btnCheckSSH.setSize(42, 16);
		btnCheckSSH.setSelection(true);
		btnCheckSSH.setText("SSH");
		new Label(composite_2, SWT.NONE);

		Label label = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

		Label lblCrop = new Label(composite_2, SWT.RIGHT);
		lblCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblCrop.setSize(29, 15);
		lblCrop.setText("Crop:");

		cbCrop = new Combo(composite_2, SWT.READ_ONLY);
		cbCrop.setEnabled(false);
		cbCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cbCrop.setSize(353, 23);
		cbCrop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String cropName = cbCrop.getItem(cbCrop.getSelectionIndex());
				//String crop = cbCrop.getData(cropName);
				App.INSTANCE.setCrop(cropName);
				try {
					ClientContext.getInstance(null, false).setCurrentClientCrop(cropName);

				} catch (Exception err) {
					Utils.log(shell, null, log, "Error selecting crop", err);
				}
			}
		});
		FormUtils.cropSetToCombo(cbCrop);
		new Label(composite_2, SWT.NONE);


		Label lblUser = new Label(composite_2, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText("Username:");

		textUsername = new Text(composite_2, SWT.BORDER);
		textUsername.setEnabled(false);
		textUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_2, SWT.NONE);

		Label lblPassword = new Label(composite_2, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblNewLabel.horizontalIndent = 1;
		lblPassword.setLayoutData(gd_lblNewLabel);
		lblPassword.setText("Password:");

		textPassword = new Text(composite_2,  SWT.BORDER| SWT.PASSWORD);
		textPassword.setEnabled(false);
		textPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnConnect = new Button(composite_2, SWT.NONE);
		btnConnect.setEnabled(false);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(textUsername.getText().isEmpty() || textPassword.getText().isEmpty()){
					Utils.log(shell, null, log, "Please enter a username and password", null);
					return;
				}

				Controller.userEmail = "";
				textEmail.setText("");

				if(Controller.authenticate(textUsername.getText(),textPassword.getText())){
					FormUtils.cropSetToCombo(cbCrop);	
					MessageDialog.openInformation(getParent(), "Connection successful", "Request processed successfully!");
					PayloadEnvelope<ContactDTO> resultEnvelope;
					try {
						resultEnvelope = Controller.getContactByUsername(textUsername.getText());

						if(Controller.getDTOResponse(Display.getCurrent().getActiveShell(), resultEnvelope.getHeader(), null, false)){
							ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);

							Controller.userEmail = contactDTO.getEmail();
							textEmail.setText(contactDTO.getEmail());

							App.INSTANCE.getUser().setUserEmail(Controller.userEmail);
							App.INSTANCE.getUser().setUserName(contactDTO.getUserName());
							App.INSTANCE.getUser().setUserId(contactDTO.getContactId());
							App.INSTANCE.getUser().setUserFullname(contactDTO.getLastName() +", "+ contactDTO.getFirstName());

							enableFileServer(true);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Utils.log(shell, null, log, "Invalid user credentials.", e1);
					}

				}else{
					Utils.log(shell, null, log, "Invalid username or password", null);
				}
			}
		});
		btnConnect.setText("Connect");

		Label lblEmail = new Label(composite_2, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("Email:");

		textEmail = new Text(composite_2, SWT.BORDER);
		textEmail.setEnabled(false);
		textEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textEmail.setText(Controller.userEmail);
		new Label(composite_2, SWT.NONE);

		Label label_1 = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

		Label lblServer = new Label(composite_2, SWT.RIGHT);
		lblServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblServer.setSize(56, 15);
		lblServer.setText("File Server:");

		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		GridData gd_composite_3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		gd_composite_3.heightHint = 37;
		composite_3.setLayoutData(gd_composite_3);

		txtServer = new Text(composite_3, SWT.BORDER);
		txtServer.setEnabled(false);
		txtServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtServer.setSize(353, 21);

		btnTest = new Button(composite_3, SWT.NONE);
		btnTest.setEnabled(false);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InetAddress host;
				try {
					host = InetAddress.getByName(txtServer.getText());
					if(host.isReachable(1000)){
						MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Connection successful");
						dialog.setMessage("Connection established successfully!");
						dialog.open();
					}
				} catch (IOException err) {
					Utils.log(shell, null, log, "Error connecting to file server", null);
				}
			}
		});
		btnTest.setText("Test");
		txtServer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				App.INSTANCE.setServer(txtServer.getText());
			}
		});
		new Label(composite_2, SWT.NONE);

		Composite composite = new Composite(composite_2, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite.setSize(353, 49);
		composite.setLayout(new GridLayout(2, false));

		Button btnOK = new Button(composite, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!App.INSTANCE.isValid()){
					Utils.log(shell, null, log, "All fields are required!", new Throwable("Invalid user information"));

				}else if(textEmail.getText().isEmpty()){
					Utils.log(shell, null, log, "It seems that you are not logged in.\n\nPlease enter a username and password then connect to the web service", new Throwable("Not Logged in"));
				}else{
					App.INSTANCE.save();
					result = Window.OK;
					shell.close();
				}
			}
		});
		btnOK.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnOK.setText("OK");

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = Window.CANCEL;
				shell.close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnCancel.setText("Cancel");
		shell.setDefaultButton(btnOK);
		scrolledComposite.setContent(composite_2);
		scrolledComposite.setMinSize(composite_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		createContent();

	}

	protected void enableFileServer(boolean b) {
		// TODO Auto-generated method stub
		txtServer.setEnabled(b);
		btnTest.setEnabled(b);
		
		if(!b) txtServer.setText("");
	}

	protected void enableUserLogin(boolean b) {
		// TODO Auto-generated method stub
		cbCrop.setEnabled(b);
		textUsername.setEnabled(b);
		textPassword.setEnabled(b);
		btnConnect.setEnabled(b);

		if(!b){
			cbCrop.setText("");
			textUsername.setText("");
			textPassword.setText("");
			textEmail.setText("");
			enableFileServer(false);
		}
	}

	protected void createContent(){
		if(App.INSTANCE.getService() != null) txtService.setText(App.INSTANCE.getService());
		if(App.INSTANCE.getServer() != null) txtServer.setText(App.INSTANCE.getServer());
		if(App.INSTANCE.getCrop() != null) cbCrop.select(cbCrop.indexOf(App.INSTANCE.getCrop()));

	}

	protected void populateContactDetails(int contactId) {

		try {

			RestUri restUriContact = App
					.INSTANCE.getUriFactory()
					.resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
			restUriContact.setParamValue("id", Integer.toString(contactId));
			GobiiEnvelopeRestResource<ContactDTO> restResource = new GobiiEnvelopeRestResource<>(restUriContact);
			PayloadEnvelope<ContactDTO> resultEnvelope = restResource
					.get(ContactDTO.class);

			if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), null, false)){
				ContactDTO contactDTO =  resultEnvelope.getPayload().getData().get(0);
				String email = contactDTO.getEmail();
				//				String username = (String)email.subSequence(0, email.indexOf("@"));

				textEmail.setText(email);
				http://localhost:8282/gobii-dev

					App.INSTANCE.getUser().setUserEmail(email);
				App.INSTANCE.getUser().setUserName(contactDTO.getUserName());
				App.INSTANCE.getUser().setUserId(contactDTO.getContactId());
				App.INSTANCE.getUser().setUserFullname(contactDTO.getLastName() +", "+ contactDTO.getFirstName());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
