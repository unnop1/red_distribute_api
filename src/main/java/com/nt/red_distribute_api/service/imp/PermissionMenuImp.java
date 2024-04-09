package com.nt.red_distribute_api.service.imp;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.repo.PermissionMenuRepo;
import com.nt.red_distribute_api.service.PermissionMenuService;

@Service
public class PermissionMenuImp implements PermissionMenuService {

    @Autowired
    private PermissionMenuRepo permissionMenuRepo;

    @Override
    public PermissionMenuEntity getUserMenuPermission(Long userId) {
        PermissionMenuEntity permissionMenu = permissionMenuRepo.findPermissionByUserId(userId);
        System.out.println("permissionMenus: " + permissionMenu);
        return permissionMenu;
    }
    
}
