package com.itwill.my_real_korea.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwill.my_real_korea.dto.chat.ChatMsg;
import com.itwill.my_real_korea.dto.chat.ChatRoom;
import com.itwill.my_real_korea.service.chat.ChatService;

import lombok.extern.log4j.Log4j2;

/*
 * Client로부터 받은 메세지를 Log출력하고 클라이언트에게 메세지를 보내는 역할
 */
@Component
@Log4j2 //log 변수를 사용하여 로그 기록 가능
public class ChatHandler extends TextWebSocketHandler{

	// 전체 채팅방에 접속한 모든 사용자들의 세션 정보
	public static Map<String, WebSocketSession> sessionMap = new HashMap<>();
	
	// ObjectMapper : JSON 데이터를 java객체로, java객체를 JSON데이터로 변환
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ChatService chatService;
	
	
	/* 채팅 유저 접속 시 호출되는 메소드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    	log.info("#ChattingHandler, afterConnectionEstablished");
        sessionMap.put(session.getId(), session);
        log.info(session.getId() + " 채팅 유저 접속");
    }

    /* payload : 전송되는 데이터 */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
    	// 등록된 세션을 돌면서 메세지 전달
    	sessionMap.forEach((sessionId, sessionInMap) -> {
			try {
				String payload = (String) message.getPayload();
//				// 채팅유저가 전송한(request) payload를 chatMsg로 변경(JSON->java객체)
//				ChatMsg chatMsg = objectMapper.readValue(payload, ChatMsg.class);
//				// 채팅 메세지가 가진 roomNo로 채팅방 정보 조회
//				ChatRoom chatRoom = chatService.selectRoomByRoomNo(chatMsg.getRoomNo());
//				// 채팅방에 있는 모든 채팅유저들에게 메세지 전달
//				chatRoom.handleAction(session, chatMsg, chatService);
				sessionInMap.sendMessage(message);
				log.info("메세지 : "+ message.getPayload());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
    }

    /* 채팅 유저 접속 해제 시 호출되는 메소드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    	log.info("#ChattingHandler, afterConnectionClosed");
        sessionMap.remove(session.getId());
        log.info(session.getId() + " 채팅 유저 접속 해제");
    }
}
