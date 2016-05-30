package com.chinaway.boss.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class FsmWebException extends WebApplicationException {

	private static final long serialVersionUID = 9133273767041391627L;

	public FsmWebException() {
		super(Response.status(401).entity("{\"result\": false, \"msg\":\"发生未知错误,请联系管理员\"").type(MediaType.APPLICATION_JSON).build());
	}

	public FsmWebException(String msg) {
		super(Response.status(401).entity("{\"result\": false,\"msg\":\"" + msg + "\"}").type(MediaType.APPLICATION_JSON).build());
	}

}
