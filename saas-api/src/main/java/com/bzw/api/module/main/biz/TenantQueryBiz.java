package com.bzw.api.module.main.biz;

import com.bzw.api.module.base.dao.TenantMapper;
import com.bzw.api.module.base.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yanbin
 */
@Service
public class TenantQueryBiz {

    @Autowired
    private TenantMapper tenantMapper;

    public Tenant getTenant(Long id){
        return tenantMapper.selectByPrimaryKey(id);
    }

}
