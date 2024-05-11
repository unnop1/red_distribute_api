package com.nt.red_distribute_api.entity.view.order_type;


public interface OrderTypeDashboard {
        Long getID();
        String getORDERTYPE_NAME();
        String getMESSAGE_EXPIRE();
        Integer getIS_ENABLE();
        Integer getIS_DELETE();
        Integer getTotalConsumer();
}
