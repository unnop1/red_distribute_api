package com.nt.red_distribute_api.entity.view.permission;


import java.sql.Clob;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

public interface ListPermissionTotalUser {
        Long getID();
        String getPERMISSION_NAME();
        @JsonBackReference
        Clob getPERMISSION_JSON();
        Timestamp getCREATED_DATE();
        String getCREATED_BY();
        Timestamp getUPDATED_DATE();
        String getUPDATED_BY();
        Integer getTotalUser();
}