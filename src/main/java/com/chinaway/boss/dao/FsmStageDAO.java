package com.chinaway.boss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmStage;
import com.chinaway.boss.model.FsmState;
import com.chinaway.boss.model.query.FsmStageQuery;
import com.chinaway.boss.util.C3P0Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 阶段数据库操作
 * 
 * @author Yaming Zhuge
 * @date 2016年5月17日 下午6:08:05
 */
@Slf4j
public class FsmStageDAO {

	private static final String SELECT_COUNT_BY_CODE = "select count(*) from `fsm_stage` where `code`=?";
	private static final String INSERT_SQL = "insert into `fsm_stage` (`code`, `name`, `desc`) values (?, ?, ?);";
	private static final String DELETE_SQL = "delete from `fsm_stage` where `code`=?";
	private static final String SELECT_ALL_STAGE_STATE = "select a.code stageCode, a.name stageName, a.desc stageDesc, b.code stateCode, b.name stateName, b.desc stateDesc, b.ex_code stateExCode from `fsm_stage` a, `fsm_state` b where a.code = b.stage_code";

	private static final String SELECT_STAGE = "select * from `fsm_stage` where `code` = ?";

	/**
	 * 封装对象
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static FsmStage buildStage(ResultSet rs) throws SQLException {
		FsmStage stage = new FsmStage();
		stage.setCode(rs.getString("code"));
		stage.setName(rs.getString("name"));
		stage.setDesc(rs.getString("desc"));
		return stage;
	}

	/**
	 * 查询
	 * 
	 * @param code
	 * @return
	 */
	public static FsmStage query(String code) {

		FsmStage stage = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_STAGE);
			pst.setString(1, code);
			rs = pst.executeQuery();
			if (rs.next()) {
				stage = buildStage(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, pst, rs);
		}

		return stage;
	}

	/**
	 * 查询所有阶段信息
	 * 
	 * @return
	 */
	public static List<FsmStageQuery> queryAll() {
		HashMap<String, FsmStageQuery> stageMap = new HashMap<>();

		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			statement = conn.createStatement();
			log.info("Run Selected : {}", SELECT_ALL_STAGE_STATE);
			rs = statement.executeQuery(SELECT_ALL_STAGE_STATE);

			while (rs.next()) {
				String stageCode = rs.getString("stageCode");
				FsmStageQuery stageQuery = stageMap.get(stageCode);
				if (stageQuery == null) {
					stageQuery = new FsmStageQuery();
					stageQuery.setCode(stageCode);
					stageQuery.setName(rs.getString("stageName"));
					stageQuery.setDesc(rs.getString("stageDesc"));
					stageQuery.setStates(new ArrayList<FsmState>());

					stageMap.put(stageCode, stageQuery); // put result into map
				}
				FsmState state = new FsmState();
				state.setCode(rs.getString("stateCode"));
				state.setName(rs.getString("stateName"));
				state.setDesc(rs.getString("stateDesc"));
				state.setExCode(rs.getString("stateExCode"));

				stageQuery.getStates().add(state);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, statement, rs);
		}

		return new ArrayList<>(stageMap.values());
	}

	/**
	 * 添加阶段
	 * 
	 * @param stage
	 * @throws FsmException
	 */
	public static void add(FsmStage stage) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(INSERT_SQL);
			pst.setString(1, stage.getCode());
			pst.setString(2, stage.getName());
			pst.setString(3, stage.getDesc());

			log.info("Excute SQL[{}], Params[{}]", INSERT_SQL, stage.toString());
			pst.execute();

		} catch (SQLException e) {
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

	/**
	 * 更新
	 * 
	 * @param stage
	 * @throws FsmException
	 */
	public static void update(FsmStage stage) throws FsmException {
		StringBuffer buffer = new StringBuffer();

		buffer.append("update `fsm_stage` set");
		if (stage.getName() != null) {
			buffer.append(" `name`=");
			buffer.append("'");
			buffer.append(stage.getName());
			buffer.append("',");
		}

		if (stage.getDesc() != null) {
			buffer.append(" `desc`=");
			buffer.append("'");
			buffer.append(stage.getDesc());
			buffer.append("',");
		}

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(" where `code`=");
		buffer.append("'");
		buffer.append(stage.getCode());
		buffer.append("'");

		Connection conn = null;
		Statement statement = null;

		try {
			conn = C3P0Util.getConnection();
			statement = conn.createStatement();
			log.info("SQL: " + buffer.toString());
			statement.execute(buffer.toString());
		} catch (SQLException e) {
			log.warn("update error :" + e.getMessage());
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, statement, null);
		}
	}

	/**
	 * 删除对象
	 * 
	 * @param stageCode
	 * @throws FsmException
	 */
	public static void delete(String stageCode) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(DELETE_SQL);
			pst.setString(1, stageCode);
			pst.execute();
		} catch (SQLException e) {
			log.warn("delete error :" + e.getMessage());
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

	/**
	 * 是否存在
	 * 
	 * @param code
	 * @return
	 * @throws FsmException
	 */
	public static boolean isExisted(String code) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_COUNT_BY_CODE);
			pst.setString(1, code);
			rs = pst.executeQuery();
			rs.next();
			long count = rs.getLong(1);

			return count > 0;

		} catch (SQLException e) {
			log.error(SELECT_COUNT_BY_CODE.concat("failed."));
			e.printStackTrace();
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

}
