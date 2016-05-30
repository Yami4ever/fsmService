package com.chinaway.boss.dao;

import org.junit.Test;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmState;

public class DaoTest {
	
//	@Test
	public void testAddState() {
		FsmState state = new FsmState();
		state.setCode("test2");
		state.setName("nameTest");
		state.setDesc("This is a test state.");
		
		try {
			FsmStateDAO.addState(state);
		} catch (FsmException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIsExistState() throws FsmException {
		System.out.println(FsmStateDAO.isExistStateByCode("test3"));
		
	}

}
