package com.zz.messagepush.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zz.messagepush.cron.constant.PendingConstant;
import com.zz.messagepush.cron.domain.vo.CrowdInfoVO;
import com.zz.messagepush.cron.pending.CrowdBatchTaskPending;
import com.zz.messagepush.cron.pending.PendingParam;
import com.zz.messagepush.cron.service.TaskHandler;
import com.zz.messagepush.cron.utils.ReadFileUtils;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description
 * @Author 张卫刚
 * @Date Created on 2023/4/4
 */

@Service
public class TaskHandlerImpl implements TaskHandler {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private CrowdBatchTaskPending crowdBatchTaskPending;

    @Override
    public void handler(Long messageTemplateId) {
        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.findById(messageTemplateId).orElse(null);

        if (messageTemplateEntity == null || messageTemplateEntity.getCronCrowdPath() == null) {
            return;
        }

        // 初始化pending的信息
        PendingParam<CrowdInfoVO> pendingParam = new PendingParam<>();
        pendingParam.setThresholdNum(PendingConstant.NUM_THRESHOLD)
                .setBlockingQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setThresholdTime(PendingConstant.TIME_THRESHOLD)
                .setThreadNum(PendingConstant.THREAD_NUM)
                .setPending(crowdBatchTaskPending);
        crowdBatchTaskPending.initAndStart(pendingParam);

        // 读取文件得到每一行记录给到队列做batch处理
        ReadFileUtils.getCsvRow(messageTemplateEntity.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }
            Map<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVO crowdInfoVo = CrowdInfoVO.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).build();
            crowdBatchTaskPending.pending(crowdInfoVo);
        });

    }
}
