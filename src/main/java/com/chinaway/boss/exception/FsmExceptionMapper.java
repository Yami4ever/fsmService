package com.chinaway.boss.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Jersey自定义网络异常
 * 
 * @author Yaming Zhuge
 * @date 2016年5月11日 上午11:47:26
 */
@Provider
public class FsmExceptionMapper implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException e) {
		return Response.status(404).entity("{\"result\": false, \"msg\":\"" + e.getMessage() + "\"}").type(MediaType.APPLICATION_JSON)
				.build();
	}

}
