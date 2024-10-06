package com.taesan.tikkle.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class ChatMessage {
//    @JsonDeserialize(using = UUIDDeserializer.class)
    private String chatroomId;
    //	private UUID senderId;
    private String content;

    @JsonCreator
    public ChatMessage(
            @JsonProperty("chatroomId") String chatroomId,
            @JsonProperty("content") String content) {
        this.chatroomId = chatroomId;
        this.content = content;
    }

//    public ChatMessage() {
//
//    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "chatroomId=" + chatroomId +
                ", content='" + content + '\'' +
                '}';
    }
}
