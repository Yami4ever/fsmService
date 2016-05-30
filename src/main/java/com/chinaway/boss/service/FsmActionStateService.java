package com.chinaway.boss.service;

import com.chinaway.boss.dao.FsmActionStateDAO;
import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmActionState;

public class FsmActionStateService {

	/**
	 * 添加
	 * 
	 * @param actionState
	 * @throws FsmException
	 */
	public static void add(FsmActionState actionState) throws FsmException {
		FsmActionStateDAO.add(actionState);
	}

	/**
	 * 删除
	 * 
	 * @param currentStateCode
	 * @param actionCode
	 * @throws FsmException
	 */
	public static void delete(String currentStateCode, String actionCode) throws FsmException {
		FsmActionStateDAO.delete(currentStateCode, actionCode);
	}

}
