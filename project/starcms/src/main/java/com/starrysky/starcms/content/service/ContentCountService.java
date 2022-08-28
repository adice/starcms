package com.starrysky.starcms.content.service;

import com.starrysky.starcms.content.dao.ContentDao;
import com.starrysky.starcms.util.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ClassName ContentCountService
 * @Description 用于内容统计的业务类
 * @Author adi
 * @Date 2022-08-27 20:29
 */
@Service
@Transactional(readOnly = true)
public class ContentCountService {
    @Resource
    private ContentDao contentDao;

    public LinkedHashMap<String, Integer> countByUserAndYear(Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        int days = (int) begin.until(now, ChronoUnit.DAYS);
        LinkedHashMap<String, Integer> heatMap = new LinkedHashMap<>(days);
        DateTimeFormatter dfMap = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i <= days; i++) {
            heatMap.put(dfMap.format(begin.plusDays(i)), 0);
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        List<Object[]> list = this.contentDao.countByUserIdAndYear(userId, Constant.CONTENT_STATUS_AUDITSUCCESS, begin.format(df), now.format(df));
        for (Object[] obj : list) {
            heatMap.put(obj[0].toString(), Integer.valueOf(obj[1].toString()));
        }
        return heatMap;
    }
}
