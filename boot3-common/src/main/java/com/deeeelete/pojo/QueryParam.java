package com.deeeelete.pojo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


public abstract class QueryParam {
    private Integer page = 1;
    private Integer limit = 10;
    private String orderItem;
    private String orderType;

    public <T> Page<T> buildPage(ServiceImpl service) {

        Page<T> page = new Page((long)this.getPage(), (long)this.getLimit());
        if (page.getSize() == 0L) {
            page.setRecords(service.list(this.getQueryP()));
            page.setTotal((long)service.count(this.getQueryP()));
        } else {
            service.getBaseMapper().selectPage(page, this.getQueryP());
            page.setTotal(service.count(this.getQueryP()));
        }

        return page;
    }

    public abstract QueryWrapper getQueryP();


    public QueryParam() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}