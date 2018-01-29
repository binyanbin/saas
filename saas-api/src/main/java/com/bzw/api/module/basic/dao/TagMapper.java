package com.bzw.api.module.basic.dao;

import com.bzw.api.module.basic.model.Tag;
import com.bzw.api.module.basic.model.TagExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int countByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int deleteByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int insert(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int insertSelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    List<Tag> selectByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    Tag selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int updateByPrimaryKeySelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_tag
     *
     * @mbggenerated Mon Jan 29 17:42:57 CST 2018
     */
    int updateByPrimaryKey(Tag record);
}