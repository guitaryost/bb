package bulleteinboard.dao;

import static bulleteinboard.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bulleteinboard.bean.User;
import bulleteinboard.exception.SQLRuntimeException;


public class UserDao {
	public User getUser(Connection connection, String loginId, String password){
		PreparedStatement ps = null;
		try{
			//仮のSQL文を作成。？はパラメータの仮の値
			String sql = "SELECT * FROM users WHERE login_id = ? AND password = ?";
			//仮のSQL文をConnectionクラスのprepareStatementメソッドに与えて、PreparedStatementオブジェクトの取得
				ps = connection.prepareStatement(sql);
			//？を入れたパラメータに実際の値を入れ込む
				ps.setString(1, loginId);
				ps.setString(2, password);

				System.out.println(ps.toString());
				ResultSet rs = ps.executeQuery();
				List<User> userList = toUserList(rs);
				if (userList.isEmpty() == true) {
					return null;
				} else if (2 <= userList.size()) {
					throw new IllegalStateException("2 <= userList.size()");
				} else {
					return userList.get(0);
				}
			} catch (SQLException e) {
				throw new SQLRuntimeException(e);
			} finally {
				close(ps);
		}
	}

	public List<User> getUsers(Connection connection){
		PreparedStatement ps = null;
		try{
			String sql = "SELECT * FROM users";
				ps = connection.prepareStatement(sql);

				System.out.println(ps.toString());
				ResultSet rs = ps.executeQuery();
				List<User> userList = toUserList(rs);
				return userList;
			} catch (SQLException e) {
				throw new SQLRuntimeException(e);
			} finally {
				close(ps);
		}


	}


	private List<User> toUserList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String loginId = rs.getString("login_id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				int branchId = rs.getInt("branch_id");
				int departmentId = rs.getInt("department_id");
				boolean account = rs.getBoolean("account");
				Timestamp insertDate = rs.getTimestamp("insert_date");
				Timestamp updateDate = rs.getTimestamp("update_date");

				User user = new User();
				user.setId(id);
				user.setLoginId(loginId);
				user.setPassword(password);
				user.setName(name);
				user.setBranchId(branchId);
				user.setDepartmentId(departmentId);
				user.setAccount(account);
				user.setInsertDate(insertDate);
				user.setUpdateDate(updateDate);

				ret.add(user);
			}
			return ret;
		} finally {
			close(rs);
		}
	}






	public void insert(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO users ( ");
			sql.append(" login_id");
			sql.append(", password");
			sql.append(", name");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", account");
			sql.append(", insert_date");
			sql.append(", update_date");
			sql.append(") VALUES (");
			sql.append(" ?");
			sql.append(", ?");
			sql.append(", ?");
			sql.append(", ?");
			sql.append(", ?");
			sql.append(", ?");
			sql.append(", CURRENT_TIMESTAMP");
			sql.append(", CURRENT_TIMESTAMP");
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, user.getLoginId());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getName());
			ps.setInt(4, user.getBranchId());
			ps.setInt(5, user.getDepartmentId());
			ps.setBoolean(6, user.isAccount());

			//sql文を表示させたいとき
			//System.out.println(ps.toString());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}



}

