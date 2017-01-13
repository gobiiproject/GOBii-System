package edu.cornell.gobii.gdi.forms;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestAnalysis;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDisplay;
import org.gobiiproject.gobiimodel.dto.container.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;

import edu.cornell.gobii.gdi.main.Main2;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.services.IDs;
import edu.cornell.gobii.gdi.utils.FormUtils;
import edu.cornell.gobii.gdi.utils.Utils;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FrmTableDisplay extends AbstractFrm{
	private static Logger log = Logger.getLogger(FrmTableDisplay.class.getName());
	private Text textName;
	private Table table;
	private TableEditor editor;
	private SelectionListener tableListener;
	private TableViewer viewerParameters;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public FrmTableDisplay(Shell shell, Composite parent, int style) {
		super(shell, parent, style);
		cbList.setEnabled(false);

		GridLayout gridLayout = (GridLayout) cmpForm.getLayout();
		gridLayout.numColumns = 2;

		Label lblName = new Label(cmpForm, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name:");

		textName = new Text(cmpForm, SWT.BORDER | SWT.READ_ONLY);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(cmpForm, SWT.NONE);

		viewerParameters = new TableViewer(cmpForm, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		table = viewerParameters.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));




		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnColumnName = tableViewerColumn.getColumn();
		tblclmnColumnName.setWidth(150);
		tblclmnColumnName.setText("Parameter");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewerParameters, SWT.NONE);
		TableColumn tblclmnDisplayName = tableViewerColumn_1.getColumn();
		tblclmnDisplayName.setWidth(100);
		tblclmnDisplayName.setText("Value");
		new Label(cmpForm, SWT.NONE);

		table.addListener(SWT.MouseDown, event -> {
			TableEditor editor = new TableEditor(table);
			// The editor must have the same size as the cell and must
			// not be any smaller than 50 pixels.
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.minimumWidth = 50;
			Control oldEditor = editor.getEditor();
			if (oldEditor != null)
				oldEditor.dispose(); 

			Point pt = new Point(event.x, event.y);
			TableItem item = table.getItem(pt);
			if (item == null) return;

			Text newEditor = new Text(table, SWT.NONE);
			int EDITABLECOLUMN = -1;
			for (int i = 1; i < table.getColumnCount(); i++) {
				Rectangle rect = item.getBounds(i);
				if (rect.contains(pt)) {
					EDITABLECOLUMN = i;
					break;
				}
			}

			final int col = EDITABLECOLUMN;
			if(col < 0) return;
			newEditor.setText(item.getText(col));
			Listener textListener = new Listener() {
				public void handleEvent(final Event e) {
					switch (e.type) {
					case SWT.FocusOut:
						item.setText(col, newEditor.getText());
						newEditor.dispose();
						break;
					case SWT.Traverse:
						switch (e.detail) {
						case SWT.TRAVERSE_RETURN:
							item
							.setText(col, newEditor.getText());
							// FALL THROUGH
						case SWT.TRAVERSE_ESCAPE:
							newEditor.dispose();
							e.doit = false;
						}
						break;
					}
				}
			};
			newEditor.addListener(SWT.FocusOut, textListener);
			newEditor.addListener(SWT.Traverse, textListener);
			newEditor.selectAll();
			newEditor.setFocus();           
			editor.setEditor(newEditor, item, col); 
		});

		Button btnUpdate = new Button(cmpForm, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!validate()) return;
				for(TableItem item : table.getSelection()){
					DisplayDTO displayDTO = new DisplayDTO(GobiiProcessType.UPDATE);
					displayDTO.setDisplayId((Integer) item.getData());
					displayDTO.setColumnName(item.getText(0));
					displayDTO.setCreatedBy(1);
					displayDTO.setDisplayName(item.getText(1));
					displayDTO.setCreatedDate(new Date());
					displayDTO.setModifiedBy(1);
					displayDTO.setModifiedDate(new Date());
					displayDTO.setTableName(textName.getText());
					displayDTO.setDisplayRank(0);
					try{
						DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();
						DisplayDTO DisplayDTOResponse = dtoRequestDisplay.process(displayDTO);
						if(Controller.getDTOResponse(shell, DisplayDTOResponse, memInfo, true)){
							populateDisplayDetails();
						};
					}catch(Exception err){
						Utils.log(shell, memInfo, log, "Error updating display name", err);
					}
				}
			}

			private boolean validate() {
				boolean successful = true;
				String message = null;
				MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				int checked = 0;

				for(TableItem tbItem : viewerParameters.getTable().getItems()){
					if(tbItem.getChecked()){
						checked++;
						if(tbItem.getText(1).isEmpty()){
							message = "Please specify a value for "+tbItem.getText(0);
							successful = false;
							break;
						}
					}
				}
				if (checked==0){
					message = "Please select a record!";
					successful = false;
				}

				if(!successful){
					dialog.setMessage(message);
					dialog.open();
				}
				return successful;
			}
		});
		btnUpdate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnUpdate.setText("Update");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	protected void createContent() {
		// TODO Auto-generated method stub
		populateTableNamesList();
		tbList.addListener (SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				populateDisplayDetails();
			}

		});

	}
	protected void populateDisplayDetails() {
		// TODO Auto-generated method stub
		String selected = tbList.getSelection()[0].getText(); //single selection
		List<TableColDisplay> displayCol = (List<TableColDisplay>) tbList.getSelection()[0].getData();
		textName.setText(selected);

		populateTableFromStringList(displayCol, table);
	}

	private void populateTableFromStringList(List<TableColDisplay> displayCol, Table table) {
		// TODO Auto-generated method stub
		table.removeAll();

		TableItem item = null;
		for (TableColDisplay c : displayCol){ //add project on list
			item= new TableItem(table, SWT.NONE); 
			item.setText(0, c.getColumnName()); //index zero - first column 
			item.setText(1, c.getDisplayName());
			item.setData(c.getDisplayId());
		}
	}
	private void populateTableNamesList() {
		// TODO Auto-generated method stub

		FormUtils.entrySetToTableDisplay(Controller.getTableDisplayNames(), tbList);

		TableColumn tblclmnTables = new TableColumn(tbList, SWT.NONE);
		tblclmnTables.setWidth(300);
		tblclmnTables.setText("Tables:");


	}
}
