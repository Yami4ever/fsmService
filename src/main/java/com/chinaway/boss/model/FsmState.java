package com.chinaway.boss.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 状态机状态 Finite-state machine, FSM
 * 
 * @author zhuge
 *
 */
@Setter
@Getter
public class FsmState implements Serializable {

	private static final long serialVersionUID = 5545866829156976853L;

	private String name; // 状态名
	private String code; // 状态编码
	private String stageCode; // 阶段编码
	private String exCode; // 前工单状态编码
	private String desc; // 状态描述
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FsmState [name=").append(name).append(", code=").append(code).append(", desc=").append(desc)
				.append(", stageCode=").append(stageCode).append(", exCode=").append(exCode).append("]");
		return builder.toString();
	}

}
