package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.custom.CCombo;

public class FrmPlatforms extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmPlatforms.class.getName());
	private Text txtCode;
	private Table tbProperties;

	private StyledText memDescription;
	private TableViewer viewerProperties;
	private String comboSelectedName;
	private Button btnUpdate;
	private CCombo nameCombo;
	private int currentPlatformId=0;
	private int currentPlatformTypeId=0;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmPlatforms(final Shell shell, final Composite parent, int style, final String config) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		TableColumn tblclmnPlatforms = new TableColumn(tbList, SWT.NONE);
		tblclmnPlatforms.setWidth(250);
		tblclmnPlatforms.setText("Platforms");
		
		Label lbltype = new Label(cmpForm, SWT.NONE);
		lbltype.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbltype.setText("*Platform Name:");
		
		nameCombo = new CCombo(cmpForm, SWT.BORDER);
		nameCombo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		nameCombo.setEditable(false);
		nameCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getCVByGroup("platform_type"), nameCombo);
		nameCombo.setText("*Select Platform");
		
		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);
		
				memDescription = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
				memDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		Label lblDescription = new Label(cmpForm, SWT.NONE);
		lblDescription.setText("Description:");
		
				Label lblProperties = new Label(cmpForm, SWT.NONE);
				lblProperties.setText("Properties:");
		
		
				viewerProperties = new TableViewer(cmpForm, SWT.BORDER | SWT.FULL_SELECTION);
				tbProperties = viewerProperties.getTable();
				tbProperties.setLinesVisible(true);
				tbProperties.setHeaderVisible(true);
				tbProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
						TableViewerColumn tableViewerColumn = new TableViewerColumn(viewerProperties, SWT.NONE);
						TableColumn tblclmnParameter = tableViewerColumn.getColumn();
						tblclmnParameter.setWidth(150);
						tblclmnParameter.setText("Property");
						
								TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewerProperties, SWT.NONE);
								TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
								tblclmnValue.setWidth(100);
								tblclmnValue.setText("Value");
								
								
										tbProperties.addListener(SWT.MouseDown, event -> {
											TableEditor editor = new TableEditor(tbProperties);
											// The editor must have the same size as the cell and must
											// not be any smaller than 50 pixels.
											editor.horizontalAlignment = SWT.LEFT;
											editor.grabHorizontal = true;
											editor.minimumWidth = 50;
											Control oldEditor = editor.getEditor();
											if (oldEditor != null)
												oldEditor.dispose(); 
								
											Point pt = new Point(event.x, event.y);
											TableItem item = tbProperties.getItem(pt);
											if (item == null)
												return;
								
											Text newEditor = new Text(tbProperties, SWT.NONE);
											int EDITABLECOLUMN = -1;
											for (int i = 1; i < tbProperties.getColumnCount(); i++) {
												Rectangle rect = item.getBounds(i);
												if (rect.contains(pt)) {
													EDITABLECOLUMN = i;
													break;
												}
											}
								
											final int col = EDITABLECOLUMN;
											if(col < 0) return;
											newEditor.setText(item.getText(col));
											Listener textListener = new Listener() {
												public void handleEvent(final Event e) {
													switch (e.type) {
													case SWT.FocusOut:
														item.setText(col, newEditor.getText());
														newEditor.dispose();
														break;
													case SWT.Traverse:
														switch (e.detail) {
														case SWT.TRAVERSE_RETURN:
															item
															.setText(col, newEditor.getText());
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
											editor.setEditor(newEditor, item, col); 
										});
										FormUtils.entrySetToTable(Controller.getCVByGroup("platform_prop"), tbProperties);
		new Label(cmpForm, SWT.NONE);
		
				Button btnAddNew = new Button(cmpForm, SWT.NONE);
				btnAddNew.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
					
						if(!validate(true)) return;
						newPlatform(true);

					}
				});
				btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);
		
				btnUpdate = new Button(cmpForm, SWT.NONE);
				btnUpdate.setEnabled(false);
				btnUpdate.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(!validate(false)) return;
						if(!FormUtils.updateForm(getShell(), "Platform", selectedName)) return;
						newPlatform(false);
					}
				});
				btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);
		
				Button btnClearFields = new Button(cmpForm, SWT.NONE);
				btnClearFields.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						currentPlatformId=0;
						FormUtils.entrySetToCombo(Controller.getCVByGroup("platform_type"), nameCombo);
						nameCombo.setText("*Select Platform");
						cleanDetails();
					}
				});
				btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				btnClearFields.setText("Clear Fields");
	}

	protected void newPlatform(boolean newPlatform) {
		try{

			PayloadEnvelope<PlatformDTO> payloadEnvelope, platformDTOResponseEnvelope;
			PlatformDTO newPlatformDto = new PlatformDTO();
			GobiiEnvelopeRestResource<PlatformDTO> restResource = null;
			setPlatformDetails(newPlatformDto);
			for(TableItem item : tbProperties.getItems()){
				if(item.getText(1).isEmpty()) continue;
				newPlatformDto.getProperties().add(new EntityPropertyDTO((Integer)item.getData("entityId"), (Integer)item.getData("propertyId"), item.getText(0), item.getText(1)));

			}
			
			if(newPlatform){
				payloadEnvelope = new PayloadEnvelope<>(newPlatformDto, GobiiProcessType.CREATE);
				restResource = new GobiiEnvelopeRestResource<>(App
						.INSTANCE.getUriFactory()
						.resourceColl(ServiceRequestId.URL_PLATFORM));
				platformDTOResponseEnvelope = restResource.post(PlatformDTO.class,
						payloadEnvelope);
			}
			else{
				newPlatformDto.setPlatformId(currentPlatformId);
				payloadEnvelope = new PayloadEnvelope<>(newPlatformDto, GobiiProcessType.UPDATE);
				RestUri restUriPlatformForGetById = App
						.INSTANCE.getUriFactory()
						.resourceByUriIdParam(ServiceRequestId.URL_PLATFORM);
				restUriPlatformForGetById.setParamValue("id", newPlatformDto.getPlatformId().toString());
				restResource = new GobiiEnvelopeRestResource<>(restUriPlatformForGetById);
				platformDTOResponseEnvelope = restResource.put(PlatformDTO.class,
						payloadEnvelope);
			}
			try{
				if(Controller.getDTOResponse(shell, platformDTOResponseEnvelope.getHeader(), memInfo, true)){
					tbList.removeAll();
					if(currentPlatformTypeId==0) populatePlatformsTable();
					else populatePlatformFromSelectedType(newPlatformDto.getTypeId());
					currentPlatformId=platformDTOResponseEnvelope.getPayload().getData().get(0).getId();
					populatePlatformDetails(currentPlatformId);
					btnUpdate.setEnabled(true);
					FormUtils.selectRowById(tbList,currentPlatformId);
				};
			}catch(Exception err){
				Utils.log(shell, memInfo, log, "Error savging Platform", err);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Platform", err);
		}
	}

	private void setPlatformDetails(PlatformDTO newPlatformDto) {
		try{
			comboSelectedName = nameCombo.getText();
			newPlatformDto.setPlatformName(comboSelectedName);
			Integer typeId = Integer.parseInt((String) nameCombo.getData(comboSelectedName));
			newPlatformDto.setTypeId(typeId);
			newPlatformDto.setPlatformCode(comboSelectedName);
			newPlatformDto.setPlatformDescription(memDescription.getText());
			newPlatformDto.setCreatedBy(App.INSTANCE.getUser().getUserId());
			newPlatformDto.setCreatedDate(new Date());
			newPlatformDto.setModifiedBy(App.INSTANCE.getUser().getUserId());
			newPlatformDto.setModifiedDate(new Date());
			newPlatformDto.setStatusId(1);
			
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Platform", err);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		populatePlatformsTable();

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbList.removeAll();

				FormUtils.entrySetToCombo(Controller.getCVByGroup("platform_type"), nameCombo);
				nameCombo.setText("*Select Platform");
				populatePlatformsTable();
				cleanDetails();
				
			}
		});

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				selectedName = selected;
				currentPlatformId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populatePlatformDetails(currentPlatformId); //retrieve and display projects by contact Id
				btnUpdate.setEnabled(true);
			}

			
		});
	}
	private void populatePlatformDetails(int platformId) {
		try{
			cleanDetails();
			
			try {
				PayloadEnvelope<PlatformDTO> resultEnvelopeForGetByID = Controller.getPlatformDetails(platformId);

				if(Controller.getDTOResponse(shell, resultEnvelopeForGetByID.getHeader(), memInfo, false)){
					PlatformDTO platformDTOResponse = resultEnvelopeForGetByID.getPayload().getData().get(0);
					selectedName = platformDTOResponse.getPlatformName();
					txtCode.setText(platformDTOResponse.getPlatformCode());
					memDescription.setText(platformDTOResponse.getPlatformDescription());
					nameCombo.setText(selectedName);
					populateTableFromStringList(platformDTOResponse.getProperties(), tbProperties);
				}
			} catch (Exception err) {
				Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
		}

	}
	private void populateTableFromStringList(List<EntityPropertyDTO> properties, Table table) {
		try{
			for(TableItem item : tbProperties.getItems()){
				Integer index = Integer.parseInt((String) item.getData(item.getText()));
				for (EntityPropertyDTO p : properties){
					if(index.equals(p.getPropertyId())){
						item.setData("entityId", p.getEntityIdId());
						item.setData("propertyId", p.getPropertyId());
						item.setText(1, p.getPropertyValue());
						break;
					}
				}
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Project properties", err);
		}
	}

	protected void populatePlatformFromSelectedType(int platformTypeId) {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getPlatformNamesByTypeId(platformTypeId), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
		}
	}

	protected void cleanDetails() {
		try{
			txtCode.setText("");
			memDescription.setText("");
			for(TableItem item : tbProperties.getItems()){
				item.setText(1, "");
			}
			btnUpdate.setEnabled(false);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	private void populatePlatformsTable() {
		try{
			FormUtils.entrySetToTable(Controller.getPlatformNames(), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
		}
	}

	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		if(nameCombo.getText().equals("*Select Platform")){
			message = "Please select a platform!";
			successful = false;
		}else if(!isNew && currentPlatformId==0){
			message = "'"+nameCombo.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else{
			if(isNew|| !nameCombo.getText().equalsIgnoreCase(selectedName)){
				for(int i=0; i<tbList.getItemCount(); i++){
					if(tbList.getItem(i).getText(0).equalsIgnoreCase(nameCombo.getText())){
						successful = false;
						message = "Name of platform already exists";
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
}
