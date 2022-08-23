package com.starrysky.starcms.lucene.dao;

import com.starrysky.starcms.entity.Content;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.SearcherManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LuceneDao
 * @Description
 * @Author adi
 * @Date 2022-08-23 14:45
 */
@Component
public class LuceneDao {
    @Resource
    private IndexWriter indexWriter;
    @Resource
    private SearcherManager searcherManager;
    @Resource
    private Analyzer analyzer;

    /**
     * 创建内容的索引
     * @param contents 要建立索引的数据集合
     * @throws IOException 建立索引失败时抛出异常
     */
    public void createIndex(List<Content> contents) throws IOException {
        List<Document> docs = new ArrayList<>();
        for (Content content : contents) {
            Document doc = createDocument(content);
            docs.add(doc);
        }
        indexWriter.addDocuments(docs);
        indexWriter.commit();
    }

    /**
     * 添加索引
     * @param content 添加索引的数据
     * @throws IOException 添加失败时抛出的异常
     */
    public void addIndex(Content content) throws IOException {
        Document doc = createDocument(content);
        indexWriter.addDocument(doc);
        indexWriter.commit();
    }

    /**
     * 删除索引
     * @param id 要删除索引的数据id
     * @throws IOException 删除失败时抛出的异常
     */
    public void deleteIndexById(String id) throws IOException {
        indexWriter.deleteDocuments(new Term("id", id));
        indexWriter.commit();
    }

    /**
     * 清空所有索引
     * @throws Exception 清空索引出错抛出的异常
     */
    public void deleteAllIndex() throws IOException {
        indexWriter.deleteAll();
        indexWriter.commit();
    }

    private Document createDocument(Content content) {
        Document doc = new Document();
        doc.add(new StringField("id", content.getId() + "", Field.Store.YES));
        doc.add(new TextField("title", content.getTitle(), Field.Store.YES));
        doc.add(new TextField("txt", content.getTxt(), Field.Store.YES));
        doc.add(new StoredField("channelTitle", content.getChannel().getTitle()));
        doc.add(new StoredField("addTime", new SimpleDateFormat().format(content.getAddTime())));
        return doc;
    }
}
