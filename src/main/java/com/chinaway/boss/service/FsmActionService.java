package com.chinaway.boss.service;

import java.util.List;

import com.chinaway.boss.dao.FsmActionDAO;
import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmAction;
import com.chinaway.boss.util.EhcacheUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * ActionService
 * 
 * @author Yaming Zhuge
 * @date 2016年5月11日 下午5:54:53
 */
@Slf4j
public class FsmActionService {
	
	/** Action缓存前缀 */
	public static final String EHCACHE_PREFIX_ACTION = "ACTION_";
	
	/**
	 * 放入缓存
	 * 
	 * @param stateCode
	 * @param state
	 */
	private static void putIntoCache(String code, FsmAction action) {
		String _key = EHCACHE_PREFIX_ACTION.concat(code);
		EhcacheUtil.put(_key, action);
		log.info("Put element into ehcaches, key=[{}]", _key);
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param stateCode
	 * @return
	 */
	private static FsmAction getFromCache(String code) {
		return (FsmAction) EhcacheUtil.get(EHCACHE_PREFIX_ACTION.concat(code));
	}
	
	/**
	 * 移除缓存
	 * 
	 * @param stateCode
	 */
	private static void removeFromCache(String stateCode) {
		EhcacheUtil.remove(EHCACHE_PREFIX_ACTION.concat(stateCode));
	}
	
	/**
	 * 添加Action
	 * 
	 * @param action
	 * @return
	 * @throws FsmException
	 */
	public static boolean addAction(FsmAction action) throws FsmException {
		boolean isExisted = isExistByCode(action.getCode());
		if (isExisted) {
			return false;
		} else {
			FsmActionDAO.add(action);
			log.info("Add new FsmAction successful.");
			return true;
		}
	}
	
	/**
	 * 判断是否存在Action
	 * 
	 * @param code
	 * @return
	 * @throws FsmException 
	 */
	public static boolean isExistByCode(String code) throws FsmException {
		// get from cache
		FsmAction action = getFromCache(code);
		return action != null ? true : FsmActionDAO.isExistByCode(code); // false then get from DB
	}

	
	/**
	 * 获取所有Actions(不使用缓存)
	 * 
	 * @return
	 */
	public static List<FsmAction> findAll() {
		return FsmActionDAO.findAll();
	}

	/**
	 * 获取Action
	 * 
	 * @param actionCode
	 * @return
	 */
	public static FsmAction getInfo(String actionCode) {
		FsmAction state = getFromCache(actionCode);
		if (state == null) {
			state = FsmActionDAO.get(actionCode);
			if (state != null) {
				putIntoCache(actionCode, state);
			}
		}
		return state;
	}
	
	
	/**
	 * 更新状态
	 *    字段为null默认不更新
	 *    
	 * @param state
	 * @return
	 * @throws FsmException 
	 */
	public static void updateAction(FsmAction action) throws FsmException {
		FsmActionDAO.update(action);
		removeFromCache(action.getCode());
		log.info("Update new FsmAction successful.");
	}

	
	/**
	 * 删除状态
	 * 
	 * @param actionCode
	 * @throws FsmException 
	 */
	public static boolean deleteAction(String actionCode) throws FsmException {
		// 查询是否存在关联
		boolean isExisted = FsmActionDAO.isExistActionWithState(actionCode);
		if(isExisted) {
			return false;
		} else {
			FsmActionDAO.delete(actionCode);
			removeFromCache(actionCode);
			return true;
		}
	}

}
