package com.chinaway.boss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmActionState;
import com.chinaway.boss.util.C3P0Util;

import lombok.extern.slf4j.Slf4j;

/**
 * 事件状态关系操作
 * 
 * @author Yaming Zhuge
 * @date 2016年5月18日 上午10:57:15
 */
@Slf4j
public class FsmActionStateDAO {
	
	private static final String INSERT_SQL = "insert into fsm_action_state (`current_state_code`, `action_code`, `next_state_code`) values (?, ?, ?)";
	private static final String DELETE_SQL = "delete from `fsm_action_state` where `current_state_code`=? and `action_code`=?";

	/**
	 * 添加关联
	 * 
	 * @param actionState
	 * @throws FsmException
	 */
	public static void add(FsmActionState actionState) throws FsmException{

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(INSERT_SQL);
			pst.setString(1, actionState.getCurrentStateCode());
			pst.setString(2, actionState.getActionCode());
			pst.setString(3, actionState.getNextStateCode());
			pst.execute();

		} catch (SQLException e) {
			log.error("add fsm_action_state failed:");
			e.printStackTrace();
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}
	
	/**
	 * 删除关联关系
	 * 
	 * @param currentStateCode 起始状态
	 * @param actionCode 触发事件
	 * @throws FsmException 
	 */
	public static void delete(String currentStateCode, String actionCode) throws FsmException{
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(DELETE_SQL);
			pst.setString(1, currentStateCode);
			pst.setString(2, actionCode);
			pst.execute();
		} catch (SQLException e) {
			log.warn("delete error :" + e.getMessage());
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}
	
}
