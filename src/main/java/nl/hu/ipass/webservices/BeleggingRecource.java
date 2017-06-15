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

@Path("/beleggingen")
public class BeleggingRecource {
	
	@GET
	@RolesAllowed("user")
	@Path("sumKoersverandering/{userID}")
	@Produces("application/json")
	public String getSumKoersverandering(@PathParam("userID") int userID) {
		Service service = ServiceProvider.getService();
		JsonArrayBuilder jab = Json.createArrayBuilder();
		JsonObjectBuilder job = Json.createObjectBuilder();

		for (Koersverandering k : service.findKoersverandering(userID)) {
			job.add("x", k.getDatum());
			job.add("y", k.getTotaal());
			jab.add(job);
		}

		JsonArray array = jab.build();
		return array.toString();
	}
	
	@POST
	@RolesAllowed("user")
	@Path("addKoers/{naam}")
	@Produces("application/json")
	public Response addKoers(InputStream is){
		Service service = ServiceProvider.getService();
		JsonObject object = Json.createReader(is).readObject();
		
		String aandeelnaam = object.getString("naam");
		String datum = object.getString("datum");
		double koers = Double.valueOf(object.get("koers").toString());
		double totaal =  Double.valueOf(object.get("totaal").toString());
		
		Koersverandering addKoers = new Koersverandering(aandeelnaam, datum, koers, totaal);
		service.addKoers(addKoers);
		
		return Response.ok().build();
	}

	@POST
	@RolesAllowed("user")
	@Path("addBelegging/{rekeningnr}")
	@Produces("application/json")
	public Response addBelegging(InputStream is) {
		Service service = ServiceProvider.getService();
		JsonObject object = Json.createReader(is).readObject();

		String rekeningnr = object.getString("rekeningnr");
		String datum = object.getString("datum");
		double koers = Double.valueOf(object.get("koers").toString());
		int aantal = object.getInt("aantal");
		double totaal =  Double.valueOf(object.get("totaal").toString());
		String naam = object.getString("naam");

		Belegging addBelegging = new Belegging(rekeningnr, datum, koers, aantal, totaal, naam);
		Bijafschrift addBijafschrift = new Bijafschrift(rekeningnr, datum, totaal);
		service.addBelegging(addBelegging);
		service.addBijafschrift(addBijafschrift);

		return Response.ok().build();
	}
	
	@DELETE
	@RolesAllowed("user")
	@Path("deleteBelegging/{id}/{totaal}/{rekeningnr}")
	@Produces("application/json")
	public Response deleteBelegging(@PathParam("id") int id, @PathParam("totaal") double totaal, @PathParam("rekeningnr") String rekeningnr) {
		Service service = ServiceProvider.getService();
		
		Belegging deleteBelegging = new Belegging(id, totaal, rekeningnr);
		service.deleteBelegging(deleteBelegging);

		return Response.ok().build();
	}
	
	@POST
	@RolesAllowed("user")
	@Path("verkoop/{id}")
	@Produces("application/json")
	public Response verkoopBelegging(InputStream is) {
		Service service = ServiceProvider.getService();
		JsonObject object = Json.createReader(is).readObject();
		
		int id = object.getInt("id");
		String datum = object.getString("datum");
		int aantal = object.getInt("aandelenAantal");
		double koers = Double.valueOf(object.get("koers").toString());
		double totaal = Double.valueOf(object.get("totaal").toString());
		double bedrag = Double.valueOf(object.get("bedrag").toString());
		String rekeningnr = object.getString("rekeningnr");
		
		Belegging verkoop = new Belegging(id, datum, koers, aantal, totaal);
		Bijafschrift bijschrift = new Bijafschrift(rekeningnr, datum, bedrag);
		Rekening updateSaldo = new Rekening(rekeningnr, bedrag);
		service.verkoopBelegging(verkoop);
		service.addBijafschrift(bijschrift);
		service.updateRekening(updateSaldo);
		return Response.ok().build();
	}
	
	@POST
	@RolesAllowed("user")
	@Path("koop/{id}")
	@Produces("application/json")
	public Response koopBelegging(InputStream is) {
		Service service = ServiceProvider.getService();
		JsonObject object = Json.createReader(is).readObject();
		
		int id = object.getInt("id");
		String datum = object.getString("datum");
		int aantal = object.getInt("aandelenAantal");
		double koers = Double.valueOf(object.get("koers").toString());
		double totaal = Double.valueOf(object.get("totaal").toString());
		double bedrag = Double.valueOf(object.get("bedrag").toString());
		String rekeningnr = object.getString("rekeningnr");
		
		Belegging verkoop = new Belegging(id, datum, koers, aantal, totaal);
		Bijafschrift bijschrift = new Bijafschrift(rekeningnr, datum, bedrag);
		Rekening updateSaldo = new Rekening(rekeningnr, bedrag);
		service.verkoopBelegging(verkoop);
		service.addBijafschrift(bijschrift);
		service.updateRekening(updateSaldo);
		return Response.ok().build();
	}
}
