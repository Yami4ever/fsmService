package com.chinaway.boss.restful.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本资源访问
 * 
 * @author Yaming Zhuge
 * @date 2016年5月11日 上午10:27:53
 * @param <T>
 * @param <K>
 */
public class BaseResource<T> {

	/**
	 * 返回对象数据
	 * 
	 * @param obj
	 * @return
	 */
	protected Map<String, Object> dataMap(T obj) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("data", obj);
		return map;
	}

	/**
	 * 返回列表数据
	 * 
	 * @param objList
	 * @return
	 */
	protected Map<String, Object> dataMap(List<? extends T> objList) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("data", objList);
		return map;
	}

	/**
	 * 返回消息
	 * 
	 * @param result
	 * @param msg
	 * @return
	 */
	protected Map<String, Object> msgMap(boolean result, String msg) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("msg", msg);
		return map;
	}

}
