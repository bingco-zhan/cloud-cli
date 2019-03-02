package com.by.base.mapper;

import io.swagger.annotations.ApiModelProperty;

public abstract class UserInfo {

    @ApiModelProperty(value = "创建人名称(只作显示字段)", example = "")
    private String creuserName;

    @ApiModelProperty(value = "修改人名称(只作显示字段)", example = "")
    private String moduserName;

    @ApiModelProperty(value = "审核人名称(只作显示字段)", example = "")
    private String cfmuserName;

    public String getCreuserName() {
        return creuserName;
    }

    public void setCreuserName(String creuserName) {
        this.creuserName = creuserName;
    }

    public String getModuserName() {
        return moduserName;
    }

    public void setModuserName(String moduserName) {
        this.moduserName = moduserName;
    }

    public String getCfmuserName() {
        return cfmuserName;
    }

    public void setCfmuserName(String cfmuserName) {
        this.cfmuserName = cfmuserName;
    }

}