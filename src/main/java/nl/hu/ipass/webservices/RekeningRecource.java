package nl.hu.ipass.webservices;

import java.io.InputStream;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import nl.hu.ipass.model.Belegging;
import nl.hu.ipass.model.Bijafschrift;
import nl.hu.ipass.model.Koersverandering;
import nl.hu.ipass.model.Rekening;
import nl.hu.ipass.model.Service;
import nl.hu.ipass.model.ServiceProvider;

@Path("/rekeningen")
public class RekeningRecource {

	@GET
//	@RolesAllowed("user")
	@Path("{userID}")
	@Produces("application/json")
	public String getAllRekenigen(@PathParam("userID") int userID) {
		Service service = ServiceProvider.getService();
		JsonArrayBuilder jab = Json.createArrayBuilder();
		JsonObjectBuilder job = Json.createObjectBuilder();

		for (Rekening r : service.getAllRekeningen(userID)) {
			job.add("rekeningnr", r.getRekeningNr());
			job.add("depotnaam", r.getDepotNaam());
			job.add("saldo", r.getSaldo());

			JsonArrayBuilder jab2 = Json.createArrayBuilder();
			JsonObjectBuilder job2 = Json.createObjectBuilder();
			for (Bijafschrift b : r.getBijafschrift()) {
				job2.add("datum", b.getDate());
				job2.add("bedrag", b.getBedrag());

				jab2.add(job2);
			}

			JsonArrayBuilder jab3 = Json.createArrayBuilder();
			JsonObjectBuilder job3 = Json.createObjectBuilder();
			for (Belegging b : r.getBelegging()) {
				job3.add("id", b.getId());
				job3.add("datum", b.getDate());
				job3.add("koers", b.getKoers());
				job3.add("aantal", b.getAantal());
				job3.add("totaal", b.getTotaal());
				job3.add("naam", b.getNaam());
				
				JsonArrayBuilder jab4 = Json.createArrayBuilder();
				JsonObjectBuilder job4 = Json.createObjectBuilder();
				for (Koersverandering k : b.getKoersverandering()) {
					job4.add("naam", k.getAandeelNaam());
					job4.add("datum", k.getDatum());
					job4.add("koers", k.getKoers());
					job4.add("totaal", k.getTotaal());
					
					jab4.add(job4);
				}

				job3.add("koersverandering", jab4);
				jab3.add(job3);
			}

			job.add("bijafschrift", jab2);
			job.add("belegging", jab3);
			jab.add(job);
		}

		JsonArray array = jab.build();
		return array.toString();
	}

	@GET
	@RolesAllowed("user")
	@Path("getRekening/{rekeningnr}")
	@Produces("abblication/json")
	public String getRekening(@PathParam("rekeningnr") String rekeningnr) {
		Service service = ServiceProvider.getService();
		Rekening rekening = service.getRekeningByNr(rekeningnr);

		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("rekeningnr", rekening.getRekeningNr());
		job.add("depotnaam", rekening.getDepotNaam());
		job.add("saldo", rekening.getSaldo());

		JsonArrayBuilder jab2 = Json.createArrayBuilder();
		JsonObjectBuilder job2 = Json.createObjectBuilder();
		for (Bijafschrift b : service.getBijafschrift(rekeningnr)) {
			job2.add("datum", b.getDate());
			job2.add("bedrag", b.getBedrag());

			jab2.add(job2);
		}

		JsonArrayBuilder jab3 = Json.createArrayBuilder();
		JsonObjectBuilder job3 = Json.createObjectBuilder();
		for (Belegging b : service.getBelegging(rekeningnr)) {
			job3.add("id", b.getId());
			job3.add("datum", b.getDate());
			job3.add("koers", b.getKoers());
			job3.add("aantal", b.getAantal());
			job3.add("totaal", b.getTotaal());
			job3.add("naam", b.getNaam());

			jab3.add(job3);
		}

		job.add("bijafschrift", jab2);
		job.add("belegging", jab3);

		return job.build().toString();
	}

	@DELETE
	@RolesAllowed("user")
	@Path("{rekeningnr}")
	public Response deleteRekening(@PathParam("rekeningnr") String rekeningnr) {
		Service service = ServiceProvider.getService();

		Rekening deleteRekening = service.getRekeningByNr(rekeningnr);
		service.deleteRekening(deleteRekening);

		return Response.ok().build();
	}

	@POST
	@RolesAllowed("user")
	@Path("{rekeningnr}")
	@Produces("application/json")
	public Response addRekening(InputStream is) {
		Service service = ServiceProvider.getService();
		JsonObject object = Json.createReader(is).readObject();
		
		int userID = object.getInt("userID");
		String rekeningnr = object.getString("rekeningnr");
		String depotnaam = object.getString("depotnaam");
		double saldo = object.getInt("saldo");

		Rekening addRekening = new Rekening(userID, rekeningnr, depotnaam, saldo);
		service.addRekening(addRekening);

		return Response.ok().build();
	}
}
