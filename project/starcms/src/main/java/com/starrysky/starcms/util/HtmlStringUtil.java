package com.starrysky.starcms.util;

/**
 * @ClassName HtmlStringUtil
 * @Description 用于截取带有HTML标签的文字
 * @Author adi
 * @Date 2022-08-24 14:29
 */
public class HtmlStringUtil {
    public static String subStringHTML(String param, int length) {
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            if (!isCode && !isHTML) {
                n = n + 1;
                // UNICODE码字符占两个字节
                if ((temp + "").getBytes().length > 1) {
                    n = n + 1;
                }
            }
            result.append(temp);
            if (n >= length) {
                break;
            }
        }
        return fix(result.toString());
    }

    private static String fix(String str) {
        StringBuffer fixed = new StringBuffer(); // 存放修复后的字符串
        TagsList[] unclosedTags = getUnclosedTags(str);
        // 生成新字符串
        for (int i = unclosedTags[0].size() - 1; i > -1; i--) {
            fixed.append("<" + unclosedTags[0].get(i) + ">");
        }
        fixed.append(str + "...");
        for (int i = unclosedTags[1].size() - 1; i > -1; i--) {
            String s;
            if ((s = unclosedTags[1].get(i)) != null) {
                fixed.append("</" + s + ">");
            }
        }
        return fixed.toString();
    }

    private static TagsList[] getUnclosedTags(String str) {
        StringBuffer temp = new StringBuffer(); // 存放标签
        TagsList[] unclosedTags = new TagsList[2];
        unclosedTags[0] = new TagsList(); // 前不闭合，如有</div>而前面没有<div>
        unclosedTags[1] = new TagsList(); // 后不闭合，如有<div>而后面没有</div>
        boolean flag = false; // 记录双引号"或单引号'
        char currentJump = ' '; // 记录需要跳过''还是""
        char current = ' ', last = ' '; // 当前 & 上一个
        // 开始判断
        for (int i = 0; i < str.length(); ) {
            current = str.charAt(i++); // 读取一个字符
            if (current == '"' || current == '\'') {
                flag = !flag; // 若为引号，flag翻转
                currentJump = current;
            }
            if (!flag) {
                if (current == '<') { // 开始提取标签
                    current = str.charAt(i++);
                    if (current == '/') { // 标签的闭合部分，如</div>
                        current = str.charAt(i++);
                        // 读取标签
                        while (i < str.length() && current != '>') {
                            temp.append(current);
                            current = str.charAt(i++);
                        }
                        // 从tags_bottom移除一个闭合的标签
                        if (!unclosedTags[1].remove(temp.toString())) { // 若移除失败，说明前面没有需要闭合的标签
                            unclosedTags[0].add(temp.toString()); // 此标签需要前闭合
                        }
                        temp.delete(0, temp.length()); // 清空temp
                    } else { // 标签的前部分，如<div>
                        last = current;
                        while (i < str.length() && current != ' ' && current != '>') {
                            temp.append(current);
                            last = current;
                            current = str.charAt(i++);
                        }
                        // 已经读取到标签，跳过其他内容，如<div id=test>跳过id=test
                        while (i < str.length() && current != '>') {
                            last = current;
                            current = str.charAt(i++);
                            if (current == '"' || current == '\'') { // 判断引号
                                flag = !flag;
                                currentJump = current;
                                if (flag) { // 若引号不闭合，跳过到下一个引号之间的内容
                                    while (i < str.length() && str.charAt(i++) != currentJump);
                                    current = str.charAt(i++);
                                    flag = false;
                                }
                            }
                        }
                        if (last != '/' && current == '>') // 判断这种类型：<TagName />
                            unclosedTags[1].add(temp.toString());
                        temp.delete(0, temp.length());
                    }
                }
            } else {
                while (i < str.length() && str.charAt(i++) != currentJump)
                flag = false;
            }
        }
        return unclosedTags;
    }
}
