package edu.cornell.gobii.gdi.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class UserDialog extends Dialog {
	private static Logger log = Logger.getLogger(UserDialog.class.getName());
	protected int result;
	protected Shell shlUserLogin;
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
	private Button btnOK;

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
		shlUserLogin.open();
		shlUserLogin.layout();
		Display display = getParent().getDisplay();
		while (!shlUserLogin.isDisposed()) {
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
		shlUserLogin = new Shell(getParent(), SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shlUserLogin.setSize(562, 393);
		shlUserLogin.setText("User Login");
		shlUserLogin.setLayout(new GridLayout(1, false));
		Rectangle screenSize = Display.getCurrent().getPrimaryMonitor().getBounds();
		shlUserLogin.setLocation((screenSize.width - shlUserLogin.getBounds().width) / 2, (screenSize.height - shlUserLogin.getBounds().height) / 2);
		ScrolledComposite scrolledComposite = new ScrolledComposite(shlUserLogin, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
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
				if(txtService.getText().contains("localhost:")) btnCheckSSH.setSelection(false);
				btnCheckSSH.setEnabled(true);
				enableUserLogin(false);

			}
		});
		txtService.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event)
			{
				if(event.detail == SWT.TRAVERSE_RETURN)
				{
					getCrops();
				}
			}
		});
		txtService.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnGetCrops = new Button(composite_1, SWT.NONE);
		btnGetCrops.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getCrops();
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
				String cropName = cbCrop.getText();
				switchToCrop(cropName);
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
		textPassword.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event)
			{
				if(event.detail == SWT.TRAVERSE_RETURN)
				{
					if(textUsername.getText().isEmpty() || textPassword.getText().isEmpty()){

						Utils.log(shlUserLogin, null, log, "Please enter a username and password", null);
						return;
					}
					connect(false); 
				}
			}
		});

		btnConnect = new Button(composite_2, SWT.NONE);
		btnConnect.setEnabled(false);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(textUsername.getText().isEmpty() || textPassword.getText().isEmpty()){

					Utils.log(shlUserLogin, null, log, "Please enter a username and password", null);
					return;
				}
				connect(true);
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
		App.INSTANCE.getUser().setUserEmail(Controller.userEmail);

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
		txtServer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if(txtServer.getEnabled() || !txtServer.getText().isEmpty()) btnTest.setEnabled(true);
				App.INSTANCE.setServer(txtServer.getText());
			}

		});

		btnTest = new Button(composite_3, SWT.NONE);
		btnTest.setEnabled(false);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InetAddress host;
				try {
					host = InetAddress.getByName(txtServer.getText());
					if(host.isReachable(1000)){
						MessageBox dialog = new MessageBox(shlUserLogin, SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Connection successful");
						dialog.setMessage("Connection established successfully!");
						dialog.open();
					}
				} catch (IOException err) {
					Utils.log(shlUserLogin, null, log, "Error connecting to file server", null);
				}
			}
		});
		btnTest.setText("Test");
		new Label(composite_2, SWT.NONE);

		Composite composite = new Composite(composite_2, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		composite.setSize(353, 49);
		composite.setLayout(new GridLayout(2, false));

		btnOK = new Button(composite, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(textEmail.getText().isEmpty() && textEmail.getEnabled()){
					Utils.log(shlUserLogin, null, log, "It seems that you are not logged in.\n\nPlease enter a username and password then connect to the web service", new Throwable("Not Logged in"));

				}else if(txtServer.getText().isEmpty() && btnTest.isEnabled()){
					Utils.log(shlUserLogin, null, log, "Please enter a file server.", new Throwable("No server name"));

				}else if(! App.INSTANCE.isValid()){
					if(!textUsername.getText().isEmpty() && textEmail.getText().isEmpty()) Utils.log(shlUserLogin, null, log, "All fields are required", new Throwable("A field is empty"));
					else if (btnCheckSSH.isEnabled()) Utils.log(shlUserLogin, null, log, "Please connect to a service", new Throwable("No server name"));
				}
				else {
					App.INSTANCE.save();
					result = Window.OK;
					shlUserLogin.close();

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
				shlUserLogin.close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		btnCancel.setText("Cancel");
		shlUserLogin.setDefaultButton(btnOK);
		scrolledComposite.setContent(composite_2);
		scrolledComposite.setMinSize(composite_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		createContent();

	}

	protected void switchToCrop(String cropName) {
		// TODO Auto-generated method stub
		App.INSTANCE.setCrop(cropName);
		try {
			ClientContext.getInstance(null, false).setCurrentClientCrop(cropName);

		} catch (Exception err) {
			Utils.log(shlUserLogin, null, log, "Error selecting crop", err);
		}
	}

	protected void getCrops() {
		// TODO Auto-generated method stub
		Controller.userEmail = "";

		if(App.INSTANCE.getService() == null || App.INSTANCE.getService().isEmpty()) return;
		if(!App.INSTANCE.getService().endsWith("/")){
			String service = App.INSTANCE.getService() +"/";
			txtService.setText(service);
			App.INSTANCE.setService(service);
		}
		if(Controller.getCrops(log, true, btnCheckSSH.getSelection(),true)){
			enableUserLogin(true);
			 FormUtils.cropSetToCombo(cbCrop);
			if(cbCrop.getItemCount()>0) cbCrop.select(0);
			String cropName = cbCrop.getText();
			if(cropName != null) switchToCrop(cropName);
			btnCheckSSH.setEnabled(false);
		}else{
			enableUserLogin(false);
			Utils.log(shlUserLogin, null, log, "Error connecting to service.", null);

		}
	}

	protected void connect(boolean displayErrors) {
		// TODO Auto-generated method stub

		Controller.userEmail = "";
		textEmail.setText("");

		if(Controller.authenticate(textUsername.getText(),textPassword.getText())){
//
//			String cropName = FormUtils.cropSetToCombo(cbCrop);
//			if(cropName != null) switchToCrop(cropName);
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
				if (displayErrors)Utils.log(shlUserLogin, null, log, "Invalid user credentials.", e1);
				else Utils.log(log, "nvalid user credentials. ", e1);
			}

		}else{
			if (displayErrors)Utils.log(shlUserLogin, null, log, "Invalid username or password", null);
		}
	}

	protected void enableFileServer(boolean b) {
		// TODO Auto-generated method stub
		txtServer.setEnabled(b);
		btnTest.setEnabled(false);

		//		if(!b) txtServer.setText("");
	}

	protected void enableUserLogin(boolean b) {
		// TODO Auto-generated method stub
		cbCrop.setEnabled(b);
		textUsername.setEnabled(b);
		textPassword.setEnabled(b);
		btnConnect.setEnabled(b);

		if(!b){
			textUsername.setText("");
			textPassword.setText("");
			textEmail.setText("");

			App.INSTANCE.getUser().setUserEmail(null);
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

			if(Controller.getDTOResponse(shlUserLogin, resultEnvelope.getHeader(), null, false)){
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
