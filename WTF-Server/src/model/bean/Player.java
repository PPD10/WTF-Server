package model.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Cette classe est le JavaBean des joueurs.
 * 
 * @author Philémon Bouzy
 * @version 1.1
 */
public class Player extends Bean {

	private String username;
	private String password;

	private Set<Game> gameAssociations = new HashSet<Game>();
	private Set<Device> deviceAssociations = new HashSet<Device>();
	private Set<Level> levelAssociations = new HashSet<Level>();

	static {
		hasMany(Game.class, Device.class, Level.class);
	}

	/**
	 * Crée un objet Player.
	 */
	public Player() {
	}

	/**
	 * Crée un objet Player à partir d'un nom d'utilisateur et d'un mot de
	 * passe.
	 * 
	 * @param username
	 *            Le nom d'utilisateur
	 * @param password
	 *            Le mot de passe
	 * @param macAddress
	 *            L'adresse MAC
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
	 * Définit le nom d'utilisateur
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
	 * Définit le mot de passe.
	 * 
	 * @param password
	 *            Le nouveau mot de passe
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Game> getGameAssociations() {
		return gameAssociations;
	}

	public void setGames(Set<Game> gameAssociations) {
		this.gameAssociations = gameAssociations;
	}

	public void setGames(Game... games) {
		this.gameAssociations.addAll(Arrays.asList(games));
	}
	
	public void addGame(Game game) {
		boolean listContains = false;
		
		for (Game g : this.gameAssociations)
			if (game.getId().equals(g.getId()))
				listContains = true;
				
		if (!listContains && game != null)
			this.gameAssociations.add(game);
	}

	public Set<Device> getDeviceAssociations() {
		return deviceAssociations;
	}

	public void setDevices(Set<Device> deviceAssociations) {
		this.deviceAssociations = deviceAssociations;
	}

	public void setDevices(Device... devices) {
		this.deviceAssociations.addAll(Arrays.asList(devices));
	}
	
	public void addDevice(Device device) {
		boolean listContains = false;
		
		for (Device d : this.deviceAssociations)
			if (device.getId().equals(d.getId()))
				listContains = true;
		
		if (!listContains && device != null)
			this.deviceAssociations.add(device);
	}
	
	public void addLevel(Level level) {
		boolean listContains = false;
		
		for (Level l : this.levelAssociations)
			if (level.getId().equals(l.getId()))
				listContains = true;
		
		if (!listContains && level != null)
			this.levelAssociations.add(level);
	}

}
