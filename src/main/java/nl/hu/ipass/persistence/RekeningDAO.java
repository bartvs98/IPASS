package nl.hu.ipass.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nl.hu.ipass.model.Belegging;
import nl.hu.ipass.model.Bijafschrift;
import nl.hu.ipass.model.Koersverandering;
import nl.hu.ipass.model.Rekening;

public class RekeningDAO extends BaseDAO {
	private List<Rekening> selectRekeningen(String query) {
		List<Rekening> results = new ArrayList<Rekening>();

		try (Connection conn = super.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				List<Bijafschrift> bijAfResult = findBijafschriftByRekening(rs.getString("rekeningnr"));
				List<Belegging> beleggingResult = findBeleggingByRekening(rs.getString("rekeningnr"));

				Rekening rekening = new Rekening(rs.getInt("userID"), rs.getString("rekeningnr"), rs.getString("depotnaam"),
						rs.getDouble("saldo"), bijAfResult, beleggingResult);

				results.add(rekening);
			}
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return results;
	}

	private List<Bijafschrift> selectBijafschriftByRekening(String query) {
		List<Bijafschrift> results = new ArrayList<Bijafschrift>();

		try (Connection conn = super.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Bijafschrift bijafschrift = new Bijafschrift(rs.getString("rekeningnr"), rs.getString("datum"), rs.getDouble("bedrag"));
				results.add(bijafschrift);
			}
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return results;
	}

	public List<Bijafschrift> findBijafschriftByRekening(String rekeningnr) {
		return selectBijafschriftByRekening("SELECT * FROM bijafschrift WHERE rekeningnr = '" + rekeningnr + "' ORDER BY datum");
	}

	private List<Belegging> selectBeleggingByRekening(String query) {
		List<Belegging> results = new ArrayList<Belegging>();

		try (Connection conn = super.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				List<Koersverandering> koersResult = findKoersveranderingByBelegging(rs.getString("naam"));
				
				Belegging belegging = new Belegging(rs.getInt("id"), rs.getString("datum"), rs.getDouble("koers"), rs.getInt("aantal"),
						rs.getDouble("totaal"), rs.getString("naam"), koersResult);
				results.add(belegging);
			}
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return results;
	}

	public List<Belegging> findBeleggingByRekening(String rekeningnr) {
		return selectBeleggingByRekening("SELECT * FROM belegging WHERE rekeningnr = '" + rekeningnr + "' ORDER BY datum");
	}
	
	private List<Koersverandering> selectKoersveranderingByBelegging(String query){
		List<Koersverandering> results = new ArrayList<Koersverandering>();

		try (Connection conn = super.getConnection()) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Koersverandering koersverandering = new Koersverandering(rs.getInt("id"), rs.getString("aandeelnaam"), rs.getString("datum"), rs.getDouble("koers"), rs.getDouble("totaal"));
				results.add(koersverandering);
			}
			
			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return results;
	}
	
	public List<Koersverandering> findKoersveranderingByBelegging(String aandeelnaam){
		return selectKoersveranderingByBelegging("SELECT * FROM koersverandering WHERE aandeelnaam = '" + aandeelnaam + "' ORDER BY datum");
	}

	public List<Rekening> findAllRekeningen(int userID) {
		return selectRekeningen("SELECT * FROM rekening WHERE userID = " + userID + " ORDER BY id");
	}

	public Rekening findRekeningByNr(String rekeningnr) {
		return selectRekeningen("SELECT * FROM rekening WHERE rekeningnr = '" + rekeningnr + "'").get(0);
	}

	public Rekening addRekening(Rekening rekening) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO rekening VALUES(nextval('rekening_id_seq'::regclass), ?, ?, ?, ?)");

			pstmt.setString(1, rekening.getRekeningNr());
			pstmt.setString(2, rekening.getDepotNaam());
			pstmt.setDouble(3, rekening.getSaldo());
			pstmt.setInt(4, rekening.getUserID());

			pstmt.executeUpdate();
			pstmt.close();

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return rekening;
	}

	public boolean deleteRekening(Rekening rekening) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM rekening WHERE rekeningnr = ?;");

			pstmt.setString(1, rekening.getRekeningNr());

			pstmt.executeUpdate();
			pstmt.close();

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}

		return true;
	}
	
	public Rekening updateRekening(Rekening rekening) {
		try (Connection conn = super.getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement("UPDATE rekening SET saldo = (saldo - ?) WHERE rekeningnr = ?");

			pstmt.setDouble(1, rekening.getSaldo());
			pstmt.setString(2, rekening.getRekeningNr());

			pstmt.executeUpdate();
			pstmt.close();

			conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return rekening;
	}
}