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
import com.chinaway.boss.model.FsmAction;
import com.chinaway.boss.restful.base.BaseResource;
import com.chinaway.boss.service.FsmActionService;

import lombok.extern.slf4j.Slf4j;

/**
 * Action资源
 * 
 * @author zhuge
 *
 */
@Slf4j
@Path("/action")
public class ActionResource extends BaseResource<FsmAction> {

	/**
	 * GET方式获取所有状态节点
	 * 
	 * @return
	 */
	@GET
	@Path("/queryAll")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FsmAction> doGetQueryAllActions() {
		log.info("Starting action -- /queryAll [GET] ...");
		return FsmActionService.findAll();
	}

	/**
	 * 添加新的Action
	 * 
	 * @param state
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doPostAdd(FsmAction action) {
		log.info("Starting -- /add [POST].  " + action.toString());

		if (action.getCode() == null || action.getCode().trim().equals("")) {
			return msgMap(false, "无效的code");
		} else if (action.getName() == null || action.getName().trim().equals("")) {
			return msgMap(false, "无效的name");
		}

		try {
			boolean result = FsmActionService.addAction(action);
			return result ? msgMap(true, "添加成功") : msgMap(false, "已存在编码为" + action.getCode() + "的动作");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}
	}

	/**
	 * 获取状态节点详细
	 * 
	 * @param stateCode
	 * @return
	 */
	@GET
	@Path("/query/{actionCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetQuery(@PathParam("actionCode") String actionCode) {
		log.info("Starting -- getInfo/{stateCode}  [GET] -- : Request params --> actionCode=[{}]", actionCode);
		FsmAction action = FsmActionService.getInfo(actionCode);
		return dataMap(action);
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
	public Map<String, Object> doPostUpdate(FsmAction action) {

		log.info("Starting -- /update [POST] : actionInfo=[{}]" + action.toString());

		if (action.getCode() == null || action.getCode().trim().equals("")) {
			return msgMap(false, "无效的code");
		}

		try {
			FsmActionService.updateAction(action);
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
	@Path("/delete/{actionCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetDelete(@PathParam("actionCode") String actionCode) {
		log.info("delete/{actionCode} [GET] : Request params --> actionCode=[{}]", actionCode);
		try {
			boolean deleteResult = FsmActionService.deleteAction(actionCode);
			return deleteResult ? msgMap(true, "删除成功") : msgMap(false, "无法删除：存在与事件编码" + actionCode + "关联的数据");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}

	}

}
