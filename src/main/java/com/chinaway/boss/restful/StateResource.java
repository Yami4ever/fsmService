package com.chinaway.boss.restful;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmState;
import com.chinaway.boss.model.query.FsmStateQuery;
import com.chinaway.boss.restful.base.BaseResource;
import com.chinaway.boss.service.FsmStateService;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取节点详细
 * 
 * @author Yaming Zhuge
 * @date 2016年5月10日上午 10:43:44
 */
@Slf4j
@Path("/state")
public class StateResource extends BaseResource<FsmState> {

	/**
	 * 获取状态节点详细
	 * 
	 * @param stateCode
	 * @return
	 */
	@GET
	@Path("/query/{stateCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetQuery(@PathParam("stateCode") String stateCode) {
		log.info("Starting -- getInfo/{stateCode}  [GET] -- : Request params --> stateCode=" + stateCode);
		FsmState state = FsmStateService.getState(stateCode);
		return dataMap(state);
	}

	/**
	 * GET方式获取下一节点
	 * 
	 * @param stateCode
	 *            起始状态编码
	 * @param actionCode
	 *            动作编码
	 * @return
	 */
	@GET
	@Path("/queryNext/{stateCode}/{actionCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetQueryNext(@PathParam("stateCode") String stateCode,
			@PathParam("actionCode") String actionCode) {

		log.info("Starting -- queryNext/{stateCode}/{actionCode} [GET] : Request params --> stateCode=" + stateCode
				+ " actionCode=" + actionCode);

		FsmStateQuery stateQuery = new FsmStateQuery();
		stateQuery.setActionCode(actionCode);
		stateQuery.setStateCode(stateCode);

		FsmState nextState = FsmStateService.getNextState(stateQuery);
		return dataMap(nextState);
	}

	/**
	 * GET方式获取上一个节点
	 * 
	 * @param stateCode
	 *            起始状态编码
	 * @param actionCode
	 *            动作编码
	 * @return
	 */
	@GET
	@Path("/queryPrevious/{stateCode}/{actionCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetQueryPrevious(@PathParam("stateCode") String stateCode,
			@PathParam("actionCode") String actionCode) {

		log.info("queryPrevious/{stateCode}/{actionCode} [GET] : Request params --> stateCode=" + stateCode
				+ " actionCode=" + actionCode + ".");

		FsmStateQuery stateQuery = new FsmStateQuery();
		stateQuery.setActionCode(actionCode);
		stateQuery.setStateCode(stateCode);

		FsmState state = FsmStateService.getPreviousState(stateQuery);
		return dataMap(state);
	}

	/**
	 * GET方式获取所有状态节点
	 * 
	 * @return
	 */
	@GET
	@Path("/queryAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetQueryAll() {
		log.info("Starting -- /queryAll [GET] ...");
		List<FsmState> states = FsmStateService.findAllStates();
		return dataMap(states);
	}

	/**
	 * 添加新的状态
	 * 
	 * @param state
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doPostAdd(FsmState state) {
		log.info("Starting -- /add [POST] : stateInfo=" + state.toString());

		if (state.getCode() == null || state.getCode().trim().equals("")) {
			return msgMap(false, "无效的code");
		} else if (state.getName() == null || state.getName().trim().equals("")) {
			return msgMap(false, "无效的name");
		}

		try {
			boolean addState = FsmStateService.addState(state);
			return addState ? msgMap(true, "添加成功") : msgMap(false, "已存在编码为" + state.getCode() + "的状态");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}
	}

	/**
	 * 更新状态
	 * 
	 * @param state
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doPostUpdate(FsmState state) {

		log.info("Starting -- /update [POST] : stateInfo=" + state.toString());

		if (state.getCode() == null || state.getCode().trim().equals("")) {
			return msgMap(false, "无效的code");
		}

		try {
			FsmStateService.updateState(state);
			return msgMap(true, "更新成功");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}

	}

	/**
	 * 删除状态
	 * 
	 * @param stateCode
	 * @return
	 */
	@GET
	@Path("/delete/{stateCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetDelete(@PathParam("stateCode") String stateCode) {
		log.info("delete/{stateCode} [GET] : Request params --> stateCode=" + stateCode);
		try {
			boolean deleteResult = FsmStateService.deleteState(stateCode);
			return deleteResult ? msgMap(true, "删除成功") : msgMap(false, "无法删除：存在与状态编码" + stateCode + "关联的事件或流程");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}

	}

	/**
	 * 获取阶段下的状态
	 * 
	 * @param stageCode
	 * @return
	 */
	@GET
	@Path("/queryByStage/{stageCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetStatesByStage(@PathParam("stageCode") String stageCode) {
		log.info("getStatesByStage/{stageCode} [GET] : Request params --> stageCode=" + stageCode);
		List<FsmState> stages = FsmStateService.getStatesByStage(stageCode);
		return dataMap(stages);
	}

}
