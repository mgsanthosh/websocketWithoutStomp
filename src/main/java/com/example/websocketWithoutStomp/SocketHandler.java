package com.example.websocketWithoutStomp;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocketHandler extends TextWebSocketHandler {

    List<WebSocketSession> totalSessions = new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(!totalSessions.contains(session)) {
            totalSessions.add(session);

        }
        System.out.println("A user with session Id:" + session.getId() + " created a session");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("A user with session Id: " + session.getId() + "send message " + message.toString());
        super.handleTextMessage(session, message);
        for(int i = 0; i<totalSessions.size(); i++) {
            WebSocketSession tempSession = totalSessions.get(i);
            if(tempSession != session) {
                tempSession.sendMessage(new TextMessage(message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        totalSessions.remove(session);
        System.out.println("A user with session Id:" + session.getId() + " changed status to " + status);
    }
}
