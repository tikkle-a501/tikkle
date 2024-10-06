package com.taesan.tikkle.domain.chat.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taesan.tikkle.domain.config.UUIDDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @JsonDeserialize(using = UUIDDeserializer.class)
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
