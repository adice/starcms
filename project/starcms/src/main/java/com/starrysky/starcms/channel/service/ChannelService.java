package com.starrysky.starcms.channel.service;

import com.starrysky.starcms.channel.dao.ChannelDao;
import com.starrysky.starcms.entity.Channel;
import com.starrysky.starcms.util.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ChannelService
 * @Description
 * @Author adi
 * @Date 2022-08-02 13:52
 */
@Service
@Transactional(readOnly = true)
public class ChannelService {
    @Resource
    private ChannelDao channelDao;

    public List<Channel> listNormal(){
        return this.channelDao.findByStateAndParentChannelIsNullOrderByPriority(Constant.STATE_NORMAL);
    }
}
