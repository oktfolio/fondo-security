package com.oktfolio.fondo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.oktfolio.fondo.common.constant.Const;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.dao.RoleMapper;
import com.oktfolio.fondo.dao.RoleResourceMapper;
import com.oktfolio.fondo.model.ListRolesRequest;
import com.oktfolio.fondo.model.SysRole;
import com.oktfolio.fondo.model.SysRoleResource;
import com.oktfolio.fondo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.oktfolio.fondo.common.result.UserResultCodeEnum.FAILED_ADD_USER_ROLES;
import static com.oktfolio.fondo.common.result.UserResultCodeEnum.FAILED_DELETE_USER_ROLES;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/15
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;


    @Override
    public int createRole(SysRole sysRole, Integer operatorId) {

        sysRole.setCreateBy(operatorId);
        sysRole.setCreateAt(LocalDateTime.now());

        return roleMapper.insert(sysRole);
    }

    @Override
    public SysRole findById(Integer roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public SysRole findByName(String name) {

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("name", name);

        return roleMapper.selectOneByExample(example);
    }

    @Override
    public SysRole findByCode(String code) {

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("code", code);

        return roleMapper.selectOneByExample(example);
    }

    @Override
    public PageInfo<SysRole> listRole(int pageNum, int pageSize, String orderBy, boolean desc,
                                      ListRolesRequest listRolesRequest) {
        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();

        // 若为降序为 true，则降序排列，否则升序
        if (desc) {
            example.orderBy(orderBy).desc();
        } else {
            example.orderBy(orderBy).asc();
        }

        if (listRolesRequest.getId() != null) {
            criteria.andEqualTo("id", listRolesRequest.getId());
        }
        if (listRolesRequest.getName() != null) {
            criteria.andLike("name", "%" + listRolesRequest.getName() + "%");
        }
        if (listRolesRequest.getCode() != null) {
            criteria.andLike("code", "%" + listRolesRequest.getCode() + "%");
        }

        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> roleMapper.selectByExample(example));
    }

    @Override
    public int deleteById(Integer roleId) {

        return roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int disableRole(Integer roleId, Integer operatorId) {

        SysRole role = new SysRole();

        role.setStatus(Const.RoleStatusEnum.DISABLED.getValue());
        role.setUpdateAt(LocalDateTime.now());
        role.setUpdateBy(operatorId);

        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int enableRole(Integer roleId, Integer operatorId) {

        SysRole role = new SysRole();

        role.setStatus(Const.RoleStatusEnum.ENABLED.getValue());
        role.setUpdateAt(LocalDateTime.now());
        role.setUpdateBy(operatorId);

        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Transactional
    @Override
    public ResultEntity allocResourcesForRole(Integer roleId, String resourceIds, Integer operatorId) {

        if (deleteAllResourcesByRoleId(roleId)) {
            String[] roles = resourceIds.split(",");

            List<String> resourceStrList = Lists.newArrayList(roles);
            List<Integer> resourceIdList = new ArrayList<>();
            List<SysRoleResource> sysRoleResourceList = new ArrayList<>();

            resourceStrList.forEach((it) -> resourceIdList.add(Integer.valueOf(it.trim())));

            resourceIdList.forEach((it) -> {
                SysRoleResource sysRoleResource = new SysRoleResource(roleId, it);
                sysRoleResource.setCreateAt(LocalDateTime.now());
                sysRoleResource.setCreateBy(operatorId);
                sysRoleResourceList.add(sysRoleResource);
            });

            int i = roleResourceMapper.insertList(sysRoleResourceList);

            if (i > 0) {
                return ResultEntity.created();
            } else {
                return ResultEntity.badRequest(FAILED_ADD_USER_ROLES);
            }
        }
        return ResultEntity.badRequest(FAILED_DELETE_USER_ROLES);
    }

    private boolean deleteAllResourcesByRoleId(Integer roleId) {
        Example example = new Example(SysRoleResource.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);

        roleResourceMapper.deleteByExample(example);

        return roleResourceMapper.selectCountByExample(example) > 0;
    }

    @Override
    public void getRoleResources(Integer roleId) {

    }

    @Override
    public int updateRole(SysRole sysRole, Integer operatorId) {

        sysRole.setUpdateAt(LocalDateTime.now());
        sysRole.setUpdateBy(operatorId);

        return roleMapper.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public List<Integer> listResourceIdsByEnabledRoleIds(List<Integer> roleIds) {

        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> enabledRoleIds = listEnableRoleIdsByRoleIds(roleIds);

        Example example = new Example(SysRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("roleId", enabledRoleIds);

        List<SysRoleResource> sysRoleResourceList = roleResourceMapper.selectByExample(example);

        List<Integer> resourceIds = new ArrayList<>();

        sysRoleResourceList.forEach((it) -> resourceIds.add(it.getResourceId()));

        return resourceIds;
    }


    private List<Integer> listEnableRoleIdsByRoleIds(List<Integer> roleIds) {

        if (roleIds.isEmpty()) {
            return null;
        }

        Example example = new Example(SysRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", Const.RoleStatusEnum.ENABLED.getValue())
                .andIn("id", roleIds);


        example.selectProperties("id");

        List<SysRole> enabledRoleList = roleMapper.selectByExample(example);

        List<Integer> enabledRoleIdList = new ArrayList<>();

        enabledRoleList.forEach((it) -> enabledRoleIdList.add(it.getId()));

        return enabledRoleIdList;
    }
}
