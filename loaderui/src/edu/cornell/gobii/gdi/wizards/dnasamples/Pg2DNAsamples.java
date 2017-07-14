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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import edu.cornell.gobii.gdi.utils.Utils;

public class Pg2DNAsamples extends WizardPage {
	private String config;
//	private List<String> header = new ArrayList<>();
	private DTOsamples dto;
	private Table tbFieldHeaders;
	private Table tbGermplasm;
	private Table tbGermplasmProp;

	/**
	 * Create the wizard.
	 */
	public Pg2DNAsamples(String config, DTOsamples dto) {
		super("wizardPage");
		setTitle("Wizard :: DNA sample Information");
		setDescription("");
		this.config = config;
//		this.header = header;
		this.dto = dto;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Group group = new Group(container, SWT.NONE);
		GridData gd_group = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_group.widthHint = 350;
		gd_group.heightHint = 299;
		group.setLayoutData(gd_group);
		group.setText("Data File");
		group.setLayout(new GridLayout(1, false));
		
		tbFieldHeaders = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		tbFieldHeaders.setLinesVisible(true);
		tbFieldHeaders.setHeaderVisible(true);
		tbFieldHeaders.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
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
		
		TableColumn tblclmnFieldHeaders = new TableColumn(tbFieldHeaders, SWT.NONE);
		tblclmnFieldHeaders.setWidth(200);
		tblclmnFieldHeaders.setText("Field Headers");
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tbGermplasm = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbGermplasm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbGermplasm.setSize(599, 368);
		tbGermplasm.setLinesVisible(true);
		tbGermplasm.setHeaderVisible(true);
		
		TableColumn tblclmnIndex = new TableColumn(tbGermplasm, SWT.NONE);
		tblclmnIndex.setWidth(60);
		
		TableColumn tblclmnGermplasmInformation = new TableColumn(tbGermplasm, SWT.NONE);
		tblclmnGermplasmInformation.setWidth(150);
		tblclmnGermplasmInformation.setText("Germplasm Information");
		
		TableColumn tableColumn_1 = new TableColumn(tbGermplasm, SWT.NONE);
		tableColumn_1.setWidth(150);
		tableColumn_1.setText("Header");
		
		TableColumn tableColumn_2 = new TableColumn(tbGermplasm, SWT.NONE);
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("From");
		
		TableColumn tableColumn_3 = new TableColumn(tbGermplasm, SWT.NONE);
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("To");
		
		Utils.unmarshalColumns(tbGermplasm, config+"/xml/Germplasm.xml", dto.getGermplasmFields(), dto.getSubGermplasmFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbGermplasm, dto.getGermplasmFields(), dto.getSubGermplasmFields());
		Utils.setTableMouseLister(tbGermplasm, dto.getGermplasmFields());
		
		tbGermplasmProp = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tbGermplasmProp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbGermplasmProp.setSize(258, 97);
		tbGermplasmProp.setLinesVisible(true);
		tbGermplasmProp.setHeaderVisible(true);
		
		TableColumn tblclmnIndex_1 = new TableColumn(tbGermplasmProp, SWT.NONE);
		tblclmnIndex_1.setWidth(60);
		
		TableColumn tableColumn_4 = new TableColumn(tbGermplasmProp, SWT.NONE);
		tableColumn_4.setWidth(200);
		tableColumn_4.setText("Property");
		
		TableColumn tableColumn_5 = new TableColumn(tbGermplasmProp, SWT.NONE);
		tableColumn_5.setWidth(200);
		tableColumn_5.setText("Value");
		Utils.loadTableProps(tbGermplasmProp, "germplasm_prop", dto.getGermplasmPropFields());
		Utils.setDndColumnTarget(tbFieldHeaders, tbGermplasmProp, dto.getGermplasmPropFields(), null);
		Utils.setTableMouseLister(tbGermplasmProp, dto.getGermplasmPropFields());
		
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
