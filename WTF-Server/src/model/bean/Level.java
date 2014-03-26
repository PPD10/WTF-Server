package model.bean;

/**
 * Cette classe est le JavaBean des niveaux.
 * 
 * @author Philémon Bouzy
 * @version 1.0
 */
public class Level extends Bean {

	private Integer maxScore;
	private Integer idGame;
	private Integer idPlayer;

	static {
		belongsTo(Game.class);
	}

	/**
	 * Crée un objet Level.
	 */
	public Level() {
	}

	/**
	 * Crée un objet Level à partir du score max, de la vitesse et du temps.
	 * 
	 * @param maxScore
	 *            Le score max
	 * @param speed
	 *            La vitesse
	 * @param time
	 *            Le temps
	 * @param idGame
	 *            L'ID du jeu
	 */
	public Level(Integer maxScore, Integer idGame, Integer idPlayer) {
		this.maxScore = maxScore;
		this.idGame = idGame;
		this.idPlayer = idPlayer;
	}

	/**
	 * Retourne le score max.
	 * 
	 * @return Le score max
	 */
	public Integer getMaxScore() {
		return maxScore;
	}

	/**
	 * Définit le score max.
	 * 
	 * @param maxScore
	 *            Le nouveau score max
	 */
	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * Retourne l'ID du jeu
	 * 
	 * @return L'ID du jeu
	 */
	public Integer getIdGame() {
		return idGame;
	}

	/**
	 * Définit l'ID du jeu
	 * 
	 * @param idGame
	 *            Le nouvel ID du jeu
	 */
	public void setIdGame(Integer idGame) {
		this.idGame = idGame;
	}

	public Integer getIdPlayer() {
		return idPlayer;
	}

	public void setIdPlayer(Integer idPlayer) {
		this.idPlayer = idPlayer;
	}

}
