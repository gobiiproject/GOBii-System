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
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestReference;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ReferenceDTO;

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

public class FrmReferences extends AbstractFrm {
	private static Logger log = Logger.getLogger(FrmReferences.class.getName());
	private Text txtName;
	private Text txtVersion;
	private Text txtLink;
	private Text txtFilePath;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmReferences(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);
		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;
		
		TableColumn tblclmnReferences = new TableColumn(tbList, SWT.NONE);
		tblclmnReferences.setWidth(250);
		tblclmnReferences.setText("References");
		
		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");
		
		txtName = new Text(cmpForm, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVersion = new Label(cmpForm, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText("Version:");
		
		txtVersion = new Text(cmpForm, SWT.BORDER);
		txtVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLink = new Label(cmpForm, SWT.NONE);
		lblLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLink.setText("Link:");
		
		txtLink = new Text(cmpForm, SWT.BORDER);
		txtLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
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

					DtoRequestReference dtoRequestReference = new DtoRequestReference();
					ReferenceDTO referenceDTORequest = new ReferenceDTO(DtoMetaData.ProcessType.CREATE);
					referenceDTORequest.setName(txtName.getText());
					referenceDTORequest.setVersion(txtVersion.getText());
					referenceDTORequest.setLink(txtLink.getText());
					referenceDTORequest.setFilePath(txtFilePath.getText());

					try {
						ReferenceDTO referenceDTOResponse = dtoRequestReference.process(referenceDTORequest);
						if(Controller.getDTOResponse(shell, referenceDTOResponse, memInfo)){
							clearDetails();
							populateReferenceTable();
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving References", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving References", err);
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
					if(!FormUtils.updateForm(getShell(), "Reference", IDs.referenceName)) return;
					DtoRequestReference dtoRequestReference = new DtoRequestReference();
					ReferenceDTO referenceDTORequest = new ReferenceDTO(DtoMetaData.ProcessType.UPDATE);
					referenceDTORequest.setReferenceId(IDs.referenceId);
					referenceDTORequest.setName(txtName.getText());
					referenceDTORequest.setVersion(txtVersion.getText());
					referenceDTORequest.setLink(txtLink.getText());
					referenceDTORequest.setFilePath(txtFilePath.getText());

					try {
						ReferenceDTO referenceDTOResponse = dtoRequestReference.process(referenceDTORequest);
						if(Controller.getDTOResponse(shell, referenceDTOResponse, memInfo)){
							clearDetails();
							populateReferenceTable();
						};
					} catch (Exception err) {
						Utils.log(shell, memInfo, log, "Error retrieving References", err);
					}
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving References", err);
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
		populateReferenceTable();
		tbList.addListener (SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				String selected = tbList.getSelection()[0].getText(); //single selection
				IDs.referenceName = selected;
				IDs.referenceId = Integer.parseInt((String) tbList.getSelection()[0].getData(selected));
				populateReferenceDetails(IDs.referenceId); 
			}


			protected void populateReferenceDetails(int referenceId) {
				try{
					DtoRequestReference dtoRequestReference = new DtoRequestReference();
					ReferenceDTO referenceDTO = new ReferenceDTO();
					referenceDTO.setReferenceId(referenceId);
					try {
						referenceDTO = dtoRequestReference.process(referenceDTO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//displayDetails
					txtName.setText(referenceDTO.getName());
					txtVersion.setText(referenceDTO.getVersion());
					txtLink.setText(referenceDTO.getLink());
					txtFilePath.setText(referenceDTO.getFilePath()==null ? "" : referenceDTO.getFilePath());
				}catch(Exception err){
					Utils.log(shell, memInfo, log, "Error retrieving References", err);
				}
			}
		});
	}
	private void populateReferenceTable() {
		try{
			tbList.removeAll();
			FormUtils.entrySetToTable(Controller.getReferenceNames(), tbList);
		}catch(Exception err){
			Utils.log(shell, memInfo, log, "Error retrieving References", err);
		}
	}
	
	private void clearDetails(){
		try{
			txtName.setText("");
			txtVersion.setText("");
			txtLink.setText("");
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
		}else if(txtVersion.getText().isEmpty()){
			successful = false;
			message = "Version is required field!";
		}else if(isNew){
			for(TableItem item : tbList.getItems()){
				if(item.getText().equals(txtName.getText())){
					successful = false;
					message = "Reference name already exists!";
					break;
				}
			}
		}
		if(!successful){
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(message);
			dialog.open();
		}
		return successful;
	}
}
