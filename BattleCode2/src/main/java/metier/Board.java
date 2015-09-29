package metier;

public class Board {

	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private int longueur;
	private int largeur;
	
	
	public Board() {
	}


	public Player getPlayer1() {
		return player1;
	}


	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}


	public Player getPlayer2() {
		return player2;
	}


	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}


	public Player getPlayer3() {
		return player3;
	}


	public void setPlayer3(Player player3) {
		this.player3 = player3;
	}


	public Player getPlayer4() {
		return player4;
	}


	public void setPlayer4(Player player4) {
		this.player4 = player4;
	}


	public int getLongueur() {
		return longueur;
	}


	public void setLongueur(int longueur) {
		this.longueur = longueur;
	}


	public int getLargeur() {
		return largeur;
	}


	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}


	@Override
	public String toString() {
		return "Board [player1=" + player1 + ", player2=" + player2 + ", player3=" + player3 + ", player4=" + player4
				+ ", longueur=" + longueur + ", largeur=" + largeur + "]";
	}
	
	
	
	
	
}
