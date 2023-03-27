package com.zz.messagepush.support.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Description 生成 消息推送的URL 工具类
 * @Author 张卫刚
 * @Date Created on 2023/3/17
 */
public class TaskInfoUtils {

    private static final int TYPE_FLAG = 1000000;

    /**
     * 生成businessId
     * 模板类型+模板id+当天日期
     *
     * @param templateId
     * @param templateType
     * @return
     */
    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), "yyyyMMdd"));
        return Long.valueOf(String.format("%d%s", (long) templateType * TYPE_FLAG + templateId, today));
    }


    /**
     * 对url添加参数（用于链路追踪数据）
     * @param url
     * @param templateId
     * @param templateType
     * @return
     */
    public static String generateUrl(String url, Long templateId, Integer templateType) {
        url = url.trim();
        Long businessId = generateBusinessId(templateId, templateType);
        if (url.indexOf('?') == -1) {
            url = url + "?track_code_bid=" + businessId;
        } else {
            url = url + "&track_code_bid=" + businessId;
        }
        return url;
    }
}
