package com.oktfolio.fondo.dao;

import com.oktfolio.fondo.model.SysRole;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
@Repository
public interface RoleMapper extends Mapper<SysRole>, MySqlMapper<SysRole> {
}
