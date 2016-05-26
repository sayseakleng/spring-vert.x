package com.mcnc.asyn.vertx.spring.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mcnc.asyn.vertx.spring.service.RequestLogService;

@Service
public class RequestLogServiceImpl implements RequestLogService {
	private final static Logger logger = LoggerFactory.getLogger(RequestLogServiceImpl.class);

	@Override
	public void print(String message) {
		logger.info(message);
	}

}
