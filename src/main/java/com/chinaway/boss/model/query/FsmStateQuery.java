package com.chinaway.boss.model.query;

import java.io.Serializable;

import com.chinaway.boss.model.FsmAction;
import com.chinaway.boss.model.FsmState;

/**
 * 状态查询对象
 * 
 * @author zhuge
 *
 */
public class FsmStateQuery implements Serializable {

	private static final long serialVersionUID = 6225548518817184522L;

	private FsmState fsmState;
	private FsmAction fsmAction;

	public FsmStateQuery() {
		this.fsmState = new FsmState();
		this.fsmAction = new FsmAction();
	}

	/**
	 * 设置状态编码
	 * 
	 * @param code
	 */
	public void setStateCode(String code) {
		this.fsmState.setCode(code);
	}

	/**
	 * 获取状态编码
	 * 
	 * @return
	 */
	public String getStateCode() {
		return this.fsmState.getCode();
	}

	/**
	 * 设置事件编码
	 * 
	 * @param actionCode
	 */
	public void setActionCode(String actionCode) {
		fsmAction.setCode(actionCode);
	}

	/**
	 * 获取事件编码
	 * 
	 * @return
	 */
	public String getActionCode() {
		return fsmAction.getCode();
	}

	@Override
	public String toString() {
		return "FsmStateQuery [fsmState=" + fsmState + ", fsmAction=" + fsmAction + "]";
	}

}
