package com.chinaway.boss.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 触发事件
 * @author zhuge
 *
 */
@Setter
@Getter
public class FsmAction implements Serializable {

	private static final long serialVersionUID = 7513638474416403709L;

	private String name;
	private String code;
	private String desc;
	
	@Override
	public String toString() {
		return "FsmAction [name=" + name + ", code=" + code + ", desc=" + desc + "]";
	}



}
