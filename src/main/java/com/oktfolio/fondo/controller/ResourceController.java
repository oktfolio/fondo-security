package com.oktfolio.fondo.controller;

import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.model.CreateResourceRequest;
import com.oktfolio.fondo.model.ListResourcesRequest;
import com.oktfolio.fondo.model.SysResource;
import com.oktfolio.fondo.security.JWTUserDetails;
import com.oktfolio.fondo.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.oktfolio.fondo.common.result.ResourceResultCodeEnum.*;
import static com.oktfolio.fondo.common.result.RoleResultCodeEnum.ROLE_CODE_EXISTS;
import static com.oktfolio.fondo.common.result.RoleResultCodeEnum.ROLE_NAME_EXISTS;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/resources")
    public ResponseEntity createResource(@RequestBody CreateResourceRequest createResourceRequest,
                                         @Autowired Authentication authentication) {

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        String name = createResourceRequest.getName();
        String code = createResourceRequest.getCode();

        SysResource byName = resourceService.findByName(name);
        if (byName != null) {

            ResultEntity resultEntity = ResultEntity.badRequest(RESOURCE_NAME_EXISTS);

            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysResource byCode = resourceService.findByCode(code);
        if (byCode != null) {
            ResultEntity resultEntity = ResultEntity.badRequest(RESOURCE_CODE_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysResource sysResource = new SysResource();
        sysResource.setName(name);
        sysResource.setLevel(createResourceRequest.getLevel());
        sysResource.setType(createResourceRequest.getType());
        sysResource.setCode(code);
        sysResource.setMethod(createResourceRequest.getMethod());
        sysResource.setUri(createResourceRequest.getUri());
        sysResource.setIcon(createResourceRequest.getIcon());
        sysResource.setParentId(createResourceRequest.getParentId());

        int res = resourceService.createResource(sysResource, operatorId);
        if (res == 1) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/resources")
    public ResponseEntity listResources(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                        @RequestParam(value = "order", defaultValue = "id") String orderBy,
                                        @RequestParam(value = "desc", defaultValue = "false") boolean desc,
                                        @ModelAttribute ListResourcesRequest listResourcesRequest) {

        return ResponseEntity.ok(resourceService.listResource(pageNum, pageSize, orderBy, desc, listResourcesRequest));
    }

    @GetMapping("/resources/{resourcesId:\\d+$}")
    public ResponseEntity getResource(@PathVariable("resourcesId") Integer resourcesId) {
        SysResource resource = resourceService.findById(resourcesId);
        if (resource != null) {
            return ResponseEntity.ok(resource);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/resources/{resourcesId:\\d+$}")
    public ResponseEntity updateWholeResource(@PathVariable("resourcesId") Integer resourcesId,
                                              @Autowired Authentication authentication,
                                              @RequestBody CreateResourceRequest updateResourceRequest) {

        SysResource byId = resourceService.findById(resourcesId);

        if (byId == null) {
            ResultEntity resultEntity = ResultEntity.notFound(RESOURCE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        String name = updateResourceRequest.getName();
        String code = updateResourceRequest.getCode();

        SysResource byName = resourceService.findByName(name);
        if (byName != null) {

            ResultEntity resultEntity = ResultEntity.badRequest(ROLE_NAME_EXISTS);

            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysResource byCode = resourceService.findByCode(code);
        if (byCode != null) {
            ResultEntity resultEntity = ResultEntity.badRequest(ROLE_CODE_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }

        SysResource foundResource = resourceService.findById(resourcesId);
        if (foundResource != null) {
            SysResource sysResource = new SysResource();
            sysResource.setId(resourcesId);
            sysResource.setName(name);
            sysResource.setLevel(updateResourceRequest.getLevel());
            sysResource.setType(updateResourceRequest.getType());
            sysResource.setCode(code);
            sysResource.setMethod(updateResourceRequest.getMethod());
            sysResource.setUri(updateResourceRequest.getUri());
            sysResource.setIcon(updateResourceRequest.getIcon());
            sysResource.setParentId(updateResourceRequest.getParentId());
            sysResource.setStatus(updateResourceRequest.getStatus());

            int res = resourceService.updateResource(sysResource, operatorId);

            if (res == 1) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
            ResultEntity resultEntity = ResultEntity.badRequest(FAILED_UPDATE_RESOURCE);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
        ResultEntity resultEntity = ResultEntity.notFound(RESOURCE_DELETE_OR_NOT_EXISTS);
        return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
    }

    @DeleteMapping("/resources/{resourcesId:\\d+$}")
    public ResponseEntity deleteResource(@PathVariable("resourcesId") Integer resourcesId) {
        int res = resourceService.deleteById(resourcesId);
        if (res == 1) {
            return ResponseEntity.ok().build();
        } else {
            ResultEntity resultEntity = ResultEntity.notFound(RESOURCE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
    }

    @PatchMapping("/resources/{resourcesId:\\d+$}/disable")
    public ResponseEntity disableResource(@PathVariable("resourcesId") Integer resourcesId,
                                          @Autowired Authentication authentication) {
        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        SysResource resource = resourceService.findById(resourcesId);

        if (resource != null) {
            int res = resourceService.disableResource(resourcesId, operatorId);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                ResultEntity resultEntity = ResultEntity.badRequest(FAILED_DISABLE_RESOURCE);
                return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
            }
        } else {
            ResultEntity resultEntity = ResultEntity.notFound(RESOURCE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
    }

    @PatchMapping("/resources/{resourcesId:\\d+$}/enable")
    public ResponseEntity enableResource(@PathVariable("resourcesId") Integer resourcesId,
                                         @Autowired Authentication authentication) {
        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        SysResource resource = resourceService.findById(resourcesId);

        if (resource != null) {
            int res = resourceService.enableResource(resourcesId, operatorId);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                ResultEntity resultEntity = ResultEntity.badRequest(FAILED_ENABLE_RESOURCE);
                return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
            }
        } else {
            ResultEntity resultEntity = ResultEntity.notFound(RESOURCE_DELETE_OR_NOT_EXISTS);
            return new ResponseEntity<>(resultEntity, resultEntity.getStatus());
        }
    }
}
