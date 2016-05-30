package com.chinaway.boss.restful;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmStage;
import com.chinaway.boss.restful.base.BaseResource;
import com.chinaway.boss.service.FsmStageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/stage")
public class StageResource extends BaseResource<FsmStage> {

	/**
	 * Add
	 * 
	 * @param stage
	 * @return
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> add(FsmStage stage) {
		log.info("Starting -- /add [POST].  " + stage.toString());

		if (stage.getCode() == null || stage.getCode().trim().equals("")) {
			return msgMap(false, "无效的code");
		} else if (stage.getName() == null || stage.getName().trim().equals("")) {
			return msgMap(false, "无效的name");
		}

		try {
			boolean result = FsmStageService.addStage(stage);
			return result ? msgMap(true, "添加成功") : msgMap(false, "已存在编码为" + stage.getCode() + "的stage");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}
	}

	/**
	 * 查询所有阶段关联
	 * 
	 * @return
	 */
	@GET
	@Path("/queryAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> queryAll() {
		return dataMap(FsmStageService.queryAll());
	}

	/**
	 * 更新
	 * 
	 * @param stage
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doPostUpdate(FsmStage stage) {

		log.info("Starting -- /update [POST] : stageInfo=" + stage.toString());

		if (stage.getCode() == null || stage.getCode().trim().equals("")) {
			return msgMap(false, "无效的code");
		}

		try {
			FsmStageService.updateStage(stage);
			return msgMap(true, "更新成功");
		} catch (FsmException e) {
			return msgMap(false, e.getMessage());
		}
	}

	/**
	 * 删除
	 * 
	 * @param stageCode
	 * @return
	 */
	@GET
	@Path("/delete/{stageCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetDelete(@PathParam("stageCode") String stageCode) {
		log.info("delete/{stateCode} [GET] : Request params --> stateCode=" + stageCode);
		boolean deleteResult = FsmStageService.deleteStage(stageCode);
		return deleteResult ? msgMap(true, "删除成功") : msgMap(false, "无法删除：存在与编码" + stageCode + "关联的数据");
	}

	/**
	 * 查询
	 * 
	 * @param stageCode
	 * @return
	 */
	@GET
	@Path("/query/{stageCode}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> doGetQuery(@PathParam("stageCode") String stageCode) {
		FsmStage stage = FsmStageService.queryStage(stageCode);
		return dataMap(stage);
	}

}