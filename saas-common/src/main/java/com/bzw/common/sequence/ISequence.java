package com.bzw.common.sequence;


import java.util.List;

/**
 *
 * @author yanbin
 * @date 2017/7/8
 */
public interface ISequence {

    /**
     * 获取一个表序列id
     *
     * @param seqType 表类型
     * @return id
     */
    Long newKey(SeqType seqType);

    /**
     * 获取一个表一组序列
     *
     * @param seqType 表类型
     * @param size 长度
     * @return 一组id
     */
    List<Long> newKeys(SeqType seqType, int size);

}
