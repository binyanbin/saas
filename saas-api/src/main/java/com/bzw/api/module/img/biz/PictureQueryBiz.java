package com.bzw.api.module.img.biz;

import com.bzw.api.module.base.dao.PictureMapper;
import com.bzw.api.module.base.model.Picture;
import com.bzw.api.module.base.model.PictureExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yanbin
 */
@Service
public class PictureQueryBiz {

    @Autowired
    private PictureMapper pictureMapper;

    public Picture getPicture(String imgId) {
        PictureExample pictureExample = new PictureExample();
        pictureExample.createCriteria().andImageIdEqualTo(imgId);
        List<Picture> pictureList = pictureMapper.selectByExample(pictureExample);
        if (!CollectionUtils.isEmpty(pictureList)) {
            return pictureList.get(0);
        } else {
            return null;
        }
    }
}
