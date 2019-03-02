package com.by.base.constants;

/**
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2019年01月05日 11:34
 * @Package: adm-workspace - com.by.base.constants
 * <p>
 * -------------------------------------------------------------------
 */
public enum StatusEnum {

    CHECKED((byte) 1), UNCHECK((byte) 2);

    StatusEnum(Byte status) {
        this.status = status;
    }

    private Byte status;

    public Byte getStatus() {
        return status;
    }
}
