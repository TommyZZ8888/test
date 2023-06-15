package com.zz.messagepush.support.config;

import com.zz.messagepush.common.domain.dto.account.WeChatOfficialAccount;
import com.zz.messagepush.support.utils.WxServiceUtil;
import lombok.Data;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * @Description WeChatLoginAccountConfig
 * @Author 张卫刚
 * @Date Created on 2023/6/15
 */

@Profile("test")
@Configuration
@ConditionalOnProperty(name = "austin.login.officialAccount.enable", havingValue = "true")
@Data
public class WeChatLoginAccountConfig {

    @Value("${austin.login.official.account.appId}")
    private String appId;
    @Value("${austin.login.official.account.secret}")
    private String secret;
    @Value("${austin.login.official.account.secret}")
    private String token;


    @Autowired
    private WxServiceUtil wxServiceUtil;

    private WxMpService wxOfficialAccountLoginService;
    private WxMpDefaultConfigImpl wxMpDefaultConfig;
    private WxMpMessageRouter wxMpMessageRouter;

    @PostConstruct
    public void init() {
        WeChatOfficialAccount account = WeChatOfficialAccount.builder().appId(appId).secret(secret).token(token).build();
        wxOfficialAccountLoginService = wxServiceUtil.initOfficialAccountService(account);
        initRoute();
        initConfig();
    }


    private void initRoute() {
        wxMpMessageRouter = new WxMpMessageRouter(wxOfficialAccountLoginService);
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(null).end();
        wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(null).end();
    }

    private void initConfig() {
        wxMpDefaultConfig.setAppId(appId);
        wxMpDefaultConfig.setSecret(secret);
        wxMpDefaultConfig.setToken(token);
    }


}