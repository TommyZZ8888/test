package com.zz.messagepush.web.controller;

import com.zz.messagepush.common.constant.AustinConstant;
import com.zz.messagepush.common.domain.ResponseResult;
import com.zz.messagepush.common.enums.ChannelType;
import com.zz.messagepush.web.service.MaterialService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 素材管理接口
 * @Author 张卫刚
 * @Date Created on 2023/5/10
 */
@Api("素材管理接口")
@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;


    public ResponseResult<Boolean> uploadMaterial(@RequestParam("file") MultipartFile file, String sendAccount, Integer sendChannel, String fileType) {
        if (ChannelType.DING_DING_WORK_NOTICE.getCode().equals(sendChannel)) {
            materialService.dingDingMaterialUpload(file, sendAccount, fileType);
        } else if (ChannelType.ENTERPRISE_WE_CHAT_ROBOT.getCode().equals(sendChannel)) {
            materialService.enterpriseWeChatRootMaterialUpload(file, sendAccount, fileType);
        } else if (ChannelType.ENTERPRISE_WE_CHAT.getCode().equals(sendChannel)) {
            materialService.enterpriseWeChatMaterialUpload(file, sendAccount, fileType);
        }
        return ResponseResult.success();
    }
}


