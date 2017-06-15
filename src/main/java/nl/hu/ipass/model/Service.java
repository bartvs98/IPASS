package nl.hu.ipass.model;

import java.util.List;

import nl.hu.ipass.persistence.BeleggingDAO;
import nl.hu.ipass.persistence.BijafschriftDAO;
import nl.hu.ipass.persistence.KoersveranderingDAO;
import nl.hu.ipass.persistence.RekeningDAO;

public class Service {
	private RekeningDAO rekdao = new RekeningDAO();
	private BeleggingDAO beldao = new BeleggingDAO();
	private BijafschriftDAO bijdao = new BijafschriftDAO();
	private KoersveranderingDAO koedao = new KoersveranderingDAO();

	public List<Rekening> getAllRekeningen(int userID) {
		return rekdao.findAllRekeningen(userID);
	}

	public List<Bijafschrift> getBijafschrift(String rekeningnr) {
		return rekdao.findBijafschriftByRekening(rekeningnr);
	}

	public List<Belegging> getBelegging(String rekeningnr) {
		return rekdao.findBeleggingByRekening(rekeningnr);
	}
	
	public List<Koersverandering> getKoersveranderingByBelegging(String aandeelnaam){
		return rekdao.findKoersveranderingByBelegging(aandeelnaam);
	}

	public Rekening getRekeningByNr(String rekeningnr) {
		return rekdao.findRekeningByNr(rekeningnr);
	}

	public void deleteRekening(Rekening rekening) {
		rekdao.deleteRekening(rekening);
	}

	public void addRekening(Rekening rekening) {
		rekdao.addRekening(rekening);
	}
	
	public void updateRekening(Rekening rekening) {
		rekdao.updateRekening(rekening);
	}

	public void addBelegging(Belegging belegging) {
		beldao.addBelegging(belegging);
	}
	
	public void deleteBelegging(Belegging belegging){
		beldao.deleteBelegging(belegging);
	}
	
	public void verkoopBelegging(Belegging belegging){
		beldao.verkoopBelegging(belegging);
	}
	
	public void addBijafschrift(Bijafschrift bijafschrift) {
		bijdao.addBijafschrift(bijafschrift);
	}
	
	public void addKoers(Koersverandering koersverandering){
		koedao.addKoers(koersverandering);
	}

	public List<Koersverandering> findKoersverandering(int userID) {
		return koedao.findKoersverandering(userID);
	}
}
