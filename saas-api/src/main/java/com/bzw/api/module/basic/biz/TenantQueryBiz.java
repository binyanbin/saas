package com.bzw.api.module.basic.biz;

import com.bzw.api.module.basic.dao.TenantMapper;
import com.bzw.api.module.basic.model.Technician;
import com.bzw.api.module.basic.model.Tenant;
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
