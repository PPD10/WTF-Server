package model.bean;

/**
 * Cette classe est le JavaBean des joueurs.
 * 
 * @author Phil�mon Bouzy
 * @version 1.0
 */
public class Player extends Bean {

	private String username;
	private String password;

	/**
	 * Cr�e un objet Player.
	 */
	public Player() {
	}

	/**
	 * Cr�e un objet Player � partir d'un nom d'utilisateur et d'un mot de
	 * passe.
	 * 
	 * @param username
	 * @param password
	 */
	public Player(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Retourne le nom d'utilisateur.
	 * 
	 * @return Le nom d'utilisateur
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * D�finit le nom d'utilisateur
	 * 
	 * @param username
	 *            Le nom d'utilisateur
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Retourne le mot de passe.
	 * 
	 * @return Le mot de passe
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * D�finit le mot de passe.
	 * 
	 * @param password
	 *            Le nouveau mot de passe
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
