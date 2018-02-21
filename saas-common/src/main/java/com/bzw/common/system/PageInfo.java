package com.bzw.common.system;

import java.util.List;

/**
 * @author yanbin
 */
public class PageInfo<T> {
    private Integer pageSize;
    private Long total;
    private Integer pageIndex;
    private List<T> items;

    public Integer getPageCount() {
        if (total.intValue() % pageSize > 0) {
            return total.intValue() / pageSize + 1;
        } else {
            return total.intValue() / pageSize;
        }
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}
