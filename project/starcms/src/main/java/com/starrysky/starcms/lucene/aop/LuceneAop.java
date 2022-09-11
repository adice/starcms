package com.starrysky.starcms.lucene.aop;

import com.starrysky.starcms.entity.Content;
import com.starrysky.starcms.lucene.service.LuceneService;
import com.starrysky.starcms.util.Constant;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @ClassName LuceneAop
 * @Description 以AOP的方式给Content添加索引、删除索引
 * @Author adi
 * @Date 2022-08-23 16:55
 */
@Aspect
@Component
public class LuceneAop {
    @Resource
    private LuceneService luceneService;

    @After("execution(* com.starrysky.starcms.content.service.ContentService.check(..))")
    public void afterCheck(JoinPoint joinPoint) {
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            try {
                luceneService.addIndex(Integer.parseInt(joinPoint.getArgs()[0].toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @After("execution(* com.starrysky.starcms.content.service.ContentService.deny(..))")
    public void afterDeny(JoinPoint joinPoint) {
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            try {
                luceneService.deleteIndexById(joinPoint.getArgs()[0].toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @After("execution(* com.starrysky.starcms.content.service.ContentService.delete(..))")
    public void afterDelete(JoinPoint joinPoint) {
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            try {
                luceneService.deleteIndexById(joinPoint.getArgs()[0].toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @After("execution(* com.starrysky.starcms.content.service.ContentService.edit*(..))")
    public void afterEditDraft(JoinPoint joinPoint) {
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            try {
                Content content = (Content) joinPoint.getArgs()[0];
                luceneService.deleteIndexById(String.valueOf(content.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
