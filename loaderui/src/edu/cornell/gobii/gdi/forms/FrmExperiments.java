package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class FrmExperiments extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmExperiments.class.getName());
	private Text txtName;
	private Text txtDatafile;
	private Combo cbVendorProtocol;
	private Button btnAddNew;
	private Button btnUpdate;
	private Button btnDnaWiz;
	private Label lblCode;
	private Text txtCode;
	private Combo comboManifest;
	private Button btnMarkerWiz;
	private ModifyListener listener;

	private String config;
	private Combo comboProject;
	private TableColumn tblColumn;
	private Button btnClearFields;
	private Button btnAddAnalysisDataset;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmExperiments(final Shell shell, Composite parent, int style, final String config) {
		super(shell, parent, style);
		this.config = config;
		tblColumn = new TableColumn(tbList, SWT.NONE);
		tblColumn.setText("Experiments:");
		tblColumn.setWidth(300);

		listener = new ModifyListener() {
			/** {@inheritDoc} */
			public void modifyText(ModifyEvent e) {
				// Handle event
				if(cbList.getItems().length>0) btnUpdate.setEnabled(true);
			}
		};

		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		txtName.addModifyListener((ModifyListener) listener);

		lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");

		txtCode = new Text(cmpForm, SWT.BORDER);
		txtCode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblProject = new Label(cmpForm, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProject.setText("*Project:");

		comboProject = new Combo(cmpForm, SWT.NONE);
		comboProject.setEnabled(false);
		comboProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		populateProjectsListByContactId(comboProject);

		Label lblVendorProtocol = new Label(cmpForm, SWT.NONE);
		lblVendorProtocol.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVendorProtocol.setText("*Vendor-Protocol:");

		cbVendorProtocol = new Combo(cmpForm, SWT.NONE);
		cbVendorProtocol.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getVendorProtocolNames(), cbVendorProtocol);

		Label lblManifest = new Label(cmpForm, SWT.NONE);
		lblManifest.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblManifest.setText("Manifest:");

		comboManifest = new Combo(cmpForm, SWT.NONE);
		comboManifest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboManifest.addModifyListener((ModifyListener) listener);
		FormUtils.entrySetToCombo(Controller.getManifestNames(), comboManifest);

		Label lblDataFile = new Label(cmpForm, SWT.NONE);
		lblDataFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDataFile.setText("Data file:");

		txtDatafile = new Text(cmpForm, SWT.BORDER);
		txtDatafile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);
		txtDatafile.addModifyListener((ModifyListener) listener);

		btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;
					ExperimentDTO experimentDTO = new ExperimentDTO();
					experimentDTO.setCreatedBy(1);
					experimentDTO.setModifiedBy(1);
					experimentDTO.setStatusId(1);
					experimentDTO.setExperimentName(txtName.getText());
					String name = txtName.getText().replaceAll(" ", "_");
					String platform = cbVendorProtocol.getText().replaceAll(" ", "_");
					String project = comboProject.getText().replaceAll(" ", "_");
					experimentDTO.setExperimentCode(name+"_"+platform+"_"+project);
					experimentDTO.setProjectId(IDs.projectId);
					String strVendorProtocolId = (String) cbVendorProtocol.getData(cbVendorProtocol.getItem(cbVendorProtocol.getSelectionIndex()));
					experimentDTO.setVendorProtocolId(Integer.parseInt(strVendorProtocolId));
					if(comboManifest.getSelectionIndex() >= 0){
						int index = comboManifest.getSelectionIndex();
						String strMId = (String) comboManifest.getData(comboManifest.getItem(index));
						if(strMId != null) experimentDTO.setManifestId(Integer.parseInt(strMId));
					}
					if(!txtDatafile.getText().isEmpty()) experimentDTO.setExperimentDataFile(txtDatafile.getText());

					try{
						RestUri experimentsUri = App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_EXPERIMENTS);
						GobiiEnvelopeRestResource<ExperimentDTO> restResourceForExperiments = new GobiiEnvelopeRestResource<>(experimentsUri);
						PayloadEnvelope<ExperimentDTO> payloadEnvelope = new PayloadEnvelope<>(experimentDTO, GobiiProcessType.CREATE);
						PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperiments
								.post(ExperimentDTO.class, payloadEnvelope);
						if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo,true)){
							populateExperimentsListFromSelectedProject(IDs.projectId);
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error saving Experiemnts", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Experiemnts", err);
				}
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
				try{
					if(!validate(false)) return;
					if(!FormUtils.updateForm(getShell(), "Experiment", IDs.experimentName)) return;
					ExperimentDTO experimentDTO = new ExperimentDTO();
					experimentDTO.setCreatedBy(1);
					experimentDTO.setCreatedDate(new Date());
					experimentDTO.setModifiedBy(1);
					experimentDTO.setStatusId(2);
					experimentDTO.setExperimentId(IDs.experimentId);
					experimentDTO.setExperimentName(txtName.getText());
					String name = txtName.getText().replaceAll(" ", "_");
					String platform = cbVendorProtocol.getText().replaceAll(" ", "_");
					String project = comboProject.getText().replaceAll(" ", "_");
					experimentDTO.setExperimentCode(name+"_"+platform+"_"+project);
					Integer projectId = IDs.projectId > 0 ? IDs.projectId : Integer.parseInt((String) comboProject.getData(comboProject.getItem(comboProject.getSelectionIndex())));
					experimentDTO.setProjectId(projectId);
					String strVendorProtocolId = (String) cbVendorProtocol.getData(cbVendorProtocol.getItem(cbVendorProtocol.getSelectionIndex()));
					experimentDTO.setVendorProtocolId(Integer.parseInt(strVendorProtocolId));
					if(comboManifest.getSelectionIndex() >= 0){
						int index = comboManifest.getSelectionIndex();
						String strMId = (String) comboManifest.getData(comboManifest.getItem(index));
						if(strMId != null) experimentDTO.setManifestId(Integer.parseInt(strMId));
					}
					if(!txtDatafile.getText().isEmpty()) experimentDTO.setExperimentDataFile(txtDatafile.getText());

					try{
						RestUri experimentsUriById = App.INSTANCE.getUriFactory()
								.resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
						experimentsUriById.setParamValue("id", Integer.toString(IDs.experimentId));
						GobiiEnvelopeRestResource<ExperimentDTO> restResourceForExperimentsById = new GobiiEnvelopeRestResource<>(experimentsUriById);
						PayloadEnvelope<ExperimentDTO> postRequestEnvelope = new PayloadEnvelope<>(experimentDTO,GobiiProcessType.UPDATE);
						PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperimentsById
								.put(ExperimentDTO.class,postRequestEnvelope);

						if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, true)){
							populateExperimentsListFromSelectedProject(IDs.projectId);
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error saving Experiemnts", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Experiemnts", err);
				}
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);

		btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanExperimentDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");
		new Label(cmpForm, SWT.NONE);

		btnAddAnalysisDataset = new Button(cmpForm, SWT.NONE);
		btnAddAnalysisDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmDatasets frm = new FrmDatasets(shell, parent, SWT.NONE, config);
				CTabFolder tabContent = (CTabFolder) parent;
				CTabItem item = new CTabItem(tabContent, SWT.NONE);
				item.setText("Analysis Datasets");
				item.setShowClose(true);
				item.setControl(frm);
				tabContent.setSelection(item);
			}
		});
		btnAddAnalysisDataset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAddAnalysisDataset.setText("Add Analysis Dataset");
		new Label(cmpForm, SWT.NONE);

		btnMarkerWiz = new Button(cmpForm, SWT.FLAT);
		btnMarkerWiz.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnMarkerWiz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateMarkerWizard(shell, config);
			}
		});
		btnMarkerWiz.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnMarkerWiz.setText("Marker Wizard");
		new Label(cmpForm, SWT.NONE);

		btnDnaWiz = new Button(cmpForm, SWT.FLAT);
		btnDnaWiz.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnDnaWiz.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.createDNASampleWizard(shell, config);
			}
		});
		btnDnaWiz.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnDnaWiz.setText("DNA Sample wizard");

		cmpForm.addListener(SWT.Show, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				if(IDs.projectId > 0){
					for(int i=0; i<cbList.getItemCount(); i++){
						String item = cbList.getItem(i);
						Integer key = Integer.parseInt((String) cbList.getData(item));
						if(key == IDs.projectId){
							cbList.select(i);
							break;
						}
					}
				}
			}

		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {

		populateAllProjectsAndExperiments(cbList,tbList);
		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try{
					String selected = cbList.getText(); //single selection
					comboProject.select(comboProject.indexOf(selected));
					comboProject.setText(selected);
					IDs.projectId = Integer.parseInt((String) cbList.getData(selected));

					cleanExperimentDetails();
					populateExperimentsListFromSelectedProject(IDs.projectId ); //retrieve and display projects by contact Id
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Project", err);
				}
			}
		});

		tbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				IDs.experimentName = selected;
				IDs.experimentId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateExperimentDetails(IDs.experimentId); //retrieve and display projects by contact Id
				tblColumn.pack();
			}
		});

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Integer id = FormUtils.getIdFromFormList(cbList);
				if(id>0){
					if (IDs.PIid>0) FormUtils.entrySetToComboSelectId(Controller.getProjectNamesByContactId(IDs.PIid), cbList, id);
					else FormUtils.entrySetToComboSelectId(Controller.getProjectNames(), cbList, id);
					populateExperimentsListFromSelectedProject(id);
				}
				else{
					populateAllProjectsAndExperiments(cbList,tbList);
				}
				FormUtils.entrySetToCombo(Controller.getPlatformNames(), cbVendorProtocol);
				FormUtils.entrySetToCombo(Controller.getManifestNames(), comboManifest);
				cleanExperimentDetails();
			}
		});
	}

	private void populateAllProjectsAndExperiments(Combo cbList, Table tbList) {
		try{
			// get projects
			cbList.setText("*Select a Project");
			tbList.removeAll();
			if(IDs.PIid>0 ){
				if(IDs.projectId>0){
					FormUtils.entrySetToComboSelectId(Controller.getProjectNamesByContactId(IDs.PIid), cbList, IDs.projectId);

				}
				else FormUtils.entrySetToCombo(Controller.getProjectNamesByContactId(IDs.PIid), cbList);
			}else{
				if(IDs.projectId>0) FormUtils.entrySetToComboSelectId(Controller.getProjectNames(), cbList, IDs.projectId);
				else FormUtils.entrySetToCombo(Controller.getProjectNames(), cbList);
			}
			// get experiments
			if(IDs.projectId>0){
				FormUtils.entrySetToTable(Controller.getExperimentNamesByProjectId(IDs.projectId), tbList);
			}
			else{FormUtils.entrySetToTable(Controller.getExperimentNames(), tbList);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects and Experiemnts", err);
		}
	}

	protected void populateExperimentDetails(int experimentId) {
			cleanExperimentDetails();
			ExperimentDTO experimentDTO = null;
			try {
				RestUri experimentsUri = App.INSTANCE.getUriFactory()
						.resourceByUriIdParam(ServiceRequestId.URL_EXPERIMENTS);
				experimentsUri.setParamValue("id", Integer.toString(experimentId));
				GobiiEnvelopeRestResource<ExperimentDTO> restResourceForExperiments = new GobiiEnvelopeRestResource<>(experimentsUri);
				PayloadEnvelope<ExperimentDTO> resultEnvelope = restResourceForExperiments
						.get(ExperimentDTO.class);

				if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, false)){
					experimentDTO = resultEnvelope.getPayload().getData().get(0);
					selectedName = experimentDTO.getExperimentName();
					//displayDetails
					txtCode.setText(experimentDTO.getExperimentCode());
					txtName.setText(experimentDTO.getExperimentName());
					if(experimentDTO.getExperimentDataFile() != null){
						txtDatafile.setText(experimentDTO.getExperimentDataFile());
					}
					populateProjectsComboAndSelect(comboProject, experimentDTO.getProjectId());
					populateVendorProtocolComboAndSelect(cbVendorProtocol, experimentDTO.getVendorProtocolId());
					if(experimentDTO.getManifestId() != null){
						populateManifestComboAndSelect(comboManifest, experimentDTO.getManifestId());
					}
				}
			} catch (Exception e) {
				Utils.log(shell, memInfo, log, "Error retrieving Experiemnts", e);
			}

	}
	private void populateProjectsComboAndSelect(Combo comboProject, int projectId) {
		try{
			FormUtils.entrySetToComboSelectId(Controller.getProjectNames(), comboProject, projectId);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects", err);
		}
	}

	private void populateManifestComboAndSelect(Combo comboManifest, int manifestId) {
		try{
			FormUtils.entrySetToComboSelectId(Controller.getManifestNames(), comboManifest, manifestId);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
		}
	}

	private void populateVendorProtocolComboAndSelect(Combo combo, int vendorProtocolId) {
		try{
			FormUtils.entrySetToComboSelectId(Controller.getVendorProtocolNames(), combo, vendorProtocolId);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Platforms", err);
		}
	}
	private void populateProjectsListByContactId(Combo cbList) {
		try{
			if(IDs.projectId > 0){
				FormUtils.entrySetToComboSelectId(Controller.getProjectNamesByContactId(IDs.PIid), cbList, IDs.projectId);
				for(int i=0; i<cbList.getItemCount(); i++){
					String key = (String) cbList.getData(cbList.getItem(i));
					if(Integer.parseInt(key) == IDs.projectId){
						cbList.select(i);
						break;
					}
				}
				populateExperimentsListFromSelectedProject(IDs.projectId );
			}else{
				FormUtils.entrySetToCombo(Controller.getProjectNamesByContactId(IDs.PIid), cbList);
				if (cbList.getItemCount()<1) FormUtils.entrySetToCombo(Controller.getProjectNames(), comboProject);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects", err);
		}
	}

	//retrieve and display experiments by project Id
	public void populateExperimentsListFromSelectedProject(Integer selectedId) {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getExperimentNamesByProjectId(selectedId), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Experiemnts", err);
		}
	}

	private void cleanExperimentDetails() {
		try{
			txtCode.setText("");
			txtName.setText("");
			cbVendorProtocol.deselectAll(); cbVendorProtocol.setText("");
			comboManifest.deselectAll(); comboManifest.setText("");
			txtDatafile.setText("");
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
		}else if(comboProject.getSelectionIndex() < 0){
			message = "Project is a required filed!";
			successful = false;
		}else if(cbList.getSelectionIndex() < 0){
			message = "Platform is a required field!";
			successful = false;
		}else if(!isNew && IDs.experimentId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}else if(isNew|| !txtName.getText().equalsIgnoreCase(selectedName)){
				for(int i=0; i<tbList.getItemCount(); i++){
					if(tbList.getItem(i).getText(0).equalsIgnoreCase(txtName.getText())){
						successful = false;
						message = "Name of Experiment already exists for this Project!";
						break;
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
