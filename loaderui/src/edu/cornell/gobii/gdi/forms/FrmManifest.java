package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestManifest;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ManifestDTO;

import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class FrmManifest extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmManifest.class.getName());
	private Text txtName;
	private Text txtCode;
	private Text txtFilePath;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmManifest(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;
		
		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCode = new Label(cmpForm, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Code:");
		
		txtCode = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		txtCode.setEditable(false);
		txtCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFilePath = new Label(cmpForm, SWT.NONE);
		lblFilePath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFilePath.setText("File Path:");
		
		txtFilePath = new Text(cmpForm, SWT.BORDER);
		txtFilePath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);
		
		Button btnAddNew = new Button(cmpForm, SWT.NONE);
		btnAddNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(!validate(true)) return;

					DtoRequestManifest dtoRequestManifest = new DtoRequestManifest();
					ManifestDTO manifestDTORequest = new ManifestDTO(DtoMetaData.ProcessType.CREATE);
					String name = txtName.getText();
					manifestDTORequest.setName(name);
					manifestDTORequest.setCode(name);
					manifestDTORequest.setFilePath(txtFilePath.getText());

					try {
						ManifestDTO manifestDTOResponse = dtoRequestManifest.process(manifestDTORequest);
						if(Controller.getDTOResponse(shell, manifestDTOResponse, memInfo)){
							clearDetails();
							populateManifestTable();
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
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
					if(!FormUtils.updateForm(getShell(), "Manifest", IDs.manifestName)) return;
					DtoRequestManifest dtoRequestManifest = new DtoRequestManifest();
					ManifestDTO manifestDTORequest = new ManifestDTO(DtoMetaData.ProcessType.UPDATE);
					manifestDTORequest.setManifestId(IDs.manifestId);
					String name = txtName.getText();
					manifestDTORequest.setName(name);
					manifestDTORequest.setCode(name);
					manifestDTORequest.setFilePath(txtFilePath.getText());

					try {
						ManifestDTO manifestDTOResponse = dtoRequestManifest.process(manifestDTORequest);
						if(Controller.getDTOResponse(shell, manifestDTOResponse, memInfo)){
							clearDetails();
							populateManifestTable();
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
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
				clearDetails();
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

		TableColumn tblclmnManifests = new TableColumn(tbList, SWT.NONE);
		tblclmnManifests.setWidth(300);
		tblclmnManifests.setText("Manifests:");
		
		populateManifestTable();
		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				IDs.manifestName = selected;
				IDs.manifestId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateManifestDetails(IDs.manifestId); 
			}


			protected void populateManifestDetails(int manifestId) {
				try{
					DtoRequestManifest dtoRequestManifest = new DtoRequestManifest();
					ManifestDTO manifestDTO = new ManifestDTO();
					manifestDTO.setManifestId(manifestId);
					try {
						manifestDTO = dtoRequestManifest.process(manifestDTO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//displayDetails
					txtName.setText(manifestDTO.getName());
					txtCode.setText(manifestDTO.getCode());
					txtFilePath.setText(manifestDTO.getFilePath()==null ? "" : manifestDTO.getFilePath());
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
				}
			}
		});
	}
	private void populateManifestTable() {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getManifestNames(), tbList);

		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving Manifests", err);
		}
	}
	
	private void clearDetails(){
		try{
			IDs.manifestId=0;
			txtName.setText("");
			txtCode.setText("");
			txtFilePath.setText("");
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error clearing fields", err);
		}
	}
	
	private boolean validate(boolean isNew){
		boolean successful = true;
		String message = null;
		if(txtName.getText().isEmpty()){
			successful = false;
			message = "Name is required field!";
		}else if(isNew){
			for(TableItem item : tbList.getItems()){
				if(item.getText().equals(txtName.getText())){
					successful = false;
					message = "Manifest name already exists!";
					break;
				}
			}
		}else if(!isNew && IDs.manifestId==0){
			message = "'"+txtName.getText()+"' is recognized as a new value. Please use Add instead.";
			successful = false;
		}
		if(!successful){
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
}
