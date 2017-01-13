package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.wb.swt.SWTResourceManager;

public class FrmContacts extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmContacts.class.getName());
	private Text txtLastName;
	private Text txtFirstName;
	private Text txtCode;
	private Text txtEmail;
	private Table tbRoles;
	private Combo comboOrganization;
	private String selectedContactEmail = "";

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public FrmContacts(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblLastName = new Label(cmpForm, SWT.NONE);
		lblLastName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLastName.setText("*Last name:");

		txtLastName = new Text(cmpForm, SWT.BORDER);
		txtLastName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblFirstName = new Label(cmpForm, SWT.NONE);
		lblFirstName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstName.setText("*First name:");

		txtFirstName = new Text(cmpForm, SWT.BORDER);
		txtFirstName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblEmail = new Label(cmpForm, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("*Email:");

		txtEmail = new Text(cmpForm, SWT.BORDER);
		txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblOrganization = new Label(cmpForm, SWT.NONE);
		lblOrganization.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrganization.setText("Organization");

		comboOrganization = new Combo(cmpForm, SWT.READ_ONLY);
		comboOrganization.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		populateOrganizationCombo(comboOrganization);

		Label lblRoles = new Label(cmpForm, SWT.NONE);
		lblRoles.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRoles.setText("Roles:");

		tbRoles = new Table(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		tbRoles.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbRoles.setHeaderVisible(true);
		tbRoles.setLinesVisible(true);
		FormUtils.entrySetToTable(Controller.getRoleNames(), tbRoles);

		TableColumn tblclmnRoles = new TableColumn(tbRoles, SWT.NONE);
		tblclmnRoles.setWidth(150);
		tblclmnRoles.setText("Roles");
		new Label(cmpForm, SWT.NONE);

		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (!validate(true))
						return;

					ContactDTO contactDTORequest = new ContactDTO();
					contactDTORequest.setLastName(txtLastName.getText());
					contactDTORequest.setFirstName(txtFirstName.getText());
					contactDTORequest.setEmail(txtEmail.getText());
					String email = txtEmail.getText().substring(0, txtEmail.getText().indexOf("@"));
					String lastname = txtLastName.getText().replaceAll(" ", "_");
					String firstname = txtFirstName.getText().replaceAll(" ", "_");
					contactDTORequest.setCode(email + "_" + lastname + "_" + firstname);
					contactDTORequest.setCreatedBy(1);
					contactDTORequest.setModifiedBy(1);
					contactDTORequest.setCreatedDate(new Date());
					contactDTORequest.setModifiedDate(new Date());
					if (comboOrganization.getSelectionIndex() > -1) {
						String ref = (String) comboOrganization
								.getData(comboOrganization.getItem(comboOrganization.getSelectionIndex()));
						contactDTORequest.setOrganizationId(Integer.parseInt(ref));
					}
					for (TableItem item : tbRoles.getItems()) {
						if (item.getChecked()) {
							Integer index = Integer.parseInt((String) item.getData(item.getText()));
							contactDTORequest.getRoles().add(index);
						}
					}
					try {
						PayloadEnvelope<ContactDTO> payloadEnvelope = new PayloadEnvelope<>(contactDTORequest,
								GobiiProcessType.CREATE);
						GobiiEnvelopeRestResource<ContactDTO> restResource = new GobiiEnvelopeRestResource<>(App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_CONTACTS));
						PayloadEnvelope<ContactDTO> contactDTOResponseEnvelope = restResource.post(ContactDTO.class,
								payloadEnvelope);
						if (Controller.getDTOResponse(shell, contactDTOResponseEnvelope.getHeader(), memInfo, true)) {
							if (cbList.getSelectionIndex() < 0)
								displayAllContacts();
							else
								populateContactFromSelectedRole(cbList.getText());
						}
						;
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error saving contact", err);
					}
				} catch (Exception err) {
					Utils.log(shell, memInfo, log, "Error saving contact", err);
				}
			}
		});
		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);

		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (!validate(false))
						return;

					ContactDTO contactDTORequest = new ContactDTO();
					contactDTORequest.setContactId(IDs.contactId);
					contactDTORequest.setLastName(txtLastName.getText());
					contactDTORequest.setFirstName(txtFirstName.getText());
					contactDTORequest.setEmail(txtEmail.getText());
					String email = txtEmail.getText().substring(0, txtEmail.getText().indexOf("@"));
					String lastname = txtLastName.getText().replaceAll(" ", "_");
					String firstname = txtFirstName.getText().replaceAll(" ", "_");
					contactDTORequest.setCode(email + "_" + lastname + "_" + firstname);
					contactDTORequest.setCreatedBy(1);
					contactDTORequest.setModifiedBy(1);
					contactDTORequest.setCreatedDate(new Date());
					contactDTORequest.setModifiedDate(new Date());
					if (comboOrganization.getSelectionIndex() > -1) {
						String ref = (String) comboOrganization
								.getData(comboOrganization.getItem(comboOrganization.getSelectionIndex()));
						contactDTORequest.setOrganizationId(Integer.parseInt(ref));
					}
					for (TableItem item : tbRoles.getItems()) {
						if (item.getChecked()) {
							Integer index = Integer.parseInt((String) item.getData(item.getText()));
							contactDTORequest.getRoles().add(index);
						}
					}
					try {
						RestUri restUriContact = App.INSTANCE.getUriFactory().resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
						restUriContact.setParamValue("id", Integer.toString(IDs.contactId));
						GobiiEnvelopeRestResource<ContactDTO> restResourceContactById = new GobiiEnvelopeRestResource<>(restUriContact);
						restResourceContactById.setParamValue("id", contactDTORequest.getContactId().toString());
						PayloadEnvelope<ContactDTO> contactDTOResponseEnvelopeUpdate = restResourceContactById.put(
								ContactDTO.class, new PayloadEnvelope<>(contactDTORequest, GobiiProcessType.UPDATE));
						if (Controller.getDTOResponse(shell, contactDTOResponseEnvelopeUpdate.getHeader(), memInfo,
								true)) {
							if (cbList.getSelectionIndex() < 0)
								displayAllContacts();
							else
								populateContactFromSelectedRole(cbList.getText());
						}
						;
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error saving contact", err);
					}
				} catch (Exception err) {
					Utils.log(shell, memInfo, log, "Error saving contact", err);
				}
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);

		Button btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");

		TableColumn tblclmnContacts = new TableColumn(tbList, SWT.NONE);
		tblclmnContacts.setWidth(300);
		tblclmnContacts.setText("Contacts");

	}

	private void populateOrganizationCombo(Combo comboOrganization) {
		// TODO Auto-generated method stub
		try {
			FormUtils.entrySetToCombo(Controller.getOrganizationNames(), comboOrganization);
		} catch (Exception e) {
			Utils.log(shell, memInfo, log, "Error retrieving organization", e);
		}

	}

	protected void displayAllContacts() {
		// TODO Auto-generated method stub
		tbList.removeAll();
		FormUtils.entrySetToTable(Controller.getContactNames(), tbList);

	}

	protected void populateContactDetails(Integer contactId) {
		try {
			cleanDetails();
			RestUri restUriContact = App.INSTANCE.getUriFactory().resourceByUriIdParam(ServiceRequestId.URL_CONTACTS);
			restUriContact.setParamValue("id", Integer.toString(contactId));
			GobiiEnvelopeRestResource<ContactDTO> restResource = new GobiiEnvelopeRestResource<>(restUriContact);
			PayloadEnvelope<ContactDTO> resultEnvelope = restResource.get(ContactDTO.class);

			try {
				if (Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, false)) {
					ContactDTO contactDTO = resultEnvelope.getPayload().getData().get(0);
					txtLastName.setText(contactDTO.getLastName());
					txtFirstName.setText(contactDTO.getFirstName());
					txtCode.setText(contactDTO.getCode());
					selectedContactEmail = contactDTO.getEmail();
					txtEmail.setText(contactDTO.getEmail());
					populateOrganizationComboAndSelect(comboOrganization, contactDTO.getOrganizationId());
					for (TableItem item : tbRoles.getItems()) {
						Integer index = Integer.parseInt((String) item.getData(item.getText()));
						boolean checked = contactDTO.getRoles().contains(index) ? true : false;
						item.setChecked(checked);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception err) {
			Utils.log(shell, memInfo, log, "Error retrieving contacts", err);
		}
	}

	private void populateOrganizationComboAndSelect(Combo combo, Integer typeId) {
		// TODO Auto-generated method stub
		try {
			FormUtils.entrySetToComboSelectId(Controller.getOrganizationNames(), combo, typeId);
		} catch (Exception e) {
			// Utils.log(shell, memInfo, log, "Error retrieving organization
			// names", e);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		// TODO Auto-generated method stub
		FormUtils.entrySetToCombo(Controller.getRoleNames(), cbList);
		cbList.setText("*Select Contact Type");
		FormUtils.entrySetToTable(Controller.getContactNames(), tbList);
		cbList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					String selected = cbList.getText(); // single selection
					IDs.roleId = Integer.parseInt((String) cbList.getData(selected));
					populateContactsFromSelectedRole(selected);
				} catch (Exception err) {
					Utils.log(shell, memInfo, log, "Error retrieving contacts", err);
				}
			}
		});
		tbList.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				try {
					String selected = tbList.getSelection()[0].getText();
					IDs.contactId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
					populateContactDetails(IDs.contactId);
				} catch (Exception e) {
					Utils.log(shell, memInfo, log, "Error retrieving contacts", e);
				}
			}
		});

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbList.removeAll();
				Integer id = FormUtils.getIdFromFormList(cbList);
				if (id > 0) {
					populateContactsFromSelectedRole(cbList.getText());
					FormUtils.entrySetToComboSelectId(Controller.getRoleNames(), cbList, id);
				} else {
					FormUtils.entrySetToCombo(Controller.getRoleNames(), cbList);
					cbList.setText("Select Contact Type");
					FormUtils.entrySetToTable(Controller.getContactNames(), tbList);
				}
				cleanDetails();
			}
		});
	}

	protected void populateContactFromSelectedRole(String selectedRole) {
		try {
			IDs.roleId = Integer.parseInt((String) cbList.getData(selectedRole));
			populateContactsFromSelectedRole(selectedRole);
		} catch (Exception e) {
			Utils.log(shell, memInfo, log, "Error retrieving contacts", e);
		}
	}

	protected void populateContactsFromSelectedRole(String selected) {
		try {
			cleanDetails();
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getContactNamesByType(selected), tbList);
		} catch (Exception e) {
			Utils.log(shell, memInfo, log, "Error retrieving contacts", e);
		}
	}

	protected void cleanDetails() {
		try {
			txtLastName.setText("");
			txtFirstName.setText("");
			txtCode.setText("");
			txtEmail.setText("");
			selectedContactEmail = "";
			comboOrganization.deselectAll();
			for (TableItem item : tbRoles.getItems()) {
				item.setChecked(false);
			}
		} catch (Exception e) {
			Utils.log(shell, memInfo, log, "Error clearing fields", e);
		}
	}

	protected boolean validate(boolean isNew) {
		boolean successful = true;
		String message = null;
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		if (txtLastName.getText().isEmpty()) {
			successful = false;
			message = "Lastname is a required field!";
		} else if (txtFirstName.getText().isEmpty()) {
			successful = false;
			message = "Firstname is a required field!";
		} else if (txtEmail.getText().isEmpty()) {
			successful = false;
			message = "Email is a required field!";
		} else if (!txtEmail.getText().contains("@") || !txtEmail.getText().contains(".")) {
			successful = false;
			message = "Incorrect Email format, please check and correct!";
		} else if (!isNew && IDs.contactId == 0) {
			message = "Your entry is recognized as a new value. Please use Add instead.";
			successful = false;
		} else if (!Controller.isNewContactEmail(txtEmail.getText())) {
			if (isNew) {
				successful = false;
				message = "Email already exists!";
			}else{
				if(!txtEmail.getText().equalsIgnoreCase(selectedContactEmail)){
					successful = false;
					message = "Email already exists!";
				}
			}
		}

		if (!successful) {
			dialog.setMessage(message);
			dialog.open();
		}

		return successful;
	}
}
