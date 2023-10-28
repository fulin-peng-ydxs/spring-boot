package websocket.stomp.entity;


import lombok.Data;

import java.util.Date;

/**
 * 聊天消息
 * author: pengshuaifeng
 * 2023/10/27
 */
@Data
public class ChatMessage {
    private String msg;
    private Date date;
}
