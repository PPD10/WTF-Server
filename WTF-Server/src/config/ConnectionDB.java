package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class ConnectionDB {

	private static final String PROPERTIES_FILE = "\\config\\PostgreSQLProperties";
	private static final String PROPERTY_DRIVER = "driver";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_USER = "user";
	private static final String PROPERTY_PASSWORD = "password";

	private static BoneCP connectionPool = null;

	static {
		// Récupération des informations de connexion à la BD à partir du
		// fichier properties
		Properties properties = new Properties();

		String driver;
		String url;
		String user;
		String password;

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		InputStream propertiesFile = classLoader
				.getResourceAsStream(PROPERTIES_FILE);

		if (propertiesFile == null) {
			throw new RuntimeException("Le fichier properties "
					+ PROPERTIES_FILE + " est introuvable.");
		}

		try {
			properties.load(propertiesFile);
			driver = properties.getProperty(PROPERTY_DRIVER);
			url = properties.getProperty(PROPERTY_URL);
			user = properties.getProperty(PROPERTY_USER);
			password = properties.getProperty(PROPERTY_PASSWORD);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Le fichier properties ("
					+ PROPERTIES_FILE + ") est introuvable.", e);
		} catch (IOException e) {
			throw new RuntimeException(
					"Impossible de charger le fichier properties "
							+ PROPERTIES_FILE, e);
		}

		// Chargement du driver
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Le driver est introuvable dans le classpath.", e);
		}

		// Création d'un pool de connexions avec BoneCP
		try {
			BoneCPConfig config = new BoneCPConfig();
			// Mise en place de l'URL, du login et du mot de passe
			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(password);
			// Paramétrage de la taille du pool
			config.setMinConnectionsPerPartition(1);
			config.setMaxConnectionsPerPartition(10);
			config.setPartitionCount(2);
			// Création du pool
			connectionPool = new BoneCP(config);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Erreur de configuration du pool de connexions.", e);
		}
	}

	// Retourne une connexion à la base de données
	public static Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}

}
