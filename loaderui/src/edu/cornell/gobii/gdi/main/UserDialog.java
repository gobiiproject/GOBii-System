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
import org.eclipse.swt.custom.ScrolledComposite;

public class UserDialog extends Dialog {
	private static Logger log = Logger.getLogger(UserDialog.class.getName());
	protected int result;
	protected Shell shell;
	private Text txtEmail;
	private Text txtUsername;
	private Combo cbContact;
	private Combo cbCrop;
	private Button btnCheckSSH;
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
		shell = new Shell(getParent(), SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shell.setSize(440, 378);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite_2 = new Composite(scrolledComposite, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		
				Label lblLogo = new Label(composite_2, SWT.NONE);
				lblLogo.setSize(0, 15);
						new Label(composite_2, SWT.NONE);
				
						Label lblWebService = new Label(composite_2, SWT.RIGHT);
						lblWebService.setSize(66, 15);
						lblWebService.setText("Web service:");
								
										Composite composite_1 = new Composite(composite_2, SWT.NONE);
										composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
										composite_1.setSize(353, 34);
										composite_1.setLayout(new GridLayout(2, false));
										
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
//				App.INSTANCE.setCrop(null);
																if(Controller.authenticate(log, true, btnCheckSSH.getSelection())){
																	FormUtils.cropSetToCombo(cbCrop);
																}else{
																	Utils.log(shell, null, log, "Error connecting to service", null);
																}
															}
														});
														btnConnect.setText("Connect...");
																
																		Label lblServer = new Label(composite_2, SWT.RIGHT);
																		lblServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
																		lblServer.setSize(56, 15);
																		lblServer.setText("File Server:");
																
																Composite composite_3 = new Composite(composite_2, SWT.NONE);
																composite_3.setLayout(new GridLayout(2, false));
																GridData gd_composite_3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
																gd_composite_3.heightHint = 37;
																composite_3.setLayoutData(gd_composite_3);
																		
																				txtServer = new Text(composite_3, SWT.BORDER);
																				txtServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
																				txtServer.setSize(353, 21);
																				
																				Button btnTest = new Button(composite_3, SWT.NONE);
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
																				btnTest.setText("Test...");
																				txtServer.addModifyListener(new ModifyListener() {
																					public void modifyText(ModifyEvent arg0) {
																						App.INSTANCE.setServer(txtServer.getText());
																					}
																				});
																new Label(composite_2, SWT.NONE);
																new Label(composite_2, SWT.NONE);
																new Label(composite_2, SWT.NONE);
																
																btnCheckSSH = new Button(composite_2, SWT.CHECK);
																btnCheckSSH.setSize(42, 16);
																btnCheckSSH.setSelection(true);
																btnCheckSSH.setText("SSH");
																
																		Label lblCrop = new Label(composite_2, SWT.RIGHT);
																		lblCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
																		lblCrop.setSize(29, 15);
																		lblCrop.setText("Crop:");
																
																		cbCrop = new Combo(composite_2, SWT.READ_ONLY);
																		cbCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
																		cbCrop.setSize(353, 23);
																		cbCrop.addSelectionListener(new SelectionAdapter() {
																			@Override
																			public void widgetSelected(SelectionEvent e) {
																				String cropName = cbCrop.getItem(cbCrop.getSelectionIndex());
																				//String crop = cbCrop.getData(cropName);
																				App.INSTANCE.setCrop(cropName);
																				try {
																					clearContactDetails();
																					ClientContext.getInstance(null, false).setCurrentClientCrop(cropName);
																					cbContact.setEnabled(true);
																					txtEmail.setEnabled(true);
																					txtUsername.setEnabled(true);
																					FormUtils.entrySetToCombo(Controller.getContactNamesByType("Curator"), cbContact);

																				} catch (Exception err) {
																					Utils.log(shell, null, log, "Error selecting crop", err);
																				}
																			}
																		});
																		FormUtils.cropSetToCombo(cbCrop);
																		new Label(composite_2, SWT.NONE);
																		
																				Label label = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
																				label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
																				label.setSize(353, 2);
																				
																						Label lblName = new Label(composite_2, SWT.RIGHT);
																						lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
																						lblName.setSize(35, 15);
																						lblName.setText("Name:");
																				
																						cbContact = new Combo(composite_2, SWT.NONE);
																						cbContact.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
																						cbContact.setSize(353, 23);
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
																						//		FormUtils.entrySetToCombo(Controller.getContactNamesByType("Curator"), cbContact);

																						Label lblEmail = new Label(composite_2, SWT.RIGHT);
																						lblEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
																						lblEmail.setSize(32, 15);
																						lblEmail.setText("Email:");
																						
																								txtEmail = new Text(composite_2, SWT.BORDER);
																								txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
																								txtEmail.setSize(353, 21);
																								txtEmail.setEnabled(false);
																								txtEmail.addModifyListener(new ModifyListener() {
																									public void modifyText(ModifyEvent arg0) {
																										App.INSTANCE.getUser().setUserEmail(txtEmail.getText());
																									}
																								});
																								
																										Label lblUsername = new Label(composite_2, SWT.RIGHT);
																										lblUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
																										lblUsername.setSize(56, 15);
																										lblUsername.setText("Username:");
																								
																										txtUsername = new Text(composite_2, SWT.BORDER);
																										txtUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
																										txtUsername.setSize(353, 21);
																										txtUsername.setEnabled(false);
																										txtUsername.addModifyListener(new ModifyListener() {
																											public void modifyText(ModifyEvent arg0) {
																												App.INSTANCE.getUser().setUserName(txtUsername.getText());
																											}
																										});
																										new Label(composite_2, SWT.NONE);
																										
																												Composite composite = new Composite(composite_2, SWT.NONE);
																												composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
																												composite.setSize(353, 49);
																												composite.setLayout(new GridLayout(2, false));
																												
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
				String username = (String)email.subSequence(0, email.indexOf("@"));
				App.INSTANCE.getUser().setUserFullname(cbContact.getItem(cbContact.getSelectionIndex()));
				txtEmail.setText(email); App.INSTANCE.getUser().setUserEmail(email);
				txtUsername.setText(username); App.INSTANCE.getUser().setUserName(username);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	protected void clearContactDetails(){
		txtEmail.setText("");
		txtUsername.setText("");
	}

}
