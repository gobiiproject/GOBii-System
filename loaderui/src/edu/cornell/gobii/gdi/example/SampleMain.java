package edu.cornell.gobii.gdi.example;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.RowLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.StyledText;

public class SampleMain {

	protected Shell shell;
	private Table table;
	private Table table_1;
	private Table table_2;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Table table_3;
	private Table table_4;
	private Table table_5;
	private Table table_6;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	private Text text_8;
	private Text text_9;
	private Text text_10;
	private Table table_7;
	private Table table_8;
	private Table table_9;
	private Table table_10;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		runApp();
	}
	
	public static void runApp(){
		try {
			SampleMain window = new SampleMain();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
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
		shell.setSize(1024, 800);
		shell.setText("SWT Application");
		shell.setLayout(new BorderLayout(0, 0));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.WEST);
		composite.setLayout(new GridLayout(1, false));
		
		ExpandBar expandBar = new ExpandBar(composite, SWT.V_SCROLL);
		GridData gd_expandBar = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_expandBar.heightHint = 767;
		gd_expandBar.widthHint = 255;
		expandBar.setLayoutData(gd_expandBar);
		
		ExpandItem xpndtmAnalysis = new ExpandItem(expandBar, SWT.NONE);
		xpndtmAnalysis.setText("Analysis");
		
		Composite composite_3 = new Composite(expandBar, SWT.NONE);
		xpndtmAnalysis.setControl(composite_3);
		composite_3.setLayout(new GridLayout(2, false));
		
		Label lblAnalysis = new Label(composite_3, SWT.NONE);
		lblAnalysis.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAnalysis.setText("Analysis:");
		
		Combo combo_4 = new Combo(composite_3, SWT.NONE);
		combo_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType = new Label(composite_3, SWT.NONE);
		lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType.setText("Type:");
		
		Combo combo_5 = new Combo(composite_3, SWT.NONE);
		combo_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProgram = new Label(composite_3, SWT.NONE);
		lblProgram.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProgram.setText("Program:");
		
		Combo combo_6 = new Combo(composite_3, SWT.NONE);
		combo_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAlgorithm = new Label(composite_3, SWT.NONE);
		lblAlgorithm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAlgorithm.setText("Algorithm");
		
		Combo combo_7 = new Combo(composite_3, SWT.NONE);
		combo_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProperties_2 = new Label(composite_3, SWT.NONE);
		lblProperties_2.setText("Properties:");
		
		table_2 = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_2.setHeaderVisible(true);
		table_2.setLinesVisible(true);
		
		TableColumn tblclmnProperty_1 = new TableColumn(table_2, SWT.NONE);
		tblclmnProperty_1.setWidth(100);
		tblclmnProperty_1.setText("Property");
		
		TableColumn tblclmnValue_2 = new TableColumn(table_2, SWT.NONE);
		tblclmnValue_2.setWidth(100);
		tblclmnValue_2.setText("Value");
		new Label(composite_3, SWT.NONE);
		
		Button btnRefresh_4 = new Button(composite_3, SWT.NONE);
		btnRefresh_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_4.setText("Refresh");
		xpndtmAnalysis.setHeight(250);
		
		ExpandItem xpndtmDnaSamples = new ExpandItem(expandBar, SWT.NONE);
		xpndtmDnaSamples.setText("DNA Samples");
		
		Composite composite_6 = new Composite(expandBar, SWT.NONE);
		xpndtmDnaSamples.setControl(composite_6);
		composite_6.setLayout(new GridLayout(2, false));
		new Label(composite_6, SWT.NONE);
		
		Button btnBrowse_1 = new Button(composite_6, SWT.NONE);
		btnBrowse_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnBrowse_1.setText("Browse");
		
		Label lblDnaSamples = new Label(composite_6, SWT.NONE);
		lblDnaSamples.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDnaSamples.setText("DNA Samples:");
		
		table_4 = new Table(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		table_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_4.setHeaderVisible(true);
		table_4.setLinesVisible(true);
		
		TableColumn tblclmnDnaSample = new TableColumn(table_4, SWT.NONE);
		tblclmnDnaSample.setWidth(100);
		tblclmnDnaSample.setText("DNA sample");
		new Label(composite_6, SWT.NONE);
		
		Button btnRefresh_2 = new Button(composite_6, SWT.NONE);
		btnRefresh_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_2.setText("Refresh");
		xpndtmDnaSamples.setHeight(200);
		
		ExpandItem xpndtmGerplasm = new ExpandItem(expandBar, SWT.NONE);
		xpndtmGerplasm.setText("Gerplasm");
		
		Composite composite_7 = new Composite(expandBar, SWT.NONE);
		xpndtmGerplasm.setControl(composite_7);
		composite_7.setLayout(new GridLayout(2, false));
		new Label(composite_7, SWT.NONE);
		
		Button btnBrowse_2 = new Button(composite_7, SWT.NONE);
		btnBrowse_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnBrowse_2.setText("Browse");
		
		Label lblGerplasm = new Label(composite_7, SWT.NONE);
		lblGerplasm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGerplasm.setText("Gerplasm:");
		
		table_5 = new Table(composite_7, SWT.BORDER | SWT.FULL_SELECTION);
		table_5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_5.setHeaderVisible(true);
		table_5.setLinesVisible(true);
		
		TableColumn tblclmnGerplasm = new TableColumn(table_5, SWT.NONE);
		tblclmnGerplasm.setWidth(100);
		tblclmnGerplasm.setText("Gerplasm");
		
		Label lblSpecies = new Label(composite_7, SWT.NONE);
		lblSpecies.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpecies.setText("Species:");
		
		Combo combo_13 = new Combo(composite_7, SWT.NONE);
		combo_13.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProperties_3 = new Label(composite_7, SWT.NONE);
		lblProperties_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProperties_3.setText("Properties:");
		
		table_6 = new Table(composite_7, SWT.BORDER | SWT.FULL_SELECTION);
		table_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_6.setHeaderVisible(true);
		table_6.setLinesVisible(true);
		
		TableColumn tblclmnProperty_2 = new TableColumn(table_6, SWT.NONE);
		tblclmnProperty_2.setWidth(100);
		tblclmnProperty_2.setText("Property");
		
		TableColumn tblclmnValue_3 = new TableColumn(table_6, SWT.NONE);
		tblclmnValue_3.setWidth(100);
		tblclmnValue_3.setText("Value");
		new Label(composite_7, SWT.NONE);
		
		Button btnRefresh_3 = new Button(composite_7, SWT.NONE);
		btnRefresh_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_3.setText("Refresh");
		xpndtmGerplasm.setHeight(300);
		
		ExpandItem xpndtmManifest = new ExpandItem(expandBar, SWT.NONE);
		xpndtmManifest.setText("Manifest");
		
		Composite composite_8 = new Composite(expandBar, SWT.NONE);
		xpndtmManifest.setControl(composite_8);
		composite_8.setLayout(new GridLayout(2, false));
		
		Label lblManifest = new Label(composite_8, SWT.NONE);
		lblManifest.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblManifest.setText("Manifest:");
		
		Combo combo_12 = new Combo(composite_8, SWT.NONE);
		combo_12.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		xpndtmManifest.setHeight(100);
		
		ExpandItem xpndtmMap = new ExpandItem(expandBar, SWT.NONE);
		xpndtmMap.setText("Map");
		
		Composite composite_4 = new Composite(expandBar, SWT.NONE);
		xpndtmMap.setControl(composite_4);
		composite_4.setLayout(new GridLayout(2, false));
		
		Label lblMap = new Label(composite_4, SWT.NONE);
		lblMap.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMap.setText("Map:");
		
		Combo combo_8 = new Combo(composite_4, SWT.NONE);
		combo_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblType_1 = new Label(composite_4, SWT.NONE);
		lblType_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblType_1.setText("Type:");
		
		Combo combo_9 = new Combo(composite_4, SWT.NONE);
		combo_9.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblReference = new Label(composite_4, SWT.NONE);
		lblReference.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReference.setText("Reference:");
		
		Combo combo_10 = new Combo(composite_4, SWT.NONE);
		combo_10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLinkageGroup = new Label(composite_4, SWT.NONE);
		lblLinkageGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLinkageGroup.setText("Linkage group:");
		
		Combo combo_11 = new Combo(composite_4, SWT.NONE);
		combo_11.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPosition = new Label(composite_4, SWT.NONE);
		lblPosition.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPosition.setText("Position:");
		
		text = new Text(composite_4, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStart = new Label(composite_4, SWT.NONE);
		lblStart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStart.setText("Start:");
		
		text_1 = new Text(composite_4, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblStop = new Label(composite_4, SWT.NONE);
		lblStop.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblStop.setText("Stop:");
		
		text_2 = new Text(composite_4, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_4, SWT.NONE);
		
		Button btnRefresh = new Button(composite_4, SWT.NONE);
		btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh.setText("Refresh");
		xpndtmMap.setHeight(250);
		
		ExpandItem xpndtmMarkers = new ExpandItem(expandBar, SWT.NONE);
		xpndtmMarkers.setText("Markers");
		
		Composite composite_5 = new Composite(expandBar, SWT.NONE);
		xpndtmMarkers.setControl(composite_5);
		composite_5.setLayout(new GridLayout(2, false));
		new Label(composite_5, SWT.NONE);
		
		Button btnBrowse = new Button(composite_5, SWT.NONE);
		btnBrowse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnBrowse.setText("Browse");
		
		Label lblMarkers = new Label(composite_5, SWT.NONE);
		lblMarkers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMarkers.setText("Markers:");
		
		table_3 = new Table(composite_5, SWT.BORDER | SWT.FULL_SELECTION);
		table_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_3.setHeaderVisible(true);
		table_3.setLinesVisible(true);
		
		TableColumn tblclmnMarker = new TableColumn(table_3, SWT.NONE);
		tblclmnMarker.setWidth(100);
		tblclmnMarker.setText("Marker");
		new Label(composite_5, SWT.NONE);
		
		Button btnRefresh_1 = new Button(composite_5, SWT.NONE);
		btnRefresh_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_1.setText("Refresh");
		xpndtmMarkers.setHeight(200);
		
		ExpandItem xpndtmMarkerGroups = new ExpandItem(expandBar, SWT.NONE);
		xpndtmMarkerGroups.setText("Marker Groups / Haplotypes");
		
		Composite composite_10 = new Composite(expandBar, SWT.NONE);
		xpndtmMarkerGroups.setControl(composite_10);
		composite_10.setLayout(new GridLayout(2, false));
		
		Label lblGroup = new Label(composite_10, SWT.NONE);
		lblGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGroup.setText("Marker Group:");
		
		Combo combo_14 = new Combo(composite_10, SWT.NONE);
		combo_14.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblGermplasmGroup = new Label(composite_10, SWT.NONE);
		lblGermplasmGroup.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGermplasmGroup.setText("Germplasm Group:");
		
		Combo combo_15 = new Combo(composite_10, SWT.NONE);
		combo_15.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_10, SWT.NONE);
		
		Button btnHasFavoriteAllele = new Button(composite_10, SWT.CHECK);
		btnHasFavoriteAllele.setText("Has favorite allele");
		new Label(composite_10, SWT.NONE);
		
		Button btnRefresh_5 = new Button(composite_10, SWT.NONE);
		btnRefresh_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_5.setText("Refresh");
		xpndtmMarkerGroups.setHeight(150);
		
		ExpandItem xpndtmPlatform = new ExpandItem(expandBar, SWT.NONE);
		xpndtmPlatform.setText("Platform");
		
		Composite composite_2 = new Composite(expandBar, SWT.NONE);
		xpndtmPlatform.setControl(composite_2);
		composite_2.setLayout(new GridLayout(2, false));
		
		Label lblPlatform = new Label(composite_2, SWT.NONE);
		lblPlatform.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlatform.setText("Platform:");
		
		Combo combo_2 = new Combo(composite_2, SWT.NONE);
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVendor = new Label(composite_2, SWT.NONE);
		lblVendor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVendor.setText("Vendor:");
		
		Combo combo_3 = new Combo(composite_2, SWT.NONE);
		combo_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProperties_1 = new Label(composite_2, SWT.NONE);
		lblProperties_1.setText("Properties:");
		
		table_1 = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnProperty = new TableColumn(table_1, SWT.NONE);
		tblclmnProperty.setWidth(100);
		tblclmnProperty.setText("Property");
		
		TableColumn tblclmnValue_1 = new TableColumn(table_1, SWT.NONE);
		tblclmnValue_1.setWidth(100);
		tblclmnValue_1.setText("Value");
		new Label(composite_2, SWT.NONE);
		
		Button btnRefresh_6 = new Button(composite_2, SWT.NONE);
		btnRefresh_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_6.setText("Refresh");
		xpndtmPlatform.setHeight(200);
		
		ExpandItem xpndtmProject = new ExpandItem(expandBar, SWT.NONE);
		xpndtmProject.setText("Project");
		
		Composite composite_1 = new Composite(expandBar, SWT.NONE);
		xpndtmProject.setControl(composite_1);
		composite_1.setLayout(new GridLayout(2, false));
		
		Label lblProjectName = new Label(composite_1, SWT.NONE);
		lblProjectName.setText("Project Name:");
		
		Combo combo = new Combo(composite_1, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPiContact = new Label(composite_1, SWT.NONE);
		lblPiContact.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPiContact.setText("PI contact:");
		
		Combo combo_1 = new Combo(composite_1, SWT.NONE);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProperties = new Label(composite_1, SWT.NONE);
		lblProperties.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProperties.setText("Properties:");
		
		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Property");
		
		TableColumn tblclmnValue = new TableColumn(table, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		new Label(composite_1, SWT.NONE);
		
		Button btnRefresh_7 = new Button(composite_1, SWT.NONE);
		btnRefresh_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRefresh_7.setText("Refresh");
		xpndtmProject.setHeight(200);
		
		Composite composite_9 = new Composite(shell, SWT.NONE);
		composite_9.setLayoutData(BorderLayout.CENTER);
		composite_9.setLayout(new GridLayout(3, false));
		
		Group grpStats = new Group(composite_9, SWT.NONE);
		grpStats.setLayout(new GridLayout(2, false));
		GridData gd_grpStats = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_grpStats.heightHint = 183;
		gd_grpStats.widthHint = 194;
		grpStats.setLayoutData(gd_grpStats);
		grpStats.setText("Stats");
		
		Label lblDnaSamples_1 = new Label(grpStats, SWT.NONE);
		lblDnaSamples_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDnaSamples_1.setText("# DNA samples:");
		
		text_4 = new Text(grpStats, SWT.BORDER);
		text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMarkers_1 = new Label(grpStats, SWT.NONE);
		lblMarkers_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMarkers_1.setText("# Markers:");
		
		text_5 = new Text(grpStats, SWT.BORDER);
		text_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDatasets = new Label(grpStats, SWT.NONE);
		lblDatasets.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatasets.setText("# Datasets:");
		
		text_6 = new Text(grpStats, SWT.BORDER);
		text_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblExperiments = new Label(grpStats, SWT.NONE);
		lblExperiments.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblExperiments.setText("# Experiments:");
		
		text_7 = new Text(grpStats, SWT.BORDER);
		text_7.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPlatforms = new Label(grpStats, SWT.NONE);
		lblPlatforms.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlatforms.setText("# Platforms:");
		
		text_8 = new Text(grpStats, SWT.BORDER);
		text_8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMaps = new Label(grpStats, SWT.NONE);
		lblMaps.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaps.setText("# Maps:");
		
		text_9 = new Text(grpStats, SWT.BORDER);
		text_9.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblProjects = new Label(grpStats, SWT.NONE);
		lblProjects.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblProjects.setText("# Projects:");
		
		text_10 = new Text(grpStats, SWT.BORDER);
		text_10.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		table_9 = new Table(composite_9, SWT.BORDER | SWT.FULL_SELECTION);
		table_9.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_9.setHeaderVisible(true);
		table_9.setLinesVisible(true);
		
		table_10 = new Table(composite_9, SWT.BORDER | SWT.FULL_SELECTION);
		table_10.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_10.setHeaderVisible(true);
		table_10.setLinesVisible(true);
		new Label(composite_9, SWT.NONE);
		
		table_8 = new Table(composite_9, SWT.BORDER | SWT.FULL_SELECTION);
		table_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_8.setHeaderVisible(true);
		table_8.setLinesVisible(true);
		
		table_7 = new Table(composite_9, SWT.BORDER | SWT.FULL_SELECTION);
		table_7.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_7.setHeaderVisible(true);
		table_7.setLinesVisible(true);

	}
}
