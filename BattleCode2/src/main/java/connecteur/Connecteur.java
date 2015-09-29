package connecteur;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

import transverse.Constantes;


public class Connecteur {

	public static String appeler(String url, List<String> arguments) throws ClientProtocolException, IOException, URISyntaxException {

		StringBuffer adresse = new StringBuffer(Constantes.URL);
		
		adresse.append(url);
		if (arguments != null && !arguments.isEmpty()) {
			for (String arg : arguments) {
				adresse = adresse.append(arg);
				adresse = adresse.append("/");
			}
		}
		
		Constantes.logs.ajouterLog(adresse.toString());
		return gestionRetour(Request.Get(new URI(adresse.toString())).execute().returnContent().asString());
	
	}

	private static String gestionRetour(String retour) {

		Constantes.logs.ajouterLog(retour);
		return retour;
	}

}
