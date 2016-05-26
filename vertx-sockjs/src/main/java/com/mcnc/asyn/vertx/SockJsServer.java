package com.mcnc.asyn.vertx;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mcnc.asyn.vertx.spring.service.RequestLogService;
import com.mcnc.asyn.vertx.websocket.handle.InboundHandler;
import com.mcnc.asyn.vertx.websocket.util.WebUtils;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

@Component
public class SockJsServer extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(SockJsServer.class);
	private int serverPort = 8282;
	
	@Autowired
	private RequestLogService connectionLogService;
	

	@Override
	public void start() throws Exception {
		
		logger.info("SockJs Server started on port {}", serverPort);
		
		Router router = Router.router(vertx);
		SockJSHandler socketHandler = SockJSHandler.create(vertx).socketHandler(new Handler<SockJSSocket>() {

			@Override
			public void handle(SockJSSocket sockJSSocket) {
				
				String uri = sockJSSocket.uri();
				String userId = WebUtils.getParam(uri, InboundHandler.USER_KEY);
				
				
				if(userId == null || "".equals(userId)) {
					logger.warn("Connection rejected");
					sockJSSocket.close();
				}
				else {
					sockJSSocket.headers().set(InboundHandler.USER_KEY, userId);
					connectionLogService.logWebSocketConnection(sockJSSocket);
					
					
					sockJSSocket.handler(new Handler<Buffer>() {
						
						@Override
						public void handle(Buffer event) {
							sockJSSocket.write(Buffer.buffer(String.format("User %s sent: %s", userId, event.toString())));
						}
					});
				}
			}
		});
		
		
		
		
		
		
		router.route("/sockjs/*").handler(
	    		socketHandler
	    );

	    router.route().handler(StaticHandler.create());

	    HttpServer server = vertx.createHttpServer().requestHandler(router::accept);
	    
	  
	 
	    server.listen(serverPort);

	}
}
