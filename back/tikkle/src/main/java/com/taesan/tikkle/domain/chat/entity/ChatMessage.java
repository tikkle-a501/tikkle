package com.taesan.tikkle.domain.chat.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @JsonDeserialize(using = ToStringDeserializer.class)
    private String chatroomId;
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
