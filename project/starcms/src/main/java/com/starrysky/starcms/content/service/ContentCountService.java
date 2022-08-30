package com.starrysky.starcms.content.service;

import com.starrysky.starcms.channel.dao.ChannelDao;
import com.starrysky.starcms.content.dao.ContentDao;
import com.starrysky.starcms.entity.Channel;
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
    @Resource
    private ChannelDao channelDao;

    /**
     * 数据录入人员登陆后，统计自己录入数据的数量，用于生成热图
     *
     * @param userId 用户id
     * @return 返回该用户每天的录入数量
     */
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

    public LinkedHashMap<String, LinkedHashMap<String, Integer>> countByMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        int days = (int) begin.until(now, ChronoUnit.DAYS);
        // 初始化Map结构，包括全部和各个栏目，每天的统计数值为0
        List<Channel> list = this.channelDao.findByStateAndParentChannelIsNullOrderByPriority(Constant.STATE_NORMAL);
        LinkedHashMap<String, LinkedHashMap<String, Integer>> chartMap = new LinkedHashMap<>(list.size() + 1);
        LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>(days);
        DateTimeFormatter dfMap = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i <= days; i++) {
            countMap.put(dfMap.format(begin.plusDays(i)), 0);
        }
        chartMap.put("全部", countMap);
        for(Channel channel : list) {
            LinkedHashMap<String, Integer> countMap1 = new LinkedHashMap<>(days);
            for (int i = 0; i <= days; i++) {
                countMap1.put(dfMap.format(begin.plusDays(i)), 0);
            }
            chartMap.put(channel.getTitle(), countMap1);
        }
        // 统计所有栏目数据
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        List<Object[]> listAll = this.contentDao.countByMonth(Constant.CONTENT_STATUS_AUDITSUCCESS, begin.format(df), now.format(df));
        for (Object[] obj : listAll) {
            chartMap.get("全部").put(obj[0].toString(), Integer.valueOf(obj[1].toString()));
        }
        // 统计各栏目数据
        for(Channel channel : list) {
            List<Object[]> listChannel = this.contentDao.countByChannelAndMonth(channel.getId(), Constant.CONTENT_STATUS_AUDITSUCCESS, begin.format(df), now.format(df));
            for (Object[] obj : listChannel) {
                chartMap.get(channel.getTitle()).put(obj[0].toString(), Integer.valueOf(obj[1].toString()));
            }
        }
        return chartMap;
    }

    public List<Object[]> countByDateGroupByUser(String beginTime, String endTime) {
        return this.contentDao.countByDateGroupByUser(beginTime, endTime, Constant.CONTENT_STATUS_AUDITSUCCESS);
    }
}
