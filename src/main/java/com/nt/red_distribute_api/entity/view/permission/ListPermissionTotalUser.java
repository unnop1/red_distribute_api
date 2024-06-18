package com.nt.red_distribute_api.entity.view.permission;


import java.sql.Timestamp;

import jakarta.persistence.Lob;

public interface ListPermissionTotalUser {
        Long getID();
        @Lob
        String getPERMISSION_NAME();
        @Lob
        String getPERMISSION_JSON();
        Timestamp getCREATED_DATE();
        String getCREATED_BY();
        Timestamp getUPDATED_DATE();
        String getUPDATED_BY();
        Integer getTOTALUSER();
}