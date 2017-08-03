package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

public class FrmProjects extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmProjects.class.getName());
	private Text txtName;
	private Combo cbPIContact;
	private Button btnAddNew;
	private Button btnUpdate;
	private Button btnDnaSampleWiz;
	private Table tbProperties;
	private StyledText styledTextDesc;
	private Label lblCode;
	private Text textCode;
	private ModifyListener listener;
	private Button btnAddPlatformExperiment;

	private String config;
	private TableEditor editor;
	private TableColumn tblclmnProperty;
	private TableColumn tblclmnValue;
	private Button btnClearFields;
	private Label label;
	private Label lblProject;
	protected int currentProjectId;
	protected int currentPIid;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmProjects(final Shell shell, final Composite parent, int style, final String config) {
		super(shell, parent, style);
		this.config = config;
		currentProjectId=0;
		currentPIid=0;
		listener = new ModifyListener() {
			/** {@inheritDoc} */
			public void modifyText(ModifyEvent e) {
				// Handle event
				if(cbList.getItems().length>0) btnUpdate.setEnabled(true);
			}
		};


		lblCbList.setText("PI Contacts:");
		cbList.setText("*Select PI contact");

		TableColumn tblColumn = new TableColumn(tbList, SWT.NONE);
		tblColumn.pack();
		tblColumn.setText("Projects:");
		tblColumn.setWidth(300);

		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Project Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtName.addModifyListener((ModifyListener) listener);
		txtName.addFocusListener(new FocusListener() {
    		ToolTip tip = new ToolTip(shell, SWT.BALLOON);
    		
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(cbList.getSelectionIndex()<0){
				
                Point loc = cbList.toDisplay(cbList.getLocation());

        		tip.setMessage("Please select a PI before creating or updating an entry.");
                tip.setLocation(loc.x + cbList.getSize().x , loc.y-cbList.getSize().y/2);
                tip.setVisible(true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				 tip.setVisible(false);
			}
        });
		
		lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		textCode = new Text(cmpForm, SWT.BORDER);
		textCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		textCode.setEditable(false);
		textCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textCode.addModifyListener((ModifyListener) listener);
		
		label = new Label(cmpForm, SWT.NONE);
		label.setText(" ");
		
				styledTextDesc = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
				GridData gd_styledTextDesc = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
				gd_styledTextDesc.heightHint = 43;
				styledTextDesc.setLayoutData(gd_styledTextDesc);
				styledTextDesc.addModifyListener((ModifyListener) listener);
		
		lblProject = new Label(cmpForm, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		lblProject.setText("Project");

		Label lblNewLabel = new Label(cmpForm, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		lblNewLabel.setText("Description:");

		Label lblPiContact = new Label(cmpForm, SWT.NONE);
		lblPiContact.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPiContact.setText("*PI contact:");

		cbPIContact = new Combo(cmpForm, SWT.NONE);
		cbPIContact.setEnabled(false);
		cbPIContact.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		populateContactsList(cbPIContact);

		Label lblProperties = new Label(cmpForm, SWT.NONE);
		lblProperties.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProperties.setText("Properties:");

		tbProperties = new Table(cmpForm, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tbProperties = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tbProperties.heightHint = 165;
		tbProperties.setLayoutData(gd_tbProperties);
		tbProperties.setHeaderVisible(true);
		tbProperties.setLinesVisible(true);

		tblclmnProperty = new TableColumn(tbProperties, SWT.NONE);
		tblclmnProperty.setWidth(100);
		tblclmnProperty.setText("Property");

		tblclmnValue = new TableColumn(tbProperties, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		tbProperties.addSelectionListener( new SelectionAdapter() {

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
		FormUtils.entrySetToTable(Controller.getCVByGroup("project_prop"), tbProperties);

		new Label(cmpForm, SWT.NONE);

		btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PayloadEnvelope<ProjectDTO> resultEnvelope = null;
				try{
					if(!validate(true)) return;
					ProjectDTO projectDTORequest = new ProjectDTO();
					projectDTORequest.setCreatedBy(1);
					projectDTORequest.setProjectName(txtName.getText());
					projectDTORequest.setProjectDescription(styledTextDesc.getText());
					projectDTORequest.setProjectCode(cbPIContact.getItem(cbPIContact.getSelectionIndex())+"_"+txtName.getText());
					projectDTORequest.setProjectStatus(1);
					projectDTORequest.setModifiedBy(1);
					projectDTORequest.setPiContact(FormUtils.getIdFromFormList(cbList));

					for(TableItem item : tbProperties.getItems()){
						if(item.getText(1).isEmpty()) continue;
						projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, item.getText(0), item.getText(1)));
					}
						RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory().resourceColl(GobiiServiceRequestId.URL_PROJECTS);
						GobiiEnvelopeRestResource<ProjectDTO> restResourceForProjects = new GobiiEnvelopeRestResource<>(projectsUri);
						PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(projectDTORequest, GobiiProcessType.CREATE);
						resultEnvelope = restResourceForProjects
								.post(ProjectDTO.class, payloadEnvelope);

				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Project", err);
				}
				
				if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, true)){
					populateProjectsFromSelectedContact(cbList.getText());
					currentProjectId = resultEnvelope.getPayload().getData().get(0).getProjectId();
					populateProjectDetails(currentProjectId);
					FormUtils.selectRowById(tbList, currentProjectId);
					
				};
			}
		});
		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);

		btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(false)) return;
					if(!FormUtils.updateForm(getShell(), "Project", selectedName)) return;
					ProjectDTO projectDTORequest = new ProjectDTO();
					projectDTORequest.setProjectId(currentProjectId);
					projectDTORequest.setCreatedBy(1);
					projectDTORequest.setProjectName(txtName.getText());
					projectDTORequest.setProjectDescription(styledTextDesc.getText());
					projectDTORequest.setProjectCode(cbPIContact.getItem(cbPIContact.getSelectionIndex())+"_"+txtName.getText());
					projectDTORequest.setProjectStatus(1);
					projectDTORequest.setCreatedDate(new Date());
					projectDTORequest.setModifiedBy(1);
					projectDTORequest.setPiContact(currentPIid);

					for(TableItem item : tbProperties.getItems()){
						if(item.getText(1).isEmpty()) continue;
						projectDTORequest.getProperties().add(new EntityPropertyDTO((Integer) item.getData("entityId"), (Integer) item.getData("propertyId"), item.getText(0), item.getText(1)));
						//			        	 System.out.println("Property:" + item.getText(0) +"  Value:"+ item.getText(1));
					}
					try {
						RestUri projectsUri = GobiiClientContext.getInstance(null, false).getUriFactory().resourceByUriIdParam(GobiiServiceRequestId.URL_PROJECTS);
						projectsUri.setParamValue("id", Integer.toString(projectDTORequest.getProjectId()));
						GobiiEnvelopeRestResource<ProjectDTO> restResourceForProjectGet = new GobiiEnvelopeRestResource<>(projectsUri);
						PayloadEnvelope<ProjectDTO> requestEnvelope =  new PayloadEnvelope<>(projectDTORequest, GobiiProcessType.UPDATE);
						PayloadEnvelope<ProjectDTO> resultEnvelope = restResourceForProjectGet.put(ProjectDTO.class, requestEnvelope);
						
						if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, true)){
							populateProjectsFromSelectedContact(cbList.getText());
							textCode.setText(resultEnvelope.getPayload().getData().get(0).getProjectCode());
							FormUtils.selectRowById(tbList,currentProjectId);
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error saving Project", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Project", err);
				}
			}
		});
		btnUpdate.setEnabled(false);
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);

		btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanProjectDetails();
				if(currentPIid==0)cbPIContact.setText("");
//				currentProjectId=0;
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");
		new Label(cmpForm, SWT.NONE);

		btnAddPlatformExperiment = new Button(cmpForm, SWT.NONE);
		btnAddPlatformExperiment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmExperiments frm = new FrmExperiments(shell, parent, SWT.NONE, config, currentPIid, currentProjectId);
				FormUtils.createContentTab(shell, frm, (CTabFolder) parent, "Experiments");

			}
		});
		btnAddPlatformExperiment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAddPlatformExperiment.setText("Add Experiment");
		new Label(cmpForm, SWT.NONE);

		btnDnaSampleWiz = new Button(cmpForm, SWT.FLAT);
		btnDnaSampleWiz.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnDnaSampleWiz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.createDNASampleWizard(shell, config, currentPIid, currentProjectId, 0);
			}
		});
		btnDnaSampleWiz.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnDnaSampleWiz.setText("DNA Sample Wizard");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		//		cbList

		populateContactsList(cbList);
		populateTableWithAllProjects(tbList);
		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String selected = cbList.getText(); //single selection
				cbPIContact.select(cbPIContact.indexOf(selected));
				cbPIContact.setText(selected);
				populateProjectsFromSelectedContact(selected);
				cleanProjectDetails();
				currentProjectId = 0;
			}
		});

		tbList.addListener (SWT.Selection, new Listener() {


			public void handleEvent(Event e) {
				currentProjectId = FormUtils.getIdFromTableList(tbList);
				populateProjectDetails(currentProjectId); //retrieve and display projects by contact Id
			}


		});
		// table

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Integer id = FormUtils.getIdFromFormList(cbList);
				String selected = cbList.getText();
				if(cbList.getSelectionIndex()>-1){
					FormUtils.entrySetToComboSelectId(Controller.getPIContactNames(), cbList, id);
					FormUtils.entrySetToComboSelectId(Controller.getPIContactNames(), cbPIContact, id);
					populateProjectsFromSelectedContact(selected);
				}
				else{
					populateContactsList(cbPIContact);
					populateContactsList(cbList);
					populateTableWithAllProjects(tbList);
					cbList.setText("*Select PI contact");
				}
				cleanProjectDetails();

				currentProjectId=0;
			}
		});
	}
	

	private void populateProjectDetails(int projectId) {
		try{
			cleanProjectDetails();

			ProjectDTO projectDTO = null;
			try {
				PayloadEnvelope<ProjectDTO> resultEnvelope = Controller.getProjectDetailsById(projectId);

				if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, false)){
					projectDTO =  resultEnvelope.getPayload().getData().get(0);

					//displayDetails

					selectedName = projectDTO.getProjectName();
					textCode.setText(projectDTO.getProjectCode());
					txtName.setText(projectDTO.getProjectName());
					styledTextDesc.setText(projectDTO.getProjectDescription());
					
					for(int i=0; i<cbPIContact.getItemCount(); i++){
						Integer piId = Integer.parseInt((String) cbPIContact.getData(cbPIContact.getItem(i)));
						if(piId == projectDTO.getPiContact()){
							cbPIContact.select(i);
							break;
						}
					}

					populateTableFromStringList(projectDTO.getProperties(), tbProperties);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects", err);
		}
	}
	
	private void populateTableWithAllProjects(Table table) {
		if(table.getItemCount()>0) table.removeAll();
		try{
			FormUtils.entrySetToTable(Controller.getProjectNames(), table);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects", err);
		}
	}

	private void populateTableFromStringList(List<EntityPropertyDTO> projectProperties, Table table) {
		try{
			for(TableItem item : tbProperties.getItems()){
				Integer index = Integer.parseInt((String) item.getData(item.getText()));
				for (EntityPropertyDTO p : projectProperties){
					if(index == p.getPropertyId()){
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

	//retrieve and display projects by contact Id

	protected void populateProjectsFromSelectedContact(String selectedContact) {
		try{
			currentPIid = FormUtils.getIdFromFormList(cbList);
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getProjectNamesByContactId(currentPIid), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Porject details", err);
		}
	}

	private void cleanProjectDetails() {
		try{
			textCode.setText("");
			txtName.setText("");
			styledTextDesc.setText("");
			for(TableItem item : tbProperties.getItems()){
				item.setText(1, "");
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	private void populateContactsList(Combo combo) {
		try{
			FormUtils.entrySetToCombo(Controller.getPIContactNames(), combo);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Contacts", err);
		}
	}

	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			message = "Name field is required!";
			successful = false;
		}else if(cbList.getSelectionIndex() < 0 && cbPIContact.getSelectionIndex() <0){
			message = "PI contact field is required!";
			successful = false;
		}else if(!isNew && currentProjectId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else{
			if(isNew || !txtName.getText().equalsIgnoreCase(selectedName)){
				for(int i=0; i<tbList.getItemCount(); i++){
					if(tbList.getItem(i).getText(0).equalsIgnoreCase(txtName.getText())){
						successful = false;
						message = "Name of project already exists for this PI contact!";
						break;
					}
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
