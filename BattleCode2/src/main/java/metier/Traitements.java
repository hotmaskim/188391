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

	// game/getIdEquipe/{nomEquipe}/{MotDePasse} (argument dŽjˆ prŽsents dans la constante)
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
			idPartie = appeler(Constantes.NEW_BOT, Arrays.asList("3", idEquipe));
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
		String lastMove = null;
		while (!Constantes.GAMEOVER.equals(status) && !Constantes.GAGNE.equals(status) && !Constantes.PERDU.equals(status)) {
			while (Constantes.NON.equals(status) || Constantes.ANNULE.equals(status)) {
				status = appeler(Constantes.STATUS, Arrays.asList(idPartie, idEquipe));
			}

			if (Constantes.OUI.equals(status)) {
				Board plateau = extraitJson(appeler(Constantes.BOARD, Arrays.asList(idPartie)));

				lastMove = ia(lastMove, plateau);
				
				String retour = appeler(Constantes.PLAY, Arrays.asList(idPartie, idEquipe, lastMove));
				
				// Mauvais Coup
				if (Constantes.KO.equals(retour)) {
					status = Constantes.PERDU;
				} else if (Constantes.GAMEOVER.equals(retour)) {
					status = Constantes.GAGNE;
				}
				
			}
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
//			if (Constantes.TEST) {
//				status = Constantes.PERDU;
//			}
			
		}
		return status;
	}

	private static String ia(String lastMove, Board plateau) {
		String dernierMouvement = appeler(Constantes.LAST_MOVE, Arrays.asList(idPartie, idEquipe));
		
		Constantes.logs.ajouterLog(plateau.toString());
		// Traitement mŽtier
		
		int notreNbBalles= plateau.getPlayer1().getBullet();
		int notreNbBouclie = plateau.getPlayer1().getShield();
		int notreNbVie = plateau.getPlayer1().getHealth();
		
		
		int nbBallesAdverse = plateau.getPlayer2().getBullet();
		int nbBouclieAdverse = plateau.getPlayer2().getShield();
		int nbVieAdverse = plateau.getPlayer2().getHealth();
		
		
		if (lastMove == null) {
			lastMove = Constantes.RELOAD;
		} else {
			if (Constantes.AIM.equals(lastMove)) {
				lastMove = Constantes.SHOOT;
			} else if (Constantes.AIM.equals(dernierMouvement)) {
				if (nbBallesAdverse > 0 && notreNbBouclie > 0)
					lastMove = Constantes.COVER;
				else {
					if (notreNbBalles > 0) 
						lastMove = Constantes.SHOOT;
					else 
						lastMove = Constantes.RELOAD;
				}
			} else if (Constantes.SHOOT.equals(dernierMouvement))
				if (nbBallesAdverse > 0)
					if (notreNbBalles > 0)
						lastMove = Constantes.SHOOT;
					else
						lastMove = Constantes.RELOAD;
				else {
					if (notreNbBalles > 0) {
						if (!Constantes.AIM.equals(lastMove)) 
							lastMove = Constantes.AIM;
						else 
							lastMove = Constantes.SHOOT;
					} else {
						lastMove = Constantes.RELOAD;
					}
				}
			else if (Constantes.RELOAD.equals(dernierMouvement))
				lastMove = Constantes.SHOOT;
		}
		return lastMove;
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
