package com.zz.messagepush.service.api.impl.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.MessageTemplate;
import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.domain.dto.ContentModel;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.common.enums.RespStatusEnum;
import com.zz.messagepush.service.api.domain.dto.MessageParam;
import com.zz.messagepush.service.api.impl.domain.SendTaskModel;
import com.zz.messagepush.support.domain.entity.MessageTemplateEntity;
import com.zz.messagepush.support.mapper.MessageTemplateMapper;
import com.zz.messagepush.support.pipeline.BusinessProcess;
import com.zz.messagepush.support.pipeline.ProcessContext;
import com.zz.messagepush.support.utils.ContentHolderUtil;
import com.zz.messagepush.support.utils.TaskInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Description 拼接参数
 * @Author 张卫刚
 * @Date Created on 2023/3/16
 */
public class AssembleAction implements BusinessProcess {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel processModel = (SendTaskModel) context.getProcessModel();
        Long messageTemplateId = processModel.getMessageTemplateId();

        MessageTemplateEntity messageTemplateEntity = messageTemplateMapper.selectById(messageTemplateId);
        if (messageTemplateEntity==null || messageTemplateEntity.getIsDeleted().equals(AustinConstant.TRUE)){
            context.setNeedBreak(true).setResponse(ResponseResult.fail(RespStatusEnum.TEMPLATE_NOT_FOUND.getDescription()));
        }
    }


    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplateEntity messageTemplate){
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

        for (MessageParam messageParam : messageParamList) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam)).build();
//                    .deduplicationTime(messageTemplate.())
//                    .isNightShield(messageTemplate.getIsNightShield()).build();

            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }


    public static ContentModel getContentModelValue(MessageTemplateEntity messageTemplate,MessageParam messageParam){
        Integer sendChannel = messageTemplate.getSendChannel();
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());
        Class contentModelClass = ChannelType.getChanelModelClassByCode(sendChannel);

        //反射获取得到不同渠道对应的值
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = (ContentModel) ReflectUtil.newInstance(contentModelClass);
        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());

            if (StrUtil.isNotBlank(originValue)) {
                String resultValue = ContentHolderUtil.replaceHolder(originValue, variables);
                ReflectUtil.setFieldValue(contentModel, field, resultValue);
            }
        }

        return contentModel;
    }
    public static void main(String[] args) {

        MessageTemplateEntity messageTemplate = MessageTemplateEntity.builder().sendChannel(ChannelType.SMS.getCode()).msgContent("{\"url\":\"www.baidu.com/{$urlParam}\",\"content\":\"{$contentValue}\"}").build();
        HashMap<String, String> map = new HashMap<>();
        map.put("urlParam", "2222");
        map.put("contentValue", "3333");
        MessageParam messageParam = new MessageParam().setVariables(map);

        ContentModel contentModelValue = getContentModelValue(messageTemplate, messageParam);
        System.out.println(JSON.toJSONString(contentModelValue));
    }
}
