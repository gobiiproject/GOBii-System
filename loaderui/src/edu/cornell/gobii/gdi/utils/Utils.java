package edu.cornell.gobii.gdi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiFilePropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;

import edu.cornell.gobii.gdi.objects.xml.Columns;
import edu.cornell.gobii.gdi.objects.xml.FileFormats;
import edu.cornell.gobii.gdi.services.Controller;
import edu.cornell.gobii.gdi.wizards.datasets.DTOdataset;
import edu.cornell.gobii.gdi.wizards.dnasamples.DTOsamples;
import edu.cornell.gobii.gdi.wizards.markers.DTOmarkers;

public class Utils {
	private static Logger log = Logger.getLogger(Utils.class.getName());
	
	public static List<List<String>> readHapmapFile(String filename, String delim, int colcount, int rowcount){
		List<List<String>> data = new ArrayList<List<String>>();
		File file = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			int lineCount = 0;
			while (sc.hasNextLine() && lineCount<rowcount) {
				String line = sc.nextLine();
				List<String> ls = Arrays.asList(line.split(delim));
				ls.subList(0, Math.min(ls.size(), colcount));
				data.add(ls);
				lineCount++;
			}
		}catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }finally{
	    	sc.close();
	    }
		return data;
	}

	public static Object unmarshalFileFormats(String xml){
		Object obj = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(FileFormats.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			obj = unmarshaller.unmarshal(new File(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Object unmarshalColumns(String xml){
		Object obj = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Columns.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			obj = unmarshaller.unmarshal(new File(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static void unmarshalColumns(Table tb, String xml, final HashMap<String, GobiiFileColumn> columns, final HashMap<String, GobiiFileColumn> subColumns){
		Columns tableColumns = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Columns.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			tableColumns = (Columns) unmarshaller.unmarshal(new File(xml));
			for(Columns.Column col : tableColumns.getColumn()){
				TableItem item = new TableItem(tb, SWT.NONE);
				item.setText(1,col.getName());
				if(col.isRequired()){
					Color color = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
					item.setBackground(1, color);
				}
				
				Button button = new Button(tb, SWT.PUSH | SWT.FLAT);
				button.setText("clear");
				button.setSize(50, 16);
				button.addListener(SWT.Selection, new Listener() {
					
					@Override
					public void handleEvent(Event arg0) {
						String name = item.getText(1);
						item.setText(2, "");
						item.setData("index", -1);
						item.setData("value", null);
						item.setData("sub_index", -1);
						item.setData("sub_value", null);
						if(tb.getColumnCount() > 0){
							item.setText(3, "");
							item.setText(4, "");
						}
						if(columns.containsKey(name)) columns.remove(name);
						if(subColumns.containsKey("sub_"+name)) subColumns.remove("sub_"+name);
					}
				});
				button.pack();
				
				TableEditor editor = new TableEditor(tb);
				editor.grabHorizontal = true;
				editor.setEditor(button, item, 0);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadTableProps(Table tb, String props, final HashMap<String, GobiiFileColumn> columns){
		try{
			List<NameIdDTO> entries = Controller.getCVByGroup(props);
			tb.removeAll();
			for(NameIdDTO entry : entries){
				TableItem item = new TableItem(tb, SWT.NONE);
				String id = entry.getId().toString();
				String name = entry.getName();
				item.setText(1, name);
				item.setData("id", id);
				Button button = new Button(tb, SWT.PUSH | SWT.FLAT);
				button.setText("clear");
				button.setSize(50, 16);
				button.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						String name = item.getText(1);
						item.setText(2, "");
						item.setData("index", -1);
						item.setData("value", null);
						item.setData("sub_index", -1);
						item.setData("sub_value", null);
						if(tb.getColumnCount() > 0){
							item.setText(3, "");
							item.setText(4, "");
						}
						if(columns.containsKey(name)) columns.put(name, null);
						if(columns.containsKey("sub_"+name)) columns.put("sub_"+name, null);
					}
				});
				button.pack();

				TableEditor editor = new TableEditor(tb);
				editor.grabHorizontal = true;
				editor.setEditor(button, item, 0);
			}
		}catch(Exception err){
			Utils.log(log, "Error loading "+props, err);
		}
	}
	
	public static void getLocalFiles(final Table table, final List<String> lsFiles){
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget dndTarget = new DropTarget(table, operations);
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
//					if (t != null) System.out.println(t);
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
			@Override
			public void dragLeave(DropTargetEvent arg0) {
				
			}
			@Override
			public void drop(DropTargetEvent event) {
				if (textTransfer.isSupportedType(event.currentDataType)) {
					String text = (String)event.data;
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(text);
				}
				if (fileTransfer.isSupportedType(event.currentDataType)){
					String[] files = (String[])event.data;
					for (int i = 0; i < files.length; i++) {
						TableItem item = new TableItem(table, SWT.NONE);
						item.setText(files[i]);
						lsFiles.add(files[i]);
					}
				}
			}
			@Override
			public void dropAccept(DropTargetEvent event) {
				
			}
		});
	}
	
	public static void setDndColumnSource(final Table table){
		int operations = DND.DROP_COPY;
		DragSource source = new DragSource(table, operations);

		// Provide data in Text format
		Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event) {
				// Only start the drag if there is actually text in the
				// label - this text will be what is dropped on the target.
				if(table.getSelectionIndex() < 0){
					event.doit = false;
				}
			}
			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = table.getItem(table.getSelectionIndex()).getText();
				}
			}
			public void dragFinished(DragSourceEvent event) {
			}
		});
	}
	
	public static void setDndColumnTarget(final Table sourceTable, Table targetTable, final HashMap<String, GobiiFileColumn> columns, final HashMap<String, GobiiFileColumn> subColumns){
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(targetTable, operations);

		// Receive data in Text or File format
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] {fileTransfer, textTransfer};
		target.setTransfer(types);

		target.addDropListener(new DropTargetListener() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
			}
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
				if (textTransfer.isSupportedType(event.currentDataType)) {
					// NOTE: on unsupported platforms this will return null
					Object o = textTransfer.nativeToJava(event.currentDataType);
					String t = (String)o;
//					if (t != null) System.out.println(t);
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
			}
			public void dragLeave(DropTargetEvent event) {
			}
			public void dropAccept(DropTargetEvent event) {
			}
			public void drop(DropTargetEvent event) {
				try{
					if (textTransfer.isSupportedType(event.currentDataType)) {
						String val = (String)event.data;
						int index = getTableIndex(sourceTable, val);
						TableItem item = (TableItem)event.item;
						String id = (String) item.getData("id");
						String name = id == null ? item.getText(1) : id;
						item.setData("name", name);
						
						
						if(item.getData("value") == null){
							item.setText(2, val);
							item.setData("index", index);
							item.setData("value", val);
							item.setData("sub_index", -1);
							GobiiFileColumn column = new GobiiFileColumn();
							column.setName(name);
							column.setCCoord(index);
							column.setRCoord(index);
							columns.put(name, column);
						}else{
							Integer sub_index = (Integer)item.getData("sub_index");
							if(sub_index < 0){
								item.setText(2, item.getText(2)+"_"+val);
								item.setData("sub_index", index);
								item.setData("sub_value", val);
								GobiiFileColumn column = new GobiiFileColumn();
								column.setName(name);
								column.setCCoord(index);
								column.setRCoord(index);
								column.setSubcolumn(true);
								column.setSubcolumnDelimiter("_");
								if(subColumns != null)
									subColumns.put("sub_"+name, column);
							}
						}
						
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setTableMouseLister(Table table, final HashMap<String, GobiiFileColumn> columns){
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
			if (item == null)
                return;
			
			Text newEditor = new Text(table, SWT.NONE);
			int EDITABLECOLUMN = -1;
			for (int i = 0; i < table.getColumnCount(); i++) {
				Rectangle rect = item.getBounds(i);
				if (rect.contains(pt)) {
					EDITABLECOLUMN = i;
					break;
				}
			}
			
			final int col = EDITABLECOLUMN;
			if(table.getColumnCount() > 3 && col < 3) return;
			else if(col < 2) return;
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
			newEditor.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent arg0) {
					GobiiFileColumn column = columns.get(item.getText(1));
					if(col == 3) column.setFilterFrom(newEditor.getText());
					else if(col == 4) column.setFilterTo(newEditor.getText());
				}
			});
			editor.setEditor(newEditor, item, col); 
		});
	}
	
	public static int getTableIndex(Table table, String str){
		int index = -1;
		for(TableItem item : table.getItems()){
			if(item.getText(0).equals(str)){
				index = table.indexOf(item);
				break;
			}
		}
		return index;
	}
	
	public static void previewData(Table tbData, LoaderFilePreviewDTO previewDTO){
		tbData.removeAll();
		if(tbData.getColumnCount() < 50){
			for(int i=0; i<50; i++){
				TableColumn tc = new TableColumn(tbData, SWT.NONE);
				tc.setWidth(50);
				tc.setText(""+(i));
			}
		}else{
			tbData.clearAll();
		}
		for(int i=0; i<previewDTO.getFilePreview().size(); i++){
			TableItem item = new TableItem(tbData, SWT.NONE);
			for(int j=0; j<previewDTO.getFilePreview().get(i).size(); j++){
				item.setText(j, previewDTO.getFilePreview().get(i).get(j));
			}
		}
	}
	
	public static void loadSampleLocalData(Table tbData, Table tbfiles, String ext, String delim){
		String filename = null;
		for(int i=0; i<tbfiles.getItemCount(); i++){
			if(tbfiles.getItem(i).getText().endsWith(ext)){
				filename = tbfiles.getItem(i).getText();
				break;
			}
		}
		if(filename == null) return;
		List<List<String>> data = Utils.readHapmapFile(filename, delim, 50, 50);
		tbData.removeAll();
		if(tbData.getColumnCount() < 50){
			for(int i=0; i<50; i++){
				TableColumn tc = new TableColumn(tbData, SWT.NONE);
				tc.setWidth(50);
				tc.setText(""+(i));
			}
		}else{
			tbData.clearAll();
		}
		for(int i=0; i<data.size(); i++){
			TableItem item = new TableItem(tbData, SWT.NONE);
			for(int j=0; j<data.get(i).size(); j++){
				item.setText(j, data.get(i).get(j));
			}
		}
	}
	
	public static void setTableCombobox(Table tb, int itemIndex, String[] ls, final HashMap<String, GobiiFileColumn> columns){
		TableEditor editor = new TableEditor(tb);
		editor.grabHorizontal = true;
		final CCombo combo = new CCombo(tb, SWT.NONE);
		combo.setItems(ls);
		editor.setEditor(combo, tb.getItem(itemIndex), 2);
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String name = combo.getItem(combo.getSelectionIndex());
				GobiiFileColumn col = columns.containsKey(name) ? columns.get(name) : new GobiiFileColumn();
				col.setConstantValue(combo.getItem(combo.getSelectionIndex()));
				col.setName(name);
				columns.put(name, col);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public static void log(Shell shell, StyledText mem, Logger logger, String message, Throwable e){
		MessageDialog.openError(shell, "ERROR in Loader", message);
		if(mem != null) mem.setText(e.getMessage());
	}
	
	public static void log(Logger log, String message, Throwable e){
		log.error(message, e);
	}

	public static String getFromTo(String str, String strFrom, String strTo){
		String strSubString;
		try{
			String sfrom = matchPattern(str, strFrom);
			if(str.indexOf(sfrom) < 0) return null;
			int fromIndex = str.indexOf(sfrom) + sfrom.length();
			String strTmp = str.substring(fromIndex);
			String sto = matchPattern(strTmp, strTo);
			int toIndex = strTmp.indexOf(sto);
			strSubString = strTmp.substring(0, toIndex);
		}catch(Exception e){
			return null;
		}
		return strSubString;
	}
	
	private static String matchPattern(String str, String pattern){
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		String res = null;
		if(m.find( )) {
			res = m.group(0);
		}
		return res;
	}

	public static void setDSInstructionFileDetails(GobiiLoaderInstruction instruction, DTOdataset dto) {
		// TODO Auto-generated method stub
		GobiiFilePropNameId projectPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId platformPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId dataSetPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId datasetTypePropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId experimentPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId mapsetPropFile = new GobiiFilePropNameId();
		
		projectPropFile.setId(dto.getProjectID());
		projectPropFile.setName(dto.getProjectName());

		platformPropFile.setId(dto.getPlatformID());
		platformPropFile.setName(dto.getPlatformName());

		dataSetPropFile.setId(dto.getDatasetID());
		dataSetPropFile.setName(dto.getDatasetName());

		datasetTypePropFile.setId(dto.getDatasetTypeID());
		datasetTypePropFile.setName(dto.getDatasetType());

		experimentPropFile.setId(dto.getExperimentID());
		experimentPropFile.setName(dto.getExperimentName());

		mapsetPropFile.setId(dto.getMapsetID());
		mapsetPropFile.setName(dto.getMapsetName());
		
		instruction.setProject(projectPropFile);
		instruction.setPlatform(platformPropFile);
		instruction.setDataSet(dataSetPropFile);
		instruction.setDatasetType(datasetTypePropFile);
		instruction.setExperiment(experimentPropFile);
		instruction.setMapset(mapsetPropFile);
		
	}

	public static void setSampleInstructionFileDetails(GobiiLoaderInstruction instruction, DTOsamples dto) {
		// TODO Auto-generated method stub

		GobiiFilePropNameId projectPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId experimentPropFile = new GobiiFilePropNameId();
		
		projectPropFile.setId(dto.getProjectID());
		projectPropFile.setName(dto.getProjectName());

		experimentPropFile.setId(dto.getExperimentID());
		experimentPropFile.setName(dto.getExperimentName());
		

		instruction.setProject(projectPropFile);
		instruction.setExperiment(experimentPropFile);
	}

	public static void setMarkerInstructionFileDetails(GobiiLoaderInstruction instruction, DTOmarkers dto) {
		// TODO Auto-generated method stub

		GobiiFilePropNameId projectPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId platformPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId dataSetPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId experimentPropFile = new GobiiFilePropNameId();
		GobiiFilePropNameId mapsetPropFile = new GobiiFilePropNameId();
		
		projectPropFile.setId(dto.getProjectID());
		projectPropFile.setName(dto.getProjectName());

		platformPropFile.setId(dto.getPlatformID());
		platformPropFile.setName(dto.getPlatformName());

		dataSetPropFile.setId(dto.getDatasetID());
		dataSetPropFile.setName(dto.getDatasetName());

		experimentPropFile.setId(dto.getExperimentID());
		experimentPropFile.setName(dto.getExperimentName());

		mapsetPropFile.setId(dto.getMapsetID());
		mapsetPropFile.setName(dto.getMapsetName());
		
		instruction.setProject(projectPropFile);
		instruction.setPlatform(platformPropFile);
		instruction.setDataSet(dataSetPropFile);
		instruction.setExperiment(experimentPropFile);
		instruction.setMapset(mapsetPropFile);
	}
}
