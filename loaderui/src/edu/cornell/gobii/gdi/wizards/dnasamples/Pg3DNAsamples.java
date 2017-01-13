package edu.cornell.gobii.gdi.wizards.dnasamples;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.widgets.Button;

public class Pg3DNAsamples extends WizardPage {
	private String config;
	private DTOsamples dto;
	private Table tbFieldHeaders;
	private Table tbDNAsample;
	private Table tbDNArun;
	private Table tbDNAsampleProp;
	private Table tbDNArunProp;

	/**
	 * Create the wizard.
	 */
	public Pg3DNAsamples(String config, DTOsamples dto) {
		super("wizardPage");
		setTitle("Wizard :: DNA sample Information");
		setDescription("");
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
		container.setLayout(new GridLayout(3, false));
		
		Group group = new Group(container, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_group.heightHint = 350;
		gd_group.widthHint = 336;
		group.setLayoutData(gd_group);
		group.setText("Data File");
		group.setLayout(new GridLayout(1, false));
		
		tbFieldHeaders = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		tbFieldHeaders.setLinesVisible(true);
		tbFieldHeaders.setHeaderVisible(true);
		tbFieldHeaders.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(tbFieldHeaders, SWT.NONE);
		tableColumn.setWidth(200);
		tableColumn.setText("Field Headers");
		
//		tbFieldHeader.addListener(SWT.Activate, new Listener() {
//			
//			@Override
//			public void handleEvent(Event arg0) {
//				tbFieldHeader.clearAll();
//				tbFieldHeader.removeAll();
//				for(int i=0; i<dto.getHeader().size(); i++){
//					TableItem item = new TableItem(tbFieldHeader, SWT.NONE);
//					item.setText(0, dto.getHeader().get(i));
//				}
//			}
//		});
		Utils.setDndColumnSource(tbFieldHeaders);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));
		
		tbDNAsample = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNAsample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDNAsample.setLinesVisible(true);
		tbDNAsample.setHeaderVisible(true);
		
		TableColumn tblclmnIndex = new TableColumn(tbDNAsample, SWT.NONE);
		tblclmnIndex.setWidth(60);
		
		TableColumn tblclmnDnasampleInformation = new TableColumn(tbDNAsample, SWT.NONE);
		tblclmnDnasampleInformation.setWidth(150);
		tblclmnDnasampleInformation.setText("DNAsample Information");
		
		TableColumn tableColumn_2 = new TableColumn(tbDNAsample, SWT.NONE);
		tableColumn_2.setWidth(150);
		tableColumn_2.setText("Header");
		
		TableColumn tableColumn_3 = new TableColumn(tbDNAsample, SWT.NONE);
		tableColumn_3.setWidth(50);
		tableColumn_3.setText("From");
		
		TableColumn tableColumn_4 = new TableColumn(tbDNAsample, SWT.NONE);
		tableColumn_4.setWidth(50);
		tableColumn_4.setText("To");
		
		Utils.unmarshalColumns(tbDNAsample, config+"/xml/Sample.xml", dto.getSampleFields(), dto.getSubSampleFields() );
		Utils.setDndColumnTarget(tbFieldHeaders, tbDNAsample, dto.getSampleFields(), dto.getSubSampleFields());
		Utils.setTableMouseLister(tbDNAsample, dto.getSampleFields());
		
		tbDNAsampleProp = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNAsampleProp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDNAsampleProp.setSize(15, 170);
		tbDNAsampleProp.setLinesVisible(true);
		tbDNAsampleProp.setHeaderVisible(true);
		
		TableColumn tblclmnIndex_1 = new TableColumn(tbDNAsampleProp, SWT.NONE);
		tblclmnIndex_1.setWidth(60);
		
		TableColumn tableColumn_9 = new TableColumn(tbDNAsampleProp, SWT.NONE);
		tableColumn_9.setWidth(150);
		tableColumn_9.setText("Property");
		
		TableColumn tableColumn_10 = new TableColumn(tbDNAsampleProp, SWT.NONE);
		tableColumn_10.setWidth(150);
		tableColumn_10.setText("Value");
		Utils.loadTableProps(tbDNAsampleProp, "dnasample_prop", dto.getSamplePropFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbDNAsampleProp, dto.getSamplePropFields(), null);
		Utils.setTableMouseLister(tbDNAsampleProp, dto.getSamplePropFields());
		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_1.setLayout(new GridLayout(1, false));
		
		tbDNArun = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNArun.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDNArun.setLinesVisible(true);
		tbDNArun.setHeaderVisible(true);
		
		TableColumn tblclmnIndex_2 = new TableColumn(tbDNArun, SWT.NONE);
		tblclmnIndex_2.setWidth(60);
		
		TableColumn tblclmnDnarunInformation = new TableColumn(tbDNArun, SWT.NONE);
		tblclmnDnarunInformation.setWidth(150);
		tblclmnDnarunInformation.setText("DNArun / DS_DNArun Information");
		
		TableColumn tableColumn_6 = new TableColumn(tbDNArun, SWT.NONE);
		tableColumn_6.setWidth(150);
		tableColumn_6.setText("Header");
		
		TableColumn tableColumn_7 = new TableColumn(tbDNArun, SWT.NONE);
		tableColumn_7.setWidth(50);
		tableColumn_7.setText("From");
		
		TableColumn tableColumn_8 = new TableColumn(tbDNArun, SWT.NONE);
		tableColumn_8.setWidth(50);
		tableColumn_8.setText("To");
		
		Utils.unmarshalColumns(tbDNArun, config+"/xml/Run.xml", dto.getRunFields(), dto.getSubRunFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbDNArun, dto.getRunFields(), dto.getSubRunFields());
		Utils.setTableMouseLister(tbDNArun, dto.getRunFields());
		
		tbDNArunProp = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tbDNArunProp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbDNArunProp.setLinesVisible(true);
		tbDNArunProp.setHeaderVisible(true);
		
		TableColumn tblclmnIndex_3 = new TableColumn(tbDNArunProp, SWT.NONE);
		tblclmnIndex_3.setWidth(60);
		
		TableColumn tableColumn_11 = new TableColumn(tbDNArunProp, SWT.NONE);
		tableColumn_11.setWidth(150);
		tableColumn_11.setText("Property");
		
		TableColumn tableColumn_12 = new TableColumn(tbDNArunProp, SWT.NONE);
		tableColumn_12.setWidth(150);
		tableColumn_12.setText("Value");
		Utils.loadTableProps(tbDNArunProp, "dnarun_prop", dto.getRunPropFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbDNArunProp, dto.getRunPropFields(), null);
		Utils.setTableMouseLister(tbDNArunProp, dto.getRunPropFields());
		
		TableColumn tblclmnPreview = new TableColumn(tbFieldHeaders, SWT.NONE);
		tblclmnPreview.setWidth(100);
		tblclmnPreview.setText("Preview");
		
		container.addListener(SWT.Show, new Listener(){

			@Override
			public void handleEvent(Event arg0) {
				tbFieldHeaders.clearAll();
				tbFieldHeaders.removeAll();
				for(int i=0; i<dto.getHeader().size(); i++){
					TableItem item = new TableItem(tbFieldHeaders, SWT.NONE);
					item.setText(dto.getHeader().get(i));
//					item.setText(0, dto.getHeader().get(i));
				}
			}
			
		});
	}

}
