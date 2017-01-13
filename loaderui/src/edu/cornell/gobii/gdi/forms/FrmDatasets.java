package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridLayout;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDataSet;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
//import org.gobiiproject.gobiiclient.dtorequests.DtoRequestMarkers;
//import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;

import edu.cornell.gobii.gdi.main.App;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;

public class FrmDatasets extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmDatasets.class.getName());
	private Table tbAnalysis;
	private Combo cbExperiment;
	private Combo cbAnalysis;
	private Button btnAddNew;
	private Button btnUpdate;

	private String config;
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
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmDatasets(final Shell shell, Composite parent, int style, final String config) {
		super(shell, parent, style);
		((GridData) cmpForm.getLayoutData()).heightHint = 585;
		this.config = config;

		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;
		
		lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblExperiment = new Label(cmpForm, SWT.NONE);
		lblExperiment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExperiment.setText("Experiment:");

		cbExperiment = new Combo(cmpForm, SWT.NONE);
		cbExperiment.setEnabled(false);
		cbExperiment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getExperimentNames(), cbExperiment);
		
		lblDatasetType = new Label(cmpForm, SWT.NONE);
		lblDatasetType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatasetType.setText("Dataset Type:");
		
		cbType = new Combo(cmpForm, SWT.NONE);
		cbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getCVByGroup("dataset_type"), cbType);

		Label lblCallAnalysis = new Label(cmpForm, SWT.NONE);
		lblCallAnalysis.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCallAnalysis.setText("Call Analysis:");

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
		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);

		btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(false)) return;
					if(!FormUtils.updateForm(getShell(), "Dataset", IDs.datasetName)) return;
					saveDataset(false);
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving Dataset", err);
				}
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);
		
		btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearFields.setText("Clear Fields");
		new Label(cmpForm, SWT.NONE);
		
		btnDatasetWizard = new Button(cmpForm, SWT.FLAT);
		btnDatasetWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateDatasetWizard(shell, App.INSTANCE.getConfigDir());
			}
		});
		btnDatasetWizard.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		btnDatasetWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDatasetWizard.setText("Dataset Wizard");
		
		tblclmnDatasets = new TableColumn(tbList, SWT.NONE);
		tblclmnDatasets.setWidth(300);
		tblclmnDatasets.setText("Datasets:");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		cbList.setText("Select Experiment");
		try{
			// get experiments
			if(IDs.projectId>0 ){
				if(IDs.projectId>0) FormUtils.entrySetToComboSelectId(Controller.getExperimentNamesByProjectId(IDs.projectId), cbList, IDs.experimentId);
				else FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(IDs.projectId), cbList);
			}else{
				if(IDs.experimentId>0) FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbList, IDs.experimentId);
				else FormUtils.entrySetToCombo(Controller.getExperimentNames(), cbList);
			}
			// get datasets
			if(IDs.experimentId>0) FormUtils.entrySetToTable(Controller.getDataSetNamesByExperimentId(IDs.experimentId), tbList);
			else FormUtils.entrySetToTable(Controller.getDataSetNames(), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Projects and Experiemnts", err);
		}
		
//		if(IDs.projectId > 0){
//			if(IDs.experimentId> 0) FormUtils.entrySetToComboSelectId(Controller.getExperimentNamesByProjectId(IDs.projectId), cbList, IDs.experimentId);
//			else FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(IDs.projectId), cbList);
//		}else{
//			if(IDs.experimentId> 0){
//				FormUtils.entrySetToComboSelectId(Controller.getExperimentNames(), cbList, IDs.experimentId);
//				FormUtils.entrySetToTable(Controller.getDataSetNamesByExperimentId(IDs.experimentId), tbList);
//			}
//			else{
//				populateExperimentList(cbList);
//				populateDatasetTable(tbList);
//			}
//		}

		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String selected = cbList.getText(); //single selection
				cbExperiment.setText(selected);
				IDs.experimentId = Integer.parseInt((String) cbList.getData(selected));

				cleanDetails();
				populateDatasetListFromSelectedExperiment(IDs.experimentId ); //retrieve and display projects by contact Id
			}

		});


		tbList.addListener (SWT.Selection, new Listener() {

		public void handleEvent(Event e) {
			String selected = tbList.getSelection()[0].getText(); //single selection
			IDs.datasetName = selected;
			IDs.datasetId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
			populateDatasetDetails(IDs.datasetId); //retrieve and display projects by contact Id
		}

		protected void populateDatasetDetails(int experimentId) {
			cleanDetails();
			try{
				DtoRequestDataSet dtoRequestDataSet = new DtoRequestDataSet();
				DataSetDTO dataSetDTORequest = new DataSetDTO();
				dataSetDTORequest.setDataSetId(IDs.datasetId);
				try {
					DataSetDTO dataSetDTOResponse = dtoRequestDataSet.process(dataSetDTORequest);
					if(dataSetDTOResponse.getName() != null)
						txtName.setText(dataSetDTOResponse.getName());
					for(int i=0; i<cbExperiment.getItemCount(); i++){
						String name = cbExperiment.getItem(i);
						Integer id = Integer.parseInt((String)cbExperiment.getData(name));
						if(id == dataSetDTOResponse.getExperimentId()){
							cbExperiment.select(i);
							break;
						}
					}
					for(int i=0; i<cbType.getItemCount(); i++){
						String name = cbType.getItem(i);
						Integer id = Integer.parseInt((String)cbType.getData(name));
						if(id == dataSetDTOResponse.getTypeId()){
							cbType.select(i);
							break;
						}
					}
					for(int i=0; i<cbAnalysis.getItemCount(); i++){
						String name = cbAnalysis.getItem(i);
						Integer id = Integer.parseInt((String)cbAnalysis.getData(name));
						if(id == dataSetDTOResponse.getCallingAnalysisId()){
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
				} catch (Exception err) {
					Utils.log(shell, memInfo, log, "Error retrieving Dataset details", err);
				}
			}catch(Exception err){
				Utils.log(shell, memInfo, log, "Error retrieving Dataset details", err);
			}
		}
	});
	}
	
//	private void populateDatasetTable(Table tbList) {
//		// TODO Auto-generated method stub
//		FormUtils.entrySetToTable(Controller.getDataSetNames(), tbList);
//	}
//
//	private void populateExperimentList(Combo cbList) {
//		// TODO Auto-generated method stub
//		FormUtils.entrySetToCombo(Controller.getExperimentNames(), cbList);
//	}

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
//	private void populateExperimentListByProjectId(Combo cbList) {
//		// TODO Auto-generated method stub
//		FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(IDs.projectId), cbList);
//	}

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
		}else if(isNew){
			for(TableItem item : tbList.getItems())
				if(item.getText().equals(txtName.getText())){
					message = "Name of this Dataset already exists for this Experiment!";
					successful = false;
					break;
				}
		}
		if(!successful){
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
	
	protected void saveDataset(boolean isNew){
		try{
			DataSetDTO datasetDTO = null;
			if(isNew){
				datasetDTO = new DataSetDTO(DtoMetaData.ProcessType.CREATE);
				setDatasetDetails(datasetDTO);
			}else{
				datasetDTO = new DataSetDTO(DtoMetaData.ProcessType.UPDATE);
				datasetDTO.setDataSetId(IDs.datasetId);
				setDatasetDetails(datasetDTO);
			}
			try{
				DtoRequestDataSet dtoRequestDataset = new DtoRequestDataSet();
				DataSetDTO datasetDTOResponse = dtoRequestDataset.process(datasetDTO);
				if(Controller.getDTOResponse(shell, datasetDTOResponse, memInfo)){
					populateDatasetListFromSelectedExperiment(IDs.experimentId);
					cleanDetails();
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
			datasetDTO.setStatus(1);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error saving Dataset", err);
		}
	}
}
