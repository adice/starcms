package com.starrysky.starcms.lucene.service;

import com.starrysky.starcms.content.dao.ContentDao;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.lucene.dao.LuceneDao;
import com.starrysky.starcms.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName LuceneService
 * @Description 索引相关类，可以初始化、新增、删除索引
 * @Author adi
 * @Date 2022-08-23 14:57
 */
@Component
public class LuceneService {
    @Resource
    private LuceneDao luceneDao;
    @Resource
    private ContentDao contentDao;

    /**
     * 初始化已有数据的索引
     * 将所有状态为审核通过的数据创建索引
     *
     * @throws IOException 初始化索引失败，抛出异常
     */
    public void initIndex() throws IOException {
        int pageNum = 1;
        int pageSize = 10000;
        int pageCount;
        // 先清空所有索引
        luceneDao.deleteAllIndex();
        do {
            Page<Content> page = contentDao.findDynamic(null, null, Constant.CONTENT_STATUS_AUDITSUCCESS, null, null, null, null, pageNum, pageSize);
            pageCount = page.getTotalPages();
            if (pageCount > 0) {
                luceneDao.createIndex(page.getContent());
                pageNum++;
            }
        } while (pageNum <= pageCount);
    }

    /**
     * 当状态改为审核通过时，增加索引
     *
     * @param id 添加索引的数据的id
     * @throws IOException 添加失败抛出的异常
     */

    public void addIndex(int id) throws IOException {
        Content content = contentDao.getOne(id);
        luceneDao.addIndex(content);
    }

    /**
     * 当状态改为草稿、审核退回或删除该数据时，删除索引
     *
     * @param id 删除索引所对应的数据的id
     * @throws IOException 删除失败抛出的异常
     */
    public void deleteIndexById(String id) throws IOException {
        luceneDao.deleteIndexById(id);
    }

    public Page<Content> search(String keywords, int pageNum, int pageSize) throws Exception {
        return luceneDao.search(keywords, pageNum, pageSize);
    }
}
