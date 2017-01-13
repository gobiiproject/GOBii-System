package edu.cornell.gobii.gdi.wizards.markers;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

import edu.cornell.gobii.gdi.objects.xml.FileFormats;
import edu.cornell.gobii.gdi.objects.xml.FileFormats.FileFormat;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;
import edu.cornell.gobii.gdi.utils.WizardUtils.TemplateCode;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.wb.swt.SWTResourceManager;

public class Pg1Markers extends WizardPage {
	private static Logger log = Logger.getLogger(Pg1Markers.class.getName());
	private Table tbLocalfiles;
	private Text txtHeaderRow;
	private Table tbData;
	private Text txtRemotePath;
	private Combo cbTemplates;
	private Combo cbFileFormat;
	private Button btnPreview;
	private Combo cbProject;
	private Combo cbPlatform;
	private Combo cbExperiment;

	private String config;
	private FileFormats fileformats;
	private DTOmarkers dto = new DTOmarkers();
	private Combo cbOrientation;
	private Combo cbMapset;
	private Combo cbDataset;
	private Button btnRemove;
	private Combo cbPi;
	private Label lblPi;
	private Button btnBrowse;
	private ScrolledComposite scrolledComposite;
	private SashForm sashForm;

	/**
	 * Create the wizard.
	 */
	public Pg1Markers(String config, DTOmarkers dto) {
		super("wizardPage");
		setTitle("Wizard :: Marker Information");
		setDescription("Wizard to load marker & map information to data warehouse.");
		this.config = config;
		this.dto = dto;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));

		sashForm = new SashForm(container, SWT.NONE);
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Group grpInformation = new Group(scrolledComposite, SWT.NONE);
		grpInformation.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		grpInformation.setText("Information");
		grpInformation.setLayout(new GridLayout(2, false));

		lblPi = new Label(grpInformation, SWT.NONE);
		lblPi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPi.setText("PI:");

		cbPi = new Combo(grpInformation, SWT.NONE);
		cbPi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					FormUtils.resetCombo(cbProject);	dto.setProjectID(null);
					FormUtils.resetCombo(cbExperiment);	dto.setExperimentID(null);
					FormUtils.resetCombo(cbDataset);	dto.setDatasetID(null);
					if(cbPi.getSelectionIndex() > -1){
						String key = (String) cbPi.getData(cbPi.getItem(cbPi.getSelectionIndex()));
						FormUtils.entrySetToCombo(Controller.getProjectNamesByContactId(Integer.parseInt(key)), cbProject);
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Projects", err);
				}
			}
		});
		cbPi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblProject = new Label(grpInformation, SWT.NONE);
		lblProject.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProject.setText("Project:");

		cbProject = new Combo(grpInformation, SWT.NONE);
		cbProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					dto.setProjectID(null);
					dto.setProjectName(null);
					FormUtils.resetCombo(cbExperiment);	dto.setExperimentID(null);
					FormUtils.resetCombo(cbDataset);	dto.setDatasetID(null);
					if(cbProject.getSelectionIndex() > -1){
						String key = (String) cbProject.getData(cbProject.getItem(cbProject.getSelectionIndex()));
						dto.setProjectID(Integer.parseInt(key));
						dto.setProjectName(cbProject.getText());
						FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(dto.getProjectID()), cbExperiment);
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Experiments", err);
				}
			}
		});
		cbProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblExperiment = new Label(grpInformation, SWT.NONE);
		lblExperiment.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExperiment.setText("Experiment:");

		cbExperiment = new Combo(grpInformation, SWT.NONE);
		cbExperiment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					dto.setExperimentID(null);
					dto.setExperimentName(null);
					FormUtils.resetCombo(cbDataset);	dto.setDatasetID(null);
					if(cbExperiment.getSelectionIndex() > -1){
						String key = (String) cbExperiment.getData(cbExperiment.getItem(cbExperiment.getSelectionIndex()));
						dto.setExperimentID(Integer.parseInt(key));
						dto.setExperimentName(cbExperiment.getText());
						FormUtils.entrySetToCombo(Controller.getDataSetNamesByExperimentId(dto.getExperimentID()), cbDataset);
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Datasets", err);
				}
			}
		});
		cbExperiment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblPlatform = new Label(grpInformation, SWT.NONE);
		lblPlatform.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlatform.setText("Platform:");

		cbPlatform = new Combo(grpInformation, SWT.NONE);
		cbPlatform.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(cbPlatform.getSelectionIndex() == -1){
						dto.setPlatformID(null);
						dto.setPlatformName(null);
					}else{
						String key = (String) cbPlatform.getData(cbPlatform.getItem(cbPlatform.getSelectionIndex()));
						dto.setPlatformID(Integer.parseInt(key));
						dto.setPlatformName(cbPlatform.getText());
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Platform", err);
				}
			}
		});
		cbPlatform.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblDataset = new Label(grpInformation, SWT.NONE);
		lblDataset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDataset.setText("Dataset:");

		cbDataset = new Combo(grpInformation, SWT.NONE);
		cbDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					dto.setDatasetID(null);
					dto.setDatasetName(null);
					if(cbDataset.getSelectionIndex() > -1){
						String key = (String) cbDataset.getData(cbDataset.getItem(cbDataset.getSelectionIndex()));
						dto.setDatasetID(Integer.parseInt(key));
						dto.setDatasetName(cbDataset.getText());
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error selecting Datasets", err);
				}
			}
		});
		cbDataset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblMapset = new Label(grpInformation, SWT.NONE);
		lblMapset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMapset.setText("Mapset:");

		cbMapset = new Combo(grpInformation, SWT.NONE);
		cbMapset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(cbMapset.getSelectionIndex() == -1){
						dto.setMapsetID(null);
						dto.setMapsetName(null);
					}else{
						String key = (String) cbMapset.getData(cbMapset.getItem(cbMapset.getSelectionIndex()));
						dto.setMapsetID(Integer.parseInt(key));
						dto.setMapsetName(cbMapset.getText());
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Mapset", err);
				}
			}
		});
		cbMapset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblRemotePath = new Label(grpInformation, SWT.NONE);
		lblRemotePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRemotePath.setText("Remote Path:");

		txtRemotePath = new Text(grpInformation, SWT.BORDER);
		txtRemotePath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				boolean isRemote = !txtRemotePath.getText().isEmpty();
				dto.setRemote(isRemote);
				dto.getFile().setSource(isRemote ? txtRemotePath.getText() : null);
			}
		});
		txtRemotePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblLocalFiles = new Label(grpInformation, SWT.NONE);
		lblLocalFiles.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 2));
		lblLocalFiles.setText("Local files:");

		btnBrowse = new Button(grpInformation, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(parent.getShell(), SWT.MULTI);
				//		        dlg.setFilterNames(FILTER_NAMES);
				//		        dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
					// Append all the selected files. Since getFileNames() returns only 
					// the names, and not the path, prepend the path, normalizing
					// if necessary
					StringBuffer buf = new StringBuffer();
					String[] files = dlg.getFileNames();
					for (int i = 0, n = files.length; i < n; i++) {
						buf = new StringBuffer();
						buf.append(dlg.getFilterPath());
						if (buf.charAt(buf.length() - 1) != File.separatorChar) {
							buf.append(File.separatorChar);
						}
						buf.append(files[i]);

						if(!dto.getFiles().contains(buf.toString())){
							TableItem item = new TableItem(tbLocalfiles, SWT.NONE);
							item.setText(buf.toString());
							dto.getFiles().add(buf.toString());
						}
					}
				}
			}
		});
		btnBrowse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnBrowse.setText("Browse");

		tbLocalfiles = new Table(grpInformation, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		GridData gd_tbLocalfiles = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gd_tbLocalfiles.minimumWidth = 150;
		gd_tbLocalfiles.minimumHeight = 150;
		gd_tbLocalfiles.heightHint = 223;
		tbLocalfiles.setLayoutData(gd_tbLocalfiles);
		tbLocalfiles.setHeaderVisible(true);
		tbLocalfiles.setLinesVisible(true);
		Utils.getLocalFiles(tbLocalfiles, dto.getFiles());
		new Label(grpInformation, SWT.NONE);

		btnRemove = new Button(grpInformation, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.removeFiles(tbLocalfiles, dto.getFiles());
			}
		});
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRemove.setText("Remove Selected File(s)");

		Label lblSavedTemplates = new Label(grpInformation, SWT.NONE);
		lblSavedTemplates.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSavedTemplates.setText("Saved Templates:");

		cbTemplates = new Combo(grpInformation, SWT.NONE);
		cbTemplates.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String template = null;
				if(cbTemplates.getSelectionIndex() == -1 || cbTemplates.getText().isEmpty()){
					template = null;
				}else{
					int index = cbTemplates.getSelectionIndex();
					template = cbTemplates.getItem(index);
				}
				dto.setTemplate(template);
			}
		});
		cbTemplates.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		WizardUtils.getTemplateFiles(TemplateCode.MKR, cbTemplates);

		Label lblFileFormat = new Label(grpInformation, SWT.NONE);
		lblFileFormat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileFormat.setText("File format:");

		cbFileFormat = new Combo(grpInformation, SWT.NONE);
		cbFileFormat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(cbFileFormat.getSelectionIndex() == -1) return;
					FileFormat ff = fileformats.getFileFormat().get(cbFileFormat.getSelectionIndex());
					dto.getFile().setGobiiFileType(GobiiFileType.valueOf((ff.getName())));
					dto.getFile().setDelimiter(ff.getDelim());
					dto.setFileExtention(ff.getExtention());
				}catch(Exception err){
					//					Utils.log(getShell(), null, log, "Error retrieving File format", err);
				}
			}
		});
		cbFileFormat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpInformation, SWT.NONE);
		fileformats = (FileFormats) Utils.unmarshalFileFormats(config+"/FileFormats.xml");
		for(FileFormat fileformat : fileformats.getFileFormat()){
			cbFileFormat.add(fileformat.toString());
		}

		btnPreview = new Button(grpInformation, SWT.NONE);
		btnPreview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//				int index = cbFileFormat.getSelectionIndex();
				//				if(index > -1){
				//					FileFormat ff = fileformats.getFileFormat().get(index);
				//					Utils.loadSampleLocalData(tbData, tbLocalfiles, ff.getExtention(), ff.getDelim());
				//				}
				int index = cbFileFormat.getSelectionIndex();
				if(txtRemotePath.getText().trim().isEmpty() && tbLocalfiles.getItems().length<1){
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.OK);  
					messageBox.setMessage("Please select a file or specify a remote path.");  
					messageBox.open();
					return;
				}
				else if(index > -1){
					FileFormat ff = fileformats.getFileFormat().get(index);
					String folder;
					boolean isRemote;
					if(txtRemotePath.getText().trim().isEmpty()){
						folder = null;
						isRemote = false;
					}else{
						folder = txtRemotePath.getText();
						isRemote = true;
					}
					LoaderFilePreviewDTO previewDTO = WizardUtils.previewData(getShell(), isRemote, folder, dto.getFiles(), ff.getExtention());
					if(previewDTO.getDirectoryName() != null){
						dto.setPreviewDTO(previewDTO);
						txtRemotePath.setText(new File(previewDTO.getDirectoryName()).getName());
						Utils.previewData(tbData, previewDTO);
						tbLocalfiles.removeAll();
						for(int i=0; i<dto.getFiles().size(); i++){
							TableItem item = new TableItem(tbLocalfiles, SWT.NONE);
							item.setText(dto.getFiles().get(i));
						}
					}
				}else{
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.OK);  
					messageBox.setMessage("Please select a file format.");  
					messageBox.open();
				}
			}
		});
		btnPreview.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnPreview.setText("Preview Data");

		Label lblOrientation = new Label(grpInformation, SWT.NONE);
		lblOrientation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrientation.setText("Header Position:");

		cbOrientation = new Combo(grpInformation, SWT.NONE);
		cbOrientation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch(cbOrientation.getSelectionIndex()){
				case 0: dto.setColumnType(GobiiColumnType.CSV_COLUMN); break;
				case 1: dto.setColumnType(GobiiColumnType.CSV_ROW); break;
				}
			}
		});
		cbOrientation.setItems(new String[] {"TOP", "LEFT"});
		cbOrientation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblFistSnpCoordinate = new Label(grpInformation, SWT.NONE);
		lblFistSnpCoordinate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFistSnpCoordinate.setText("Field header coordinate:");

		txtHeaderRow = new Text(grpInformation, SWT.BORDER);
		txtHeaderRow.setEnabled(false);
		txtHeaderRow.setEditable(false);
		txtHeaderRow.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		scrolledComposite.setContent(grpInformation);
		scrolledComposite.setMinSize(grpInformation.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		tbData = new Table(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		tbData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = tbData.getSelectionIndex();
				if(index > -1){
					txtHeaderRow.setText(""+tbData.getSelectionIndex());
					dto.getHeader().clear();
					TableItem it = tbData.getItem(index);
					TableItem itPrev = tbData.getItem(index+1);
					for(int i=0; i<50; i++){
						if(it.getText(i)==""){
							break;
						}else{
							dto.getHeader().add(new String[]{it.getText(i), itPrev.getText(i)});
						}
					}
					if(cbFileFormat.getSelectionIndex() > -1){
						dto.setrCoord(index+1);
						dto.setcCoord(-1);
					}else{
						dto.setcCoord(-1);
						dto.setrCoord(-1);
					}
				}
			}
		});
		tbData.setLinesVisible(true);
		tbData.setHeaderVisible(true);
		sashForm.setWeights(new int[] {350, 860});

		createContent();
	}

	public void createContent(){
		if(IDs.PIid!=0){
			FormUtils.entrySetToComboSelectId(Controller.getPIContactNames(), cbPi, IDs.PIid);
			if(IDs.projectId!=0){
				FormUtils.entrySetToComboSelectId(Controller.getProjectNamesByContactId(IDs.PIid), cbProject,IDs.projectId);
				dto.setProjectName(cbProject.getText());
				dto.setProjectID(IDs.projectId);
				if(IDs.experimentId!=0){
					FormUtils.entrySetToComboSelectId(Controller.getExperimentNamesByProjectId(IDs.projectId), cbExperiment, IDs.experimentId);
					dto.setExperimentName(cbExperiment.getText());
					dto.setExperimentID(IDs.experimentId);
					if(IDs.datasetId!=0){
						FormUtils.entrySetToComboSelectId(Controller.getDataSetNamesByExperimentId(IDs.experimentId), cbDataset, IDs.datasetId);
						dto.setDatasetName(cbDataset.getText());
						dto.setDatasetID(IDs.datasetId);
					}else FormUtils.entrySetToCombo(Controller.getDataSetNamesByExperimentId(IDs.experimentId), cbDataset);
				}else FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(IDs.projectId), cbExperiment);
			}else FormUtils.entrySetToCombo(Controller.getProjectNamesByContactId(IDs.PIid), cbProject);
		}
		else FormUtils.entrySetToCombo(Controller.getPIContactNames(), cbPi);
		FormUtils.entrySetToCombo(Controller.getPlatformNames(), cbPlatform);
		FormUtils.entrySetToCombo(Controller.getMapNames(), cbMapset);


	}
}
