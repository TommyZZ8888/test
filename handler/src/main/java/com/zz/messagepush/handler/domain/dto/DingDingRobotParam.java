package com.zz.messagepush.handler.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 钉钉自定义机器人 入参
 * @Author 张卫刚
 * @Date Created on 2023/4/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingRobotParam {

    private AtVO at;

    private TextVO text;

    private LinkVO link;

    private MarkdownVO markdown;

    private ActionCardVO actionCard;

    private FeedCardVO feedCard;

    private String msgType;

    /**
     * AtVO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class AtVO {
        /**
         * atMobiles
         */
        private List<String> atMobiles;
        /**
         * atUserIds
         */
        private List<String> atUserIds;
        /**
         * isAtAll
         */
        private Boolean isAtAll;
    }

    /**
     * TextVO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TextVO {
        /**
         * content
         */
        private String content;
    }

    /**
     * LinkVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class LinkVO {
        /**
         * text
         */
        private String text;
        /**
         * title
         */
        private String title;
        /**
         * picUrl
         */
        private String picUrl;
        /**
         * messageUrl
         */
        private String messageUrl;
    }

    /**
     * MarkdownVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class MarkdownVO {
        /**
         * title
         */
        private String title;
        /**
         * text
         */
        private String text;
    }

    /**
     * ActionCardVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ActionCardVO {
        /**
         * title
         */
        private String title;
        /**
         * text
         */
        private String text;
        /**
         * btnOrientation
         */
        private String btnOrientation;
        /**
         * btns
         */
        private List<BtnsVO> btns;

        /**
         * BtnsVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class BtnsVO {
            /**
             * title
             */
            private String title;
            /**
             * actionURL
             */
            private String actionURL;
        }
    }

    /**
     * FeedCardVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class FeedCardVO {
        /**
         * links
         */
        private List<LinksVO> links;

        /**
         * LinksVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class LinksVO {
            /**
             * title
             */
            private String title;
            /**
             * messageURL
             */
            private String messageURL;
            /**
             * picURL
             */
            private String picURL;
        }
    }
}