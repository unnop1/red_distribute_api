package com.nt.red_distribute_api.entity.view.trigger;

import java.sql.Timestamp;
public interface DashboardTrigger {
    Long getID();
    String getORDERID();
    String getPHONENUMBER();
    String getORDERTYPE_NAME();
    String getPUBLISH_CHANNEL();
    Timestamp getRECEIVE_DATE();
    Timestamp getSEND_DATE();
}
