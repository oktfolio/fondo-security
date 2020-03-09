package com.oktfolio.fondo.dao;

import com.oktfolio.fondo.model.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/10/15
 */
@Repository
public interface UserRoleMapper extends Mapper<SysUserRole>, MySqlMapper<SysUserRole> {
}