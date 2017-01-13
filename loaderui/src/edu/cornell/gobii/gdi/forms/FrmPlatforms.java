package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestPlatform;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.container.PlatformDTO;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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

public class FrmPlatforms extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmPlatforms.class.getName());
	private Text txtName;
	private Text txtCode;
	private Table tbProperties;

	private String config;
	private StyledText memDescription;
	private Combo cbVendor;
	private TableViewer viewerProperties;
	private Combo cbType;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmPlatforms(final Shell shell, final Composite parent, int style, final String config) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		this.config = config;
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;
		
		TableColumn tblclmnPlatforms = new TableColumn(tbList, SWT.NONE);
		tblclmnPlatforms.setWidth(250);
		tblclmnPlatforms.setText("Platforms");
		
		Label lblType = new Label(cmpForm, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type:");

		cbType = new Combo(cmpForm, SWT.READ_ONLY);
		cbType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cbType.getSelectionIndex() == -1) return;
				txtName.setText(cbType.getItem(cbType.getSelectionIndex()));
			}
		});
		//		cbType.setEnabled(false);
		cbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getCVByGroup("platform_type"), cbType);

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setEditable(false);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER);
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblVendor = new Label(cmpForm, SWT.NONE);
		lblVendor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVendor.setText("Vendor:");

		cbVendor = new Combo(cmpForm, SWT.NONE);
		cbVendor.setEnabled(false);
		cbVendor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getContactNamesByType("Vendor"), cbVendor);
		new Label(cmpForm, SWT.NONE);
		new Label(cmpForm, SWT.NONE);
//		cbType.addListener (SWT.Selection, new Listener() {
//			public void handleEvent(Event e) {
//				IDs.platformTypeId = Integer.parseInt((String) cbType.getData(cbType.getText()));
//			}
//		});
		Label lblDescription = new Label(cmpForm, SWT.NONE);
		lblDescription.setText("Description:");

		memDescription = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
		memDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

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
		new Label(cmpForm, SWT.NONE);


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
		
		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate(true)) return;
				newPlatform(true);

			}
		});
		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);

		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate(false)) return;
				if(!FormUtils.updateForm(getShell(), "Platform", IDs.platformName)) return;
				newPlatform(false);
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);

		Button btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IDs.platformId=0;
				cleanDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearFields.setText("Clear Fields");
		cbList.setText("Select platform type");
	}

	protected void newPlatform(boolean newPlatform) {
		try{
			PlatformDTO newPlatformDto = null;
			if(newPlatform){
				newPlatformDto = new PlatformDTO(DtoMetaData.ProcessType.CREATE);

			}
			else{
				newPlatformDto = new PlatformDTO(DtoMetaData.ProcessType.UPDATE);
				newPlatformDto.setPlatformId(IDs.platformId);
			}
			try{
				setPlatformDetails(newPlatformDto);
				for(TableItem item : tbProperties.getItems()){
					if(item.getText(1).isEmpty()) continue;
					newPlatformDto.getProperties().add(new EntityPropertyDTO((Integer)item.getData("entityId"), (Integer)item.getData("propertyId"), item.getText(0), item.getText(1)));
				}
				DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();

				PlatformDTO platformDTOResponse = dtoRequestPlatform.process(newPlatformDto);

				if(Controller.getDTOResponse(shell, platformDTOResponse, memInfo)){
					tbList.removeAll();
					cleanDetails();
					if(IDs.platformTypeId==0) populatePlatformsTable();
					else populatePlatformFromSelectedType(newPlatformDto.getTypeId());
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
			String type = cbType.getItem(cbType.getSelectionIndex());
			String name = txtName.getText();
			newPlatformDto.setPlatformCode(name);
			newPlatformDto.setPlatformDescription(memDescription.getText());
			newPlatformDto.setPlatformName(name);
//			newPlatformDto.setPlatformVendor(Integer.parseInt((String) cbVendor.getData(cbVendor.getText())));
			newPlatformDto.setTypeId(Integer.parseInt((String) cbType.getData(cbType.getText())));
			newPlatformDto.setCreatedBy(App.INSTANCE.getUser().getUserId());
			newPlatformDto.setCreatedDate(new Date());
			newPlatformDto.setModifiedBy(App.INSTANCE.getUser().getUserId());
			newPlatformDto.setModifiedDate(new Date());
			newPlatformDto.setStatus(1);
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
//		FormUtils.entrySetToCombo(Controller.getCVByGroup("platform_type"), cbList);
		populatePlatformsTable();
//		cbList.addListener (SWT.Selection, new Listener() {
//			public void handleEvent(Event e) {
//				cleanDetails();
//				String selected = cbList.getText(); //single selection
//				cbType.setText(selected);
//				IDs.platformTypeId = Integer.parseInt((String) cbList.getData(selected));
//				populatePlatformFromSelectedType(IDs.platformTypeId); //retrieve and display projects by contact Id
//			}
//		});

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				IDs.platformName = selected;
				IDs.platformId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populatePlatformDetails(IDs.platformId); //retrieve and display projects by contact Id
			}

			protected void populatePlatformDetails(int platformId) {
				try{
					cleanDetails();
					DtoRequestPlatform dtoRequestPlatform = new DtoRequestPlatform();
					PlatformDTO PlatformDTORequest = new PlatformDTO();
					PlatformDTORequest.setPlatformId(platformId);
					try {
						PlatformDTO platformDTOResponse = dtoRequestPlatform.process(PlatformDTORequest);
						txtName.setText(platformDTOResponse.getPlatformName());
						txtCode.setText(platformDTOResponse.getPlatformCode());
//						FormUtils.entrySetToComboSelectId(Controller.getContactNamesByType("Vendor"), cbVendor, platformDTOResponse.getPlatformVendor());

						FormUtils.entrySetToComboSelectId(Controller.getCVByGroup("platform_type"), cbType, platformDTOResponse.getTypeId());
						memDescription.setText(platformDTOResponse.getPlatformDescription());

//						FormUtils.entrySetToTable(Controller.getCVByGroup("platform_prop"), tbProperties);
						populateTableFromStringList(platformDTOResponse.getProperties(), tbProperties);
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
				}

			}
		});
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
			txtName.setText("");
			txtCode.setText("");
			cbVendor.deselectAll();
			cbVendor.setText("");
			memDescription.setText("");
			cbType.deselectAll();
			cbType.setText("");
			for(TableItem item : tbProperties.getItems()){
					item.setText(1, "");
				}
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
		if(txtName.getText().isEmpty()){
			message = "Name field is required!";
			successful = false;
		}else if(cbType.getText().isEmpty()){
			message = "Please specify the platform type!";
			successful = false;
		}else{
			if(isNew){
				for(int i=0; i<tbList.getItemCount(); i++){
					if(tbList.getItem(i).getText(0).equals(txtName.getText())){
						successful = false;
						message = "Name of analysis already exists for this Platform Type";
						break;
					}
				}
			}else if(!isNew && IDs.platformId==0){
				message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
				successful = false;
			}
		}
		if(!successful){
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
}
