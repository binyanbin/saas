package com.bzw.api.module.basic.dao;

import com.bzw.api.module.basic.model.TechnicianPhoto;
import com.bzw.api.module.basic.model.TechnicianPhotoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yanbin
 */
public interface TechnicianPhotoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int countByExample(TechnicianPhotoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int deleteByExample(TechnicianPhotoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int insert(TechnicianPhoto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int insertSelective(TechnicianPhoto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    List<TechnicianPhoto> selectByExample(TechnicianPhotoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    TechnicianPhoto selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int updateByExampleSelective(@Param("record") TechnicianPhoto record, @Param("example") TechnicianPhotoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int updateByExample(@Param("record") TechnicianPhoto record, @Param("example") TechnicianPhotoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int updateByPrimaryKeySelective(TechnicianPhoto record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_technician_photo
     *
     * @mbggenerated Fri Jan 05 10:59:13 CST 2018
     */
    int updateByPrimaryKey(TechnicianPhoto record);
}