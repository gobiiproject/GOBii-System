package edu.cornell.gobii.gdi.wizards.datasets;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.custom.SashForm;

public class Page1Datasets extends WizardPage {
	private static Logger log = Logger.getLogger(Page1Datasets.class.getName());
	private Text txtRemotePath;
	private Table tbLocalfiles;
	private static Table tbData;
	
	private String config;
	private FileFormats fileformats;
	private Combo cbFileFormat;
	private DTOdataset dto;
	private Combo cbMarkerOrientation;
	private Combo cbDNAsampleOrientation;
	private Text txtRowCoord;
	private Text txtColCoord;
	private Combo cbProject;
	private Combo cbExperiment;
	private Combo cbDataset;
	private Combo cbMapset;
	private Combo cbTemplates;
	private Combo cbPi;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Button btnQcCheckButton;
	private Text textDatasetType;
	private Text textPlatform;

	/**
	 * Create the wizard.
	 */
	public Page1Datasets(String config, DTOdataset dto) {
		super("wizardPage");
		setTitle("Wizard :: Dataset Information");
		setDescription("Wizard to load dataset information");
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
		
		SashForm sashForm = new SashForm(container, SWT.NONE);
		sashForm.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(sashForm);
		formToolkit.paintBordersFor(sashForm);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Group grpInformation = new Group(scrolledComposite, SWT.NONE);
		grpInformation.setLayout(new GridLayout(2, false));
		grpInformation.setText("Information");
		
		Label lblPi = new Label(grpInformation, SWT.NONE);
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
						dto.setProjectName(cbProject.getText());
						dto.setProjectID(Integer.parseInt(key));
						FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(dto.getProjectID()), cbExperiment);
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Experiments", err);
				}
			}
		});
		cbProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		FormUtils.entrySetToCombo(Controller.getProjectNames(), cbProject);
		
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
						Integer platformId = Controller.getPlatformIdByExperimentId(dto.getExperimentID());
						dto.setPlatformID(platformId);
						WizardUtils.populatePlatformText(getShell(), platformId, textPlatform);
						dto.setPlatformName(textPlatform.getText());
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error retrieving Datasets", err);
				}
			}
		});
		cbExperiment.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDataset = new Label(grpInformation, SWT.NONE);
		lblDataset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDataset.setText("Dataset:");
		
		cbDataset = new Combo(grpInformation, SWT.NONE);
		cbDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					dto.setDatasetName(null);
					dto.setDatasetID(null);
					if(cbDataset.getSelectionIndex() > -1){
						String key = (String) cbDataset.getData(cbDataset.getItem(cbDataset.getSelectionIndex()));
						dto.setDatasetName(cbDataset.getText());
						Integer datasetId = Integer.parseInt(key);
						dto.setDatasetID(datasetId);
						WizardUtils.populateDatasetInformation(getShell(), datasetId, textDatasetType, dto);
					}
				}catch(Exception err){
					Utils.log(getShell(), null, log, "Error selecting Datasets", err);
				}
			}
		});
		cbDataset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDatasetType = new Label(grpInformation, SWT.NONE);
		lblDatasetType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatasetType.setText("Dataset Type:");
		
		textDatasetType = new Text(grpInformation, SWT.BORDER);
		textDatasetType.setEditable(false);
		textDatasetType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(textDatasetType, true, true);
		
		Label lblPlatform = new Label(grpInformation, SWT.NONE);
		lblPlatform.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlatform.setText("Platform:");
		
		textPlatform = new Text(grpInformation, SWT.BORDER);
		textPlatform.setEditable(false);
		textPlatform.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(textPlatform, true, true);
		
		Label lblMapset = new Label(grpInformation, SWT.NONE);
		lblMapset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMapset.setText("Mapset");
		
		cbMapset = new Combo(grpInformation, SWT.NONE);
		cbMapset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cbMapset.getSelectionIndex() == -1){
					dto.setMapsetID(null);
					dto.setMapsetName(null);
				}else{
					String key = (String) cbMapset.getData(cbMapset.getItem(cbMapset.getSelectionIndex()));
					dto.setMapsetID(Integer.parseInt(key));
					dto.setMapsetName(cbMapset.getText());
				}
			}
		});
		cbMapset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblRemotePath = new Label(grpInformation, SWT.NONE);
		lblRemotePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRemotePath.setText("Remote path:");
		
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
		
		Button btnBrowse = new Button(grpInformation, SWT.NONE);
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
		gd_tbLocalfiles.heightHint = 150;
		gd_tbLocalfiles.minimumWidth = 120;
		gd_tbLocalfiles.minimumHeight = 100;
		tbLocalfiles.setLayoutData(gd_tbLocalfiles);
		tbLocalfiles.setHeaderVisible(true);
		tbLocalfiles.setLinesVisible(true);
		Utils.getLocalFiles(tbLocalfiles, dto.getFiles());
		new Label(grpInformation, SWT.NONE);
		
		Button btnRemove = new Button(grpInformation, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.removeFiles(tbLocalfiles, dto.getFiles());
			}
		});
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRemove.setText("Remove Selected File(s)");
		
		Label lblSavedTemplate = new Label(grpInformation, SWT.NONE);
		lblSavedTemplate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSavedTemplate.setText("Saved template:");
		
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
		WizardUtils.getTemplateFiles(TemplateCode.DSS, cbTemplates);
		
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
		fileformats = (FileFormats) Utils.unmarshalFileFormats(config+"/fileformats.xml");
		for(FileFormat fileformat : fileformats.getFileFormat()){
			cbFileFormat.add(fileformat.toString());
		}
		
		Button btnPreviewData = new Button(grpInformation, SWT.NONE);
		btnPreviewData.addSelectionListener(new SelectionAdapter() {
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
//							}
						}
					}
				}else{
					MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING | SWT.OK);  
					messageBox.setMessage("Please select a file format.");  
					messageBox.open();
				}
			}
		});
		btnPreviewData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnPreviewData.setText("Preview Data");
		
		Label lblMarkerOrientation = new Label(grpInformation, SWT.NONE);
		lblMarkerOrientation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMarkerOrientation.setText("Marker Position:");
		
		cbMarkerOrientation = new Combo(grpInformation, SWT.NONE);
		cbMarkerOrientation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cbMarkerOrientation.getSelectionIndex() < 0) return;
				switch(cbMarkerOrientation.getSelectionIndex()){
				case 0 : 	cbDNAsampleOrientation.select(1);
							setOrientation(GobiiColumnType.CSV_COLUMN, GobiiColumnType.CSV_ROW);
							dto.setOrientation(DataSetOrientationType.MARKER_FAST);
							break;
				case 1 :	cbDNAsampleOrientation.select(0);
							setOrientation(GobiiColumnType.CSV_ROW, GobiiColumnType.CSV_COLUMN);
							dto.setOrientation(DataSetOrientationType.SAMPLE_FAST);
							break;
				}
				validateDataCoordinate();
			}
		});
		cbMarkerOrientation.setItems(new String[] {"LEFT", "TOP"});
		cbMarkerOrientation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDnaSampleOrientation = new Label(grpInformation, SWT.NONE);
		lblDnaSampleOrientation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDnaSampleOrientation.setText("DNA sample Position:");
		
		cbDNAsampleOrientation = new Combo(grpInformation, SWT.NONE);
		cbDNAsampleOrientation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cbDNAsampleOrientation.getSelectionIndex() < 0) return;
				switch(cbMarkerOrientation.getSelectionIndex()){
				case 0 :	cbMarkerOrientation.select(1);
							setOrientation(GobiiColumnType.CSV_ROW, GobiiColumnType.CSV_COLUMN);
							dto.setOrientation(DataSetOrientationType.SAMPLE_FAST);
							break;
				case 1 :	cbMarkerOrientation.select(0);
							setOrientation(GobiiColumnType.CSV_COLUMN, GobiiColumnType.CSV_ROW);
							dto.setOrientation(DataSetOrientationType.MARKER_FAST);
							break;
				}
				validateDataCoordinate();
			}
		});
		cbDNAsampleOrientation.setItems(new String[] {"LEFT", "TOP"});
		cbDNAsampleOrientation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpInformation, SWT.NONE);
		
		btnQcCheckButton = new Button(grpInformation, SWT.CHECK);
		btnQcCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dto.setQcCheck(btnQcCheckButton.getSelection());
			}
		});
		btnQcCheckButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(btnQcCheckButton, true, true);
		btnQcCheckButton.setText("QC Check");
		
		Label lblFirstSnpCoordinate = new Label(grpInformation, SWT.NONE);
		lblFirstSnpCoordinate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstSnpCoordinate.setText("First Data coordinate:");
		
		Group group = new Group(grpInformation, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		GridData gd_group = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_group.heightHint = 30;
		group.setLayoutData(gd_group);
		
		txtRowCoord = new Text(group, SWT.BORDER | SWT.READ_ONLY);
		txtRowCoord.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRowCoord.setEnabled(false);
		txtRowCoord.setEditable(false);
		txtRowCoord.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		txtColCoord = new Text(group, SWT.BORDER | SWT.READ_ONLY);
		txtColCoord.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtColCoord.setEnabled(false);
		txtColCoord.setEditable(false);
		txtColCoord.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		scrolledComposite.setContent(grpInformation);
		scrolledComposite.setMinSize(grpInformation.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		tbData = new Table(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		tbData.setLinesVisible(true);
		tbData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent event) {
				Point pt = new Point(event.x, event.y);
				TableItem item = tbData.getItem(pt);
				if (item == null)
			          return;
				int row = tbData.getSelectionIndex();
				int col = -1;
				for (int i = 0; i < tbData.getColumnCount(); i++) {
					Rectangle rect = item.getBounds(i);
					if (rect.contains(pt)) {
						col = i;
					}
				}
				if(col <= 0 || row <= 0) return;
				Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
				for(int i=0; i<tbData.getItemCount(); i++){
					TableItem tabItem = tbData.getItem(i);
					for(int j=0; j<tbData.getColumnCount(); j++)
						tabItem.setBackground(j, white);
				}
				txtRowCoord.setText(""+row); dto.setrCoord(row);
				txtColCoord.setText(""+col); dto.setcCoord(col);
				setColColors(row, col);
				setRowColors(col, row);
				dto.getDtoMarkers().setcCoord(col); dto.getDtoMarkers().setrCoord(row);
				dto.getDtoSamples().setcCoord(col); dto.getDtoSamples().setrCoord(row);
				String field = null;
				String prev = null;
				if(cbMarkerOrientation.getSelectionIndex() == 0){
					// set information for dtoMarker
					dto.getDtoMarkers().getHeader().clear();
					TableItem itemFields = tbData.getItem(row-1);
					TableItem itemPrev = tbData.getItem(row);
					for(int i=0; i<col; i++){
						field = itemFields.getText(i).isEmpty() ? "COL "+i : itemFields.getText(i);
						prev = itemPrev.getText(i);
						dto.getDtoMarkers().getHeader().add(new String[]{field, prev});
					}
					// set information for dtoSample
					dto.getDtoSamples().getHeader().clear();
					for(int i=0; i<row; i++){
						TableItem itemSample = tbData.getItem(i);
						if(col>0 && !itemSample.getText(col-1).isEmpty())
							dto.getDtoSamples().getHeader().add(new String[]{itemSample.getText(col-1), itemSample.getText(col)});
						else
							dto.getDtoSamples().getHeader().add(new String[]{"ROW "+i, itemSample.getText(col)});
//						dto.getDtoSamples().getHeader().add("ROW "+i+"("+itemSample.getText(col)+")");
					}
				}else if(cbMarkerOrientation.getSelectionIndex() == 1){
					// set dtoMarker information
					dto.getDtoMarkers().getHeader().clear();
					for(int i=0; i<row; i++){
						TableItem itemFields = tbData.getItem(i);
						if(col>0 && !itemFields.getText(col-1).isEmpty())
							dto.getDtoMarkers().getHeader().add(new String[]{itemFields.getText(col-1), itemFields.getText(col)});
						else
							dto.getDtoMarkers().getHeader().add(new String[]{"ROW "+i, itemFields.getText(col)});
//						dto.getDtoMarkers().getHeader().add("ROW "+i+"("+itemFields.getText(col)+")");
					}
					// set dtoSample information
					dto.getDtoSamples().getHeader().clear();
					TableItem itemFields = tbData.getItem(row-1);
					TableItem itemPrev = tbData.getItem(row);
					for(int i=0; i<col; i++){
						field = itemFields.getText(i).isEmpty() ? "COL "+i : itemFields.getText(i);
						prev = itemPrev.getText(i);
						dto.getDtoSamples().getHeader().add(new String[]{field, prev});
					}
				}
				
			}
		});
		tbData.setHeaderVisible(true);
		sashForm.setWeights(new int[] {416, 630});
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
					Integer platformId = Controller.getPlatformIdByExperimentId(dto.getExperimentID());
					dto.setPlatformID(platformId);
					WizardUtils.populatePlatformText(getShell(), platformId, textPlatform);
					dto.setPlatformName(textPlatform.getText());
					if(IDs.datasetId!=0){
					 FormUtils.entrySetToComboSelectId(Controller.getDataSetNamesByExperimentId(IDs.experimentId), cbDataset, IDs.datasetId);
						dto.setDatasetName(cbDataset.getText());
						dto.setDatasetID(IDs.datasetId);
						WizardUtils.populateDatasetInformation(getShell(), IDs.datasetId, textDatasetType, dto);
					}else FormUtils.entrySetToCombo(Controller.getDataSetNamesByExperimentId(IDs.experimentId), cbDataset);
				}else FormUtils.entrySetToCombo(Controller.getExperimentNamesByProjectId(IDs.projectId), cbExperiment);
			}else FormUtils.entrySetToCombo(Controller.getProjectNamesByContactId(IDs.PIid), cbProject);
		}
		else FormUtils.entrySetToCombo(Controller.getPIContactNames(), cbPi);
		FormUtils.entrySetToCombo(Controller.getMapNames(), cbMapset);
	}
	
	public void validateDataCoordinate(){
		if(tbData.getItems().length==0){
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Check first data coordinate", "Please click 'preview data' and select the first data coordinate");
		}
		else if(txtRowCoord.getText().isEmpty()){
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Check first data coordinate", "Please select the first data coordinate from the data preview");
		}else{
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Check first data coordinate", "Please re-select the first data coordinate from the data preview");
		}
		txtColCoord.setText(""); dto.setcCoord(-1);
		txtRowCoord.setText(""); dto.setrCoord(-1);
	}
	private void setRowColors(int col, int index){
		Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		for(int i=0; i<index; i++){
			TableItem item = tbData.getItem(i);
			for(int j=col; j<tbData.getColumnCount(); j++){
				item.setBackground(j, gray);
			}
		}
	}
	
	private void setColColors(int row, int index){
		Color blue = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
		for(int i=row; i<tbData.getItemCount(); i++){
			TableItem item = tbData.getItem(i);
			for(int j=0; j<index; j++){
				item.setBackground(j, blue);
			}
		}
	}
	
	private void setOrientation(GobiiColumnType markerOrientation, GobiiColumnType sampleOrientation){
		dto.getDtoMarkers().setColumnType(markerOrientation);
		dto.getDtoSamples().setColumnType(sampleOrientation);
	}
}
