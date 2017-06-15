package nl.hu.ipass.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO {
	public String findRoleForUsernameAndPassword(String username, String password) {
		String role = null;
		String query = "SELECT role FROM useraccount WHERE username = ? AND password = ?";

		try (Connection conn = super.getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				role = rs.getString("role");

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return role;
	}
	
	public int findUserIdForUser(String username) {
		int userID = 0;
		String query = "SELECT userid FROM useraccount WHERE username = ? ";

		try (Connection conn = super.getConnection()) {

			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				userID = rs.getInt("userid");
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return userID;
	}
}
