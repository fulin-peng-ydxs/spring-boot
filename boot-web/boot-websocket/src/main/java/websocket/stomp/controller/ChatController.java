package websocket.stomp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import websocket.stomp.entity.ChatMessage;

/**
 * 聊天控制器
 * 2023/10/27 01:11
 * @author pengshuaifeng
 */
@Controller
public class ChatController {

    /**
     * 消息接收&广播
     * 2023/10/27 01:11
     * @author pengshuaifeng
     */

    //@MessageMapping 是与 STOMP（Simple Text Oriented Messaging Protocol）一起使用的
    //具体来说，@MessageMapping 通常与 Spring 的 WebSocket 和 STOMP 结合使用，以实现实时通信功能。
    // 当客户端发送一个 STOMP 消息到指定的目的地（destination），@MessageMapping 注解将帮助你将这个消息路由到正确的处理方法。
    @MessageMapping("chat.sendMessage")

    //@SendTo("/topic/public") 是一个Spring Framework中用于处理WebSocket消息的注解，通常与STOMP协议一起使用，用于指定消息的目标（destination）。
    // 具体来说，它用于将处理后的消息发送到指定的目标，以便客户端可以订阅该目标以接收消息。
    //在WebSocket聊天应用程序等实时通信场景中，@SendTo 的作用如下：
    //指定目标（Destination）：@SendTo("/topic/public") 中的/topic/public 是消息的目标或目的地，也称为消息的发布位置。当处理程序处理完一条消息后，消息将被广播到指定的目标。
    //广播消息：处理程序使用@SendTo指示将处理后的消息广播给连接到目标/topic/public的所有客户端。这使得多个客户端可以订阅该目标，以接收实时消息。
    //实现发布-订阅模式：@SendTo 帮助实现了发布-订阅模式，其中消息发布者将消息发布到特定的目标，而订阅者可以监听该目标以接收相关消息。
    //在你的WebSocket处理程序中，当你使用@SendTo("/topic/public")时，任何发送到这个处理程序的消息都会被广播到/topic/public目标，以便所有连接到该目标的客户端都可以接收并显示这条消息。
    // 这是一种简单但强大的方式来实现群聊或广播消息给多个客户端的功能。

    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
