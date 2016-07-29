package com.shiro.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shiro.model.Permission;
import com.shiro.model.PermissionExample;

/**
 * 权限 Dao 接口
 * 
 * @author StarZou
 * @since 2014年7月5日 上午11:59:03
 **/
public interface PermissionMapper  {
    int countByExample(PermissionExample example);

    int deleteByExample(PermissionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Permission record, @Param("example") PermissionExample example);

    int updateByExample(@Param("record") Permission record, @Param("example") PermissionExample example);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    /**
     * 通过角色id 查询角色 拥有的权限
     * 
     * @param roleId
     * @return
     */
    List<Permission> selectPermissionsByRoleId(Long roleId);
}