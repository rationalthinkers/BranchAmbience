package com.branch.log.event;

public class LogEventHandler extends Thread {

	LogPublisher publisher = new LogPublisher();
	String message = "";

	public LogEventHandler(String str) {

		message = str;
	}

	public void run() {

		publisher.handleMessage(message, null);
	}
}
