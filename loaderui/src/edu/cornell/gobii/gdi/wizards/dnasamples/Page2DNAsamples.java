package edu.cornell.gobii.gdi.wizards.dnasamples;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.objects.xml.Columns;
import edu.cornell.gobii.gdi.utils.Utils;

@Deprecated
public class Page2DNAsamples extends WizardPage {
	private Table tbDataColumns;
	private Table tbDNAsample;
	private Table tbDNAsampleProps;
	private Table tbGermplasm;
	private Table tbGermplasmProps;
	private Table tbDNArun;
	private Table tbDNArunProps;
	private Button btnLoad;
	
	private String config;
	private List<String> header = new ArrayList<>();

	/**
	 * Create the wizard.
	 */
	public Page2DNAsamples(String config, List<String> header) {
		super("wizardPage");
		setTitle("Wizard :: DNA sample Information");
		setDescription("");
		this.config = config;
		this.header = header;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(4, false));
		
		Group grpDataColumns = new Group(container, SWT.NONE);
		grpDataColumns.setLayout(new GridLayout(1, false));
		GridData gd_grpDataColumns = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpDataColumns.heightHint = 474;
		gd_grpDataColumns.widthHint = 219;
		grpDataColumns.setLayoutData(gd_grpDataColumns);
		grpDataColumns.setText("Data File");
		
		tbDataColumns = new Table(grpDataColumns, SWT.BORDER | SWT.FULL_SELECTION);
		tbDataColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDataColumns.setHeaderVisible(true);
		tbDataColumns.setLinesVisible(true);
		tbDataColumns.addListener(SWT.Activate, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				tbDataColumns.clearAll();
				tbDataColumns.removeAll();
				for(int i=0; i<header.size(); i++){
					TableItem item = new TableItem(tbDataColumns, SWT.NONE);
					item.setText(0, header.get(i));
				}
			}
		});
		Utils.setDndColumnSource(tbDataColumns);
		
		btnLoad = new Button(grpDataColumns, SWT.NONE);
		btnLoad.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLoad.setText("Load from file");
		
		Group grpGermplasmInformation = new Group(container, SWT.NONE);
		GridData gd_grpGermplasmInformation = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpGermplasmInformation.widthHint = 285;
		grpGermplasmInformation.setLayoutData(gd_grpGermplasmInformation);
		grpGermplasmInformation.setText("Germplasm Information");
		grpGermplasmInformation.setLayout(new GridLayout(1, false));
		
		tbGermplasm = new Table(grpGermplasmInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbGermplasm.setLinesVisible(true);
		tbGermplasm.setHeaderVisible(true);
		tbGermplasm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		Columns columns = (Columns) Utils.unmarshalColumns(config+"/xmlGermplasm.xml");
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbGermplasm, SWT.NONE);
//			item.setText(col);
//		}
//		Utils.setDdnColumnTarget(tbGermplasm);
		
		TableColumn tblclmnName = new TableColumn(tbGermplasm, SWT.NONE);
		tblclmnName.setWidth(70);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnHeader = new TableColumn(tbGermplasm, SWT.NONE);
		tblclmnHeader.setWidth(70);
		tblclmnHeader.setText("Header");
		
		TableColumn tblclmnFrom = new TableColumn(tbGermplasm, SWT.NONE);
		tblclmnFrom.setWidth(30);
		tblclmnFrom.setText("From");
		
		TableColumn tblclmnTo = new TableColumn(tbGermplasm, SWT.NONE);
		tblclmnTo.setWidth(30);
		tblclmnTo.setText("To");
		
		tbGermplasmProps = new Table(grpGermplasmInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbGermplasmProps.setLinesVisible(true);
		tbGermplasmProps.setHeaderVisible(true);
		tbGermplasmProps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tblclmnProperty = new TableColumn(tbGermplasmProps, SWT.NONE);
		tblclmnProperty.setWidth(100);
		tblclmnProperty.setText("Property");
		
		TableColumn tblclmnValue = new TableColumn(tbGermplasmProps, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		
		Group grpDnaSamples = new Group(container, SWT.NONE);
		grpDnaSamples.setText("DNA samples Information");
		grpDnaSamples.setLayout(new GridLayout(1, false));
		GridData gd_grpDnaSamples = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpDnaSamples.widthHint = 267;
		grpDnaSamples.setLayoutData(gd_grpDnaSamples);
		
		tbDNAsample = new Table(grpDnaSamples, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNAsample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDNAsample.setHeaderVisible(true);
		tbDNAsample.setLinesVisible(true);
//		columns = (Columns) Utils.unmarshalColumns(config+"/xmlSample.xml");
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbDNAsample, SWT.NONE);
//			item.setText(col);
//		}
//		Utils.setDdnColumnTarget(tbDNAsample);
		
		TableColumn tblclmnName_1 = new TableColumn(tbDNAsample, SWT.NONE);
		tblclmnName_1.setWidth(70);
		tblclmnName_1.setText("Name");
		
		TableColumn tblclmnHeader_1 = new TableColumn(tbDNAsample, SWT.NONE);
		tblclmnHeader_1.setWidth(70);
		tblclmnHeader_1.setText("Header");
		
		TableColumn tblclmnFrom_1 = new TableColumn(tbDNAsample, SWT.NONE);
		tblclmnFrom_1.setWidth(30);
		tblclmnFrom_1.setText("From");
		
		TableColumn tblclmnTo_1 = new TableColumn(tbDNAsample, SWT.NONE);
		tblclmnTo_1.setWidth(30);
		tblclmnTo_1.setText("To");
		
		tbDNAsampleProps = new Table(grpDnaSamples, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNAsampleProps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDNAsampleProps.setHeaderVisible(true);
		tbDNAsampleProps.setLinesVisible(true);
		
		TableColumn tblclmnProperty_1 = new TableColumn(tbDNAsampleProps, SWT.NONE);
		tblclmnProperty_1.setWidth(100);
		tblclmnProperty_1.setText("Property");
		
		TableColumn tblclmnValue_1 = new TableColumn(tbDNAsampleProps, SWT.NONE);
		tblclmnValue_1.setWidth(100);
		tblclmnValue_1.setText("Value");
		
		Group grpDnaRunInformation = new Group(container, SWT.NONE);
		GridData gd_grpDnaRunInformation = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_grpDnaRunInformation.widthHint = 216;
		grpDnaRunInformation.setLayoutData(gd_grpDnaRunInformation);
		grpDnaRunInformation.setText("DNA run Information");
		grpDnaRunInformation.setLayout(new GridLayout(1, false));
		
		tbDNArun = new Table(grpDnaRunInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNArun.setLinesVisible(true);
		tbDNArun.setHeaderVisible(true);
		tbDNArun.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		columns = (Columns) Utils.unmarshalColumns(config+"/xmlRun.xml");
//		for(String col : columns.getColumn()){
//			TableItem item = new TableItem(tbDNArun, SWT.NONE);
//			item.setText(col);
//		}
//		Utils.setDdnColumnTarget(tbDNArun);
		
		TableColumn tblclmnName_2 = new TableColumn(tbDNArun, SWT.NONE);
		tblclmnName_2.setWidth(70);
		tblclmnName_2.setText("Name");
		
		TableColumn tblclmnHeader_2 = new TableColumn(tbDNArun, SWT.NONE);
		tblclmnHeader_2.setWidth(70);
		tblclmnHeader_2.setText("Header");
		
		TableColumn tblclmnFrom_2 = new TableColumn(tbDNArun, SWT.NONE);
		tblclmnFrom_2.setWidth(30);
		tblclmnFrom_2.setText("From");
		
		TableColumn tblclmnTo_2 = new TableColumn(tbDNArun, SWT.NONE);
		tblclmnTo_2.setWidth(30);
		tblclmnTo_2.setText("To");
		
		tbDNArunProps = new Table(grpDnaRunInformation, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNArunProps.setLinesVisible(true);
		tbDNArunProps.setHeaderVisible(true);
		tbDNArunProps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tblclmnProperty_2 = new TableColumn(tbDNArunProps, SWT.NONE);
		tblclmnProperty_2.setWidth(100);
		tblclmnProperty_2.setText("Property");
		
		TableColumn tblclmnValue_2 = new TableColumn(tbDNArunProps, SWT.NONE);
		tblclmnValue_2.setWidth(100);
		tblclmnValue_2.setText("Value");
	}

}
