package nl.hu.ipass.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import nl.hu.ipass.model.Belegging;

public class BeleggingDAO extends BaseDAO {
	public Belegging addBelegging(Belegging belegging) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO belegging VALUES(nextval('belegging_id_seq'::regclass), ?, (to_date(?, 'YYYY-MM-DD')), ?, ?, ?, ?)");

			pstmt.setString(1, belegging.getRekeningNr());
			pstmt.setString(2, belegging.getDate());
			pstmt.setDouble(3, belegging.getKoers());
			pstmt.setInt(4, belegging.getAantal());
			pstmt.setDouble(5, belegging.getTotaal());
			pstmt.setString(6, belegging.getNaam());
			
			pstmt.executeUpdate();
			pstmt.close();
			
			PreparedStatement pstmt2 = conn.prepareStatement("UPDATE rekening SET saldo = (saldo - ?) WHERE rekeningnr = ?");
			
			pstmt2.setDouble(1, belegging.getTotaal());
			pstmt2.setString(2, belegging.getRekeningNr());
			
			pstmt2.executeUpdate();
			pstmt2.close();
			
			PreparedStatement pstmt3 = conn.prepareStatement("INSERT INTO koersverandering VALUES(nextval('koersverandering_id_seq'::regclass), ?, (to_date(?, 'YYYY-MM-DD')), ?, ?)");
			
			pstmt3.setString(1, belegging.getNaam());
			pstmt3.setString(2, belegging.getDate());
			pstmt3.setDouble(3, belegging.getKoers());
			pstmt3.setDouble(4, belegging.getTotaal());
			
			pstmt3.executeUpdate();
			pstmt3.close();

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return belegging;
	}

	public Belegging deleteBelegging(Belegging belegging) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM belegging WHERE id = ?");

			pstmt.setInt(1, belegging.getId());

			pstmt.executeUpdate();
			pstmt.close();
			
			PreparedStatement pstmt2 = conn.prepareStatement("UPDATE rekening SET saldo = (saldo + ?) WHERE rekeningnr = ?");
			
			pstmt2.setDouble(1, belegging.getTotaal());
			pstmt2.setString(2, belegging.getRekeningNr());
			
			pstmt2.executeUpdate();
			pstmt2.close();
			
			PreparedStatement pstmt3 = conn.prepareStatement("DELETE FROM bijafschrift WHERE rekeningnr = ?");
			
			pstmt3.setString(1, belegging.getRekeningNr());
			
			pstmt3.executeUpdate();
			pstmt3.close();
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return belegging;
	}
	
	public Belegging verkoopBelegging(Belegging belegging) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("UPDATE belegging SET datum = (to_date(?, 'YYYY-MM-DD')), aantal = ?, koers = ?, totaal = ? WHERE id = ?");

			pstmt.setString(1, belegging.getDate());
			pstmt.setInt(2, belegging.getAantal());
			pstmt.setDouble(3, belegging.getKoers());
			pstmt.setDouble(4, belegging.getTotaal());
			pstmt.setInt(5, belegging.getId());
			
			pstmt.executeUpdate();
			pstmt.close();
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return belegging;
	}
}
