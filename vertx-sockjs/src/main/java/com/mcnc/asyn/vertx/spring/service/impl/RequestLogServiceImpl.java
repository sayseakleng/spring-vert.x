package com.mcnc.asyn.vertx.spring.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mcnc.asyn.vertx.spring.service.RequestLogService;
import com.mcnc.asyn.vertx.websocket.handle.InboundHandler;

import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

@Service
public class RequestLogServiceImpl implements RequestLogService {
	private final static Logger logger = LoggerFactory.getLogger(RequestLogServiceImpl.class);

	@Override
	public void print(String message) {
		logger.info(message);
	}
	
	@Override
	public void logWebSocketConnection(ServerWebSocket webSocket) {
		logger.info("Connection {} from user {}", webSocket.binaryHandlerID(),
				webSocket.headers().get(InboundHandler.USER_KEY));
	}

	@Override
	public void logWebSocketConnection(SockJSSocket sockJSSocket) {
		logger.info("Connection {} from user {}", sockJSSocket.writeHandlerID(),
				sockJSSocket.headers().get(InboundHandler.USER_KEY));
	}

}
