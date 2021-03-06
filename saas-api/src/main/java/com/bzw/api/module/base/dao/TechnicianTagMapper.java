package com.bzw.api.module.base.dao;

import com.bzw.api.module.base.model.TechnicianTag;
import com.bzw.api.module.base.model.TechnicianTagExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TechnicianTagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int countByExample(TechnicianTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int deleteByExample(TechnicianTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int insert(TechnicianTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int insertSelective(TechnicianTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    List<TechnicianTag> selectByExample(TechnicianTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    TechnicianTag selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int updateByExampleSelective(@Param("record") TechnicianTag record, @Param("example") TechnicianTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int updateByExample(@Param("record") TechnicianTag record, @Param("example") TechnicianTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int updateByPrimaryKeySelective(TechnicianTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_tag
     *
     * @mbggenerated Sun Jan 28 16:55:30 CST 2018
     */
    int updateByPrimaryKey(TechnicianTag record);
}