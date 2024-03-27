package com.nt.red_distribute_api.service;

import com.nt.red_distribute_api.enitiy.PermissionMenuEntity;


public interface PermissionMenuService {
    PermissionMenuEntity getUserMenuPermission(Long userId);
}
