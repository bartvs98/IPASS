package nl.hu.ipass.model;

public class ServiceProvider {
	private static Service Service = new Service();

	public static Service getService() {
		return Service;
	}
}
