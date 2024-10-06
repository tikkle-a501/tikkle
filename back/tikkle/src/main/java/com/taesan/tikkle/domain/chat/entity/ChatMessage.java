package com.taesan.tikkle.domain.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    //    @JsonDeserialize(using = ToStringDeserializer.class)
    private UUID chatroomId;
    //	private UUID senderId;
    private String content;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "chatroomId=" + chatroomId +
                ", content='" + content + '\'' +
                '}';
    }
}
