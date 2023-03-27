package com.zz.messagepush.handler.handler;

import com.zz.messagepush.common.domain.AnchorInfo;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.AnchorStateEnum;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/3/13
 */
public abstract class Handler {

    /**
     * 标识渠道的code
     * 子类初始化时指定
     */
    protected Integer channelCode;

    @Autowired
    private HandlerHolder handlerHolder;

    /**
     * 初始化渠道和handler的映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    /**
     * 消息处理
     *
     * @param taskInfo
     * @return
     */
    public void doHandler(TaskInfo taskInfo) {
        if (!handler(taskInfo)) {
            LogUtils.print(AnchorInfo.builder().state(AnchorStateEnum.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
        }
        LogUtils.print(AnchorInfo.builder().state(AnchorStateEnum.SEND_SUCCESS.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());    }

    /**
     * 统一处理的下发接口
     *
     * @param taskInfo
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
