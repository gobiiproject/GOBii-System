package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 11/2016.
 */
public class LoaderFilePreviewDTO extends DTOBase {

    private Integer id=0;
    private String directoryName;
    private String previewFileName;
    private List<String> fileList =  new ArrayList<String>();
    private List<List<String>> filePreview = new ArrayList<List<String>>(); //will contain the A list of 50 rows with 50 items each.


    @Override
    public Integer getId() {
        return this.id;
    }


    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getPreviewFileName() {
        return previewFileName;
    }

    public void setPreviewFileName(String previewFileName) {
        this.previewFileName = previewFileName;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public List<List<String>> getFilePreview() {
        return filePreview;
    }

    public void setFilePreview(List<List<String>> filePreview) {
        this.filePreview = filePreview;
    }

}
