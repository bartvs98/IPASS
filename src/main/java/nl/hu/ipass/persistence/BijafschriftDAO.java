package nl.hu.ipass.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import nl.hu.ipass.model.Bijafschrift;

public class BijafschriftDAO extends BaseDAO {
	public Bijafschrift addBijafschrift(Bijafschrift bijafschrift) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO bijafschrift VALUES(?, (to_date(?, 'YYYY-MM-DD')), -?)");

			pstmt.setString(1, bijafschrift.getRekeningNr());
			pstmt.setString(2, bijafschrift.getDate());
			pstmt.setDouble(3, bijafschrift.getBedrag());

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return bijafschrift;
	}
}
