package com.chinaway.boss.exception;

/**
 * FSM 自定义异常
 * 
 * @author zhuge
 *
 */
public class FsmException extends Exception{

	private static final long serialVersionUID = 9133273767041391627L;
	
	public FsmException(String msg) {
		super(msg);
	}

}
