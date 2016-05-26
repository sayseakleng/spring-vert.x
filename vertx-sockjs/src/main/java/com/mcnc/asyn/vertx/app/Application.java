package com.mcnc.asyn.vertx.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mcnc.asyn.vertx.HttpServer;
import com.mcnc.asyn.vertx.SockJsServer;
import com.mcnc.asyn.vertx.spring.SpringConfiguration;

import io.vertx.core.Vertx;


public class Application {
	
	public static void main(String[] arg) {
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
		Vertx vertx = Vertx.vertx();
		
		HttpServer httpServer = context.getBean(HttpServer.class);
		vertx.deployVerticle(httpServer);
		
		SockJsServer sockJsServer = context.getBean(SockJsServer.class);
		vertx.deployVerticle(sockJsServer);
	}
}
