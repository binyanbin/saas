package com.bzw.api.module.base.dao;

import com.bzw.api.module.base.model.Role;
import com.bzw.api.module.base.model.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int countByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int deleteByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int insert(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int insertSelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    List<Role> selectByExample(RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    Role selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pl_role
     *
     * @mbggenerated Thu Feb 08 09:32:52 CST 2018
     */
    int updateByPrimaryKey(Role record);
}