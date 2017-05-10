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
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;

public class FrmProtocol  extends AbstractFrm {

	private static Logger log = Logger.getLogger(FrmProtocol.class.getName());
	private Text txtName;
	private Combo cbPlatform;
	private StyledText memDescription;
	private Table tbVendorProtocol;
	private Button btnApply;
	protected int newlyCheckedItems=0;
	protected boolean isNameChanged = false;
	private int currentPlatformId=0;
	private int currentProtocolId=0;
	public FrmProtocol(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cmpForm.setLayout(new GridLayout(2, false));

		lblCbList.setText("Platforms:");
		
		Label lblprotocolName = new Label(cmpForm, SWT.NONE);
		lblprotocolName.setText("*Protocol Name:");
		lblprotocolName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtName.addModifyListener(new ModifyListener() {
			/** {@inheritDoc} */
			public void modifyText(ModifyEvent e) {
				// Handle event
				if (currentProtocolId>0 && !isNameChanged){
					refreshVendorProtocolGroup();
					isNameChanged = true;
					btnApply.setEnabled(false);
				}
			}
		});
		
		Label lblPlatform = new Label(cmpForm, SWT.NONE);
		lblPlatform.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlatform.setText("Platform:");

		cbPlatform = new Combo(cmpForm, SWT.NONE);
		cbPlatform.setEnabled(false);
		cbPlatform.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		populateDisabledComboPlatform();

		Label lblNewLabel = new Label(cmpForm, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 46;
		gd_lblNewLabel.heightHint = 16;
		lblNewLabel.setLayoutData(gd_lblNewLabel);

		memDescription = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
		GridData gd_memDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_memDescription.heightHint = 76;
		memDescription.setLayoutData(gd_memDescription);
		new Label(cmpForm, SWT.NONE);

		Button btnAdd = new Button(cmpForm, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate(true)) return;
				newProtocol(true);
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAdd.setText("Add New Protocol");
		new Label(cmpForm, SWT.NONE);

		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate(false)) return;
				if(!FormUtils.updateForm(getShell(), "Protocol", selectedName)) return;
				newProtocol(false);
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update Protocol");
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
		new Label(cmpForm, SWT.NONE);

		Group grpUpdateProtocolvendor = new Group(cmpForm, SWT.BORDER);
		grpUpdateProtocolvendor.setText("Update Vendor-Protocols");
		grpUpdateProtocolvendor.setLayout(new GridLayout(8, false));
		grpUpdateProtocolvendor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3));

		tbVendorProtocol = new Table(grpUpdateProtocolvendor, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		tbVendorProtocol.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		tbVendorProtocol.addSelectionListener(new SelectionAdapter() {
			private TableEditor editor;

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null || currentProtocolId==0)
					return;

				if(e.detail == SWT.CHECK){
					if((boolean) item.getData("checked")){
						item.setChecked(true);
					}else{
						if(item.getChecked() && !isNameChanged){
							newlyCheckedItems++;
							String protocolName = txtName.getText().isEmpty() ? "" : txtName.getText();
							String vendorName = item.getText(0).replaceAll(" ", "_");
							item.setText(1, vendorName+"_"+protocolName);
							btnApply.setEnabled(true);
						}else{
							item.setText(1, "");
							newlyCheckedItems--;
							if(newlyCheckedItems==0) btnApply.setEnabled(false);
						}
					}

				}else {
					if(item.getChecked()){
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
								if(!newEditor.getText().equals(item.getText(EDITABLECOLUMN))){
									item.setData("updated", "true");
									enableApplyButton();
								}
								item.setText(EDITABLECOLUMN, newEditor.getText());
								newEditor.dispose();
								break;
							case SWT.Traverse:
								switch (e.detail) {
								case SWT.TRAVERSE_RETURN:
									if(!newEditor.getText().equals(item.getText(EDITABLECOLUMN))){
									item.setData("updated", "true");
									enableApplyButton();
									}
									item.setText(EDITABLECOLUMN, newEditor.getText());
									// FALL THROUGH
								case SWT.TRAVERSE_ESCAPE:
									newEditor.dispose();
									e.doit = false;
								}
								break;
							}
						}

						private void enableApplyButton() {
							// TODO Auto-generated method stub
							if((boolean)item.getData("checked") && !isNameChanged){
								item.setData("updated", "true");
								newlyCheckedItems++;
								btnApply.setEnabled(true);
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
			}
		});
		tbVendorProtocol.setHeaderVisible(true);
		tbVendorProtocol.setLinesVisible(true);

		TableColumn tblclmnVendor = new TableColumn(tbVendorProtocol, SWT.NONE);
		tblclmnVendor.setWidth(207);
		tblclmnVendor.setText("Vendor");

		TableColumn tblclmnVendorprotocol = new TableColumn(tbVendorProtocol, SWT.NONE);
		tblclmnVendorprotocol.setWidth(150);
		tblclmnVendorprotocol.setText("Vendor-Protocol");

		FormUtils.entrySetToTable(Controller.getOrganizationNames(), tbVendorProtocol);

		btnApply = new Button(grpUpdateProtocolvendor, SWT.NONE);
		btnApply.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				if( currentProtocolId==0){
					String message = "'"+txtName.getText()+"' is recognized as a new entry. Please Add or Select a protocol first.";
					dialog.setMessage(message);
					dialog.open();

				}else{
					String confirmationMessage = "Do you want to update "+ txtName.getText() +" with the Protocol-Vendor changes?";
					if(MessageDialog.openConfirm(shell, "Confirm", confirmationMessage)){
						newVendorProtocol(currentProtocolId);
						tbList.removeAll();
						populateProtocolDetails(currentProtocolId);
						if(currentPlatformId==0) populatePlatformsAndProtocols();
						else populateProtocolListFromSelectedPlatform(currentPlatformId);
					}
				}
			}
		});
		btnApply.setEnabled(false);
		btnApply.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 8, 1));
		btnApply.setText("Apply");
		new Label(cmpForm, SWT.NONE);
		new Label(cmpForm, SWT.NONE);
		
		TableColumn tblclmnProtocols = new TableColumn(tbList, SWT.NONE);
		tblclmnProtocols.setWidth(200);
		tblclmnProtocols.setText("Protocols");
		
		// TODO Auto-generated constructor stub
	}

	private void populateDisabledComboPlatform() {
		// TODO Auto-generated method stub
		if(currentPlatformId > 0)
			FormUtils.entrySetToComboSelectId(Controller.getPlatformNames(), cbPlatform, currentPlatformId);
		else
			FormUtils.entrySetToCombo(Controller.getPlatformNames(), cbPlatform);
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
				populateDisabledComboPlatform();
			}
		});

		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try{
					cleanDetails();
					String selected = cbList.getText(); //single selection
					cbPlatform.select(cbPlatform.indexOf(selected));
					currentPlatformId = Integer.parseInt((String) cbList.getData(selected));
					isNameChanged = false;
					populateProtocolListFromSelectedPlatform(currentPlatformId ); //retrieve and display projects by contact Id
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving protocols", err);
				}
			}
		});

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				currentProtocolId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateProtocolDetails(currentProtocolId); //retrieve and display projects by contact Id
			}

			
		});
	}
	
	private void populateProtocolDetails(int protocolId) {
		try{
			cleanDetails();
			try {
				PayloadEnvelope<ProtocolDTO> resultEnvelopeForGetByID = Controller.getProtocolDetails(protocolId);

				if(Controller.getDTOResponse(shell, resultEnvelopeForGetByID.getHeader(), memInfo, false)){
					ProtocolDTO protocolDTOResponse = resultEnvelopeForGetByID.getPayload().getData().get(0);
					selectedName = protocolDTOResponse.getName();
					txtName.setText(protocolDTOResponse.getName());

					isNameChanged = false;
					FormUtils.entrySetToComboSelectId(Controller.getPlatformNames(), cbPlatform, protocolDTOResponse.getPlatformId());
					memDescription.setText(protocolDTOResponse.getDescription());

					for(VendorProtocolDTO vpDTO : protocolDTOResponse.getVendorProtocols()){
						for(TableItem item : tbVendorProtocol.getItems()){
							Integer organizationId = Integer.parseInt((String) item.getData(item.getText(0)));
							if(vpDTO.getOrganizationId().equals(organizationId) && vpDTO.getProtocolId().equals(protocolId)){
								item.setText(1, vpDTO.getName());
								item.setChecked(true);
								item.setGrayed(true);
								item.setData("vendorProtocolId", vpDTO.getId());
								item.setData("updated", "false");
								item.setData("checked", true);
								item.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
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
	
	private void populatePlatformsAndProtocols() {
		try{
			cbList.setText("*Select a Platform");
//			tbList.removeAll();
			if(currentPlatformId > 0){
				FormUtils.entrySetToComboSelectId(Controller.getPlatformNames(), cbList, currentPlatformId);
				FormUtils.entrySetToTable(Controller.getProtocolNamesByPlatformId(currentPlatformId), tbList);
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
			btnApply.setEnabled(false);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects", err);
		}
	}

	private void cleanDetails(){
		try{
			//			currentProtocolId = 0;
			txtName.setText("");
			cbPlatform.deselectAll(); cbPlatform.setText("");
			memDescription.setText("");
			
			refreshVendorProtocolGroup();
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	private void refreshVendorProtocolGroup() {
		// TODO Auto-generated method stub
		tbVendorProtocol.removeAll();
		FormUtils.entrySetToTable(Controller.getOrganizationNames(), tbVendorProtocol);
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
		}else if(!isNew && currentProtocolId==0){
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
			
			
			if(newProtocol){
				payloadEnvelope = new PayloadEnvelope<>(newProtocolDto, GobiiProcessType.CREATE);
				restResource = new GobiiEnvelopeRestResource<>(App
						.INSTANCE.getUriFactory()
						.resourceColl(ServiceRequestId.URL_PROTOCOL));
				protocolDTOResponseEnvelope = restResource.post(ProtocolDTO.class,
						payloadEnvelope);
			}
			else{
				newProtocolDto.setProtocolId(currentProtocolId);
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
					
					currentProtocolId = protocolDTOResponseEnvelope.getPayload().getData().get(0).getId();
					if(currentPlatformId==0) populatePlatformsAndProtocols();
					else   populateProtocolListFromSelectedPlatform(currentPlatformId);
					btnApply.setEnabled(false);
					isNameChanged = false;
					selectedName = newProtocolDto.getName();
					FormUtils.selectRowById(tbList,currentProtocolId);
				}
			}catch(Exception err){
				Utils.log(shell, memInfo, log, "Error saving Platform", err);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Platform", err);
		}
	}

	public void newVendorProtocol(Integer protocolId){
		
		Boolean success = true;
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
					success = false;
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
							success = false;
							item.setBackground(new Display().getSystemColor(SWT.COLOR_RED));
						}
							
						
						
			} catch (Exception err) {
				Utils.log(shell, memInfo, log, "Error saving vendor-protocols", err);
			}
		}
		if(success){
				MessageBox dialog;
				dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				dialog.setText("Request information");
				dialog.setMessage("Request processed successfully!");
				dialog.open();
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
