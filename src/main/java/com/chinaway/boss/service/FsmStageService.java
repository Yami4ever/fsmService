package com.chinaway.boss.service;

import java.util.List;

import com.chinaway.boss.dao.FsmStageDAO;
import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmStage;
import com.chinaway.boss.model.query.FsmStageQuery;
import com.chinaway.boss.util.EhcacheUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Stage Service
 * 
 * @author Yaming Zhuge
 * @date 2016年5月19日 下午2:22:32
 */
@Slf4j
public class FsmStageService {
	
	/** 缓存主键前缀 */
	public static final String EHCACHE_PREFIX_STAGE = "STAGE_";
	
	/**
	 * Add New Stage
	 * 
	 * @param stage
	 * @return
	 * @throws FsmException
	 */
	public static boolean addStage(FsmStage stage) throws FsmException {
		boolean isExisted = isExisted(stage.getCode());
		if (isExisted) {
			return false;
		} else {
			FsmStageDAO.add(stage);
			log.info("Add new FsmStage successful.");
			return true;
		}
	}
	
	/**
	 * Check Exists
	 * 
	 * @param stageCode
	 * @return
	 * @throws FsmException
	 */
	private static boolean isExisted(String stageCode) throws FsmException {
		FsmStage stage = getFromCache(stageCode);
		return stage != null ? true : FsmStageDAO.isExisted(stageCode);
	}
	
	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public static List<FsmStageQuery> queryAll() {
		return FsmStageDAO.queryAll();
	}
	
	
	/**
	 * 放入缓存
	 * 
	 * @param stateCode
	 * @param state
	 */
	private static void putIntoCache(String stageCode, FsmStage stage) {
		String _key = EHCACHE_PREFIX_STAGE.concat(stageCode);
		EhcacheUtil.put(_key, stageCode);
		log.info("Put element into ehcaches, key=[{}]", _key);
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param stateCode
	 * @return
	 */
	private static FsmStage getFromCache(String stageCode) {
		return (FsmStage) EhcacheUtil.get(EHCACHE_PREFIX_STAGE.concat(stageCode));
	}

	/**
	 * 移除缓存
	 * 
	 * @param stateCode
	 */
	private static void removeFromCache(String stageCode) {
		EhcacheUtil.remove(EHCACHE_PREFIX_STAGE.concat(stageCode));
	}

	/**
	 * UPDATE
	 * 
	 * @param stage
	 * @throws FsmException
	 */
	public static void updateStage(FsmStage stage) throws FsmException {
		FsmStageDAO.update(stage);
		removeFromCache(stage.getCode());
	}
	
	
	/**
	 * 查询
	 * 
	 * @param code
	 * @return
	 */
	public static FsmStage queryStage(String code) {
		FsmStage stage = getFromCache(code);
		if (stage == null) {
			stage = FsmStageDAO.query(code);
			if (stage != null) {
				putIntoCache(code, stage);
			}
		}
		return stage;
	}

	/**
	 * 删除
	 * 
	 * @param stageCode
	 * @return
	 */
	public static boolean deleteStage(String stageCode) {
		 try {
			FsmStageDAO.delete(stageCode);
			removeFromCache(stageCode);
			return true;
		} catch (FsmException e) {
			e.printStackTrace();
			return false;
		}
	}
}
