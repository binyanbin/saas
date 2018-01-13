package com.bzw.api.module.basic.dao;

import com.bzw.api.module.basic.model.OrderDetail;
import com.bzw.api.module.basic.model.OrderDetailExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int countByExample(OrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int deleteByExample(OrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int insert(OrderDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int insertSelective(OrderDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    List<OrderDetail> selectByExample(OrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    OrderDetail selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int updateByExampleSelective(@Param("record") OrderDetail record, @Param("example") OrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int updateByExample(@Param("record") OrderDetail record, @Param("example") OrderDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int updateByPrimaryKeySelective(OrderDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table biz_order_detail
     *
     * @mbggenerated Mon Jan 08 16:06:20 CST 2018
     */
    int updateByPrimaryKey(OrderDetail record);
}