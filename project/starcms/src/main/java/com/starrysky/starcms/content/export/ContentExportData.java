package com.starrysky.starcms.content.export;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @ClassName ContentExportData
 * @Description
 * @Author adi
 * @Date 2022-08-30 19:45
 */
public class ContentExportData {
    @ExcelProperty("用户ID")
    private String id;
    @ExcelProperty("真实姓名")
    private String realName;
    @ExcelProperty("用户状态")
    private int state;
    @ExcelProperty("数量")
    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
