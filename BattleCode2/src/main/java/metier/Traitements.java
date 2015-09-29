package metier;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;

import transverse.Constantes;

import com.google.gson.Gson;

import connecteur.Connecteur;

public class Traitements {

	private static String idEquipe = null;
	private static String idPartie = null;

	public static void traiterPing() {
		appeler(Constantes.PING, null);
	}

	public static void traiterPing500() {
		appeler(Constantes.PING500, null);
	}

	public static void traiterPing403() {
		appeler(Constantes.PING403, null);
	}

	// game/getIdEquipe/{nomEquipe}/{MotDePasse} (argument déjà présents dans la constante)
	public static void traiterGetIdEquipe() {
		idEquipe = appeler(Constantes.GETID, null);
	}

	// game/status/{idPartie}/{idEquipe}
	public static void traiterStatus() {
		if (idEquipe == null) {
			traiterGetIdEquipe();
		}
		appeler(Constantes.STATUS, null);
	}

	// game/board/{idPartie}
	public static void traiterBoard() {
		if (idEquipe == null) {
			traiterGetIdEquipe();
		}
		appeler(Constantes.BOARD, null);
	}

	// game/getlastmove/{idPartie}
	public static void traiterLastMove() {
		if (idEquipe == null) {
			traiterGetIdEquipe();
		}
		appeler(Constantes.LAST_MOVE, null);
	}
	

	// game/getlastmove/{idPartie}
	public static void traiterGoBot() {
		Constantes.logs.ajouterLog("\n");
		traiterGetIdEquipe();
		
		idPartie = Constantes.NA;
		while (Constantes.NA.equals(idPartie)) {
			idPartie = appeler(Constantes.NEXT_BOT, Arrays.asList(idEquipe));
		}

		String status = traitementJeu();
		
		if (Constantes.GAGNE.equals(status)) {
			Constantes.logs.ajouterLog("YEAH");
		} else if (Constantes.PERDU.equals(status)) {
			Constantes.logs.ajouterLog("YOU LOSE");
		}
		
	}
	



	// game/getlastmove/{idPartie}
	public static void traiterGoVersus() {
		Constantes.logs.ajouterLog("\n");
		traiterGetIdEquipe();
		
		idPartie = Constantes.NA;
		while (Constantes.NA.equals(idPartie)) {
			idPartie = appeler(Constantes.NEXT_JOUEUR, Arrays.asList(idEquipe));
		}

		String status = traitementJeu();
		
		if (Constantes.GAGNE.equals(status)) {
			Constantes.logs.ajouterLog("YEAH");
		} else if (Constantes.PERDU.equals(status)) {
			Constantes.logs.ajouterLog("YOU LOSE");
		}
		
	}

	private static String traitementJeu() {
		String status = Constantes.NON;
		while (!Constantes.GAGNE.equals(status) && !Constantes.PERDU.equals(status)) {
			while (Constantes.NON.equals(status) || Constantes.ANNULE.equals(status)) {
				status = appeler(Constantes.STATUS, Arrays.asList(idEquipe, idPartie));
			}

			if (Constantes.OUI.equals(status)) {
				Board plateau = extraitJson(appeler(Constantes.BOARD, Arrays.asList(idPartie)));

				//Constantes.plateauIHM.genererPlateau(plateau.getLongueur(), plateau.getLargeur());

				
				
				Constantes.logs.ajouterLog(plateau.toString());
				// Traitement métier
				
				
				String retour = appeler(Constantes.PLAY, Arrays.asList(idPartie, idEquipe, "1", "1"));
				
				// Mauvais Coup
				if (Constantes.KO.equals(retour)) {
					status = Constantes.PERDU;
				}
				
			}
			
			if (Constantes.TEST) {
				status = Constantes.PERDU;
			}
			
		}
		return status;
	}
	
	private static Board extraitJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Board.class);
	}

	private static String appeler(String url, List<String> arguments) {
		try {
			return Connecteur.appeler(url, arguments);
		} catch (ClientProtocolException e) {
			int erreur = ((HttpResponseException) e).getStatusCode();
			Constantes.logs.ajouterLog(String.valueOf(erreur));
			return String.valueOf(erreur);
		} catch (IOException e) {
			Constantes.logs.ajouterLog(e.getMessage());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Constantes.logs.ajouterLog(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
