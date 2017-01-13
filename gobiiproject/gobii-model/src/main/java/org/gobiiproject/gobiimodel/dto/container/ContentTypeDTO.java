// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiimodel.dto.container;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.header.*;

public class ContentTypeDTO extends DtoMetaData {

    private Integer contentTypeId;
    private String name;
    private String scope;


    public ContentTypeDTO() {
    }

    public ContentTypeDTO(Integer contentTypeId, String name, String scope) {
        this.contentTypeId = contentTypeId;
        this.name = name;
        this.scope = scope;
    }

    public void mapFromDTO(ContentTypeDTO contentTypeDTO) {
        this.contentTypeId = contentTypeDTO.contentTypeId;
        this.name = contentTypeDTO.name;
        this.scope = contentTypeDTO.scope;
    }

//    public void mapFromeEntity(ContentType contentType) {
//
//        this.setContentTypeId(contentType.getContentTypeId());
//        this.setName(contentType.getName());
//        this.setScope(contentType.getScope());
//    }

    public Integer getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(Integer contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }


}//ResourceDTO
