package com.chinaway.boss.service;

import org.junit.Test;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmState;

public class ServiceTest {
	
	@Test
	public void testAdd() throws FsmException {
		FsmState state = new FsmState();
		state.setCode("test3");
		System.out.println(FsmStateService.addState(state));
	}

}
