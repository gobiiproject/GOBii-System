package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.MapsetDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class FrmMapset extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmMapset.class.getName());
	private Text txtName;
	private Text txtCode;
	private Table tbProperties;

	private String config;
	private Combo cbReference;
	private Combo cbMapType;
	private StyledText memoDescription;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmMapset(final Shell shell, Composite parent, int style, final String config) {
		super(shell, parent, style);
		this.config = config;

		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Mapset Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(cmpForm, SWT.NONE);
		label.setText(" ");
		
				memoDescription = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
				memoDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		Label lblMapset = new Label(cmpForm, SWT.NONE);
		lblMapset.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		lblMapset.setText("Mapset");

		Label lblDescription = new Label(cmpForm, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description:");

		Label lblReference = new Label(cmpForm, SWT.NONE);
		lblReference.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReference.setText("Reference:");

		cbReference = new Combo(cmpForm, SWT.READ_ONLY);
		cbReference.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getReferenceNames(), cbReference);

		Label lblMapType = new Label(cmpForm, SWT.NONE);
		lblMapType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMapType.setText("*Map type:");

		cbMapType = new Combo(cmpForm, SWT.READ_ONLY);
		cbMapType.setEnabled(false);
		cbMapType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		populateMapTypesCombo(cbMapType);
		FormUtils.entrySetToCombo(Controller.getMapTypes(), cbMapType);

		Label lblProperties = new Label(cmpForm, SWT.NONE);
		lblProperties.setText("Properties:");

		tbProperties = new Table(cmpForm, SWT.BORDER | SWT.FULL_SELECTION);
		tbProperties.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbProperties.setHeaderVisible(true);
		tbProperties.setLinesVisible(true);
		FormUtils.entrySetToTable(Controller.getCVByGroup("mapset_prop"), tbProperties);
		tbProperties.addSelectionListener( new SelectionAdapter() {

			private TableEditor editor;

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Clean up any previous editor control
				editor = new TableEditor(tbProperties);
				// The editor must have the same size as the cell and must
				// not be any smaller than 50 pixels.
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				editor.minimumWidth = 50;
				Control oldEditor = editor.getEditor();
				if (oldEditor != null)
					oldEditor.dispose();                

				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				// The control that will be the editor must be a child of the
				// Table
				Text newEditor = new Text(tbProperties, SWT.NONE);
				final int EDITABLECOLUMN=1;
				newEditor.setText(item.getText(EDITABLECOLUMN));
				Listener textListener = new Listener() {
					public void handleEvent(final Event e) {
						switch (e.type) {
						case SWT.FocusOut:
							item.setText(EDITABLECOLUMN, newEditor.getText());
							newEditor.dispose();
							break;
						case SWT.Traverse:
							switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								item
								.setText(EDITABLECOLUMN, newEditor.getText());
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
		});

		TableColumn tblclmnProperty = new TableColumn(tbProperties, SWT.NONE);
		tblclmnProperty.setWidth(150);
		tblclmnProperty.setText("Property");

		TableColumn tblclmnValue = new TableColumn(tbProperties, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		new Label(cmpForm, SWT.NONE);

		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;

					MapsetDTO mapsetDTO = new MapsetDTO();
					mapsetDTO.setName(txtName.getText());
					mapsetDTO.setCode(cbList.getText()+"_"+txtName.getText().replaceAll(" ", "_"));
					mapsetDTO.setDescription(memoDescription.getText() == null ? "" : memoDescription.getText());
					if(cbReference.getSelectionIndex() > -1){
						String key = (String) cbReference.getData(cbReference.getText());
						mapsetDTO.setReferenceId(Integer.parseInt(key));
					}
					mapsetDTO.setMapType(IDs.mapTypeId);
					for(TableItem item : tbProperties.getItems()){
						if(!item.getText(1).isEmpty()){
							Integer id = Integer.parseInt((String) item.getData(item.getText(0)));
							String name = item.getText(0);
							String value = item.getText(1);
							EntityPropertyDTO prop = new EntityPropertyDTO(null, id, name, value);
							mapsetDTO.getProperties().add(prop);
						}
					}
					mapsetDTO.setCreatedBy(1);
					mapsetDTO.setModifiedBy(1);
					mapsetDTO.setCreatedDate(new Date());
					mapsetDTO.setModifiedDate(new Date());
					mapsetDTO.setStatusId(1);
					
					try{
					PayloadEnvelope<MapsetDTO> payloadEnvelope = new PayloadEnvelope<>(mapsetDTO,
							GobiiProcessType.CREATE);
					GobiiEnvelopeRestResource<MapsetDTO> restResource = new GobiiEnvelopeRestResource<>(App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_MAPSET));
					PayloadEnvelope<MapsetDTO> mapsetDTOResponse = restResource.post(MapsetDTO.class,
							payloadEnvelope);

						if(Controller.getDTOResponse(shell, mapsetDTOResponse.getHeader(), memInfo, true)){
							populateMapsetFromSelectedMapType(cbList.getText());
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error saving Mapset", err);
					}
				}catch (Exception err) {
					Utils.log(shell, memInfo, log, "Error saving Mapset", err);
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
					if(!FormUtils.updateForm(getShell(), "Mapset", IDs.mapsetName)) return;
					MapsetDTO mapsetDTO = new MapsetDTO();
					mapsetDTO.setMapsetId(IDs.mapsetId);
					mapsetDTO.setName(txtName.getText());
					mapsetDTO.setCode(cbList.getText()+"_"+txtName.getText().replaceAll(" ", "_"));
					mapsetDTO.setDescription(memoDescription.getText() == null ? "" : memoDescription.getText());
					if(cbReference.getSelectionIndex() > -1){
						String key = (String) cbReference.getData(cbReference.getText());
						mapsetDTO.setReferenceId(Integer.parseInt(key));
					}
					mapsetDTO.setMapType(IDs.mapTypeId);
					for(TableItem item : tbProperties.getItems()){
						if(!item.getText(1).isEmpty()){
							Integer id = Integer.parseInt((String) item.getData(item.getText(0)));
							String name = item.getText(0);
							String value = item.getText(1);
							EntityPropertyDTO prop = new EntityPropertyDTO(null, id, name, value);
							mapsetDTO.getProperties().add(prop);
						}
					}
					mapsetDTO.setCreatedBy(1);
					mapsetDTO.setModifiedBy(1);
					mapsetDTO.setCreatedDate(new Date());
					mapsetDTO.setModifiedDate(new Date());
					mapsetDTO.setStatusId(1);
					try{
						RestUri restUri = App.INSTANCE.getUriFactory().resourceByUriIdParam(ServiceRequestId.URL_MAPSET);
						restUri.setParamValue("id", Integer.toString(IDs.mapsetId));
						GobiiEnvelopeRestResource<MapsetDTO> restResourceById = new GobiiEnvelopeRestResource<>(restUri);
						restResourceById.setParamValue("id", mapsetDTO.getMapsetId().toString());
						PayloadEnvelope<MapsetDTO> mapsetDTOResponseEnvelope = restResourceById.put(
								MapsetDTO.class, new PayloadEnvelope<>(mapsetDTO, GobiiProcessType.UPDATE));
						
						if(Controller.getDTOResponse(shell, mapsetDTOResponseEnvelope.getHeader(), memInfo, true)){
							populateMapsetFromSelectedMapType(cbList.getText());
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error saving Mapset", err);
					}
				}catch (Exception err) {
					Utils.log(shell, memInfo, log, "Error saving Mapset", err);
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
		new Label(cmpForm, SWT.NONE);

		Button btnMarkerWizard = new Button(cmpForm, SWT.FLAT);
		btnMarkerWizard.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnMarkerWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateMarkerWizard(shell, config);
			}
		});
		btnMarkerWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnMarkerWizard.setText("Marker Wizard");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		try{
			FormUtils.entrySetToCombo(Controller.getMapTypes(), cbList);
			cbList.setText("*Select mapset type");
			FormUtils.entrySetToTable(Controller.getMapNames(), tbList);

			TableColumn tblclmnMapsets = new TableColumn(tbList, SWT.NONE);
			tblclmnMapsets.setWidth(300);
			tblclmnMapsets.setText("Mapsets:");
			cbList.addListener (SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					String selected = cbList.getText(); //single selection
					cbMapType.select(cbMapType.indexOf(selected));
					cleanDetails();
					populateMapsetFromSelectedMapType(selected);
				}
			});
			tbList.addListener (SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					String selected = tbList.getSelection()[0].getText(); //single selection
					IDs.mapsetName = selected;
					IDs.mapsetId= Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
					populateMapsetDetails(IDs.mapsetId);
				}


			});

			btnRefresh.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					tbList.removeAll();
					Integer id = FormUtils.getIdFromFormList(cbList);
					if(id>0){
						cleanDetails();
						populateMapsetFromSelectedMapType(cbList.getText());
						FormUtils.entrySetToComboSelectId(Controller.getMapTypes(), cbList, id);
					}
					else{
						FormUtils.entrySetToCombo(Controller.getMapTypes(), cbList);
						FormUtils.entrySetToTable(Controller.getMapNames(), tbList);
					}

					FormUtils.entrySetToCombo(Controller.getReferenceNames(), cbReference);
					cleanDetails();
				}
			});
		}catch (Exception err) {
			Utils.log(shell, memInfo, log, "Error retrieving Mapsets", err);
		}
	}

	protected void populateMapsetFromSelectedMapType(String selected){
		try{
			IDs.mapTypeId = Integer.parseInt((String) cbList.getData(selected));
			populateMapsetFromSelectedMapType(IDs.mapTypeId);
		}catch (Exception err) {
			Utils.log(shell, memInfo, log, "Error retrieving Mapsets", err);
		}
	}

	protected void populateMapsetFromSelectedMapType(Integer selected){
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getMapNamesByTypeId(selected), tbList);
		}catch (Exception err) {
			Utils.log(shell, memInfo, log, "Error retrieving Mapsets", err);
		}
	}

	protected void cleanDetails() {
		try{
			txtName.setText("");
			txtCode.setText("");
			memoDescription.setText("");
			cbReference.deselectAll();
			for(TableItem item : tbProperties.getItems()){
				item.setText(1, "");
			}
		}catch (Exception err) {
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	protected void populateMapsetDetails(int mapsetId) {
		cleanDetails();
		MapsetDTO MapsetDTORequest = new MapsetDTO();
		MapsetDTORequest.setMapsetId(IDs.mapsetId);
		try {
			
			RestUri restUri = App.INSTANCE.getUriFactory().resourceByUriIdParam(ServiceRequestId.URL_MAPSET);
			restUri.setParamValue("id", Integer.toString(mapsetId));
			GobiiEnvelopeRestResource<MapsetDTO> restResource = new GobiiEnvelopeRestResource<>(restUri);
			PayloadEnvelope<MapsetDTO> dtoRequestMapset = restResource.get(MapsetDTO.class);
			
			MapsetDTO mapsetDTOResponse = dtoRequestMapset.getPayload().getData().get(0);
			if(mapsetDTOResponse.getReferenceId() != null){
				FormUtils.entrySetToComboSelectId(Controller.getReferenceNames(), cbReference, mapsetDTOResponse.getReferenceId());
			}
			selectedName = mapsetDTOResponse.getName();
			txtName.setText(mapsetDTOResponse.getName());
			txtCode.setText(mapsetDTOResponse.getCode());
			memoDescription.setText(mapsetDTOResponse.getDescription());
			for(TableItem item : tbProperties.getItems()){
				for(EntityPropertyDTO prop : mapsetDTOResponse.getProperties()){
					String key = (String) item.getData(item.getText(0));
					if(prop.getPropertyId().equals(Integer.parseInt(key))){
						item.setText(1, prop.getPropertyValue());
						break;
					}
				}
			}
//			}
		} catch (Exception err) {
			Utils.log(shell, memInfo, log, "Error retrieving Mapset details", err);
		}
	}

	private void populateMapTypesCombo(Combo combo) {
		try{
			FormUtils.entrySetToCombo(Controller.getMapTypes(), combo);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Mapset types", err);
		}
	}

	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(cbList.getSelectionIndex() < 0){
			successful = false;
			message = "Please select a Map Type!";
		}else if(txtName.getText().isEmpty()){
			successful = false;
			message = "Name is a required field!";
		}else if(!isNew && IDs.mapsetId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else if(isNew|| !txtName.getText().equalsIgnoreCase(selectedName)){
			for(TableItem item : tbList.getItems()){
				if(item.getText().equalsIgnoreCase(txtName.getText())){
					successful = false;
					message = "Mapset name already exists for this Map Type!";
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
