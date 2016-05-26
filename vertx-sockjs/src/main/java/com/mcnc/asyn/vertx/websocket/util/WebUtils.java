package com.mcnc.asyn.vertx.websocket.util;

import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.MultiMap;
import io.vertx.core.http.CaseInsensitiveHeaders;

public class WebUtils {
	/**
	 * Extract Map from request params
	 * @param uri
	 * @return
	 */
	public static MultiMap getParams(String uri) {
		MultiMap params = new CaseInsensitiveHeaders();
		
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
		Map<String, List<String>> prms = queryStringDecoder.parameters();

		if (!prms.isEmpty()) {
			for (Map.Entry<String, List<String>> entry : prms.entrySet()) {
				params.add(entry.getKey(), entry.getValue());
			}
		}

		return params;
	}
	
	/**
	 * Get specific request param from user request
	 * @param uri
	 * @param key
	 * @return
	 */
	
	public static String getParam(String uri, String key) {
		MultiMap params = getParams(uri);
		return params.get(key);
	}
}