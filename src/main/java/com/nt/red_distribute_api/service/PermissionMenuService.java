package com.nt.red_distribute_api.service;

import java.util.HashMap;

import com.nt.red_distribute_api.dto.req.permission.AddPermissionReq;
import com.nt.red_distribute_api.dto.req.permission.PermissionListReq;
import com.nt.red_distribute_api.dto.req.permission.UpdateByPermissionReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;


public interface PermissionMenuService {
    PaginationDataResp ListMenuPermission(PermissionListReq req);
    PermissionMenuEntity getMenuPermission(Long permissionID);
    Long addSaMenuPermission(AddPermissionReq req, String createdBy);
    Void updatePermission(Long permissionID, AddPermissionReq updateInfo, String updatedBy);
    Void removePermission(Long permissionID);
}
