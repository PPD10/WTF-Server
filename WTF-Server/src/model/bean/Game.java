package model.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Cette classe est le JavaBean des parties (ou jeux)
 * 
 * @author Philémon Bouzy
 * @version 1.0
 */
public class Game extends Bean {

	private Integer score;
	private Integer livesNumber;
	private Integer time;
	private Integer idPlayer;
	
	private Set<Level> levelAssociations = new HashSet<Level>();

	static {
		belongsTo(Player.class);
		//hasMany(Level.class);
	}

	/**
	 * Crée un objet Game.
	 */
	public Game() {
	}

	/**
	 * Crée un objet Game à partir d'un score, d'un nombre de vies, d'un temps
	 * et d'un ID de joueur
	 * 
	 * @param score
	 *            Le score
	 * @param livesNumber
	 *            Le nombre de vies
	 * @param time
	 *            Le temps
	 * @param idPlayer
	 *            L'ID du joueur
	 */
	public Game(Integer score, Integer livesNumber, Integer time,
			Integer idPlayer) {
		this.score = score;
		this.livesNumber = livesNumber;
		this.time = time;
		this.idPlayer = idPlayer;
	}

	/**
	 * Retourne le score.
	 * 
	 * @return Le score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * Définit le score.
	 * 
	 * @param score
	 *            Le nouveau score
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * Retourne le nombre de vies.
	 * 
	 * @return Le nombre de vies
	 */
	public Integer getLivesNumber() {
		return livesNumber;
	}

	/**
	 * Définit le nombre de vies.
	 * 
	 * @param livesNumber
	 *            Le nouveau nombre de vies
	 */
	public void setLivesNumber(Integer livesNumber) {
		this.livesNumber = livesNumber;
	}

	/**
	 * Retourne le temps
	 * 
	 * @return Le temps
	 */
	public Integer getTime() {
		return time;
	}

	/**
	 * Définit le temps
	 * 
	 * @param time
	 *            Le nouveau temps
	 */
	public void setTime(Integer time) {
		this.time = time;
	}

	/**
	 * Retourne l'ID du joueur
	 * 
	 * @return L'ID du joueur
	 */
	public Integer getIdPlayer() {
		return idPlayer;
	}

	/**
	 * Définit l'ID du joueur
	 * 
	 * @param idPlayer
	 *            Le nouvel ID du joueur
	 */
	public void setIdPlayer(Integer idPlayer) {
		this.idPlayer = idPlayer;
	}

}
