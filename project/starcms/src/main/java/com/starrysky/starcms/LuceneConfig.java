package com.starrysky.starcms;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName LuceneConfig
 * @Description
 * @Author adi
 * @Date 2022-08-23 14:41
 */
@Configuration
public class LuceneConfig {
    /**
     * lucene索引,存放位置
     */
    private static final String LUCENE_INDEX_PATH = "D:/starcms/lucene/index/";

    /**
     * 创建一个 Analyzer 实例
     * @return 分析器
     */
    @Bean
    public Analyzer analyzer() {
        return new SmartChineseAnalyzer();
    }

    /**
     * 获取索引位置，如果没有则创建
     * @return 索引目录
     */
    @Bean
    public Directory directory() throws IOException {
        Path path = Paths.get(LUCENE_INDEX_PATH);
        File file = path.toFile();
        if(!file.exists()) {
            file.mkdirs();
        }
        return FSDirectory.open(path);
    }

    /**
     * 创建indexWriter
     * @param directory 索引目录
     * @param analyzer 分析器
     * @return 索引Writer
     * @throws IOException
     */
    @Bean
    public IndexWriter indexWriter(Directory directory, Analyzer analyzer) throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 清空索引
//        indexWriter.deleteAll();
//        indexWriter.commit();
        return indexWriter;
    }

    /**
     * SearcherManager管理
     * @param directory 索引目录
     * @return 检索管理器
     * @throws IOException
     */
    @Bean
    public SearcherManager searcherManager(Directory directory, IndexWriter indexWriter) throws IOException {
        SearcherManager searcherManager = new SearcherManager(indexWriter, false, false, new SearcherFactory());
        ControlledRealTimeReopenThread controlledRealTimeReopenThread = new ControlledRealTimeReopenThread(indexWriter, searcherManager,
                5.0, 0.025);
        controlledRealTimeReopenThread.setDaemon(true);
        //线程名称
        controlledRealTimeReopenThread.setName("更新IndexReader线程");
        // 开启线程
        controlledRealTimeReopenThread.start();
        return searcherManager;
    }
}
