package com.nt.red_distribute_api.entity.view.order_type;

import java.sql.Timestamp;

public interface OrderTypeDashboardTrigger {
        Long getID();
        Long getSA_CHANNEL_CONNECT_ID();
        String getORDERTYPE_NAME();
        String getDESCRIPTION();
        String getMESSAGE_EXPIRE();
        Integer getIS_ENABLE();
        Integer getIS_DELETE();
        String getIS_DELETE_BY();
        Timestamp getIS_DELETE_DATE();
        Timestamp getCREATED_DATE();
        Timestamp getCREATED_BY();
        Long getTotalConsumer();
        Long getTotalTrigger();
        String getCHANNEL_NAME();
}
