package nl.hu.ipass.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nl.hu.ipass.model.Koersverandering;

public class KoersveranderingDAO extends BaseDAO {
	public Koersverandering addKoers(Koersverandering koersverandering) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO koersverandering VALUES(nextval('koersverandering_id_seq'::regclass), ?, (to_date(?, 'YYYY-MM-DD')), ?, ?)");

			pstmt.setString(1, koersverandering.getAandeelNaam());
			pstmt.setString(2, koersverandering.getDatum());
			pstmt.setDouble(3, koersverandering.getKoers());
			pstmt.setDouble(4, koersverandering.getTotaal());
			
			pstmt.executeUpdate();
			pstmt.close();

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return koersverandering;
	}
	
	private List<Koersverandering> selectKoersverandering(String query) {
		List<Koersverandering> results = new ArrayList<Koersverandering>();

		try (Connection conn = super.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Koersverandering koersverandering = new Koersverandering(rs.getString("datum"), rs.getDouble("sum"));

				results.add(koersverandering);
			}
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return results;
	}
	
	public List<Koersverandering> findKoersverandering(int userID){
		return selectKoersverandering("SELECT k.datum, sum(k.totaal) FROM rekening r, belegging b, koersverandering k WHERE r.rekeningnr = b.rekeningnr AND b.naam = k.aandeelnaam AND r.userID = " + userID + " GROUP BY k.datum");
	}
}
