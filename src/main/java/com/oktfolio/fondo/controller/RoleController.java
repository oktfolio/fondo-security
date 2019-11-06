package com.oktfolio.fondo.controller;


import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.model.AllocResourceRequest;
import com.oktfolio.fondo.model.CreateRoleRequest;
import com.oktfolio.fondo.model.ListRolesRequest;
import com.oktfolio.fondo.model.SysRole;
import com.oktfolio.fondo.security.JWTUserDetails;
import com.oktfolio.fondo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;

import static com.oktfolio.fondo.common.result.RoleResultCodeEnum.*;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity createRole(@RequestBody CreateRoleRequest createRoleRequest,
                                     @Autowired Authentication authentication) {

        String name = createRoleRequest.getName();
        String code = createRoleRequest.getCode();

        SysRole byName = roleService.findByName(name);
        if (byName != null) {

            ResultEntity resultEntity = ResultEntity.badRequest(ROLE_NAME_EXISTS);

            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysRole byCode = roleService.findByCode(code);
        if (byCode != null) {
            ResultEntity resultEntity = ResultEntity.badRequest(ROLE_CODE_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        SysRole role = new SysRole();
        role.setName(name);
        role.setCode(code);
        role.setCreateAt(LocalDateTime.now());
        role.setCreateBy(operatorId);

        int res = roleService.createRole(role, operatorId);

        if (res == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        ResultEntity resultEntity = ResultEntity.badRequest(FAILED_CREATE_ROLE);

        return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
    }

    @GetMapping("/roles")
    public ResponseEntity getRolesPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                       @RequestParam(value = "order", defaultValue = "id") String orderBy,
                                       @RequestParam(value = "desc", defaultValue = "false") boolean desc,
                                       @ModelAttribute ListRolesRequest listRolesRequest) {
        return ResponseEntity.ok(roleService.listRole(pageNum, pageSize, orderBy, desc, listRolesRequest));
    }

    @GetMapping("/roles/{roleId:\\d+$}")
    public ResponseEntity getRole(@PathVariable("roleId") Integer roleId) {
        SysRole role = roleService.findById(roleId);
        if (role != null) {
            return ResponseEntity.ok(role);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/roles/{roleId:\\d+$}")
    public ResponseEntity updateWholeRole(@PathVariable("roleId") Integer roleId,
                                          @RequestBody CreateRoleRequest updateRoleRequest,
                                          @Autowired Authentication authentication) {

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        String name = updateRoleRequest.getName();
        String code = updateRoleRequest.getCode();

        SysRole byName = roleService.findByName(name);
        if (byName != null) {

            ResultEntity resultEntity = ResultEntity.badRequest(ROLE_NAME_EXISTS);

            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysRole byCode = roleService.findByCode(code);
        if (byCode != null) {
            ResultEntity resultEntity = ResultEntity.badRequest(ROLE_CODE_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysRole foundRole = roleService.findById(roleId);
        if (foundRole != null) {
            SysRole sysRole = new SysRole();
            sysRole.setName(name);
            sysRole.setCode(code);

            int res = roleService.updateRole(sysRole, operatorId);

            if (res == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            ResultEntity resultEntity = ResultEntity.badRequest(FAILED_UPDATE_ROLE);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
        ResultEntity resultEntity = ResultEntity.notFound(ROLE_DELETE_OR_NOT_EXISTS);
        return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
    }

    @DeleteMapping("/roles/{roleId:\\d+$}")
    public ResponseEntity deleteRole(@PathVariable("roleId") Integer roleId) {

        int res = roleService.deleteById(roleId);

        if (res == 1) {
            return ResponseEntity.ok().build();
        } else {
            ResultEntity resultEntity = ResultEntity.notFound(ROLE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
    }

    @PatchMapping("/roles/{roleId:\\d*}/status/disable")
    public ResponseEntity disableRole(@PathVariable("roleId") Integer roleId,
                                      @Autowired Authentication authentication) {

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        SysRole role = roleService.findById(roleId);

        if (role != null) {
            int res = roleService.disableRole(roleId, operatorId);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                ResultEntity resultEntity = ResultEntity.badRequest(FAILED_DISABLE_ROLE);
                return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
            }
        } else {
            ResultEntity resultEntity = ResultEntity.notFound(ROLE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
    }

    @PatchMapping("/roles/{roleId:\\d*}/status/enable")
    public ResponseEntity enableRole(@PathVariable("roleId") Integer roleId,
                                     @Autowired Authentication authentication) {

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        SysRole role = roleService.findById(roleId);

        if (role != null) {
            int res = roleService.enableRole(roleId, operatorId);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                ResultEntity resultEntity = ResultEntity.badRequest(FAILED_ENABLE_ROLE);
                return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
            }
        } else {
            ResultEntity resultEntity = ResultEntity.notFound(ROLE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
    }

    @GetMapping("/roles/{roleId:\\d*}/resources")
    public ResponseEntity getRoleResources(@PathVariable("roleId") Integer roleId) {
        roleService.getRoleResources(roleId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/roles/{roleId:\\d+$}/resources")
    public ResponseEntity allocResourcesForRole(@PathVariable("roleId") Integer roleId,
                                                @RequestBody AllocResourceRequest allocResourceRequest,
                                                @Autowired Authentication authentication) {

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        ResultEntity result = roleService.allocResourcesForRole(roleId, allocResourceRequest.getResourceIds(), operatorId);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
