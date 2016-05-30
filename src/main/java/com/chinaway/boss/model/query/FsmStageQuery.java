package com.chinaway.boss.model.query;

import java.util.List;

import com.chinaway.boss.model.FsmStage;
import com.chinaway.boss.model.FsmState;

import lombok.Getter;
import lombok.Setter;

/**
 * 阶段查询容器
 * 
 * @author Yaming Zhuge
 * @date 2016年5月18日 上午11:48:25
 */
@Getter
@Setter
public class FsmStageQuery extends FsmStage{
	
	private static final long serialVersionUID = -8816832944953984028L;
	private List<FsmState> states;

}
