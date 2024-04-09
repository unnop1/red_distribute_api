package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.entity.PermissionMenuEntity;


public interface PermissionMenuService {
    PermissionMenuEntity getUserMenuPermission(Long userId);
}
