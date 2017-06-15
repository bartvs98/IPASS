package nl.hu.ipass.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends BaseDAO {
	public String findRoleForUsernameAndPassword(String username, String password) {
		String role = null;
		String query = "SELECT role FROM useraccount WHERE username = ? AND password = ?";

		try (Connection con = super.getConnection()) {

			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				role = rs.getString("role");

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return role;
	}
	
	public int findUserIdForUser(String username) {
		int userID = 0;
		String query = "SELECT userID FROM useraccount WHERE username = ? ";

		try (Connection ccon = super.getConnection()) {

			PreparedStatement pstmt = ccon.prepareStatement(query);
			pstmt.setString(1, username);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				userID = rs.getInt("userID");

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return userID;
	}
}
