package com.chinaway.boss.service;

import java.util.List;

import com.chinaway.boss.dao.FsmStateDAO;
import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmState;
import com.chinaway.boss.model.query.FsmStateQuery;
import com.chinaway.boss.util.EhcacheUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 有限状态机服务
 * 
 * @author Yaming Zhuge
 * @date 2016年5月11日 上午11:42:57
 */
@Slf4j
public class FsmStateService {

	/** 缓存主键前缀 */
	public static final String EHCACHE_PREFIX_STATE = "STATE_";

	/**
	 * 放入缓存
	 * 
	 * @param stateCode
	 * @param state
	 */
	private static void putIntoCache(String stateCode, FsmState state) {
		String _key = EHCACHE_PREFIX_STATE.concat(stateCode);
		EhcacheUtil.put(_key, state);
		log.info("Put element into ehcaches, key=[{}]", _key);
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param stateCode
	 * @return
	 */
	private static FsmState getFromCache(String stateCode) {
		return (FsmState) EhcacheUtil.get(EHCACHE_PREFIX_STATE.concat(stateCode));
	}

	/**
	 * 移除缓存
	 * 
	 * @param stateCode
	 */
	private static void removeFromCache(String stateCode) {
		EhcacheUtil.remove(EHCACHE_PREFIX_STATE.concat(stateCode));
	}

	/**
	 * @param stateCode
	 *            唯一标识
	 * @return
	 */
	public static FsmState getState(String stateCode) {
		FsmState state = getFromCache(stateCode);
		if (state == null) {
			state = FsmStateDAO.getState(stateCode);

			if (state != null) {
				putIntoCache(stateCode, state);
			}

		}
		return state;
	}

	/**
	 * 获取下一个状态节点
	 * 
	 * @param nextState
	 * @return
	 */
	public static FsmState getNextState(FsmStateQuery stateQuery) {
		return getNextState(stateQuery.getStateCode(), stateQuery.getActionCode());
	}

	/**
	 * 获取下一个状态节点
	 * 
	 * @param stateCode
	 *            起始状态编码
	 * @param actionCode
	 *            触发编码
	 * @return
	 */
	public static FsmState getNextState(String stateCode, String actionCode) {
		// TODO 此处需要将关系存入内存
		// 从缓存中获取对象
		// FsmState nextState = getFromCache(stateCode);
		// if (nextState == null) {
		// nextState = FsmStateDAO.getNextState(stateCode, actionCode);
		//
		// if (nextState != null) {
		// putIntoCache(stateCode, nextState);
		// }
		//
		// }

		FsmState nextState = FsmStateDAO.getNextState(stateCode, actionCode);
		return nextState;
	}

	/**
	 * 获取上一步节点
	 * 
	 * @param stateQuery
	 * @return
	 */
	public static FsmState getPreviousState(FsmStateQuery stateQuery) {
		return getPreviousState(stateQuery.getStateCode(), stateQuery.getActionCode());
	}

	/**
	 * 获取上一个状态节点
	 * 
	 * @param stateCode
	 *            起始状态编码
	 * @param actionCode
	 *            触发编码
	 * @return
	 */
	public static FsmState getPreviousState(String stateCode, String actionCode) {
		// TODO 需要将关心存入缓存
		// FsmState previousState = getFromCache(stateCode);
		//
		// if (previousState == null) {
		// previousState = FsmStateDAO.getPreviousState(stateCode, actionCode);
		// if (previousState != null) {
		// putIntoCache(stateCode, previousState);
		// }
		// }
		FsmState previousState = FsmStateDAO.getPreviousState(stateCode, actionCode);
		return previousState;
	}

	/**
	 * 获取所有状态(不使用缓存)
	 * 
	 * @return
	 */
	public static List<FsmState> findAllStates() {
		return FsmStateDAO.findAll();
	}

	/**
	 * 添加新的节点
	 * 
	 * @param state
	 * @return
	 * @throws FsmException
	 */
	public static boolean addState(FsmState state) throws FsmException {
		boolean isExisted = isExistStateByCode(state.getCode());
		if (isExisted) {
			return false;
		} else {
			FsmStateDAO.addState(state);
			log.info("Add new FsmState successful.");
			return true;
		}
	}

	/**
	 * 判断是否存在状态
	 * 
	 * @param code
	 * @return
	 * @throws FsmException
	 */
	public static boolean isExistStateByCode(String code) throws FsmException {
		FsmState fsmState = getFromCache(code);
		return fsmState != null ? true : FsmStateDAO.isExistStateByCode(code);
	}

	/**
	 * 更新状态 字段为null默认不更新
	 * 
	 * @param state
	 * @return
	 * @throws FsmException
	 */
	public static void updateState(FsmState state) throws FsmException {
		FsmStateDAO.updateState(state);
		removeFromCache(state.getCode());
		log.info("Update new FsmState successful.");
	}

	/**
	 * 删除状态
	 * 
	 * @param stateCode
	 * @return true: 删除成功 false: 存在关联信息，禁止删除
	 * 
	 * @throws FsmException
	 */
	public static boolean deleteState(String stateCode) throws FsmException {
		// 查询是否存在关联
		boolean isExisted = FsmStateDAO.isExistStateWithAction(stateCode);
		if (isExisted) {
			return false;
		} else {
			FsmStateDAO.deleteState(stateCode);
			removeFromCache(stateCode);
			return true;
		}
	}

	/**
	 * 获取阶段下的状态
	 * 
	 * @param stageCode
	 * @return
	 */
	// TODO need cahces?
	public static List<FsmState> getStatesByStage(String stageCode) {
		return FsmStateDAO.getStatesByStage(stageCode);

	}

}
