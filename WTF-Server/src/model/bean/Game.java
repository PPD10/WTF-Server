package model.bean;

/**
 * Cette classe est le JavaBean des parties (ou jeux)
 * 
 * @author Phil�mon Bouzy
 * @version 1.0
 */
public class Game extends Bean {

	private Integer score;
	private Integer livesNumber;
	private Integer time;

	/**
	 * Cr�e un objet Game.
	 */
	public Game() {
	}

	/**
	 * Cr�e un objet Game � partir d'un score, d'un nombre de vies et d'un
	 * temps.
	 * 
	 * @param score
	 *            Le score
	 * @param livesNumber
	 *            Le nombre de vies
	 * @param time
	 *            Le temps
	 */
	public Game(Integer score, Integer livesNumber, Integer time) {
		this.setScore(score);
		this.setLivesNumber(livesNumber);
		this.setTime(time);
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
	 * D�finit le score.
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
	 * D�finit le nombre de vies.
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
	 * D�finit le temps
	 * 
	 * @param time
	 *            Le nouveau temps
	 */
	public void setTime(Integer time) {
		this.time = time;
	}

}
