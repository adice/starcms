package com.starrysky.starcms.lucene.test;

import com.starrysky.starcms.util.Constant;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName Test
 * @Description
 * @Author adi
 * @Date 2022-08-23 08:56
 */
public class Test {

    String LUCENE_INDEX_PATH = "D:/starcms/lucene/index/";

    public static void main(String[] args) {
        Test test = new Test();
        try {
//            test.createIndex();
            test.searchIndex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createIndex() throws Exception {
        // 获取索引目录
        Path path = Paths.get(LUCENE_INDEX_PATH);
        File file = path.toFile();
        if(!file.exists()) {
            file.mkdirs();
        }
        FSDirectory fsDirectory = FSDirectory.open(path);
        // 索引配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        // 构建IndexWriter，用于生成索引
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);

        // 生成文档的索引
        for (int i = 0; i < 10; i++) {
            Document document = new Document();
            document.add(new StringField("id", String.valueOf(i + 1), Field.Store.YES));
            document.add(new StringField("title", "title" + i, Field.Store.YES));
            document.add(new TextField("content", "这是内容" + i, Field.Store.YES));
            indexWriter.addDocument(document);
        }
//        Document document = new Document();
//        document.add(new StringField("id", String.valueOf(11), Field.Store.YES));
//        document.add(new StringField("title", "title", Field.Store.YES));
//        document.add(new TextField("content", "这是是是是是是是内容", Field.Store.YES));
//        indexWriter.addDocument(document);

        indexWriter.commit();
        indexWriter.close();

    }

    public void searchIndex() throws Exception {
        Path path = Paths.get(LUCENE_INDEX_PATH);
        FSDirectory fsDirectory = FSDirectory.open(path);
        // 索引读取类
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        // 索引搜索类
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 查询解析器
        Query query = new TermQuery(new Term(("content"), "是"));
        // 检索
        TopDocs topDocs = indexSearcher.search(query, 100);
        // 符合条件的文档个数
        long count = topDocs.totalHits.value;
        System.out.println("文档个数：" + count);
        // 获取搜索结果信息
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc scoreDoc : scoreDocs) {
            int docId = scoreDoc.doc;
            System.out.println("id:" + docId);
            System.out.println("分数：" + scoreDoc.score);
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
            System.out.println("==================================");
        }
        indexReader.close();

    }
}
