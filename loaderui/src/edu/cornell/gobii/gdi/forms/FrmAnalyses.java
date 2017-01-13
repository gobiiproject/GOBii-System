package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestAnalysis;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;

public class FrmAnalyses extends AbstractFrm{
	private static Logger log = Logger.getLogger(FrmAnalyses.class.getName());
	private Text txtName;
	private Text txtProgram;
	private Text txtProgramVersion;
	private Text txtAlgorithm;
	private Text txtSourceName;
	private Text txtSourceVersion;
	private Text txtSourceURL;

	private String config;
	private StyledText memoDescription;
	private Combo cbType;
	private Combo cbReference;
	private Table tbParameters;
	private TableViewer viewerParameters;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmAnalyses(final Shell shell, Composite parent, int style, final String config) {
		super(shell, parent, style);
		this.config = config;
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		cbList.setText("Select Analysis Type");

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");

		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDescription = new Label(cmpForm, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Description:");

		memoDescription = new StyledText(cmpForm, SWT.BORDER | SWT.WRAP);
		memoDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label lblType = new Label(cmpForm, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type:");

		cbType = new Combo(cmpForm, SWT.NONE);
		cbType.setEnabled(false);
		cbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblProgram = new Label(cmpForm, SWT.NONE);
		lblProgram.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProgram.setText("Program:");

		txtProgram = new Text(cmpForm, SWT.BORDER);
		txtProgram.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblProgramVersion = new Label(cmpForm, SWT.NONE);
		lblProgramVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProgramVersion.setText("Program version:");

		txtProgramVersion = new Text(cmpForm, SWT.BORDER);
		txtProgramVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblAlgorithm = new Label(cmpForm, SWT.NONE);
		lblAlgorithm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAlgorithm.setText("Algorithm:");

		txtAlgorithm = new Text(cmpForm, SWT.BORDER);
		txtAlgorithm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblSource = new Label(cmpForm, SWT.NONE);
		lblSource.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSource.setText("Source:");

		Group grpSource = new Group(cmpForm, SWT.NONE);
		grpSource.setLayout(new GridLayout(2, false));
		grpSource.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		Label lblSourceName = new Label(grpSource, SWT.NONE);
		lblSourceName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSourceName.setText("Name:");

		txtSourceName = new Text(grpSource, SWT.BORDER);
		txtSourceName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblSourceVersion = new Label(grpSource, SWT.NONE);
		lblSourceVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSourceVersion.setText("Version:");

		txtSourceVersion = new Text(grpSource, SWT.BORDER);
		txtSourceVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblUri = new Label(grpSource, SWT.NONE);
		lblUri.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUri.setText("URI:");

		txtSourceURL = new Text(grpSource, SWT.BORDER);
		txtSourceURL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblParameters = new Label(cmpForm, SWT.NONE);
		lblParameters.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblParameters.setText("Parameters:");

		viewerParameters = new TableViewer(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		tbParameters = viewerParameters.getTable();
		tbParameters.setLinesVisible(true);
		tbParameters.setHeaderVisible(true);
		tbParameters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnParameter = tableViewerColumn.getColumn();
		tblclmnParameter.setWidth(150);
		tblclmnParameter.setText("Parameter");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn_1.getColumn();
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		new Label(cmpForm, SWT.NONE);

		tbParameters.addListener(SWT.MouseDown, event -> {
			TableEditor editor = new TableEditor(tbParameters);
			// The editor must have the same size as the cell and must
			// not be any smaller than 50 pixels.
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.minimumWidth = 50;
			Control oldEditor = editor.getEditor();
			if (oldEditor != null)
				oldEditor.dispose(); 

			Point pt = new Point(event.x, event.y);
			TableItem item = tbParameters.getItem(pt);
			if (item == null)
				return;

			Text newEditor = new Text(tbParameters, SWT.NONE);
			int EDITABLECOLUMN = -1;
			for (int i = 0; i < tbParameters.getColumnCount(); i++) {
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

		Group group = new Group(cmpForm, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		GridData gd_group = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gd_group.heightHint = 48;
		group.setLayoutData(gd_group);

		Button btnNewParameter = new Button(group, SWT.NONE);
		btnNewParameter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnNewParameter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				@SuppressWarnings("unused")
				TableItem item = new TableItem(tbParameters, SWT.NONE);
			}
		});
		btnNewParameter.setText("New Parameter");

		Button btnDeleteParameter = new Button(group, SWT.NONE);
		btnDeleteParameter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnDeleteParameter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanParameterDetails(false);
			}
		});
		btnDeleteParameter.setText("Delete Parameter");

		Label lblReference = new Label(cmpForm, SWT.NONE);
		lblReference.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReference.setText("Reference:");

		cbReference = new Combo(cmpForm, SWT.NONE);
		cbReference.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);
		populateReferenceCombo(cbReference);

		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;
					AnalysisDTO analysisDTO = new AnalysisDTO(DtoMetaData.ProcessType.CREATE);
					analysisDTO.setAnalysisName(txtName.getText());
					analysisDTO.setAnlaysisTypeId(IDs.analysisTypeId);
					analysisDTO.setAnalysisDescription(memoDescription.getText());
					analysisDTO.setProgram(txtProgram.getText());
					analysisDTO.setProgramVersion(txtProgramVersion.getText());
					analysisDTO.setAlgorithm(txtAlgorithm.getText());
					analysisDTO.setSourceName(txtSourceName.getText());
					analysisDTO.setSourceUri(txtSourceURL.getText());
					analysisDTO.setSourceVersion(txtSourceVersion.getText());
					analysisDTO.setStatus(1);
					if(cbReference.getSelectionIndex() > -1){
						String ref = (String) cbReference.getData(cbReference.getItem(cbReference.getSelectionIndex()));
						analysisDTO.setReferenceId(Integer.parseInt(ref));
					}
					for(TableItem item : tbParameters.getItems()){
						EntityPropertyDTO prop = new EntityPropertyDTO();
						if(item.getText(0).isEmpty()) continue;
						prop.setPropertyName(item.getText(0));
						prop.setPropertyValue(item.getText(1));
						analysisDTO.getParameters().add(prop);
					}
					try{
						DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
						AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTO);
						if(Controller.getDTOResponse(shell, analysisDTOResponse, memInfo)){
							populateAnalysisFromSelectedType(IDs.analysisTypeId);
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error saving analysis", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving analysis", err);
				}
			}
		});
		btnAddNew.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAddNew.setText("Add New");
		new Label(cmpForm, SWT.NONE);

		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(false)) return;
					if(!FormUtils.updateForm(getShell(), "Analysis", IDs.analysisName)) return;
					AnalysisDTO analysisDTO = new AnalysisDTO(DtoMetaData.ProcessType.UPDATE);
					analysisDTO.setAnalysisId(IDs.analysisId);
					analysisDTO.setAnalysisName(txtName.getText());
					analysisDTO.setAnlaysisTypeId(IDs.analysisTypeId);
					analysisDTO.setAnalysisDescription(memoDescription.getText());
					analysisDTO.setProgram(txtProgram.getText());
					analysisDTO.setProgramVersion(txtProgramVersion.getText());
					analysisDTO.setAlgorithm(txtAlgorithm.getText());
					analysisDTO.setSourceName(txtSourceName.getText());
					analysisDTO.setSourceUri(txtSourceURL.getText());
					analysisDTO.setSourceVersion(txtSourceVersion.getText());
					analysisDTO.setStatus(1);
					if(cbReference.getSelectionIndex() > -1){
						String ref = (String) cbReference.getData(cbReference.getItem(cbReference.getSelectionIndex()));
						analysisDTO.setReferenceId(Integer.parseInt(ref));
					}
					for(TableItem item : tbParameters.getItems()){
						EntityPropertyDTO prop = new EntityPropertyDTO();
						if(item.getText(0).isEmpty()) continue;
						prop.setPropertyName(item.getText(0));
						prop.setPropertyValue(item.getText(1));
						analysisDTO.getParameters().add(prop);
					}
					try{
						DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
						AnalysisDTO analysisDTOResponse = dtoRequestAnalysis.process(analysisDTO);
						if(Controller.getDTOResponse(shell, analysisDTOResponse, memInfo)){
							populateAnalysisFromSelectedType(IDs.analysisTypeId);
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error saving analysis", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error saving analysis", err);
				}
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnUpdate.setText("Update");
		new Label(cmpForm, SWT.NONE);

		Button btnClearFields = new Button(cmpForm, SWT.NONE);
		btnClearFields.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cleanAnalysisDetails();
			}
		});
		btnClearFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnClearFields.setText("Clear Fields");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		// TODO Auto-generated method stub
		populateAnalysisTypesCombo();
		populateAnalysisTable();
		cbList.addListener (SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String selected = cbList.getText(); //single selection
				cbType.setText(selected);
				IDs.analysisTypeId = Integer.parseInt((String) cbList.getData(selected));
				cleanAnalysisDetails();
				populateAnalysisFromSelectedType(IDs.analysisTypeId ); //retrieve and display projects by contact Id
			}

		});

		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				IDs.analysisName = selected;
				IDs.analysisId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				cleanAnalysisDetails();
				populateAnalysisDetails(IDs.analysisId); //retrieve and display projects by contact Id
			}


			protected void populateAnalysisDetails(int analysisId) {
				//				cleanAnalysisDetails();
				DtoRequestAnalysis dtoRequestAnalysis = new DtoRequestAnalysis();
				AnalysisDTO analysisDTORequest = new AnalysisDTO();
				analysisDTORequest.setAnalysisId(analysisId);
				AnalysisDTO analysisDTOResponse;
				try {
					analysisDTOResponse = dtoRequestAnalysis.process(analysisDTORequest);
					//displayDetails
					txtName.setText(analysisDTOResponse.getAnalysisName());
					if(analysisDTOResponse.getAnalysisDescription()!=null) memoDescription.setText(analysisDTOResponse.getAnalysisDescription());
					populateAnalysisTypesComboAndSelect(cbType, analysisDTOResponse.getAnlaysisTypeId());
					if(analysisDTOResponse.getProgram() != null) txtProgram.setText(analysisDTOResponse.getProgram());
					if(analysisDTOResponse.getProgramVersion()!=null) txtProgramVersion.setText(analysisDTOResponse.getProgramVersion());
					if(analysisDTOResponse.getAlgorithm()!=null) txtAlgorithm.setText(analysisDTOResponse.getAlgorithm());
					if(analysisDTOResponse.getSourceName()!=null) txtSourceName.setText(analysisDTOResponse.getSourceName());
					if(analysisDTOResponse.getSourceVersion()!=null) txtSourceVersion.setText(analysisDTOResponse.getSourceVersion());
					if(analysisDTOResponse.getSourceUri()!=null) txtSourceURL.setText(analysisDTOResponse.getSourceUri());
					if(analysisDTOResponse.getReferenceId()!=null){
						for(int i=0; i<cbReference.getItemCount(); i++){
							String ref = cbReference.getItem(i);
							Integer refId = Integer.parseInt((String) cbReference.getData(ref));
							if(refId == analysisDTOResponse.getReferenceId()){
								cbReference.select(i);
								break;
							}
						}
					}
					for(EntityPropertyDTO property : analysisDTOResponse.getParameters()){
						TableItem item = new TableItem(tbParameters, SWT.NONE);
						String prop = property.getPropertyName();
						String val = property.getPropertyValue() == null ? "" : property.getPropertyValue();
						item.setText(new String[]{prop, val});
					}
				} catch (Exception e) {
					Utils.log(log, "Error getting analysis details", e);
				}


				//				populateTableFromStringList(projectDTO.getProperties(), table);
			}

		});
	}

	private void cleanAnalysisDetails() {
		try{
			txtName.setText("");
			memoDescription.setText("");
			txtProgram.setText("");
			txtProgramVersion.setText("");
			txtAlgorithm.setText("");
			txtSourceName.setText("");
			txtSourceVersion.setText("");
			txtSourceURL.setText("");
			cbReference.deselectAll();
			cbReference.setText("");
			cleanParameterDetails(true);
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error clearing fields", e);
		}
	}

	private void cleanParameterDetails(boolean deleteAll){
		try{
			for(TableItem item : tbParameters.getItems()){
				if(deleteAll || item.getChecked()){
					//				Text txt1 = (Text) item.getData("0"); txt1.dispose();
					//				Text txt2 = (Text) item.getData("1"); txt2.dispose();
					item.dispose();
				}
			}
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error clearing parameter details from form", e);
		}
	}

	protected void populateAnalysisFromSelectedType(int analysisTypeId) {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getAnalysisNamesByTypeId(analysisTypeId), tbList);
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error retrieving analysis names", e);
		}
	}

	private void populateAnalysisTypesCombo() {
		try{
			FormUtils.entrySetToCombo(Controller.getAnalysisTypes(), cbList);
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error retrieving analysis types", e);
		}
	}

	protected void populateReferenceComboAndSelect(Combo comboReference, Integer referenceId) {
		try{
			FormUtils.entrySetToComboSelectId(Controller.getReferenceNames(), comboReference, referenceId);
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error retrieving reference", e);
		}
	}

	protected void populateReferenceCombo(Combo comboReference){
		try{
			FormUtils.entrySetToCombo(Controller.getReferenceNames(), comboReference);
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error retrieving references", e);
		}
	}

	private void populateAnalysisTable() {
		try{
			FormUtils.entrySetToTable(Controller.getAnalysisNames(), tbList);
			
			TableColumn tblclmnAnalyses = new TableColumn(tbList, SWT.NONE);
			tblclmnAnalyses.setWidth(300);
			tblclmnAnalyses.setText("Analyses:");
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error retrieving analysis names", e);
		}

	}
	private void populateAnalysisTypesComboAndSelect(Combo combo, Integer typeId) {
		try{
			FormUtils.entrySetToComboSelectId(Controller.getAnalysisTypes(), combo, typeId);
		}catch(Exception e){
			Utils.log(shell, memInfo, log, "Error retrieving analysis types", e);
		}
	}

	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		if(txtName.getText().isEmpty()){
			message = "Name field is required!";
			successful = false;
		}else if(cbList.getSelectionIndex() < 0){
			message = "Analysis Type field is required!";
			successful = false;
		}else{
			if(isNew)
				for(int i=0; i<tbList.getItemCount(); i++){
					if(tbList.getItem(i).getText(0).equals(txtName.getText())){
						successful = false;
						message = "Name of analysis already exists for this Analysis Type";
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
