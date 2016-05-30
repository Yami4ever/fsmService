package com.chinaway.boss.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 状态、事件关系表
 * 
 * @author Yaming Zhuge
 * @date 2016年5月16日 下午5:59:44
 */
@Setter
@Getter
public class FsmActionState implements Serializable {

	private static final long serialVersionUID = -1973656951467437506L;

	private String currentStateCode;
	private String actionCode;
	private String nextStateCode;

	@Override
	public String toString() {
		return "FsmActionState [currentStateCode=" + currentStateCode + ", actionCode=" + actionCode
				+ ", nextStateCode=" + nextStateCode + "]";
	}

}
