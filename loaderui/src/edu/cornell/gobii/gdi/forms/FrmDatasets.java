package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
//import org.gobiiproject.gobiiclient.dtorequests.DtoRequestMarkers;
//import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

public class FrmDatasets extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmDatasets.class.getName());
	private Table tbAnalysis;
	private Combo cbExperiment;
	private Combo cbAnalysis;
	private Button btnAddNew;
	private Button btnUpdate;

	private Combo cbType;
	private Label lblDatasetType;
	private Button btnClearFields;
	private Button btnDatasetWizard;
	private Text txtName;
	private Label lblName;
	private Text txtDataFile;
	private Label lblDataFile;
	private Text txtDataTable;
	private Label lblDataTable;
	private TableColumn tblclmnDatasets;
	private int currentProjectId;
	private int currentExperimentId;
	protected int currentDatasetId;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 * @wbp.parser.constructor
	 */
	public FrmDatasets(final Shell shell, Composite parent, int style, final String config) {
		super(shell, parent, style);

		currentProjectId = 0;
		currentExperimentId = 0;
		currentDatasetId = 0;
		

		populateCbListAndTbList();
	}

	public FrmDatasets(Shell shell, Composite parent, int style, String config, int projectId,
			int experimentId) {
		super(shell, parent, style);

		currentProjectId = projectId;
		currentExperimentId = experimentId;
		currentDatasetId = 0;
		

		populateCbListAndTbList();

	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		lblCbList.setText("Experiments:");
		cbList.setText("*Select Experiment");

		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tbList.removeAll();
				Integer id = FormUtils.getIdFromFormList(cbList);
				if (currentProjectId>0){
					FormUtils.entrySetToComboSelectId(Controller.getExperimentNamesByProjectId(currentProjectId), cbList, id);
					FormUtils.entrySetToTable(Controller.getDataSetNames(), tbList);
				}
				else if(id>0){ //if there is a selected experiment
					FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbList, id);
					FormUtils.entrySetToTable(Controller.getDataSetNamesByExperimentId(id), tbList);
					FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbExperiment, currentExperimentId);
				}
				else{
					FormUtils.entrySetToCombo(Controller.getExperimentNames(), cbList);
					cbList.setText("Select Experiment");
					FormUtils.entrySetToTable(Controller.getDataSetNames(), tbList);
					cbExperiment.removeAll();
				}

				FormUtils.entrySetToCombo(Controller.getAnalysisNamesByType("calling"), cbAnalysis);
				FormUtils.entrySetToCombo(Controller.getCVByGroup("dataset_type"), cbType);

				selectedName = null;
				currentDatasetId = 0;
				cleanDetails();
				
			}
		});

		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String selected = cbList.getText(); //single selection
				cbExperiment.setText(selected);
				cleanDetails();
				currentExperimentId= FormUtils.getIdFromFormList(cbList);
				currentDatasetId = 0;
				populateDatasetListFromSelectedExperiment(currentExperimentId); //retrieve and display projects by contact Id
			}

		});


		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				currentDatasetId = FormUtils.getIdFromTableList(tbList);
				populateDatasetDetails(currentDatasetId); //retrieve and display projects by contact Id
				
			}

		});
		

		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("*Dataset Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtName.addFocusListener(new FocusListener() {
    		ToolTip tip = new ToolTip(shell, SWT.BALLOON);
    		
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(cbList.getSelectionIndex()<0){
				
				Point loc = cbList.toDisplay(cbList.getLocation());

        		tip.setMessage("Please select an Experiment before creating or updating an entry.");
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
		
		Label lblExperiment = new Label(cmpForm, SWT.NONE);
		lblExperiment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExperiment.setText("*Experiment:");

		cbExperiment = new Combo(cmpForm, SWT.NONE);
		cbExperiment.setEnabled(false);
		cbExperiment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblDatasetType = new Label(cmpForm, SWT.NONE);
		lblDatasetType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatasetType.setText("*Dataset Type:");

		cbType = new Combo(cmpForm, SWT.NONE);
		cbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getCVByGroup("dataset_type"), cbType);

		Label lblCallAnalysis = new Label(cmpForm, SWT.NONE);
		lblCallAnalysis.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCallAnalysis.setText("*Calling Analysis:");

		cbAnalysis = new Combo(cmpForm, SWT.NONE);
		cbAnalysis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getAnalysisNamesByType("calling"), cbAnalysis);

		lblDataFile = new Label(cmpForm, SWT.NONE);
		lblDataFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDataFile.setText("Data File:");

		txtDataFile = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtDataFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lblDataTable = new Label(cmpForm, SWT.NONE);
		lblDataTable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDataTable.setText("Data Table:");

		txtDataTable = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtDataTable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));


		Label lblAnalyses = new Label(cmpForm, SWT.NONE);
		lblAnalyses.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAnalyses.setText("Analyses:");

		tbAnalysis = new Table(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		GridData gd_tbAnalysis = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tbAnalysis.heightHint = 105;
		tbAnalysis.setLayoutData(gd_tbAnalysis);
		tbAnalysis.setHeaderVisible(true);
		tbAnalysis.setLinesVisible(true);
		new Label(cmpForm, SWT.NONE);
		FormUtils.entrySetToTable(Controller.getAnalysisNames(), tbAnalysis);

		btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;
					saveDataset(true);
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Dataset", err);
				}
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
					if(!FormUtils.updateForm(getShell(), "Dataset", selectedName)) return;
					saveDataset(false);
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Dataset", err);
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
				selectedName = null;
				currentDatasetId = 0;
				cleanDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnClearFields.setText("Clear Fields");
		new Label(cmpForm, SWT.NONE);

		btnDatasetWizard = new Button(cmpForm, SWT.FLAT);
		btnDatasetWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
//				ArrayList<Integer> parameterIDs = FormUtils.getCompleteIDset(0, currentProjectId,currentExperimentId,currentDatasetId);
//				parameterIDs.get
				WizardUtils.CreateDatasetWizard(shell, App.INSTANCE.getConfigDir(), 0, currentProjectId, currentExperimentId, currentDatasetId);
			}
		});
		btnDatasetWizard.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnDatasetWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnDatasetWizard.setText("Dataset Wizard");

		tblclmnDatasets = new TableColumn(tbList, SWT.NONE);
		tblclmnDatasets.setWidth(300);
		tblclmnDatasets.setText("Datasets:");

	}

	private void populateCbListAndTbList() {
		// TODO Auto-generated method stub
		try{
			// get experiments
			if(currentProjectId>0 ){ 
				if(currentExperimentId>0){
					FormUtils.entrySetToComboSelectId(Controller.getExperimentNamesByProjectId(currentProjectId), cbList, currentExperimentId);
					cbExperiment.setText(cbList.getText());
				}
				else FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(currentProjectId), cbList);
			}else{
				if(currentExperimentId>0){
					FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbList, currentExperimentId);
					cbExperiment.setText(cbList.getText());
				}
				else FormUtils.entrySetToCombo(Controller.getExperimentNames(), cbList);
			}
			// get datasets
			if(currentExperimentId>0) FormUtils.entrySetToTable(Controller.getDataSetNamesByExperimentId(currentExperimentId), tbList);
			else FormUtils.entrySetToTable(Controller.getDataSetNames(), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects and Experiemnts", err);
		}

	}

	public void populateDatasetListFromSelectedExperiment(Integer selectedId) {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getDataSetNamesByExperimentId(selectedId), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Datasets", err);
		}
	}

	private void cleanDetails() {
		try{
			txtName.setText("");
			cbType.deselectAll(); cbType.setText("");
			cbAnalysis.deselectAll(); cbAnalysis.setText("");
			txtDataFile.setText("");
			txtDataTable.setText("");
			for(TableItem item : tbAnalysis.getItems()){
				item.setChecked(false);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}

	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			message = "Name field is required!";
			successful = false;
		}else if(cbList.getSelectionIndex() < 0){
			message = "Please select an Experiment!";
			successful = false;
		}else if(cbType.getSelectionIndex() < 0){
			message = "Dataset Type is a required field!";
			successful = false;
		}else if(cbAnalysis.getSelectionIndex() < 0){
			message = "Calling analysis is a required field!";
			successful = false;
		}else if(!isNew && selectedName==null){
			message = "You have not selected an existing dataset to update. Use \"Add New\" to add a new dataset.";
			successful = false;
		}
		else{
			if(isNew || !txtName.getText().equalsIgnoreCase(selectedName)){

				for(TableItem item : tbList.getItems())
					if(item.getText().equalsIgnoreCase(txtName.getText())){
						message = "Name of this Dataset already exists for this Experiment!";
						successful = false;
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

	private void populateDatasetDetails(int datasetId) {
		cleanDetails();

			try {
				FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbExperiment, currentExperimentId);
				
				PayloadEnvelope<DataSetDTO> resultEnvelope = Controller.getDatasetDetailsById(currentDatasetId);

				if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, false)){
					DataSetDTO dataSetDTOResponse = resultEnvelope.getPayload().getData().get(0);

					FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbExperiment, dataSetDTOResponse.getExperimentId());
					selectedName = dataSetDTOResponse.getName(); 
					if(dataSetDTOResponse.getName() != null)
						txtName.setText(dataSetDTOResponse.getName());
					for(int i=0; i<cbExperiment.getItemCount(); i++){
						String name = cbExperiment.getItem(i);
						Integer id = Integer.parseInt((String)cbExperiment.getData(name));
						if(id.equals(dataSetDTOResponse.getExperimentId())){
							cbExperiment.select(i);
							break;
						}
					}
					for(int i=0; i<cbType.getItemCount(); i++){
						String name = cbType.getItem(i);
						Integer id = Integer.parseInt((String)cbType.getData(name));
						if(id.equals(dataSetDTOResponse.getTypeId())){
							cbType.select(i);
							break;
						}
					}
					for(int i=0; i<cbAnalysis.getItemCount(); i++){
						String name = cbAnalysis.getItem(i);
						Integer id = Integer.parseInt((String)cbAnalysis.getData(name));
						if(id.equals(dataSetDTOResponse.getCallingAnalysisId())){
							cbAnalysis.select(i);
							break;
						}
					}
					if(dataSetDTOResponse.getDataFile() != null)
						txtDataFile.setText(dataSetDTOResponse.getDataFile());
					if(dataSetDTOResponse.getDataTable() != null)
						txtDataTable.setText(dataSetDTOResponse.getDataTable());
					for(TableItem item : tbAnalysis.getItems()){
						String name = item.getText();
						Integer id = Integer.parseInt((String) item.getData(name));
						if(dataSetDTOResponse.getAnalysesIds().contains(id)){
							item.setChecked(true);
							continue;
						}
					}
				}
			} catch (Exception err) {
				Utils.log(shell, memInfo, log, "Error retrieving Dataset details", err);
			}
	}
	protected void saveDataset(boolean isNew){
		try{
			RestUri projectsCollUri = null;
			GobiiEnvelopeRestResource<DataSetDTO> restResource = null;
			DataSetDTO dataSetDTORequest =  new DataSetDTO();
			PayloadEnvelope<DataSetDTO> resultEnvelope = null;

			if(isNew){
				projectsCollUri = App.INSTANCE.getUriFactory().resourceColl(ServiceRequestId.URL_DATASETS);
				restResource = new GobiiEnvelopeRestResource<>(projectsCollUri);
				setDatasetDetails(dataSetDTORequest);
				dataSetDTORequest.setDataFile(null);
				dataSetDTORequest.setDataTable(null);
				resultEnvelope = restResource.post(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTORequest, GobiiProcessType.CREATE));

			}else{
				projectsCollUri = App.INSTANCE.getUriFactory().resourceByUriIdParam(ServiceRequestId.URL_DATASETS);
				restResource = new GobiiEnvelopeRestResource<>(projectsCollUri);
				dataSetDTORequest.setDataSetId(currentDatasetId);
				setDatasetDetails(dataSetDTORequest);
				dataSetDTORequest.setDataFile(txtDataFile.getText());
				dataSetDTORequest.setDataTable(txtDataTable.getText());
				restResource.setParamValue("id", dataSetDTORequest.getDataSetId().toString());
				resultEnvelope = restResource.put(DataSetDTO.class, new PayloadEnvelope<>(dataSetDTORequest, GobiiProcessType.UPDATE));
			}
			try{
				if(Controller.getDTOResponse(shell, resultEnvelope.getHeader(), memInfo, true)){
					currentDatasetId = resultEnvelope.getPayload().getData().get(0).getDataSetId();
					populateDatasetListFromSelectedExperiment(currentExperimentId);
					populateDatasetDetails(currentDatasetId);
					FormUtils.selectRowById(tbList,currentDatasetId);
				};
			}catch(Exception err){
				Utils.log(shell, memInfo, log, "Error saving Dataset", err);
			}
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Dataset", err);
		}
	}

	protected void setDatasetDetails(DataSetDTO datasetDTO){
		try{
			String expName = cbList.getItem(cbList.getSelectionIndex());
			Integer expId = Integer.parseInt((String) cbList.getData(expName));
			datasetDTO.setExperimentId(expId);
			String typeName = cbType.getItem(cbType.getSelectionIndex()); 
			Integer typeId = Integer.parseInt((String) cbType.getData(typeName));
			datasetDTO.setTypeId(typeId);
			datasetDTO.setName(txtName.getText());
			String callName = cbAnalysis.getItem(cbAnalysis.getSelectionIndex());
			Integer callId = Integer.parseInt((String) cbAnalysis.getData(callName));
			datasetDTO.setCallingAnalysisId(callId);
			for(TableItem item : tbAnalysis.getItems()){
				if(item.getChecked()){
					Integer analysisId = Integer.parseInt((String) item.getData(item.getText()));
					datasetDTO.getAnalysesIds().add(analysisId);
				}
			}
			datasetDTO.setCreatedBy(1);
			datasetDTO.setCreatedDate(new Date());
			datasetDTO.setModifiedBy(1);
			datasetDTO.setModifiedDate(new Date());
			datasetDTO.setStatusId(1);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Dataset", err);
		}
	}
}
