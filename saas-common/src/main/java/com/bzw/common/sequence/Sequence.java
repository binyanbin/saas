package com.bzw.common.sequence;

/**
 *
 * @author yanbin
 * @date 2017/7/8
 */
public class Sequence {
    private String table;
    private Long nextValue;

    public Sequence(String table,Long nextValue){
        this.table = table;
        this.nextValue = nextValue;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Long getNextValue() {
        return nextValue;
    }

    public void setNextValue(Long nextValue) {
        this.nextValue = nextValue;
    }
}
