package nl.hu.ipass.webservices;

import java.io.InputStream;
import java.security.Key;
import java.util.Calendar;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.ipass.persistence.UserDAO;

@Path("/authentication")
public class AuthenticationResource {
	final static public Key key = MacProvider.generateKey();

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(InputStream is) {
		try {
			JsonObject object = Json.createReader(is).readObject();

			String username = object.getString("username");
			String password = object.getString("password");
			// Authenticate the user against the database
			UserDAO dao = new UserDAO();
			String role = dao.findRoleForUsernameAndPassword(username, password);

			if (role == null) {
				throw new IllegalArgumentException("No user found!");
			}

			// Issue a token for the user
			Calendar expiration = Calendar.getInstance();
			expiration.add(Calendar.MINUTE, 30);

			String token = Jwts.builder().setSubject(username).claim("role", role).setExpiration(expiration.getTime())
					.signWith(SignatureAlgorithm.HS512, key).compact();
			// Return the token on the response
			return Response.ok(token).build();
		} catch (JwtException | IllegalArgumentException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	@GET
	@Path("getUserID/{username}")
	public Response getUserID(@PathParam("username") String username) {
		UserDAO dao = new UserDAO();
		int userID = dao.findUserIdForUser(username);
		
		return Response.ok(userID).build();
	}
	
}
