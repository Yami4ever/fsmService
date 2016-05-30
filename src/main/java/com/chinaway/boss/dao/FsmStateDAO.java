package com.chinaway.boss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmState;
import com.chinaway.boss.util.C3P0Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库操作
 * 
 * @author zhuge
 *
 */
@Slf4j
public class FsmStateDAO {

	private static final String SELECT_ALL_STATE = "select * from fsm_state";
	private static final String SELECT_NEXT_FSM_STATE = "select fs.* from fsm_action_state fsa, fsm_state fs where fsa.current_state_code = ? and action_code = ? and fs.code = next_state_code";
	private static final String SELECT_PREVIOUS_FSM_STATE = "select fs.* from fsm_action_state fsa, fsm_state fs where fsa.next_state_code = ? and action_code = ? and fs.code = current_state_code";
	private static final String SELECT_STATE_COUNT_BY_CODE = "select count(*) from `fsm_state` where `code` = ?";
	private static final String SELECT_STATE_BY_CODE = "select * from `fsm_state` where `code` = ?";
	private static final String SELECT_EXIST_STATE_WITH_ACTION = "select count(*) from `fsm_action_state` where `current_state_code` = ? or `next_state_code`= ?";
	private static final String SELECT_STATUS_BY_STAGE = "select state.* from fsm_stage stage, fsm_state state where stage.code=state.stage_code and stage.code=?";
	
	private static final String ADD_FSM_STATE = "insert into fsm_state (`code`, `name`, `ex_code`, `stage_code`, `desc`) values (?, ?, ?, ?, ?)";
	
	private static final String DELETE_FSM_STATE = "delete from `fsm_state` where `code` = ?";
	
	/**
	 * 封装对象
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static FsmState buildState(ResultSet rs) throws SQLException {
		FsmState state = new FsmState();
		state.setCode(rs.getString("code"));
		state.setName(rs.getString("name"));
		state.setDesc(rs.getString("desc"));
		state.setStageCode(rs.getString("stage_code"));
		state.setExCode(rs.getString("ex_code"));
		return state;
	}
	
	/**
	 * 通过编码获取状态节点信息
	 * 
	 * @param stateCode
	 *            状态编码
	 * @return
	 */
	public static FsmState getState(String stateCode) {

		FsmState state = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_STATE_BY_CODE);
			pst.setString(1, stateCode);
			rs = pst.executeQuery();

			if (rs.next()) {
				state = buildState(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, pst, rs);
		}

		return state;
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

		FsmState state = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_NEXT_FSM_STATE);
			pst.setString(1, stateCode);
			pst.setString(2, actionCode);
			rs = pst.executeQuery();

			if (rs.next()) {
				state = buildState(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, pst, rs);
		}

		return state;
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

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		FsmState state = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_PREVIOUS_FSM_STATE);
			pst.setString(1, stateCode);
			pst.setString(2, actionCode);
			rs = pst.executeQuery();

			if (rs.next()) {
				state = buildState(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, pst, rs);
		}

		return state;
	}

	/**
	 * 获取所有的节点状态
	 * 
	 * @return
	 */
	public static List<FsmState> findAll() {

		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		List<FsmState> stateList = new ArrayList<FsmState>();

		try {
			conn = C3P0Util.getConnection();
			statement = conn.createStatement();
			rs = statement.executeQuery(SELECT_ALL_STATE);

			while (rs.next()) {
				stateList.add(buildState(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, statement, rs);
		}

		return stateList;
	}

	/**
	 * 添加新的节点
	 * 
	 * @param state
	 * @throws FsmException
	 */
	public static void addState(FsmState state) throws FsmException {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(ADD_FSM_STATE);
			pst.setString(1, state.getCode());
			pst.setString(2, state.getName());
			pst.setString(3, state.getExCode());
			pst.setString(4, state.getStageCode());
			pst.setString(5, state.getDesc());
			pst.execute();

		} catch (SQLException e) {
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

	/**
	 * 查询是否存在指定Code的状态
	 * 
	 * @param code
	 * @return
	 * @throws FsmException 
	 */
	public static boolean isExistStateByCode(String code) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_STATE_COUNT_BY_CODE);
			pst.setString(1, code);
			rs = pst.executeQuery();
			rs.next();
			long count = rs.getLong(1);

			return count > 0;

		} catch (SQLException e) {
			log.error(SELECT_STATE_COUNT_BY_CODE.concat(code));
			e.printStackTrace();
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}

	}

	/**
	 * 更新状态
	 * 
	 * @param state
	 * @throws FsmException 
	 */
	public static void updateState(FsmState state) throws FsmException {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("update `fsm_state` set");
		if (state.getName() != null) {
			buffer.append(" `name`=");
			buffer.append("'");
			buffer.append(state.getName());
			buffer.append("',");
		}

		if (state.getExCode() != null) {
			buffer.append(" `ex_code`=");
			buffer.append("'");
			buffer.append(state.getExCode());
			buffer.append("',");
		}
		
		if (state.getStageCode() != null) {
			buffer.append(" `stage_code`=");
			buffer.append("'");
			buffer.append(state.getStageCode());
			buffer.append("',");
		}
		
		if (state.getDesc() != null) {
			buffer.append(" `desc`=");
			buffer.append("'");
			buffer.append(state.getDesc());
			buffer.append("',");
		}

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(" where `code`=");
		buffer.append("'");
		buffer.append(state.getCode());
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
	 * 删除状态
	 * 
	 * @param stateCode
	 * @throws FsmException 
	 */
	public static void deleteState(String stateCode) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(DELETE_FSM_STATE);
			pst.setString(1, stateCode);
			pst.execute();
		} catch (SQLException e) {
			log.warn("delete error :" + e.getMessage());
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

	/**
	 * 判断是否还存State与Action关联
	 * 
	 * @param stateCode
	 * @return
	 * @throws FsmException 
	 */
	public static boolean isExistStateWithAction(String stateCode) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_EXIST_STATE_WITH_ACTION);
			pst.setString(1, stateCode);
			pst.setString(2, stateCode);
			
			log.info("excute sql [{}]", SELECT_EXIST_STATE_WITH_ACTION);
			rs = pst.executeQuery();
			
			rs.next();
			long count = rs.getLong(1);

			return count > 0;

		} catch (SQLException e) {
			log.error(SELECT_EXIST_STATE_WITH_ACTION.concat(stateCode) + " failed. ");
			e.printStackTrace();
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}
	
	/**
	 * 获取阶段下的所有状态节点
	 * 
	 * @param stageCode
	 * @return
	 */
	public static List<FsmState> getStatesByStage(String stageCode) {

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<FsmState> stateList = new ArrayList<FsmState>();

		try {

			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_STATUS_BY_STAGE);
			pst.setString(1, stageCode);
			rs = pst.executeQuery();

			while (rs.next()) {
				stateList.add(buildState(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, pst, rs);
		}

		return stateList;
	}

}
