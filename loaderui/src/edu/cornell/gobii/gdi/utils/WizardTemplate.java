package edu.cornell.gobii.gdi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;

import edu.cornell.gobii.gdi.forms.DlgWizTemplate;
import edu.cornell.gobii.gdi.main.App;

public class WizardTemplate{

	/**
	 * 
	 */
	private LoaderInstructionFilesDTO instructions;
	private static String code;
	private static String filename;

	public LoaderInstructionFilesDTO getInstructions() {
		return instructions;
	}
	
	public void setInstructions(String code, LoaderInstructionFilesDTO instructions) {
		this.instructions = instructions;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public static String getFilename() {
		return filename;
	}

	public static void setFilename(String filename) {
		WizardTemplate.filename = filename;
	}

	public WizardTemplate(String code, LoaderInstructionFilesDTO instructions){
		this.code = code;
		this.instructions = instructions;
	}
	
	public static void CreateSaveTemplateDialog(Shell shell) {
		DlgWizTemplate template = new DlgWizTemplate(shell);
		if(template.open() == Window.OK){
			String dir = App.INSTANCE.getConfigDir()+"/template/";
			File d = new File(dir);
			if(!d.exists()) d.mkdirs();
			filename = dir+code+"_"+template.getTemplName();
		}
	}
	
	public static boolean save(WizardTemplate template) {
		File file = new File(filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(template);
            oos.close();
            fos.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
