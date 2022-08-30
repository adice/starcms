package com.starrysky.starcms.content.controller;

import com.alibaba.excel.EasyExcel;
import com.starrysky.starcms.content.export.ContentExport;
import com.starrysky.starcms.content.export.ContentExportData;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName ContentExportController
 * @Description
 * @Author adi
 * @Date 2022-08-30 19:50
 */
@Controller
@RequestMapping("/backstage/contentexport")
public class ContentExportController {
    @Resource
    private ContentExport contentExport;

    @RequestMapping("/userscontent")
    public void list(String beginTime, String endTime, HttpServletResponse response) {
        try {
            if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
                response.getWriter().print("{\"status\":\"failure\",\"message\":\"起止时间不能为空\"}");
                response.getWriter().flush();
                response.getWriter().close();
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.parse(beginTime);
                simpleDateFormat.parse(endTime);
                List<ContentExportData> list = this.contentExport.exportContentData(beginTime, endTime);

                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
                String fileName = URLEncoder.encode("任务量统计", "UTF-8").replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
                EasyExcel.write(response.getOutputStream(), ContentExportData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                        .doWrite(list);
            }
        } catch (Exception e) {
            try {
                response.getWriter().print("{\"status\":\"failure\",\"message\":\"导出数据失败\"}");
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
