package edu.cornell.gobii.gdi.main;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestContact;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiCropType;

import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class UserDialog extends Dialog {
	private static Logger log = Logger.getLogger(UserDialog.class.getName());
	protected int result;
	protected Shell shell;
	private Text txtEmail;
	private Text txtUsername;
	private Combo cbContact;
	private Combo cbCrop;
	private Text txtServer;
	private Text txtService;

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
		shell = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setSize(450, 350);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblLogo = new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblWebService = new Label(shell, SWT.NONE);
		lblWebService.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWebService.setText("Web service:");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_1.heightHint = 34;
		composite_1.setLayoutData(gd_composite_1);
		
		txtService = new Text(composite_1, SWT.BORDER);
		txtService.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				App.INSTANCE.setService(txtService.getText());
			}
		});
		txtService.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnConnect = new Button(composite_1, SWT.NONE);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(App.INSTANCE.getService() == null || App.INSTANCE.getService().isEmpty()) return;
				if(!App.INSTANCE.getService().endsWith("/")){
					String service = App.INSTANCE.getService() +"/";
					txtService.setText(service);
					App.INSTANCE.setService(service);
				}
				App.INSTANCE.setCrop(null);
				if(Controller.authenticate(log, true)){
					FormUtils.cropSetToCombo(cbCrop);
				}else{
					Utils.log(shell, null, log, "Error connecting to service", null);
				}
			}
		});
		btnConnect.setText("Connect...");
		
		Label lblServer = new Label(shell, SWT.NONE);
		lblServer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServer.setText("Server:");
		
		txtServer = new Text(shell, SWT.BORDER);
		txtServer.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				App.INSTANCE.setServer(txtServer.getText());
			}
		});
		txtServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCrop = new Label(shell, SWT.NONE);
		lblCrop.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCrop.setText("Crop:");
		
		cbCrop = new Combo(shell, SWT.READ_ONLY);
		cbCrop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String cropName = cbCrop.getItem(cbCrop.getSelectionIndex());
				GobiiCropType crop = (GobiiCropType) cbCrop.getData(cropName);
				App.INSTANCE.setCrop(cropName);
				try {
					ClientContext.getInstance(null, false).setCurrentClientCrop(crop);
					if(Controller.authenticate(log, false)){
						cbContact.setEnabled(true);
						txtEmail.setEnabled(true);
						txtUsername.setEnabled(true);
						FormUtils.entrySetToCombo(Controller.getContactNamesByType("Curator"), cbContact);
					}
				} catch (Exception err) {
					Utils.log(shell, null, log, "Error selecting crop", err);
				}
			}
		});
		cbCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.cropSetToCombo(cbCrop);
		new Label(shell, SWT.NONE);
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		cbContact = new Combo(shell, SWT.NONE);
		cbContact.setEnabled(false);
		cbContact.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = cbContact.getItem(cbContact.getSelectionIndex());
				Integer id = Integer.parseInt((String) cbContact.getData(name));
				App.INSTANCE.getUser().setUserName(name);
				App.INSTANCE.getUser().setUserId(id);
				populateContactDetails(id);
			}
		});
		cbContact.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		FormUtils.entrySetToCombo(Controller.getContactNamesByType("Curator"), cbContact);
		
		Label lblEmail = new Label(shell, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("Email:");
		
		txtEmail = new Text(shell, SWT.BORDER);
		txtEmail.setEnabled(false);
		txtEmail.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				App.INSTANCE.getUser().setUserEmail(txtEmail.getText());
			}
		});
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUsername.setText("Username:");
		
		txtUsername = new Text(shell, SWT.BORDER);
		txtUsername.setEnabled(false);
		txtUsername.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				App.INSTANCE.getUser().setUserName(txtUsername.getText());
			}
		});
		txtUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(shell, SWT.NONE);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 46;
		composite.setLayoutData(gd_composite);
		
		Button btnOK = new Button(composite, SWT.NONE);
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(App.INSTANCE.isValid()){
					App.INSTANCE.save();
					result = Window.OK;
					shell.close();
				}else{
					Utils.log(shell, null, log, "All fields are required!", new Throwable("Invalid user information"));
				}
			}
		});
		btnOK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnOK.setText("OK");
		
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
		
		createContent();
		shell.setDefaultButton(btnOK);
//		try {
//			List<GobiiCropType> crops = ClientContext.getInstance().getCropTypeTypes();
//			for(GobiiCropType crop : crops){
//				cbCrop.add(crop.name());
//				cbCrop.setData(crop.name(), crop);
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}
	
	protected void createContent(){
		if(App.INSTANCE.getService() != null) txtService.setText(App.INSTANCE.getService());
		if(App.INSTANCE.getServer() != null) txtServer.setText(App.INSTANCE.getServer());
		if(App.INSTANCE.getCrop() != null) cbCrop.select(cbCrop.indexOf(App.INSTANCE.getCrop()));
		if(App.INSTANCE.getUser() != null){
			for(int i=0; i<cbContact.getItemCount(); i++){
				String name = cbContact.getItem(i);
				Integer id = Integer.parseInt((String) cbContact.getData(name));
				if(name.equals(App.INSTANCE.getUser().getUserFullname()) && (id.equals(App.INSTANCE.getUser().userId))){
					cbContact.select(i);
					break;
				}
			}
			if(App.INSTANCE.getUser().userEmail != null) txtEmail.setText(App.INSTANCE.getUser().userEmail);
			if(App.INSTANCE.getUser().userName != null) txtUsername.setText(App.INSTANCE.getUser().userName);
		}
	}
	
	protected void populateContactDetails(int contactId) {
		DtoRequestContact dtoRequestContact = new DtoRequestContact();
		ContactDTO contactDTO = new ContactDTO();
		contactDTO.setContactId(contactId);
		try {
			contactDTO = dtoRequestContact.process(contactDTO);
			if(!contactDTO.getDtoHeaderResponse().isSucceeded()){
				String message = "";
				for (HeaderStatusMessage currentStatusMesage : contactDTO.getDtoHeaderResponse().getStatusMessages()) {
	                message += currentStatusMesage.getMessage();
	            }
				System.out.println(message);
//				Utils.log(shell, null, log, "Error getting contact details.", new Throwable(message));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String email = contactDTO.getEmail();
		String username = (String)email.subSequence(0, email.indexOf("@"));
		App.INSTANCE.getUser().setUserFullname(cbContact.getItem(cbContact.getSelectionIndex()));
		txtEmail.setText(email); App.INSTANCE.getUser().setUserEmail(email);
		txtUsername.setText(username); App.INSTANCE.getUser().setUserName(username);
	}
	
	
}
