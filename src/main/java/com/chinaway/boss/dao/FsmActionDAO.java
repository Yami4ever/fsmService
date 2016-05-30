package com.chinaway.boss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.chinaway.boss.exception.FsmException;
import com.chinaway.boss.model.FsmAction;
import com.chinaway.boss.util.C3P0Util;

import lombok.extern.slf4j.Slf4j;

/**
 * ActionDAO
 * 
 * @author Yaming Zhuge
 * @date 2016年5月11日 下午6:01:34
 */
@Slf4j
public class FsmActionDAO {

	private static final String SELECT_SQL = "select * from `fsm_action` where `code`= ?";
	private static final String SELECT_ALL_SQL = "select * from `fsm_action`";
	private static final String SELECT_COUNT_SQL = "select count(*) from `fsm_action` where `code` = ?";
	private static final String SELECT_COUNT_ACTION_SQL = "select count(*) from `fsm_action_state` where `action_code` = ?";

	private static final String INSERT_SQL = "insert into fsm_action (`code`, `name`, `desc`) values (?, ?, ?)";
	private static final String DELETE_SQL = "delete from `fsm_action` where `code` = ?";

	/**
	 * 添加
	 * 
	 * @param action
	 * @throws FsmException
	 */
	public static void add(FsmAction action) throws FsmException {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(INSERT_SQL);
			pst.setString(1, action.getCode());
			pst.setString(2, action.getName());
			pst.setString(3, action.getDesc());

			log.info("Excute SQL[{}], Params[{}]", INSERT_SQL, action.toString());
			pst.execute();

		} catch (SQLException e) {
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

	/**
	 * 查询是否存在指定Code的Action
	 * 
	 * @param code
	 * @return
	 * @throws FsmException
	 */
	public static boolean isExistByCode(String code) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_COUNT_SQL);
			pst.setString(1, code);

			log.info("Excute SQL[{}]", SELECT_COUNT_SQL);
			rs = pst.executeQuery();

			rs.next();
			long count = rs.getLong(1);

			return count > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}

	}

	/**
	 * 查询所有的Actions
	 * 
	 * @return
	 */
	public static List<FsmAction> findAll() {
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		List<FsmAction> stateList = new ArrayList<FsmAction>();

		try {
			conn = C3P0Util.getConnection();
			statement = conn.createStatement();

			log.info("Excute SQL[{}]", SELECT_ALL_SQL);
			rs = statement.executeQuery(SELECT_ALL_SQL);

			while (rs.next()) {
				FsmAction action = new FsmAction();
				action.setCode(rs.getString("code"));
				action.setName(rs.getString("name"));
				action.setDesc(rs.getString("desc"));
				stateList.add(action);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, statement, rs);
		}

		return stateList;
	}

	/**
	 * 依据code获取action
	 * 
	 * @param actionCode
	 * @return
	 */
	public static FsmAction get(String actionCode) {

		FsmAction action = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_SQL);
			pst.setString(1, actionCode);

			log.info("Excute SQL[{}], Params[{}]", SELECT_SQL, actionCode);
			rs = pst.executeQuery();

			if (rs.next()) {
				action = new FsmAction();
				action.setCode(rs.getString("code"));
				action.setName(rs.getString("name"));
				action.setDesc(rs.getString("desc"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			C3P0Util.close(conn, pst, rs);
		}

		return action;
	}

	/**
	 * 更新
	 * 
	 * @param action
	 * @throws FsmException
	 */
	public static void update(FsmAction action) throws FsmException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("update `fsm_action` set");
		if (action.getName() != null) {
			buffer.append(" `name`=");
			buffer.append("'");
			buffer.append(action.getName());
			buffer.append("',");
		}

		if (action.getDesc() != null) {
			buffer.append(" `desc`=");
			buffer.append("'");
			buffer.append(action.getDesc());
			buffer.append("',");
		}

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(" where `code`=");
		buffer.append("'");
		buffer.append(action.getCode());
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
	 * 删除
	 * 
	 * @param code
	 * @throws FsmException
	 */
	public static void delete(String code) throws FsmException {

		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(DELETE_SQL);
			pst.setString(1, code);
			pst.execute();
		} catch (SQLException e) {
			log.warn("delete error :" + e.getMessage());
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}

	/**
	 * 判断是否存在与Action相关的数据
	 * 
	 * @param actionCode
	 * @return
	 * @throws FsmException
	 */
	public static boolean isExistActionWithState(String actionCode) throws FsmException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getConnection();
			pst = conn.prepareStatement(SELECT_COUNT_ACTION_SQL);
			pst.setString(1, actionCode);

			log.info("excute sql [{}]", SELECT_COUNT_ACTION_SQL);
			rs = pst.executeQuery();

			rs.next();
			long count = rs.getLong(1);

			return count > 0;

		} catch (SQLException e) {
			log.error(SELECT_COUNT_ACTION_SQL.concat(actionCode) + " failed. ");
			e.printStackTrace();
			throw new FsmException(e.getMessage());
		} finally {
			C3P0Util.close(conn, pst, null);
		}
	}
}
