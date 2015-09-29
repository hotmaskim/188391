package metier;

public class Player {

	private String nom;
	private int nbrDePieces;
	private String dernierMouvement;
	
	public Player() {
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNbrDePieces() {
		return nbrDePieces;
	}

	public void setNbrDePieces(int nbrDePieces) {
		this.nbrDePieces = nbrDePieces;
	}

	public String getDernierMouvement() {
		return dernierMouvement;
	}

	public void setDernierMouvement(String dernierMouvement) {
		this.dernierMouvement = dernierMouvement;
	}

	@Override
	public String toString() {
		return "Player [nom=" + nom + ", nbrDePieces=" + nbrDePieces + ", dernierMouvement=" + dernierMouvement + "]";
	}
	
	
	
	
}
