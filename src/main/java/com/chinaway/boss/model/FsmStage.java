package com.chinaway.boss.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 状态阶段
 * 
 * @author Yaming Zhuge
 * @date 2016年5月17日 下午5:32:23
 */
@Setter
@Getter
public class FsmStage implements Serializable {

	private static final long serialVersionUID = 1135935033444695390L;

	private String code; // 编码
	private String name; // 名称
	private String desc; // 描述

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FsmStage [code=").append(code).append(", name=").append(name).append(", desc=").append(desc)
				.append("]");
		return builder.toString();
	}

}
