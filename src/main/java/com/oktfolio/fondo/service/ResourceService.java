package com.oktfolio.fondo.service;

import com.github.pagehelper.PageInfo;
import com.oktfolio.fondo.model.ListResourcesRequest;
import com.oktfolio.fondo.model.ResourcesLiteBo;
import com.oktfolio.fondo.model.SysResource;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
public interface ResourceService {

    SysResource findById(Integer resourcesId);

    List<ResourcesLiteBo> findEnabledResourcesLiteBoListByUsername(String username);

    SysResource findByName(String name);

    SysResource findByCode(String code);

    int createResource(SysResource sysResource, Integer operatorId);

    PageInfo<SysResource> listResource(int pageNum, int pageSize, String orderBy, boolean desc,
                                       ListResourcesRequest listResourcesRequest);

    int deleteById(Integer resourcesId);

    int disableResource(Integer resourcesId, Integer operatorId);

    int enableResource(Integer resourcesId, Integer operatorId);

    int updateResource(SysResource sysResource, Integer operatorId);
}
