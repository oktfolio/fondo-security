package com.oktfolio.fondo.service;

import com.github.pagehelper.PageInfo;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.model.ListRolesRequest;
import com.oktfolio.fondo.model.SysRole;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
public interface RoleService {

    int createRole(SysRole sysRole, Integer operatorId);

    SysRole findById(Integer roleId);

    SysRole findByName(String name);

    SysRole findByCode(String code);

    PageInfo<SysRole> listRole(int pageNum, int pageSize, String orderBy, boolean desc, ListRolesRequest listRolesRequest);

    int deleteById(Integer roleId);

    int disableRole(Integer roleId, Integer operatorId);

    int enableRole(Integer roleId, Integer operatorId);

    ResultEntity allocResourcesForRole(Integer roleId, String resourceIds, Integer operatorId);

    void getRoleResources(Integer roleId);

    int updateRole(SysRole sysRole, Integer operatorId);

    List<Integer> listResourceIdsByEnabledRoleIds(List<Integer> roleIds);
}
