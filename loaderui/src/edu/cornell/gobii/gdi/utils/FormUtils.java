package edu.cornell.gobii.gdi.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.entity.TableColDisplay;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.ProjectDTO;

import edu.cornell.gobii.gdi.services.Controller;

public class FormUtils {
	public static String newLine = System.getProperty("line.separator");

	public static void entrySetToCombo(List<NameIdDTO> list, Combo combo) {
		// TODO Auto-generated method stub
		if(combo.getItemCount()>0) combo.removeAll();
		for (NameIdDTO entry : list){ //add contact on list
			String input = entry.getName();
			String output = input.substring(0, 1).toUpperCase() + input.substring(1);
			combo.add(output); //contact name
			combo.setData(output, entry.getId().toString()); // pair name with id
		}
	}

	public static void exportTableAsTxt(Shell shell, Table tbList, String fileName) {
		// TODO Auto-generated method stub
		TableItem[] items = tbList.getItems();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog
		.setFilterNames(new String[] { "Txt Files" });
		dialog.setFilterExtensions(new String[] { "*.txt" }); // Windows
		dialog.setFilterPath(System.getProperty("user.dir")); // Windows path
		dialog.setFileName(fileName+".txt");
		dialog.setOverwrite(true);
		dialog.open();
		File fl = new File(dialog.getFilterPath() + System.getProperty("file.separator") + dialog.getFileName());
		FileWriter flwr;

		int cls = tbList.getColumnCount();
		try {
			flwr = new FileWriter(fl);
			
			//writeColumns
			for (int j = 0; j < cls; j++) {
				flwr.write(tbList.getColumn(j).getText() + "\t");
			}
			flwr.write(newLine);
			
			//proceedToRows
			for (int i = 0; i < items.length; i++) {
				for (int j = 0; j <= cls; j++) {
					flwr.write(items[i].getText(j) + "\t");
				}
				flwr.write(newLine);
			}
			flwr.flush();
			flwr.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void entrySetToComboSelectId(List<NameIdDTO> list, Combo combo, int id) {
		// TODO Auto-generated method stub
		int count=0;
		int selectIndex = 0;
		if(combo.getItemCount()>0) combo.removeAll();
		for (NameIdDTO entry : list){ //add contact on list
			combo.add( entry.getName()); //contact name
			combo.setData(entry.getName(), entry.getId().toString()); // pair name with id
			if (id==entry.getId()) selectIndex=count;
			count++;
		}
		combo.select(selectIndex);
	}

	public static void entrySetToTable(List<NameIdDTO> list, Table tbList) {
		// TODO Auto-generated method stub
		TableItem item = null;
		for (NameIdDTO entry : list){ //add project on list
			item = new TableItem(tbList, SWT.NONE); // single column = index zero
			item.setText( entry.getName());//project name
			item.setData(entry.getName(), entry.getId().toString()); // pair name with id
			item.setData("checked", false);
		}
	}

	public static void entrySetToTableDisplay(Set<Entry<String, List<TableColDisplay>>> tableDisplayNames, Table table) {
		// TODO Auto-generated method stub
		TableItem item = null;
		for (Entry<String, List<TableColDisplay>> entry : tableDisplayNames){ //add project on list
			item = new TableItem(table, SWT.NONE); // single column = index zero
			item.setText((String) entry.getKey());//project name
			item.setData(entry.getValue()); // pair name with columns and displays list
		}
	}

	public static void cropSetToCombo(Combo combo){
		combo.removeAll();
		try {
			List<String> crops = ClientContext.getInstance(null, false).getCropTypeTypes();
			for(String crop : crops){
				combo.add(crop);
				combo.setData(crop, crop);
			}
			
		} catch (Exception err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}
	}

	public static void createContentTab(Shell shell, Composite frm, CTabFolder tabContent, String title){
		boolean createNew = true;
		int index = -1;
		for(CTabItem item : tabContent.getItems()){
			if(item.getText().equals(title)){
				if(MessageDialog.openQuestion(shell, "Confirmation", "This tab is already open, do you want to open a new one?")){
					createNew = true;
					index = -1;
				}else{
					index = tabContent.indexOf(item);
					createNew = false;
				}
				break;
			}
		}
		if(createNew){
			CTabItem item = new CTabItem(tabContent, SWT.NONE);
			item.setText(title);
			item.setShowClose(true);
			item.setControl(frm);
			tabContent.setSelection(item);
		}else if(index > -1)
			tabContent.setSelection(index);
	}

	public static void resetCombo(Combo cb){
		cb.removeAll();
		cb.deselectAll();
		cb.setText("");
	}

	public static boolean updateForm(Shell shell, String entity, String name){
		return MessageDialog.openQuestion(shell, "Confirmation", "Do you want to update "+entity+" '"+ name +"'?");
	}

	public static Integer getIdFromFormList(Combo cbList) {
		// TODO Auto-generated method stub
		Integer id;
		try{
			id = Integer.parseInt((String) cbList.getData(cbList.getText()));
		}catch(NumberFormatException nfe){
			id = 0;
		}
		return id;
	}

	public static String getNameById(List<NameIdDTO> nameIdList, Integer typeId) {
		// TODO Auto-generated method stub
		String name = null;
		for(NameIdDTO names: nameIdList){
			
			if(names.getId().equals(typeId)){
				name=names.getName();
				break;
			}
			
		}
		return name;
	}

	public static void entrySetToCombo(List<NameIdDTO> list, CCombo combo) {
		// TODO Auto-generated method stub
		if(combo.getItemCount()>0) combo.removeAll();
		for (NameIdDTO entry : list){ //add contact on list
			String input = entry.getName();
			String output = input.substring(0, 1).toUpperCase() + input.substring(1);
			combo.add(output); //contact name
			combo.setData(output, entry.getId().toString()); // pair name with id
		}
	}

	public static int getIdFromTableList(Table tbList) {
		// TODO Auto-generated method stub
		Integer id = null;
		String name= tbList.getSelection()[0].getText();
		id = Integer.parseInt((String) tbList.getSelection()[0].getData(name));
		return id;
	}

	public static void selectRowById(Table tbList, Integer id) {
		// TODO Auto-generated method stub
		for(int index=0; index<tbList.getItemCount(); index++){
			TableItem item = tbList.getItem(index);
			Integer itemId = Integer.parseInt((String) item.getData(item.getText()));
			if(itemId == id){
				tbList.select(index);
				break;
			}
		}
	}

	public static int getExperimentIdByDatasetId(int datasetId) {
		// TODO Auto-generated method stub
		int returnVal = 0;
			PayloadEnvelope<DataSetDTO> datasetDetailEnvelope = Controller.getDatasetDetailsById(datasetId);
			returnVal =  datasetDetailEnvelope.getPayload().getData().get(0).getExperimentId();
		return returnVal;
	}

	public static int getProjectIdByExperimentId(int experimentId) {
		// TODO Auto-generated method stub
		int returnVal = 0;
		
		PayloadEnvelope<ExperimentDTO> experimentDetailEnvelope = Controller.getExperimentDetailsById(experimentId);
		returnVal = experimentDetailEnvelope.getPayload().getData().get(0).getProjectId();	
		
		return returnVal;
	}

	public static int getPIidByProjectId(int projectId) {
		// TODO Auto-generated method stub
		int returnVal = 0;
		
		PayloadEnvelope<ProjectDTO> projectDetailsEnvelope = Controller.getProjectDetailsById(projectId);
		returnVal = projectDetailsEnvelope.getPayload().getData().get(0).getPiContact();
		
		return returnVal;
	}

//	public static ArrayList<Integer> getCompleteIDset(int i, int currentProjectId, int currentExperimentId,
//			int currentDatasetId) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
