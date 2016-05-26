package com.mcnc.asyn.vertx;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mcnc.asyn.vertx.spring.service.RequestLogService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

@Component
public class HttpServer extends AbstractVerticle {
	private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
	private int serverPort = 8080;
	
	@Autowired
	private RequestLogService logService;
	
	@Override
	public void start() throws Exception {
		
		logger.info("HTTP Server started on port: {}", serverPort);

		vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {

			public void handle(HttpServerRequest req) {
				
				logService.print(String.format("Request on path: %s", req.path()));
				
				String file = req.path().equals("/") ? "/index.html" : req.path();
				
				//send file and handle file not found
				req.response().sendFile("webroot" + file, new Handler<AsyncResult<Void>>() {
					@Override
					public void handle(AsyncResult<Void> event) {
						if(event.cause() instanceof FileNotFoundException) {
							req.response().sendFile("webroot/error/404.html");
						}
					}
				});
				
				
			}

		}).listen(serverPort);
		
	}
}
