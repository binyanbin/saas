package com.bzw.api.module.img.biz;

import com.bzw.api.module.base.dao.PictureMapper;
import com.bzw.api.module.img.enums.Source;
import com.bzw.api.module.img.enums.Type;
import com.bzw.api.module.base.model.Picture;
import com.bzw.common.enums.Status;
import com.bzw.common.sequence.SeqType;
import com.bzw.common.sequence.SequenceService;
import com.bzw.common.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yanbin
 */
@Service
public class PictureEventBiz {

    @Autowired
    private PictureMapper pictureMapper;

    @Autowired
    private SequenceService sequenceService;

    public String add(String filePath,String orginalPath,Long tenantId,Long size){
        Picture picture = new Picture();
        picture.setId(sequenceService.newKey(SeqType.picture));
        picture.setCreatedTime(new Date());
        picture.setImageId(UuidUtil.newUuidString());
        picture.setOriginalPath(orginalPath);
        picture.setFilePath(filePath);
        picture.setStatusId(Status.Valid.getValue());
        picture.setSource(Source.api.getValue());
        picture.setType(Type.NoType.getValue());
        picture.setTenantId(tenantId);
        picture.setSize(size);
        pictureMapper.insert(picture);
        return picture.getImageId();
    }

    public boolean update(Picture picture){
        return  pictureMapper.updateByPrimaryKey(picture)>0;
    }
}
