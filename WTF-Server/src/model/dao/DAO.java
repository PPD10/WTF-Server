package model.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import utility.Inflector;
import config.ConnectionDB;
import model.bean.*;

/**
 * Cette classe gère la création, la suppression et la récupération de données à
 * partir de la base de données
 * 
 * @author Philémon
 * 
 * @param <T>
 *            Un héritier de Bean
 */
public class DAO<T extends Bean> {

	private final T bean;
	private String query;

	/**
	 * Construit l'objet DAO.
	 * 
	 * @param bean
	 *            un Bean
	 */
	@SuppressWarnings("unchecked")
	public DAO(Bean bean) {
		this.bean = (T) bean;
		this.query = new String();
	}

	/**
	 * Sauvegarde une ligne dans une table. Si l'ID n'est pas renseigné, la
	 * méthode crée une ligne, sinon, elle met à jour la ligne correspondant à
	 * l'ID renseigné.
	 * 
	 * @param bean
	 *            La classe héritant de Bean
	 * @return true si les données ont bien été enregistrées, false sinon
	 */
	public boolean save(T bean) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		// On récupère les attributs/champs de la classe héritière de Bean
		// et de Bean elle-même
		ArrayList<Field> fields = bean.getAttributes();

		// Génération de la requête
		if (isIdSet(bean)) {
			this.query = "UPDATE "
					+ Inflector.tableize(bean.getClass().getSimpleName())
					+ " SET " + getFieldNamesList(fields, true)
					+ " WHERE id = " + bean.getId();
		} else {
			this.query = "INSERT INTO "
					+ Inflector.tableize(bean.getClass().getSimpleName())
					+ " (" + getFieldNamesList(fields, false) + ") VALUES ("
					+ getQuestionMarksList(fields) + ")";
		}

		try {
			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête préparée
			preparedStatement = initializePreparedStatement(connection,
					this.query, true, fields, bean);

			// Exécution de la requête
			int statut = preparedStatement.executeUpdate();

			// Si aucune ligne insérée, lancement d'une exception
			if (statut == 0) {
				throw new SQLException("Problème lors de la création du "
						+ Inflector.tableize(bean.getClass().getSimpleName()));
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Récupère une ligne dans une table à partir de l'ID du Bean préalablement
	 * renseigné.
	 * 
	 * @return Le Bean obtenu
	 */
	@SuppressWarnings("unchecked")
	public T find() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			if (!isIdSet(bean)) {
				throw new Exception(
						"ID manquant. Merci de l'indiquer en faisant"
								+ " un setId(...) de la classe Bean.");
			}

			// Génération de la requête
			this.query = "SELECT * FROM "
					+ Inflector.tableize(bean.getClass().getSimpleName())
					+ " WHERE id = " + bean.getId();

			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête
			statement = connection.createStatement();

			// Exécution de la requête
			resultSet = statement.executeQuery(this.query);

			// Création de l'objet bean
			T bean = (T) BeanFactory.getBean(this.bean.getClass(), resultSet);

			return (T) bean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère toutes les lignes d'une table.
	 * 
	 * @return La liste des Bean obtenus
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> findAll() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		// Génération de la requête
		this.query = "SELECT * FROM "
				+ Inflector.tableize(this.bean.getClass().getSimpleName());

		try {
			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête
			statement = connection.createStatement();

			// Exécution de la requête
			resultSet = statement.executeQuery(this.query);

			// On récupère les attributs/champs de la classe héritière de Bean
			// et de Bean elle-même
			ArrayList<T> beans = (ArrayList<T>) BeanFactory.getBeanList(
					this.bean.getClass(), resultSet);

			return beans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère plusieurs lignes dans une table en fonction d'une condition.
	 * 
	 * @param condition
	 *            Ladite condition
	 * @return La liste des Bean obtenus
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> findAll(String condition) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		// Génération de la requête
		this.query = "SELECT * FROM "
				+ Inflector.tableize(this.bean.getClass().getSimpleName())
				+ " WHERE ";

		try {
			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête
			statement = connection.createStatement();

			// Exécution de la requête
			resultSet = statement.executeQuery(this.query);

			// On récupère les attributs/champs de la classe héritière de Bean
			// et de Bean elle-même
			ArrayList<T> beans = (ArrayList<T>) BeanFactory.getBeanList(
					this.bean.getClass(), resultSet);

			return beans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retourne l'état de l'attribut "id".
	 * 
	 * @param bean
	 *            Le bean
	 * @return true si l'ID est renseigné, false sinon
	 */
	private boolean isIdSet(Bean bean) {
		return (bean.getId() != null);
	}

	/**
	 * Retourne une chaine de caractère contenant la liste des champs d'une
	 * table.
	 * 
	 * @param fields
	 *            Les attributs
	 * @param isUpdate
	 *            S'agit-il d'une mise à jour ?
	 * @return La chaine contenant la liste des champs
	 */
	private static String getFieldNamesList(ArrayList<Field> fields,
			boolean isUpdate) {
		String fieldNamesList = new String();

		for (int i = 0; i < fields.size(); ++i) {
			if (fields.get(i).getName() != "id") {
				fieldNamesList += Inflector.underscore(fields.get(i).getName());
				if (isUpdate)
					fieldNamesList += " = ?";
				if (i != fields.size() - 1)
					fieldNamesList += ", ";
			}
		}

		return fieldNamesList;
	}

	/**
	 * Retourne une chaine contenant la liste des points d'interrogation servant
	 * à la création d'un PreparedStatement.
	 * 
	 * @param fields
	 *            Les attributs
	 * @return La chaine contenant la liste des points d'interrogation
	 */
	private static String getQuestionMarksList(ArrayList<Field> fields) {
		String questionMarksList = new String("");

		for (int i = 0; i < fields.size(); ++i) {
			if (fields.get(i).getName() != "id") {
				questionMarksList += "?";
				if (i != fields.size() - 1)
					questionMarksList += ", ";
			}
		}

		return questionMarksList;
	}

	/**
	 * Crée une PreparedStatement servant à la création ou à la mise à jour
	 * d'une ligne.
	 * 
	 * @param connection
	 *            La connexion au SGBD
	 * @param query
	 *            La requête SQL
	 * @param returnGeneratedKeys
	 *            Faut-il retourner l'ID généré automatiquement
	 * @param fields
	 *            Les attributs
	 * @param bean
	 *            Le Bean
	 * @return Le PreparedStatement créé
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static PreparedStatement initializePreparedStatement(
			Connection connection, String query, boolean returnGeneratedKeys,
			ArrayList<Field> fields, Bean bean) throws SQLException,
			IllegalArgumentException, IllegalAccessException {
		// Préparation de la requête, avec ou non retour de la clé générée
		PreparedStatement preparedStatement = connection.prepareStatement(
				query, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS
						: Statement.NO_GENERATED_KEYS);

		// Mise en place des paramètres de la requête
		int i = 0;
		for (Field field : fields) {
			if (field.getName() != "id") {
				field.setAccessible(true);
				preparedStatement.setObject(i, field.get(bean));
			}
			++i;
		}

		return preparedStatement;
	}

}
