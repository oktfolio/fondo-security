package com.oktfolio.fondo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oktfolio.fondo.common.constant.Const;
import com.oktfolio.fondo.dao.ResourceMapper;
import com.oktfolio.fondo.model.ListResourcesRequest;
import com.oktfolio.fondo.model.ResourcesLiteBo;
import com.oktfolio.fondo.model.SysResource;
import com.oktfolio.fondo.model.SysUser;
import com.oktfolio.fondo.service.ResourceService;
import com.oktfolio.fondo.service.RoleService;
import com.oktfolio.fondo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/15
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private final static Logger LOG = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Override
    public SysResource findById(Integer resourcesId) {
        return resourceMapper.selectByPrimaryKey(resourcesId);
    }


    @Override
    public List<ResourcesLiteBo> findEnabledResourcesLiteBoListByUsername(String username) {
        LOG.info("findByUsername");

        SysUser sysUser = userService.findByUsername(username);

        if (sysUser == null) {
            return new ArrayList<>();
        }

        List<Integer> roleIdList = userService.listRoleIdsByUserId(sysUser.getId());

        List<Integer> resourceIdList = roleService.listResourceIdsByEnabledRoleIds(roleIdList);

        List<SysResource> resourceList = listEnabledResourcesIds(resourceIdList);

        List<ResourcesLiteBo> list = new ArrayList<>();

        resourceList.forEach((it) -> {
            ResourcesLiteBo resourcesLiteBo = new ResourcesLiteBo();
            resourcesLiteBo.setCode(it.getCode());
            resourcesLiteBo.setMethod(it.getMethod());
            resourcesLiteBo.setUri(it.getUri());
            list.add(resourcesLiteBo);
        });

        return list;
    }

    private List<SysResource> listEnabledResourcesIds(List<Integer> resourceIdList) {

        if (resourceIdList.isEmpty()) {
            return new ArrayList<>();
        }

        Example example = new Example(SysResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", Const.ResourceStatusEnum.ENABLED.getValue())
                .andIn("id", resourceIdList);

        return resourceMapper.selectByExample(example);
    }

    @Override
    public SysResource findByName(String name) {

        Example example = new Example(SysResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);

        return resourceMapper.selectOneByExample(example);
    }

    @Override
    public SysResource findByCode(String code) {

        Example example = new Example(SysResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", code);

        return resourceMapper.selectOneByExample(example);
    }

    @Override
    public int createResource(SysResource sysResource, Integer operatorId) {

        sysResource.setCreateBy(operatorId);
        sysResource.setCreateAt(LocalDateTime.now());

        return resourceMapper.insert(sysResource);
    }

    @Override
    public PageInfo<SysResource> listResource(int pageNum, int pageSize, String orderBy, boolean desc,
                                              ListResourcesRequest listResourcesRequest) {
        Example example = new Example(SysResource.class);
        Example.Criteria criteria = example.createCriteria();

        // 若为降序为 true，则降序排列，否则升序
        if (desc) {
            example.orderBy(orderBy).desc();
        } else {
            example.orderBy(orderBy).asc();
        }

        if (listResourcesRequest.getId() != null) {
            criteria.andEqualTo("id", listResourcesRequest.getId());
        }
        if (listResourcesRequest.getName() != null) {
            criteria.andEqualTo("name", listResourcesRequest.getName());
        }
        if (listResourcesRequest.getCode() != null) {
            criteria.andEqualTo("type", listResourcesRequest.getCode());
        }
        if (listResourcesRequest.getCode() != null) {
            criteria.andLike("code", "%" + listResourcesRequest.getCode() + "%");
        }


        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> resourceMapper.selectByExample(example));
    }

    @Override
    public int deleteById(Integer resourcesId) {
        return resourceMapper.deleteByPrimaryKey(resourcesId);
    }

    @Override
    public int disableResource(Integer resourcesId, Integer operatorId) {
        SysResource sysResource = new SysResource();

        sysResource.setStatus(Const.ResourceStatusEnum.DISABLED.getValue());
        sysResource.setUpdateAt(LocalDateTime.now());
        sysResource.setUpdateBy(operatorId);

        return resourceMapper.updateByPrimaryKeySelective(sysResource);
    }

    @Override
    public int enableResource(Integer resourcesId, Integer operatorId) {

        SysResource sysResource = new SysResource();

        sysResource.setStatus(Const.ResourceStatusEnum.ENABLED.getValue());
        sysResource.setUpdateAt(LocalDateTime.now());
        sysResource.setUpdateBy(operatorId);

        return resourceMapper.updateByPrimaryKeySelective(sysResource);
    }

    @Override
    public int updateResource(SysResource sysResource, Integer operatorId) {

        sysResource.setUpdateAt(LocalDateTime.now());
        sysResource.setUpdateBy(operatorId);

        return resourceMapper.updateByPrimaryKeySelective(sysResource);
    }
}
