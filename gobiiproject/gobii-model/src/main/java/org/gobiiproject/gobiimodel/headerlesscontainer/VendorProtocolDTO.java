package org.gobiiproject.gobiimodel.headerlesscontainer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityParam;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VCalaminos on 2016-12-12.
 */
public class VendorProtocolDTO extends DTOBase{

    public VendorProtocolDTO(){
    }

    public VendorProtocolDTO(Integer organizationId, Integer protocolId, String name) {
        this.organizationId = organizationId;
        this.protocolId = protocolId;
        this.name = name;
    }

    private Integer vendorProtocolId = 0;
    private Integer organizationId = 0;
    private Integer protocolId = 0;
    private String name;
    private Integer status;

    @Override
    public Integer getId(){ return this.vendorProtocolId; }

    @Override
    public void setId(Integer id){ this.vendorProtocolId= id; }

    @GobiiEntityParam(paramName = "vendorProtocolId")
    public Integer getVendorProtocolId() {
        return vendorProtocolId;
    }

    @GobiiEntityColumn(columnName = "vendor_protocol_id")
    public void setVendorProtocolId(Integer vendorProtocolId) {
        this.vendorProtocolId = vendorProtocolId;
    }

    @GobiiEntityParam(paramName = "vendorId")
    public Integer getOrganizationId() {
        return organizationId;
    }

    @GobiiEntityColumn(columnName = "vendor_id")
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    @GobiiEntityParam(paramName = "protocolId")
    public Integer getProtocolId() {
        return protocolId;
    }

    @GobiiEntityColumn(columnName = "protocol_id")
    public void setProtocolId(Integer protocolId) {
        this.protocolId = protocolId;
    }

    @GobiiEntityParam(paramName = "name")
    public String getName() {
        return name;
    }

    @GobiiEntityColumn(columnName = "name")
    public void setName(String name) {
        this.name = name;
    }

    @GobiiEntityParam(paramName = "status")
    public Integer getStatus() {
        return status;
    }

    @GobiiEntityColumn(columnName = "status")
    public void setStatus(Integer status) {
        this.status = status;
    }
}
