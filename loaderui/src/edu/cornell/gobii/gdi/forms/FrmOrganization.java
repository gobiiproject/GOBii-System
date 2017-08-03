package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class FrmOrganization extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmOrganization.class.getName());
	private Text txtName;
	private Text txtAddress;
	private Text txtWebsite;
	private int currentOrganizationId;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmOrganization(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Organization Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAddress = new Label(cmpForm, SWT.NONE);
		lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAddress.setText("Address:");

		txtAddress = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtAddress.setEditable(true);
		txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblWebsite = new Label(cmpForm, SWT.NONE);
		lblWebsite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWebsite.setText("Website:");

		txtWebsite = new Text(cmpForm, SWT.BORDER);
		txtWebsite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);

		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;


					OrganizationDTO organizationDTORequest = new OrganizationDTO();
					String name = txtName.getText();
					organizationDTORequest.setName(name);
					organizationDTORequest.setAddress(txtAddress.getText());
					organizationDTORequest.setWebsite(txtWebsite.getText());
					organizationDTORequest.setStatusId(1);

					try {
						PayloadEnvelope<OrganizationDTO> payloadEnvelope = new PayloadEnvelope<>(organizationDTORequest, GobiiProcessType.CREATE);
						GobiiEnvelopeRestResource<OrganizationDTO> restResource = new GobiiEnvelopeRestResource<>( GobiiClientContext.getInstance(null, false)
								.getUriFactory()
								.resourceColl(GobiiServiceRequestId.URL_ORGANIZATION));
						PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelope = restResource.post(OrganizationDTO.class,
								payloadEnvelope);

						if(Controller.getDTOResponse(shell, organizationDTOResponseEnvelope.getHeader(), memInfo, true)){
							populateOrganizationTable();
							currentOrganizationId = organizationDTOResponseEnvelope.getPayload().getData().get(0).getOrganizationId();
							FormUtils.selectRowById(tbList,currentOrganizationId);
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Organizations", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Organizations", err);
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
				try{
					if(!validate(false)) return;
					if(!FormUtils.updateForm(getShell(), "Organization", selectedName)) return;

					OrganizationDTO organizationDTORequest = new OrganizationDTO();
					organizationDTORequest.setOrganizationId(currentOrganizationId);
					String name = txtName.getText();
					organizationDTORequest.setName(name);
					organizationDTORequest.setAddress(txtAddress.getText());
					organizationDTORequest.setWebsite(txtWebsite.getText());
					organizationDTORequest.setStatusId(1);
					try {
						RestUri restUriOrganizationForGetById =  GobiiClientContext.getInstance(null, false).getUriFactory()
								.resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
						restUriOrganizationForGetById.setParamValue("id", organizationDTORequest.getOrganizationId().toString());
						GobiiEnvelopeRestResource<OrganizationDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);

						restResourceForGetById.setParamValue("id", organizationDTORequest.getOrganizationId().toString());
						PayloadEnvelope<OrganizationDTO> organizationDTOResponseEnvelopeUpdate = restResourceForGetById.put(OrganizationDTO.class,
								new PayloadEnvelope<>(organizationDTORequest, GobiiProcessType.UPDATE));


						if(Controller.getDTOResponse(shell, organizationDTOResponseEnvelopeUpdate.getHeader(), memInfo, true)){
							populateOrganizationTable();
							FormUtils.selectRowById(tbList,currentOrganizationId);
							
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Organizations", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Organizations", err);
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
				clearDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {

		TableColumn tblclmnOrganizations = new TableColumn(tbList, SWT.NONE);
		tblclmnOrganizations.setWidth(300);
		tblclmnOrganizations.setText("Organizations:");

		populateOrganizationTable();

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateOrganizationTable();
				clearDetails();
				currentOrganizationId=0;
			}
		});

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				selectedName = selected;
				currentOrganizationId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateOrganizationDetails(currentOrganizationId); 
			}


			protected void populateOrganizationDetails(Integer organizationId) {
				try{
					OrganizationDTO organizationDTO = new OrganizationDTO();
					organizationDTO.setOrganizationId(organizationId);
					try {
						RestUri restUriOrganizationForGetById =  GobiiClientContext.getInstance(null, false).getUriFactory()
								.resourceByUriIdParam(GobiiServiceRequestId.URL_ORGANIZATION);
						restUriOrganizationForGetById.setParamValue("id", organizationId.toString());
						GobiiEnvelopeRestResource<OrganizationDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
						PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = restResourceForGetById
								.get(OrganizationDTO.class);
						if(Controller.getDTOResponse(shell, resultEnvelopeForGetByID.getHeader(), memInfo, false)){
							organizationDTO = resultEnvelopeForGetByID.getPayload().getData().get(0);
							selectedName = organizationDTO.getName();
							txtName.setText(organizationDTO.getName());
							txtAddress.setText(organizationDTO.getAddress());
							txtWebsite.setText(organizationDTO.getWebsite()==null ? "" : organizationDTO.getWebsite());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//displayDetails
					
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Organizations", err);
				}
			}
		});
	}
	private void populateOrganizationTable() {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getOrganizationNames(), tbList);

		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Organizations", err);
		}
	}

	private void clearDetails(){
		try{
			txtName.setText("");
			txtAddress.setText("");
			txtWebsite.setText("");
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			successful = false;
			message = "Name is required field!";
		}else if(!isNew && currentOrganizationId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else if(isNew|| !txtName.getText().equalsIgnoreCase(selectedName)){
			for(TableItem item : tbList.getItems()){
				if(item.getText().equalsIgnoreCase(txtName.getText())){
					successful = false;
					message = "Organization name already exists!";
					break;
				}
			}
		}
		if(!successful){
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
}
