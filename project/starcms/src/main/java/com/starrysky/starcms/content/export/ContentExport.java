package com.starrysky.starcms.content.export;

import com.starrysky.starcms.content.dao.ContentDao;
import com.starrysky.starcms.util.Constant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ContentExport
 * @Description 用于导出一个时间段内所有用户的工作量
 * @Author adi
 * @Date 2022-08-30 19:45
 */
@Component
public class ContentExport {
    @Resource
    private ContentDao contentDao;

    public List<ContentExportData> exportContentData(String beginTime, String endTime) {
        List<Object[]> list = this.contentDao.countByDateGroupByUser(beginTime, endTime, Constant.CONTENT_STATUS_AUDITSUCCESS);
        List<ContentExportData> datas = new ArrayList<>();
        for (Object[] obj : list) {
            ContentExportData contentExportData = new ContentExportData();
            contentExportData.setId(obj[0].toString());
            contentExportData.setRealName(obj[1].toString());
            contentExportData.setState(Integer.parseInt(obj[2].toString()));
            contentExportData.setCount(Integer.parseInt(obj[3].toString()));
            datas.add(contentExportData);
        }
        return datas;
    }

}
