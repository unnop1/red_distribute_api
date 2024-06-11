package com.nt.red_distribute_api.entity.view.permission;


import java.sql.Clob;
import java.sql.Timestamp;

public interface ListPermissionTotalUser {
        Long getID();
        String getPERMISSION_NAME();
        Clob getPERMISSION_JSON();
        Timestamp getCREATED_DATE();
        String getCREATED_BY();
        Timestamp getUPDATED_DATE();
        String getUPDATED_BY();
        Integer getTotalUser();
}
