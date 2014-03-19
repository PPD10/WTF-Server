package model.bean;

/**
 * Cette classe est le JavaBean des niveaux.
 * 
 * @author Phil�mon Bouzy
 * @version 1.0
 */
public class Level extends Bean {

	private Integer maxScore;
	private Integer speed;
	private Integer time;

	/**
	 * Cr�e un objet Level.
	 */
	public Level() {
	}

	/**
	 * Cr�e un objet Level � partir du score max, de la vitesse et du temps.
	 * 
	 * @param maxScore
	 *            Le score max
	 * @param speed
	 *            La vitesse
	 * @param time
	 *            Le temps
	 */
	public Level(Integer maxScore, Integer speed, Integer time) {
		this.maxScore = maxScore;
		this.speed = speed;
		this.time = time;
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
	 * D�finit le score max.
	 * 
	 * @param maxScore
	 *            Le nouveau score max
	 */
	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * Retourne la vitesse.
	 * 
	 * @return La vitesse
	 */
	public Integer getSpeed() {
		return speed;
	}

	/**
	 * D�finit la vitesse.
	 * 
	 * @param speed
	 *            La nouvelle vitesse
	 */
	public void setSpeed(Integer speed) {
		this.speed = speed;
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
