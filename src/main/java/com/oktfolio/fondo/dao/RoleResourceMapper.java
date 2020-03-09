package com.oktfolio.fondo.dao;

import com.oktfolio.fondo.model.SysRoleResource;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/10/15
 */
@Repository
public interface RoleResourceMapper extends Mapper<SysRoleResource>, MySqlMapper<SysRoleResource> {

    int allocResourcesForRole(Integer roleId, List<Integer> resourceIds);

    int deleteResourcesForRole(Integer roleId);
}
