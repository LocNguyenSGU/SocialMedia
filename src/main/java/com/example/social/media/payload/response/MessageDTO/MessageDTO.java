package com.example.social.media.payload.response.MessageDTO;

import com.example.social.media.entity.MessageEmotion;
import com.example.social.media.enumm.MediaTypeEnum;
import com.example.social.media.enumm.MessageStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    //id user đang đăng nhập
    private int idSender;
    //inf about this message
    private MessageStatusEnum messageStatus;
    private LocalDateTime readAt;
    private MediaTypeEnum typeMessage;
    private String content;
    private LocalDateTime updateAt;
    private Boolean isDelete = false;
    private LocalDateTime createdAt;
    private List<MessageEmotion> messageEmotionList;
}
