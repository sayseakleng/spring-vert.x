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
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

@Component
public class WebSocketServer extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
	private int serverPort = 8181;
	
	@Autowired
	private RequestLogService connectionLogService;
	
	@Override
	public void start() throws Exception {
		
		logger.info("WebSocket Server started on port {}", serverPort);
		HttpServer server = vertx.createHttpServer();
		server.websocketHandler(new Handler<ServerWebSocket>() {
		    public void handle(ServerWebSocket ws) {
		       
				try {
					String uri = ws.uri();
					String userId = WebUtils.getParam(uri, InboundHandler.USER_KEY);
			        		
			    	
					if (ws.path().equals("/websocket") && userId != null && !"".equals(userId)) {
						
						ws.headers().set(InboundHandler.USER_KEY, userId);
			    		
			    		// server responds on connection allowed
			    		//ws.write(Buffer.buffer(" Welcome from server"));

			    		//  Pumps data from a ReadStream to a WriteStream
			    		// Pump.pump(ws, ws).start();
						
						connectionLogService.logWebSocketConnection(ws);
						
						ws.handler(new InboundHandler(ws));
						
						
						// close event
						/*ws.closeHandler(new Handler<Void>() {
							@Override
							public void handle(Void event) {
							}
						});*/
			            
			        } else {
			            ws.reject();
			            logger.warn("Connection rejected");
			        }
				} catch (Exception e) {
					ws.reject();
					logger.error(e.getMessage(), e);
				}
				
		    	
		    }
		}).listen(serverPort);
		
		
		

	}
	
	@Override
	public void stop() throws Exception {
		logger.debug("Stopping Vertx...");
	}
}
