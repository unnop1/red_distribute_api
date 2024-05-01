package com.nt.red_distribute_api.entity.view.user;

import java.sql.Timestamp;


public interface ListUser {
    
    Long getID();
    String getNAME();
    String getEMAIL();
    String getUSERNAME();
    String getDEPARTMENTNAME();
    Integer getIS_ENABLE();
    Timestamp getLAST_LOGIN();
    Timestamp getCREATED_DATE();
    String getPERMISSION_NAME();

}
