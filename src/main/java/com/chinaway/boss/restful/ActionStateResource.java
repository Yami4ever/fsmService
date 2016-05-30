package com.chinaway.boss.restful;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmActionState;
import com.chinaway.boss.restful.base.BaseResource;
import com.chinaway.boss.service.FsmActionStateService;

import lombok.extern.slf4j.Slf4j;

/**
 * 时间状态接口
 * 
 * @author Yaming Zhuge
 * @date 2016年5月19日 下午6:17:24
 */
@Slf4j
@Path("/actionState")
public class ActionStateResource extends BaseResource<FsmActionState> {

	/**
	 * 添加关联
	 * 
	 * @param actionState
	 * @return
	 */
	@POST
	@Path("/bind")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doPostBind(FsmActionState actionState) {

		if (actionState.getCurrentStateCode() == null || "".equals(actionState.getCurrentStateCode().trim())) {
			return msgMap(false, "currentStateCode不可为空");
		} else if (actionState.getActionCode() == null || "".equals(actionState.getActionCode().trim())) {
			return msgMap(false, "actionCode不可为空");
		} else if (actionState.getNextStateCode() == null || "".equals(actionState.getNextStateCode().trim())) {
			return msgMap(false, "nextStateCode不可为空");
		}

		log.info("add [POST] : Request params --> actionState=" + actionState.toString());

		try {
			FsmActionStateService.add(actionState);
			return msgMap(true, "绑定成功");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}

	}

	/**
	 * 删除
	 * 
	 * @param state
	 */
	@POST
	@Path("/unbind")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doPostDelete(FsmActionState actionState) {
		
		String currentStateCode = actionState.getCurrentStateCode();
		String actionCode = actionState.getActionCode();

		if (currentStateCode == null || "".equals(currentStateCode.trim())) {
			return msgMap(false, "currentStateCode不可为空");
		} else if (actionCode == null || "".equals(actionCode.trim())) {
			return msgMap(false, "actionCode不可为空");
		}

		log.info("delete [POST] : Request params --> currentStateCode={}, actionCode={}", currentStateCode, actionCode);

		try {
			FsmActionStateService.delete(currentStateCode, actionCode);
			return msgMap(true, "解绑成功");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}

	}

}
