package edu.cornell.gobii.gdi.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiimodel.types.GobiiCropType;

import edu.cornell.gobii.gdi.forms.FrmAnalyses;
import edu.cornell.gobii.gdi.forms.FrmCV;
import edu.cornell.gobii.gdi.forms.FrmContacts;
import edu.cornell.gobii.gdi.forms.FrmDatasets;
import edu.cornell.gobii.gdi.forms.FrmExperiments;
import edu.cornell.gobii.gdi.forms.FrmManifest;
import edu.cornell.gobii.gdi.forms.FrmMapset;
import edu.cornell.gobii.gdi.forms.FrmMarkerGroups;
import edu.cornell.gobii.gdi.forms.FrmPlatforms;
import edu.cornell.gobii.gdi.forms.FrmProjects;
import edu.cornell.gobii.gdi.forms.FrmReferences;
import edu.cornell.gobii.gdi.forms.FrmTableDisplay;
import edu.cornell.gobii.gdi.forms.FrmBrowser;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;
import edu.cornell.gobii.gdi.utils.WizardUtils;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;

public class Main2 {

//	private static String config;
	private static Logger log = Logger.getLogger(Main2.class.getName());
	protected Shell shell;
	private CTabFolder tabContent;
	private Display display;
	private Label lblLogo;
	private Tree tree;
	private Combo cbCrop;
	private Button btnCrop;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String config = System.getProperty("user.dir")+"/config";
			App.INSTANCE.load(config+"/App.xml");
			App.INSTANCE.setConfigDir(config);
			App.INSTANCE.setLogFile(config+"/log.txt");
			System.setProperty("log.dir",App.INSTANCE.getConfigDir());
			BasicConfigurator.configure();
			Controller.authenticate(log, true);
			Main2 window = new Main2();
			window.open();
		} catch (Exception e) {
			Utils.log(log, "Error starting application", e);
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		if(!App.INSTANCE.isValid()){
			UserDialog userDialog = new UserDialog(shell);
			if(userDialog.open() == Window.OK){
				for(String item : cbCrop.getItems()){
					if(item.equals(App.INSTANCE.crop)){
						cbCrop.select(cbCrop.indexOf(item));
					}
				}
				resetCrop();
			}else{
				System.exit(1);
			}
		}else{
			for(String item : cbCrop.getItems()){
				if(item.equals(App.INSTANCE.crop)){
					cbCrop.select(cbCrop.indexOf(item));
				}
			}
			resetCrop();
		}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(1280, 1024);
		shell.setText("GOBII :: Genomic Data Integration");
		shell.setLayout(new GridLayout(2, false));
		
		lblLogo = new Label(shell, SWT.NONE);
		GridData gd_lblLogo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblLogo.heightHint = 74;
		gd_lblLogo.widthHint = 157;
		lblLogo.setLayoutData(gd_lblLogo);
		lblLogo.setText("Logo");
		Image imglogo = new Image(display, App.INSTANCE.getConfigDir()+"/logo.png");
		lblLogo.setImage(imglogo);
		
		Label lblGlyph = new Label(shell, SWT.CENTER);
		if(!SystemUtils.IS_OS_WINDOWS){
			lblGlyph.setFont(SWTResourceManager.getFont("Didot", 60, SWT.BOLD));
		}else{
			lblGlyph.setFont(SWTResourceManager.getFont("Century", 60, SWT.BOLD));
		}
		lblGlyph.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblGlyph.setText("GOBII Data Loader");
		
		Composite composite_3 = new Composite(shell, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		GridData gd_composite_3 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite_3.heightHint = 36;
		composite_3.setLayoutData(gd_composite_3);
		//		FormUtils.cropSetToCombo(cbCrop);
				
		btnCrop = new Button(composite_3, SWT.NONE);
		btnCrop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UserDialog userDialog = new UserDialog(shell);
				if(userDialog.open() == Window.OK){
					btnCrop.setText(App.INSTANCE.crop);
					resetCrop();
				}
			}
		});
		btnCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCrop.setText(App.INSTANCE.crop == null ? "CROP" : App.INSTANCE.crop);
		
		cbCrop = new Combo(composite_3, SWT.READ_ONLY);
		cbCrop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cbCrop.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				if(cbCrop.getSelectionIndex() == -1) return;
//				if(cbCrop.getItem(cbCrop.getSelectionIndex()).equals(App.INSTANCE.getCrop())) return;
//				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
//				messageBox.setMessage("Changing CROP will close all form tabs, do you want to continue?");
//				int msg = messageBox.open();
//				if(msg == SWT.YES){
//					resetCrop();
//				}
			}
		});
		cbCrop.setText("Select a Crop");
		cbCrop.setVisible(false);
		new Label(shell, SWT.NONE);
		
		CTabFolder tabFolder = new CTabFolder(shell, SWT.BORDER);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_tabFolder.widthHint = 250;
		gd_tabFolder.heightHint = 673;
		tabFolder.setLayoutData(gd_tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.setSelection(0);
		
		CTabItem tbtmMenu = new CTabItem(tabFolder, SWT.NONE);
		tbtmMenu.setImage(SWTResourceManager.getImage(Main2.class, "/javax/swing/plaf/metal/icons/ocean/homeFolder.gif"));
		tbtmMenu.setText("Menu");
		
		ExpandBar expandBar = new ExpandBar(tabFolder, SWT.NONE);
		tbtmMenu.setControl(expandBar);
		
		ExpandItem xpndtmProjects = new ExpandItem(expandBar, SWT.NONE);
		xpndtmProjects.setExpanded(true);
		xpndtmProjects.setText("Create");
		
		Composite composite = new Composite(expandBar, SWT.NONE);
		xpndtmProjects.setControl(composite);
		xpndtmProjects.setHeight(200);
		composite.setLayout(new GridLayout(1, false));
		
		Button btnProjects = new Button(composite, SWT.NONE);
		btnProjects.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmProjects frm = new FrmProjects(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
				FormUtils.createContentTab(shell, frm, tabContent, "Projects");
//				Utils.createContentTab(frm, "Projects");
			}
		});
		GridData gd_btnProjects = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnProjects.widthHint = 200;
		btnProjects.setLayoutData(gd_btnProjects);
		btnProjects.setText("Projects");
		
		Button btnPlatformExperiments = new Button(composite, SWT.NONE);
		btnPlatformExperiments.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmExperiments frm = new FrmExperiments(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
				FormUtils.createContentTab(shell, frm, tabContent, "Platform Experiments");
//				createContentTab(frm, "Platform Experiments");
			}
		});
		btnPlatformExperiments.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnPlatformExperiments.setText("Platform Experiments");
		
		Button btnAnalyses = new Button(composite, SWT.NONE);
		btnAnalyses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmDatasets frm = new FrmDatasets(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
				FormUtils.createContentTab(shell, frm, tabContent, "Analysis Datasets");
//				createContentTab(frm, "Analysis Datasets");
			}
		});
		btnAnalyses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAnalyses.setText("Analysis Datasets");
		
		Button btnMapsets = new Button(composite, SWT.NONE);
		btnMapsets.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmMapset frm = new FrmMapset(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
				FormUtils.createContentTab(shell, frm, tabContent, "Mapsets");
//				createContentTab(frm, "Mapsets");
			}
		});
		btnMapsets.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnMapsets.setText("Mapsets");
		
		Button btnMarkerGroups = new Button(composite, SWT.NONE);
		btnMarkerGroups.setEnabled(false);
		btnMarkerGroups.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmMarkerGroups frm = new FrmMarkerGroups(shell, tabContent, SWT.NONE);
				FormUtils.createContentTab(shell, frm, tabContent, "Marker Groups");
//				createContentTab(frm, "Marker Groups");
			}
		});
		btnMarkerGroups.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnMarkerGroups.setText("Marker Groups");
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		ExpandItem xpndtmManage = new ExpandItem(expandBar, SWT.NONE);
		xpndtmManage.setText("Define");
		
		Composite composite_1 = new Composite(expandBar, SWT.NONE);
		xpndtmManage.setControl(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		Button btnManageAnalyses = new Button(composite_1, SWT.NONE);
		btnManageAnalyses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmAnalyses frm = new FrmAnalyses(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
				FormUtils.createContentTab(shell, frm, tabContent, "Analyses");
//				createContentTab(frm, "Analyses");
			}
		});
		GridData gd_btnManageAnalyses = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnManageAnalyses.widthHint = 200;
		btnManageAnalyses.setLayoutData(gd_btnManageAnalyses);
		btnManageAnalyses.setText("Analyses");
		
		Button btnManagePlatforms = new Button(composite_1, SWT.NONE);
		btnManagePlatforms.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmPlatforms frm = new FrmPlatforms(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
				FormUtils.createContentTab(shell, frm, tabContent, "Platforms");
//				createContentTab(frm, "Platforms");
			}
		});
		btnManagePlatforms.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnManagePlatforms.setText("Platforms");
		
		Button btnManageCvs = new Button(composite_1, SWT.NONE);
		btnManageCvs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmCV frm = new FrmCV(shell, tabContent, SWT.NONE);
				FormUtils.createContentTab(shell, frm, tabContent, "Controlled Vocabulary");
//				createContentTab(frm, "Controlled Vocabulary");
			}
		});
		btnManageCvs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnManageCvs.setText("Controlled Vocabulary");
		
		Button btnManageTableDisplays = new Button(composite_1, SWT.NONE);
		btnManageTableDisplays.setEnabled(false);
		btnManageTableDisplays.setGrayed(true);
		btnManageTableDisplays.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmTableDisplay frm = new FrmTableDisplay(shell, tabContent, SWT.NONE);
				FormUtils.createContentTab(shell, frm, tabContent, "Table Display");
//				createContentTab(frm, "Table Display");
			}
		});
		btnManageTableDisplays.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnManageTableDisplays.setText("Table Displays");
		
		Button btnManageContacts = new Button(composite_1, SWT.NONE);
		btnManageContacts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmContacts frm = new FrmContacts(shell, tabContent, SWT.NONE);
				FormUtils.createContentTab(shell, frm, tabContent, "Contacts");
//				createContentTab(frm, "Contacts");
			}
		});
		btnManageContacts.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnManageContacts.setText("Contacts");
		
		Button btnManageReferences = new Button(composite_1, SWT.NONE);
		btnManageReferences.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmReferences frm = new FrmReferences(shell, tabContent, SWT.NONE);
				FormUtils.createContentTab(shell, frm, tabContent, "Reference Genomes");
//				createContentTab(frm, "Reference Genomes");
			}
		});
		btnManageReferences.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnManageReferences.setText("References");
		
		Button btnManageManifest = new Button(composite_1, SWT.NONE);
		btnManageManifest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmManifest frm = new FrmManifest(shell, tabContent, SWT.NONE);
				FormUtils.createContentTab(shell, frm, tabContent, "Manifest");
//				createContentTab(frm, "Manifest");
			}
		});

		btnManageManifest.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnManageManifest.setText("Manifest");
		
		Label label_1 = new Label(composite_1, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		xpndtmManage.setHeight(275);
		
		ExpandItem xpndtmWizards = new ExpandItem(expandBar, SWT.NONE);
		xpndtmWizards.setText("Wizards");
		
		Composite composite_2 = new Composite(expandBar, SWT.NONE);
		xpndtmWizards.setControl(composite_2);
		composite_2.setLayout(new GridLayout(1, false));
		
		Button btnMarkerWizard = new Button(composite_2, SWT.FLAT);
		btnMarkerWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateMarkerWizard(shell, App.INSTANCE.getConfigDir());
			}
		});
		GridData gd_btnMarkerWizard = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnMarkerWizard.widthHint = 200;
		btnMarkerWizard.setLayoutData(gd_btnMarkerWizard);
		btnMarkerWizard.setText("Marker Wizard");
		
		Button btnDnaSampleWizard = new Button(composite_2, SWT.FLAT);
		btnDnaSampleWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.createDNASampleWizard(shell, App.INSTANCE.getConfigDir());
			}
		});
		btnDnaSampleWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDnaSampleWizard.setText("DNA Sample Wizard");
		
		Button btnDatasetWizard = new Button(composite_2, SWT.FLAT);
		btnDatasetWizard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardUtils.CreateDatasetWizard(shell, App.INSTANCE.getConfigDir());
			}
		});
		btnDatasetWizard.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDatasetWizard.setText("Dataset Wizard");
		xpndtmWizards.setHeight(150);
		
		CTabItem tbtmProject = new CTabItem(tabFolder, SWT.NONE);
		tbtmProject.setImage(SWTResourceManager.getImage(Main2.class, "/com/sun/java/swing/plaf/windows/icons/DetailsView.gif"));
		tbtmProject.setText("Explorer");
		
		tree = new Tree(tabFolder, SWT.BORDER);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TreeItem item = tree.getItem(point);
				if(item == null) return;
				Integer projId = item.getData("project") != null ? Integer.parseInt((String) item.getData("project").toString()) : null;
				Integer expId = item.getData("experiment") != null ? Integer.parseInt((String) item.getData("experiment").toString()) : null;
				Integer dsId = item.getData("dataset") != null ? Integer.parseInt((String) item.getData("dataset").toString()) : null;
				if(dsId != null && dsId > 0){
					// To nothing
				}else if(expId != null && expId > 0){
					FrmDatasets frm = new FrmDatasets(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
					FormUtils.createContentTab(shell, frm, tabContent, "Datasets");
//					createContentTab(frm, "Datasets");
					IDs.experimentId = expId;
					frm.populateDatasetListFromSelectedExperiment(expId);
				}else if(projId != null && projId > 0){
					FrmExperiments frm = new FrmExperiments(shell, tabContent, SWT.NONE, App.INSTANCE.getConfigDir());
					FormUtils.createContentTab(shell, frm, tabContent, "Platform Experiments");
//					createContentTab(frm, "Experiments");
					IDs.projectId = projId;
					frm.populateExperimentsListFromSelectedProject(projId);
				}
			}
			@Override
			public void mouseDown(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TreeItem item = tree.getItem(point);
				if(item == null || (boolean) item.getData("clicked")) return;
				Integer projId = item.getData("project") != null ? Integer.parseInt((String) item.getData("project").toString()) : null;
				Integer expId = item.getData("experiment") != null ? Integer.parseInt((String) item.getData("experiment").toString()) : null;
				Integer dsId = item.getData("dataset") != null ? Integer.parseInt((String) item.getData("dataset").toString()) : null;
				if(dsId != null && dsId > 0){
					// Do nothing
				}else if(expId != null && expId > 0){
					Set<Entry<String, String>> itemDS = Controller.getDataSetNamesByExperimentId(expId);
					for(Entry<String, String> entryDS : itemDS){
						TreeItem ds = new TreeItem(item, 0);
						ds.setText(entryDS.getValue());
						ds.setData("project", projId);
						ds.setData("experiment", expId);
						ds.setData("dataset", entryDS.getKey());
						ds.setData("clicked", true);
						ds.setBackground(0, display.getSystemColor(SWT.COLOR_GRAY));
					}
					item.setData("clicked", true);
				}else if(projId != null && projId > 0){
					Set<Entry<String, String>> itemExps = Controller.getExperimentNamesByProjectId(projId);
					for(Entry<String, String> entryExp : itemExps){
						TreeItem exp = new TreeItem(item, 0);
						exp.setText(entryExp.getValue());
						exp.setData("project", projId);
						exp.setData("experiment", entryExp.getKey());
						exp.setData("clicked", false);
						exp.setBackground(0, display.getSystemColor(SWT.COLOR_CYAN));
					}
					item.setData("clicked", true);
				}
			}
		});
		tbtmProject.setControl(tree);
		
		CTabItem tbtmHelp = new CTabItem(tabFolder, SWT.NONE);
		tbtmHelp.setImage(SWTResourceManager.getImage(Main2.class, "/javax/swing/plaf/metal/icons/ocean/info.png"));
		tbtmHelp.setText("Help");
		
		Composite composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmHelp.setControl(composite_4);
		composite_4.setLayout(new GridLayout(1, false));
		
		Button btnServiceDesk = new Button(composite_4, SWT.NONE);
		btnServiceDesk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				Program.launch("http://cbsugobii05.tc.cornell.edu:6081/servicedesk/customer/portal/3");
				FrmBrowser browser = new FrmBrowser(tabContent, SWT.None, "http://cbsugobii05.tc.cornell.edu:6081/servicedesk/customer/portal/4");
				FormUtils.createContentTab(shell, browser, tabContent, "Service Desk");
//				createContentTab(browser, "Service Desk");
			}
		});
		btnServiceDesk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnServiceDesk.setText("Service Desk");
		
		Button btnFaq = new Button(composite_4, SWT.NONE);
		btnFaq.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnFaq.setText("FAQ");
		
		Button btnTutorials = new Button(composite_4, SWT.NONE);
		btnTutorials.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnTutorials.setText("Tutorials");
		
		Button btnManuals = new Button(composite_4, SWT.NONE);
		btnManuals.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FrmBrowser browser = new FrmBrowser(tabContent, SWT.None, "http://cbsugobii05.tc.cornell.edu:6084/display/UM/User+Manuals");
				FormUtils.createContentTab(shell, browser, tabContent, "User Manual");
			}
		});
		btnManuals.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnManuals.setText("Manuals");
		
		Button btnTechnicalDocumentation = new Button(composite_4, SWT.NONE);
		btnTechnicalDocumentation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				Program.launch("http://cbsugobii05.tc.cornell.edu:6084/display/TD/Technical+Documentation");
				FrmBrowser browser = new FrmBrowser(tabContent, SWT.None, "http://cbsugobii05.tc.cornell.edu:6084/display/TD/Technical+Documentation");
				FormUtils.createContentTab(shell, browser, tabContent, "Tech Documentation");
//				createContentTab(browser, "Tech Documentation");
			}
		});
		btnTechnicalDocumentation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnTechnicalDocumentation.setText("Technical Documentation");
		
		Button btnAbout = new Button(composite_4, SWT.NONE);
		btnAbout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnAbout.setText("About");
		
		tabContent = new CTabFolder(shell, SWT.BORDER);
		tabContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabContent.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tabFolder.setSelection(0);
	}
	
	private void resetCrop(){
		try{
			for(CTabItem item : tabContent.getItems()){
				item.dispose();
			}
//			String cropName = cbCrop.getItem(cbCrop.getSelectionIndex());
//			GobiiCropType crop = (GobiiCropType) cbCrop.getData(cropName);
//			App.INSTANCE.setCrop(cropName);
//			if(App.INSTANCE.isValid()){
//				App.INSTANCE.save();
//			}
			GobiiCropType crop = GobiiCropType.valueOf(App.INSTANCE.crop);
			ClientContext.getInstance(null, false).setCurrentClientCrop(crop);
			System.out.println(ClientContext.getInstance(null, false).getCurrentCropContextRoot());
			if(Controller.authenticate(log, false)){
				getTreeItems();
			}
		}catch (Exception err) {
			Utils.log(shell, null, log, "Error selecting crop", err);
		}
	}
	
//	private void createContentTab(Composite frm, String title){
//		boolean createNew = true;
//		int index = -1;
//		for(CTabItem item : tabContent.getItems()){
//			if(item.getText().equals(title)){
//				if(MessageDialog.openQuestion(shell, "Confirmation", "This tab is already open, do you want to open a new one?")){
//					createNew = true;
//					index = -1;
//				}else{
//					index = tabContent.indexOf(item);
//					createNew = false;
//				}
//				break;
//			}
//		}
//		if(createNew){
//			CTabItem item = new CTabItem(tabContent, SWT.NONE);
//			item.setText(title);
//			item.setShowClose(true);
//			item.setControl(frm);
//			tabContent.setSelection(item);
//		}else if(index > -1)
//			tabContent.setSelection(index);
//	}

	private void getTreeItems(){
		tree.removeAll();
		Set<Entry<String, String>> itemProjs = Controller.getProjectNames();
		for(Entry<String, String> entryProj : itemProjs){
			TreeItem proj = new TreeItem(tree, 0);
			proj.setText(entryProj.getValue());
			proj.setData("project", entryProj.getKey());
			proj.setData("clicked", false);
		}
		
//		Random r_exp = new Random();
//		Random r_ds = new Random();
//		for(int i=1; i<=100; i++){
//			TreeItem trProj = new TreeItem(tree, 0);
//			trProj.setText("Project :: "+i);
//			for(int j=0; j<r_exp.nextInt(1000); j++){
//				TreeItem trExp = new TreeItem(trProj, 0);
//				trExp.setText("Experiment :: "+j);
//				for(int k=0; k<r_ds.nextInt(1000); k++){
//					TreeItem trDs = new TreeItem(trExp, 0);
//					trDs.setText("Dataset :: "+k);
//				}
//			}
//		}
	}
}
