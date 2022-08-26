package com.starrysky.starcms.lucene.dao;

import com.starrysky.starcms.entity.Channel;
import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.util.HtmlStringUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
     *
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
     *
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
     *
     * @param id 要删除索引的数据id
     * @throws IOException 删除失败时抛出的异常
     */
    public void deleteIndexById(String id) throws IOException {
        indexWriter.deleteDocuments(new Term("id", id));
        indexWriter.commit();
    }

    /**
     * 清空所有索引
     *
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
        doc.add(new TextField("txt", content.getTxt(), Field.Store.NO));
        if (content.getTxt().length() > 10) {
            doc.add(new StoredField("shorttxt", HtmlStringUtil.subStringHTML(content.getTxt(), 20)));
        } else {
            doc.add(new StoredField("shorttxt", content.getTxt()));
        }
        doc.add(new StoredField("channelTitle", content.getChannel().getTitle()));
        doc.add(new StoredField("addTime", new SimpleDateFormat("yyyy-MM-dd hh:mm:dd").format(content.getAddTime())));
        return doc;
    }

    public Page<Content> search(String keywords, int pageNum, int pageSize) throws Exception {
        String[] keys = keywords.split("\\s+");
        if (keys != null && keys.length > 0) {
            searcherManager.maybeRefresh();
            IndexSearcher indexSearcher = searcherManager.acquire();
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            for (String key : keys) {
                MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(new String[]{"title", "txt"}, analyzer);
                Query query = multiFieldQueryParser.parse(key);
                builder.add(query, BooleanClause.Occur.SHOULD);
            }
            BooleanQuery booleanQuery = builder.build();
            TopDocs topDocs = indexSearcher.search(booleanQuery, pageNum * pageSize);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            ArrayList<Content> list = new ArrayList<>();
            for (int i = (pageNum - 1) * pageSize; i < scoreDocs.length && i < pageNum * pageSize; i++) {
                Content content = new Content();
                Document doc = indexSearcher.doc(scoreDocs[i].doc);
                content.setId(Integer.parseInt(doc.get("id")));
                String title = getHtmlHighlight(booleanQuery, "title", doc.get("title"));
                content.setTitle(title == null ? doc.get("title") : title);
                String shorttxt = getHtmlHighlight(booleanQuery, "shorttxt", doc.get("shorttxt"));
                if(shorttxt == null) {
                    StringBuffer stringBuffer = new StringBuffer(doc.get("shorttxt"));
                    stringBuffer = stringBuffer.replace(stringBuffer.lastIndexOf("..."), stringBuffer.lastIndexOf("...") + 3, "<font color='red'>...</font>");
                    shorttxt = stringBuffer.toString();
                }
                content.setTxt(shorttxt);
                Channel channel = new Channel();
                channel.setTitle(doc.get("channelTitle"));
                content.setChannel(channel);
                content.setAddTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:dd").parse(doc.get("addTime")));
                list.add(content);
            }
            Page<Content> page = new PageImpl<>(list, PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC, "id"), topDocs.totalHits.value);
            return page;
        } else {
            return null;
        }
    }

    public String getHtmlHighlight(Query query, String fieldName, String fieldContent) throws IOException, InvalidTokenOffsetsException {
        // 设置高亮标签,可以自定义,这里我用html将其显示为红色
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        // 评分
        QueryScorer scorer = new QueryScorer(query);
        // 创建Fragmenter
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        // 高亮分析器
        Highlighter highlight = new Highlighter(formatter, scorer);
        highlight.setTextFragmenter(fragmenter);
        // 调用高亮方法
        return highlight.getBestFragment(analyzer, fieldName, fieldContent);
    }

}
