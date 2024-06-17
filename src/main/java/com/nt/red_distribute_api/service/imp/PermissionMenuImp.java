package com.nt.red_distribute_api.service.imp;


import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.nt.red_distribute_api.dto.req.permission.AddPermissionReq;
import com.nt.red_distribute_api.dto.req.permission.PermissionListReq;
import com.nt.red_distribute_api.dto.resp.PaginationDataResp;
import com.nt.red_distribute_api.entity.PermissionMenuEntity;
import com.nt.red_distribute_api.entity.view.permission.ListPermissionTotalUser;
import com.nt.red_distribute_api.repo.PermissionMenuRepo;
import com.nt.red_distribute_api.repo.UserRepo;
import com.nt.red_distribute_api.repo.view.permission.ListPermissionTotalUserRepo;

import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.nt.red_distribute_api.Util.DateTime;
import com.nt.red_distribute_api.service.PermissionMenuService;

@Service
public class PermissionMenuImp implements PermissionMenuService {

    @Autowired
    private PermissionMenuRepo permissionMenuRepo;

    @Autowired
    private ListPermissionTotalUserRepo viewPermissionMenuRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public PaginationDataResp ListMenuPermission(PermissionListReq req) {
        PaginationDataResp resp = new PaginationDataResp();
            try{
            Integer offset = req.getStart();
            Integer limit = req.getLength();
            Integer page = 0;
            if(limit > 0){
                page = offset / limit;
            }
            String searchName = req.getSearch();
            String searchField = req.getSearchField();
            String sortName = req.getSortName();
            String sortBy = req.getSortBy();

            if ( searchName.isEmpty()){
                if (limit > 0){
                    List<ListPermissionTotalUser> permissionMenu = viewPermissionMenuRepo.GetAllWithTotalUser(
                        PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName )
                    );
                    Integer count = viewPermissionMenuRepo.getGetAllWithTotalUserTotalCount();
                    resp.setData(permissionMenu);
                    resp.setCount(count);
                    return resp;
                }else{
                    List<ListPermissionTotalUser> permissionMenu = viewPermissionMenuRepo.GetAllWithTotalUser();
                    Integer count = viewPermissionMenuRepo.getGetAllWithTotalUserTotalCount();
                    resp.setData(permissionMenu);
                    resp.setCount(count);
                    return resp;
                }
                
            } else {
                if( !req.getSearchField().isEmpty()){
                    List<ListPermissionTotalUser> permissionMenu = viewPermissionMenuRepo.GetAllWithTotalUserLike(
                        searchField, searchName, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName )
                    );
                    Integer count = viewPermissionMenuRepo.getGetAllWithTotalUserLikeTotalCount(searchField, searchName);
                    resp.setData(permissionMenu);
                    resp.setCount(count);
                    return resp;
                }
                // System.out.println("all like:");
                List<ListPermissionTotalUser> permissionMenu = viewPermissionMenuRepo.GetAllWithTotalUserAllLike(
                    searchName, PageRequest.of(page, limit, Sort.Direction.fromString(sortBy), sortName )
                );
                Integer count = viewPermissionMenuRepo.getGetAllWithTotalUserAllLikeTotalCount(searchName);
                resp.setData(permissionMenu);
                resp.setCount(count);
                // System.out.println("permissionMenus: " + permissionMenu);
                return resp;
            }
        }catch (Exception e) {
            resp.setData(e.getMessage());
            return resp;
        }

    }

    @Override
    public PermissionMenuEntity getMenuPermission(Long permissionID) {
        PermissionMenuEntity permissionMenu = permissionMenuRepo.findPermissionById(permissionID);
        return permissionMenu;
    }

    @Override
    public Long addSaMenuPermission(AddPermissionReq req, String createdBy) {
        Timestamp timeNow = DateTime.getTimeStampNow();
        PermissionMenuEntity newPermissionMenu = new PermissionMenuEntity();
        newPermissionMenu.setPermission_Name(req.getPermissionName());
        Clob permission_jsonClob;
        try {
            // permission_jsonClob = new javax.sql.rowset.serial.SerialClob(req.getPermission_json().toCharArray());
            newPermissionMenu.setPermission_json(req.getPermission_json());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        newPermissionMenu.setCreated_By(createdBy);
        newPermissionMenu.setCreated_Date(timeNow);
        PermissionMenuEntity created = permissionMenuRepo.save(newPermissionMenu);

        return created.getId()+1;
    }

    @Override
    public Void updatePermission(Long permissionID, AddPermissionReq updates, String updatedBy) {
        PermissionMenuEntity existingEntity = permissionMenuRepo.findById(permissionID).orElse(null);
        // If the entity exists
        if (existingEntity != null) {
            Timestamp timeNow = DateTime.getTimeStampNow();
            if (updates.getPermissionName() != null ){
                existingEntity.setPermission_Name(updates.getPermissionName());
            }

            if (updates.getPermission_json() != null ){
                try{
                    Clob permission_jsonClob;
                    // permission_jsonClob = new javax.sql.rowset.serial.SerialClob(updates.getPermission_json().toCharArray());
                    existingEntity.setPermission_json(updates.getPermission_json());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            existingEntity.setUpdated_Date(timeNow);
            existingEntity.setUpdated_By(updatedBy);

            // Save the updated entity back to the database
            permissionMenuRepo.save(existingEntity);
        }

        return null;
    }

    @Override
    public Void removePermission(Long permissionID) {
        permissionMenuRepo.deleteById(permissionID);
        return null;
    }
    
}
