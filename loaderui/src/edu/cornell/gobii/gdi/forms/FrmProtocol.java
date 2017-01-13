package edu.cornell.gobii.gdi.forms;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.OrganizationDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProtocolDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class FrmProtocol  extends AbstractFrm {
	
	private static Logger log = Logger.getLogger(FrmProtocol.class.getName());
	private Text txtName;
	private Combo cbPlatform;
	private StyledText memDescription;
	private Table tbVendorProtocol;

	public FrmProtocol(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cmpForm.setLayout(new GridLayout(2, false));
		
		Label label = new Label(cmpForm, SWT.NONE);
		label.setText("*Name:");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPlatform = new Label(cmpForm, SWT.NONE);
		lblPlatform.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlatform.setText("Platform:");
		
		cbPlatform = new Combo(cmpForm, SWT.NONE);
		cbPlatform.setEnabled(false);
		cbPlatform.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(IDs.platformId > 0)
			FormUtils.entrySetToComboSelectId(Controller.getPlatformNames(), cbPlatform, IDs.platformId);
		else
			FormUtils.entrySetToCombo(Controller.getPlatformNames(), cbPlatform);
		
		Label label_2 = new Label(cmpForm, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("Description:");
		
		memDescription = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
		memDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblVendorprotocol = new Label(cmpForm, SWT.NONE);
		lblVendorprotocol.setText("Vendor-Protocol:");
		
		tbVendorProtocol = new Table(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		tbVendorProtocol.addSelectionListener(new SelectionAdapter() {
			private TableEditor editor;

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;
				
				if(e.detail == SWT.CHECK){
					String protocolName = txtName.getText().isEmpty() ? "" : txtName.getText();
					String vendorName = item.getText(0).replaceAll(" ", "_");
					item.setText(1, vendorName+"_"+protocolName);
				}else{
					// Clean up any previous editor control
					editor = new TableEditor(tbVendorProtocol);
					// The editor must have the same size as the cell and must
					// not be any smaller than 50 pixels.
					editor.horizontalAlignment = SWT.LEFT;
					editor.grabHorizontal = true;
					editor.minimumWidth = 50;
					Control oldEditor = editor.getEditor();
					if (oldEditor != null)
						oldEditor.dispose();                

					// The control that will be the editor must be a child of the
					// Table
					Text newEditor = new Text(tbVendorProtocol, SWT.NONE);
					final int EDITABLECOLUMN=1;
					newEditor.setText(item.getText(EDITABLECOLUMN));
					Listener textListener = new Listener() {
						public void handleEvent(final Event e) {
							switch (e.type) {
							case SWT.FocusOut:
								item.setText(EDITABLECOLUMN, newEditor.getText());
								item.setData("updated", "true");
								newEditor.dispose();
								break;
							case SWT.Traverse:
								switch (e.detail) {
								case SWT.TRAVERSE_RETURN:
									item
									.setText(EDITABLECOLUMN, newEditor.getText());
									item.setData("updated", "true");
									// FALL THROUGH
								case SWT.TRAVERSE_ESCAPE:
									newEditor.dispose();
									e.doit = false;
								}
								break;
							}
						}
					};
					newEditor.addListener(SWT.FocusOut, textListener);
					newEditor.addListener(SWT.Traverse, textListener);
					newEditor.selectAll();
					newEditor.setFocus();           
					editor.setEditor(newEditor, item, EDITABLECOLUMN);  
				}
			}
		});
		tbVendorProtocol.setHeaderVisible(true);
		tbVendorProtocol.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbVendorProtocol.setLinesVisible(true);
		
		TableColumn tblclmnVendor = new TableColumn(tbVendorProtocol, SWT.NONE);
		tblclmnVendor.setWidth(100);
		tblclmnVendor.setText("Vendor");
		
		TableColumn tblclmnVendorprotocol = new TableColumn(tbVendorProtocol, SWT.NONE);
		tblclmnVendorprotocol.setWidth(100);
		tblclmnVendorprotocol.setText("Vendor-Protocol");
		new Label(cmpForm, SWT.NONE);
		
		FormUtils.entrySetToTable(Controller.getOrganizationNames(), tbVendorProtocol);
		
		Button btnAdd = new Button(cmpForm, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate(true)) return;
				newProtocol(true);
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAdd.setText("Add New");
		new Label(cmpForm, SWT.NONE);
		
		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate(false)) return;
				if(!FormUtils.updateForm(getShell(), "Protocol", txtName.getText())) return;
				newProtocol(false);
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);
		
		Button btnClear = new Button(cmpForm, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanDetails();
			}
		});
		btnClear.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClear.setText("Clear Fields");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createContent() {
		populatePlatformsAndProtocols();
		
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbList.removeAll();

				populatePlatformsAndProtocols();
				cleanDetails();
			}
		});
		
		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try{
					cleanDetails();
					String selected = cbList.getText(); //single selection
					cbPlatform.select(cbPlatform.indexOf(selected));
					IDs.platformId = Integer.parseInt((String) cbList.getData(selected));

					populateProtocolListFromSelectedPlatform(IDs.platformId ); //retrieve and display projects by contact Id
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving protocols", err);
				}
			}
		});

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				IDs.protocolId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateProtocolDetails(IDs.protocolId); //retrieve and display projects by contact Id
			}

			protected void populateProtocolDetails(int protocolId) {
				try{
					cleanDetails();
					
					RestUri restUriProtocolForGetById = App
							.INSTANCE.getUriFactory()
							.resourceByUriIdParam(ServiceRequestId.URL_PROTOCOL);
					restUriProtocolForGetById.setParamValue("id", Integer.toString(protocolId));
					GobiiEnvelopeRestResource<ProtocolDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriProtocolForGetById);
					try {
						PayloadEnvelope<ProtocolDTO> resultEnvelopeForGetByID = restResourceForGetById
								.get(ProtocolDTO.class);

						if(Controller.getDTOResponse(shell, resultEnvelopeForGetByID.getHeader(), memInfo, false)){
							ProtocolDTO protocolDTOResponse = resultEnvelopeForGetByID.getPayload().getData().get(0);
							selectedName = protocolDTOResponse.getName();
							txtName.setText(protocolDTOResponse.getName());

							FormUtils.entrySetToComboSelectId(Controller.getPlatformNames(), cbPlatform, protocolDTOResponse.getPlatformId());
							memDescription.setText(protocolDTOResponse.getDescription());
							
							for(VendorProtocolDTO vpDTO : protocolDTOResponse.getVendorProtocols()){
								for(TableItem item : tbVendorProtocol.getItems()){
									Integer organizationId = Integer.parseInt((String) item.getData(item.getText(0)));
									if(vpDTO.getOrganizationId().equals(organizationId)){
										item.setText(1, vpDTO.getName());
										item.setChecked(true);
										item.setData("vendorProtocolId", vpDTO.getId());
										item.setData("updated", "false");
										break;
									}
								}
							}
						}
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
				}

			}
		});
	}

	private void populatePlatformsAndProtocols() {
		try{
			cbList.setText("*Select a Platform");
			tbList.removeAll();
			if(IDs.platformId > 0){
				FormUtils.entrySetToComboSelectId(Controller.getPlatformNames(), cbList, IDs.platformId);
				FormUtils.entrySetToTable(Controller.getProtocolNamesByPlatformId(IDs.platformId), tbList);
			}else{
				FormUtils.entrySetToCombo(Controller.getPlatformNames(), cbList);
				FormUtils.entrySetToTable(Controller.getProtocolNames(), tbList);
			}
			
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
		}
	}
	
	private void populateProtocolListFromSelectedPlatform(Integer selectedId) {
		tbList.removeAll();
		try{
			FormUtils.entrySetToTable(Controller.getProtocolNamesByPlatformId(selectedId), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects", err);
		}
	}
	
	private void cleanDetails(){
		try{
//			IDs.protocolId = 0;
			txtName.setText("");
			cbPlatform.deselectAll(); cbPlatform.setText("");
			memDescription.setText("");
			tbVendorProtocol.removeAll();
			FormUtils.entrySetToTable(Controller.getOrganizationNames(), tbVendorProtocol);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}
	
	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		if(txtName.getText().isEmpty()){
			message = "Name field is required!";
			successful = false;
		}else if(cbPlatform.getText().isEmpty()){
			message = "Please specify the platform!";
			successful = false;
		}else if(!isNew && IDs.protocolId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else{
			if(isNew|| !txtName.getText().equalsIgnoreCase(selectedName)){
				for(int i=0; i<tbList.getItemCount(); i++){
					if(tbList.getItem(i).getText(0).equalsIgnoreCase(txtName.getText())){
						successful = false;
						message = "Name of protocol already exists for this Platform";
						break;
					}
				}
			}

		}
		if(!successful){
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
	
	protected void newProtocol(boolean newProtocol) {
		try{

			PayloadEnvelope<ProtocolDTO> payloadEnvelope;
			PayloadEnvelope<ProtocolDTO> protocolDTOResponseEnvelope;
			ProtocolDTO newProtocolDto = new ProtocolDTO();
			GobiiEnvelopeRestResource<ProtocolDTO> restResource = null;
			setProtocolDetails(newProtocolDto);
//			for(TableItem item : tbProperties.getItems()){
//				if(item.getText(1).isEmpty()) continue;
//				newPlatformDto.getProperties().add(new EntityPropertyDTO((Integer)item.getData("entityId"), (Integer)item.getData("propertyId"), item.getText(0), item.getText(1)));
//
//			}
			if(newProtocol){
				payloadEnvelope = new PayloadEnvelope<>(newProtocolDto, GobiiProcessType.CREATE);
				restResource = new GobiiEnvelopeRestResource<>(App
						.INSTANCE.getUriFactory()
						.resourceColl(ServiceRequestId.URL_PROTOCOL));
				protocolDTOResponseEnvelope = restResource.post(ProtocolDTO.class,
						payloadEnvelope);
			}
			else{
				newProtocolDto.setProtocolId(IDs.protocolId);
				payloadEnvelope = new PayloadEnvelope<>(newProtocolDto, GobiiProcessType.UPDATE);
				RestUri restUriProtocolForGetById = App
						.INSTANCE.getUriFactory()
						.resourceByUriIdParam(ServiceRequestId.URL_PROTOCOL);
				restUriProtocolForGetById.setParamValue("id", newProtocolDto.getProtocolId().toString());
				restResource = new GobiiEnvelopeRestResource<>(restUriProtocolForGetById);
				protocolDTOResponseEnvelope = restResource.put(ProtocolDTO.class,
						payloadEnvelope);
			}
			try{
				if(Controller.getDTOResponse(shell, protocolDTOResponseEnvelope.getHeader(), memInfo, true)){
					Integer protocolId = protocolDTOResponseEnvelope.getPayload().getData().get(0).getId();
					newVendorProtocol(protocolId);
					tbList.removeAll();
					populateProtocolListFromSelectedPlatform(newProtocolDto.getPlatformId());
				};
			}catch(Exception err){
				Utils.log(shell, memInfo, log, "Error savging Platform", err);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Platform", err);
		}
	}
	
	public void newVendorProtocol(Integer protocolId){
		for(TableItem item : tbVendorProtocol.getItems()){
			Integer organizationId = Integer.parseInt((String) item.getData(item.getText(0)));
			String vendorProtocolName = item.getText(1).isEmpty() ? null : item.getText(1);
			Integer vendorProtocolId = item.getData("vendorProtocolId")==null ? null : (Integer) item.getData("vendorProtocolId");
			boolean updated = item.getData("updated") == null ? false : Boolean.parseBoolean((String) item.getData("updated"));
			boolean checked = item.getChecked();
			GobiiProcessType gobiiProcessType = GobiiProcessType.CREATE;
			if(!checked) continue;
			else if(checked && vendorProtocolId != null && !updated) continue;
			else if(vendorProtocolId==null)
				gobiiProcessType = GobiiProcessType.CREATE;
			else if(updated)
				gobiiProcessType = GobiiProcessType.UPDATE;
			
			try {
				// get organization details
				OrganizationDTO organizationDTO = new OrganizationDTO();
				organizationDTO.setOrganizationId(organizationId);
				RestUri restUriOrganizationForGetById = App
						.INSTANCE.getUriFactory()
						.resourceByUriIdParam(ServiceRequestId.URL_ORGANIZATION);
				restUriOrganizationForGetById.setParamValue("id", organizationId.toString());
				GobiiEnvelopeRestResource<OrganizationDTO> restResourceForGetById = new GobiiEnvelopeRestResource<>(restUriOrganizationForGetById);
				PayloadEnvelope<OrganizationDTO> resultEnvelopeForGetByID = restResourceForGetById
						.get(OrganizationDTO.class);
				if(!Controller.getDTOResponse(shell, resultEnvelopeForGetByID.getHeader(), memInfo, false)){
					return;
				}
				organizationDTO = resultEnvelopeForGetByID.getPayload().getData().get(0);
				for(int i=organizationDTO.getVendorProtocols().size()-1; i>=0; i--)
					organizationDTO.getVendorProtocols().remove(i);
				
				// create vendor-protocol details
				VendorProtocolDTO vendorProtocolDTO = new VendorProtocolDTO(organizationDTO.getOrganizationId(),
	                    protocolId,
	                    vendorProtocolName);
				vendorProtocolDTO.setId(vendorProtocolId);
				
	            organizationDTO.getVendorProtocols().add(vendorProtocolDTO);
				RestUri restUriProtocoLVendor = ClientContext.getInstance(null, false)
				        .getUriFactory()
				        .childResourceByUriIdParam(ServiceRequestId.URL_PROTOCOL,
				                ServiceRequestId.URL_VENDORS);
				restUriProtocoLVendor.setParamValue("id", protocolId.toString());
				GobiiEnvelopeRestResource<OrganizationDTO> protocolVendorResource =
	                    new GobiiEnvelopeRestResource<>(restUriProtocoLVendor);
				PayloadEnvelope<OrganizationDTO> vendorPayloadEnvelope =
	                    new PayloadEnvelope<>(organizationDTO, gobiiProcessType);
				PayloadEnvelope<OrganizationDTO> protocolVendorResult = gobiiProcessType == GobiiProcessType.CREATE
	                    ? protocolVendorResource.post(OrganizationDTO.class, vendorPayloadEnvelope)
	                    : protocolVendorResource.put(OrganizationDTO.class, vendorPayloadEnvelope);
				if(!Controller.getDTOResponse(shell, protocolVendorResult.getHeader(), memInfo, false)){
					item.setBackground(new Display().getSystemColor(SWT.COLOR_RED));
				};
			} catch (Exception err) {
				Utils.log(shell, memInfo, log, "Error saving vendor-protocols", err);
			}
		}
	}
	
	private void setProtocolDetails(ProtocolDTO newProtocolDto) {
		try{
			String name = txtName.getText();
			newProtocolDto.setName(name);
			newProtocolDto.setDescription(memDescription.getText());
			newProtocolDto.setPlatformId(Integer.parseInt((String) cbPlatform.getData(cbPlatform.getText())));
			newProtocolDto.setCreatedBy(App.INSTANCE.getUser().getUserId());
			newProtocolDto.setCreatedDate(new Date());
			newProtocolDto.setModifiedBy(App.INSTANCE.getUser().getUserId());
			newProtocolDto.setModifiedDate(new Date());
			newProtocolDto.setStatus(1);
			
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Platform", err);
		}
	}
}
