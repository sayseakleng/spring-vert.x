package com.mcnc.asyn.vertx.websocket.handle;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;


public class InboundHandler implements Handler<Buffer> {
	public static final String USER_KEY = "userId";
	
	private ServerWebSocket webSocket = null;
	
	public InboundHandler(ServerWebSocket webSocket) {
		this.webSocket = webSocket;
	}

	@Override
	public void handle(Buffer event) {
		MultiMap headers = webSocket.headers();
		String userId = headers.get(USER_KEY);
		
		webSocket.write(Buffer.buffer(String.format("User %s sent: %s", userId, event.toString())));
	}

}
