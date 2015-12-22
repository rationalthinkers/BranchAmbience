package com.branch.log.event;

import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnClose;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/log")
public class LogPublisher {

	private static HashMap<String, Session> clientMap = new HashMap<String, Session>();

	@OnOpen
	public void open(Session session) {

		System.out.println("Session Id: " + session.getId());
	}

	@OnClose
	public void close(Session session) {
	}

	@OnMessage
	public void handleMessage(String clientId, Session session) {

		if (session == null) {

			Session localSession = clientMap.get(clientId.substring(0, clientId.indexOf(":")));
			try {

				localSession.getBasicRemote().sendText(clientId.substring(clientId.indexOf(":") + 1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			System.out.println("Client Id: " + clientId);
			if (!clientMap.containsKey(clientId)) {

				clientMap.put(clientId, session);
			}
		}
	}

}
