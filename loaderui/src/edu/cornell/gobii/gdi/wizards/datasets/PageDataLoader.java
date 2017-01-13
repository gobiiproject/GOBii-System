package edu.cornell.gobii.gdi.wizards.datasets;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Text;

import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PageDataLoader extends WizardPage {
	private Text txtServerFolder;
	private Table tbLocalfiles;
	private Text txtSamplePosition;
	private Text txtMarkerPosition;
	private Text txtAllelePosition;
	private Table tbData;
	private Combo cbFileformat;
	private Button btnViewSampleData;
	private Combo cbSampleOrientation;
	private Combo cbMarkerOrientation;
	
	/**
	 * Global variables
	 */
	private String fileExt;

	/**
	 * Create the wizard.
	 */
	public PageDataLoader() {
		super("wizardPage");
		setTitle("Data Loader");
		setDescription("Load genotype call matrix");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		RowLayout rl_container = new RowLayout(SWT.HORIZONTAL);
		rl_container.wrap = false;
		container.setLayout(rl_container);
		
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new RowData(310, 353));
		group.setBounds(0, 0, 78, 78);
		group.setLayout(new GridLayout(2, false));
		
		Label lblServerFolder = new Label(group, SWT.NONE);
		lblServerFolder.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerFolder.setSize(76, 14);
		lblServerFolder.setText("Server folder:");
		
		txtServerFolder = new Text(group, SWT.BORDER);
		txtServerFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLocalFiles = new Label(group, SWT.NONE);
		lblLocalFiles.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLocalFiles.setSize(61, 14);
		lblLocalFiles.setText("Local files:");
		
		tbLocalfiles = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_tbLocalfiles = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tbLocalfiles.heightHint = 127;
		tbLocalfiles.setLayoutData(gd_tbLocalfiles);
		tbLocalfiles.setHeaderVisible(true);
		tbLocalfiles.setLinesVisible(true);
		
		Label lblFileFormat = new Label(group, SWT.NONE);
		lblFileFormat.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFileFormat.setSize(64, 14);
		lblFileFormat.setText("File format:");
		
		cbFileformat = new Combo(group, SWT.NONE);
		cbFileformat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch (cbFileformat.getSelectionIndex()) {
				case 0: fileExt = ".cssv"; break;
				case 1: fileExt = ".hmp.txt"; break;
				case 2: fileExt = ".vcf"; break;
				default: fileExt = "";
					break;
				}
			}
		});
		cbFileformat.setItems(new String[] {"GENERIC (.csv)", "HAPMAP (.hmp.txt)", "VCF (.vcf)"});
		cbFileformat.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(group, SWT.NONE);
		
		btnViewSampleData = new Button(group, SWT.NONE);
		btnViewSampleData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filename = null;
				for(int i=0; i<tbLocalfiles.getItemCount(); i++){
					if(tbLocalfiles.getItem(i).getText().endsWith(fileExt)){
						filename = tbLocalfiles.getItem(i).getText();
						break;
					}
				}
				loadSampleData(filename);
			}

			private void loadSampleData(String filename) {
				List<List<String>> data = Utils.readHapmapFile(filename, "\t", 50, 50);
				for(int i=0; i<data.size(); i++){
					TableItem item = new TableItem(tbData, SWT.NONE);
					for(int j=0; j<data.get(i).size(); j++){
						item.setText(j, data.get(i).get(j));
					}
				}
			}
		});
		btnViewSampleData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnViewSampleData.setSize(126, 28);
		btnViewSampleData.setText("View Sample data");
		
		Label lblDnaSampleOrientation = new Label(group, SWT.NONE);
		lblDnaSampleOrientation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDnaSampleOrientation.setSize(140, 14);
		lblDnaSampleOrientation.setText("DNA Sample orientatbion:");
		
		cbSampleOrientation = new Combo(group, SWT.NONE);
		cbSampleOrientation.setItems(new String[] {"COLUMN", "ROW"});
		cbSampleOrientation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFirstDnaSample = new Label(group, SWT.NONE);
		lblFirstDnaSample.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstDnaSample.setText("First DNA Sample Name:");
		
		txtSamplePosition = new Text(group, SWT.BORDER);
		txtSamplePosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMarkerOrientation = new Label(group, SWT.NONE);
		lblMarkerOrientation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMarkerOrientation.setSize(104, 14);
		lblMarkerOrientation.setText("Marker orientation:");
		
		cbMarkerOrientation = new Combo(group, SWT.NONE);
		cbMarkerOrientation.setItems(new String[] {"COLUMN", "ROW"});
		cbMarkerOrientation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFirstMarkerName = new Label(group, SWT.NONE);
		lblFirstMarkerName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstMarkerName.setText("First Marker Name:");
		
		txtMarkerPosition = new Text(group, SWT.BORDER);
		txtMarkerPosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFirstAllele = new Label(group, SWT.NONE);
		lblFirstAllele.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFirstAllele.setSize(63, 14);
		lblFirstAllele.setText("First Allele:");
		
		txtAllelePosition = new Text(group, SWT.BORDER);
		txtAllelePosition.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tbData = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		tbData.setLayoutData(new RowData(438, 339));
		tbData.setHeaderVisible(true);
		tbData.setLinesVisible(true);
		
		for(int i=0; i<50; i++){
			TableColumn tc = new TableColumn(tbData, SWT.NONE);
			tc.setWidth(50);
			tc.setText(""+(i));
		}
		
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget dndTarget = new DropTarget(tbLocalfiles, operations);
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] {fileTransfer, textTransfer};
		dndTarget.setTransfer(types);
		dndTarget.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				// will accept text but prefer to have files dropped
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (fileTransfer.isSupportedType(event.dataTypes[i])){
						event.currentDataType = event.dataTypes[i];
						// files should only be copied
						if (event.detail != DND.DROP_COPY) {
							event.detail = DND.DROP_NONE;
						}
						break;
					}
				}
			}
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
				if (textTransfer.isSupportedType(event.currentDataType)) {
					// NOTE: on unsupported platforms this will return null
					Object o = textTransfer.nativeToJava(event.currentDataType);
					String t = (String)o;
					if (t != null) System.out.println(t);
				}
			}
			public void dragOperationChanged(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				// allow text to be moved but files should only be copied
				if (fileTransfer.isSupportedType(event.currentDataType)){
					if (event.detail != DND.DROP_COPY) {
						event.detail = DND.DROP_NONE;
					}
				}
			}
			public void dragLeave(DropTargetEvent event) {
			}
			public void dropAccept(DropTargetEvent event) {
			}
			public void drop(DropTargetEvent event) {
				if (textTransfer.isSupportedType(event.currentDataType)) {
					String text = (String)event.data;
					TableItem item = new TableItem(tbLocalfiles, SWT.NONE);
					item.setText(text);
				}
				if (fileTransfer.isSupportedType(event.currentDataType)){
					String[] files = (String[])event.data;
					for (int i = 0; i < files.length; i++) {
						TableItem item = new TableItem(tbLocalfiles, SWT.NONE);
						item.setText(files[i]);
					}
				}
			}
		});
	}

}
