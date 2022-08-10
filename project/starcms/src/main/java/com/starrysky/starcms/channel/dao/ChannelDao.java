package com.starrysky.starcms.channel.dao;

import com.starrysky.starcms.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ClassName ChannelDao
 * @Description
 * @Author adi
 * @Date 2022-08-02 13:51
 */
public interface ChannelDao extends JpaRepository<Channel, Integer> {
    public List<Channel> findByStateAndParentChannelIsNullOrderByPriority(int state);
}
