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
 * Cette classe g�re la cr�ation, la suppression et la r�cup�ration de donn�es �
 * partir de la base de donn�es
 * 
 * @author Phil�mon
 * 
 * @param <T>
 *            Un h�ritier de Bean
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
	 * Sauvegarde une ligne dans une table. Si l'ID n'est pas renseign�, la
	 * m�thode cr�e une ligne, sinon, elle met � jour la ligne correspondant �
	 * l'ID renseign�.
	 * 
	 * @param bean
	 *            La classe h�ritant de Bean
	 * @return true si les donn�es ont bien �t� enregistr�es, false sinon
	 */
	public boolean save(T bean) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		// On r�cup�re les attributs/champs de la classe h�riti�re de Bean
		// et de Bean elle-m�me
		ArrayList<Field> fields = bean.getAttributes();

		// G�n�ration de la requ�te
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
			// R�cup�ration d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requ�te pr�par�e
			preparedStatement = initializePreparedStatement(connection,
					this.query, true, fields, bean);

			// Ex�cution de la requ�te
			int statut = preparedStatement.executeUpdate();

			// Si aucune ligne ins�r�e, lancement d'une exception
			if (statut == 0) {
				throw new SQLException("Probl�me lors de la cr�ation du "
						+ Inflector.tableize(bean.getClass().getSimpleName()));
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * R�cup�re une ligne dans une table � partir de l'ID du Bean pr�alablement
	 * renseign�.
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

			// G�n�ration de la requ�te
			this.query = "SELECT * FROM "
					+ Inflector.tableize(bean.getClass().getSimpleName())
					+ " WHERE id = " + bean.getId();

			// R�cup�ration d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requ�te
			statement = connection.createStatement();

			// Ex�cution de la requ�te
			resultSet = statement.executeQuery(this.query);

			// Cr�ation de l'objet bean
			T bean = (T) BeanFactory.getBean(this.bean.getClass(), resultSet);

			return (T) bean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * R�cup�re toutes les lignes d'une table.
	 * 
	 * @return La liste des Bean obtenus
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> findAll() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		// G�n�ration de la requ�te
		this.query = "SELECT * FROM "
				+ Inflector.tableize(this.bean.getClass().getSimpleName());

		try {
			// R�cup�ration d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requ�te
			statement = connection.createStatement();

			// Ex�cution de la requ�te
			resultSet = statement.executeQuery(this.query);

			// On r�cup�re les attributs/champs de la classe h�riti�re de Bean
			// et de Bean elle-m�me
			ArrayList<T> beans = (ArrayList<T>) BeanFactory.getBeanList(
					this.bean.getClass(), resultSet);

			return beans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * R�cup�re plusieurs lignes dans une table en fonction d'une condition.
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

		// G�n�ration de la requ�te
		this.query = "SELECT * FROM "
				+ Inflector.tableize(this.bean.getClass().getSimpleName())
				+ " WHERE ";

		try {
			// R�cup�ration d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requ�te
			statement = connection.createStatement();

			// Ex�cution de la requ�te
			resultSet = statement.executeQuery(this.query);

			// On r�cup�re les attributs/champs de la classe h�riti�re de Bean
			// et de Bean elle-m�me
			ArrayList<T> beans = (ArrayList<T>) BeanFactory.getBeanList(
					this.bean.getClass(), resultSet);

			return beans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retourne l'�tat de l'attribut "id".
	 * 
	 * @param bean
	 *            Le bean
	 * @return true si l'ID est renseign�, false sinon
	 */
	private boolean isIdSet(Bean bean) {
		return (bean.getId() != null);
	}

	/**
	 * Retourne une chaine de caract�re contenant la liste des champs d'une
	 * table.
	 * 
	 * @param fields
	 *            Les attributs
	 * @param isUpdate
	 *            S'agit-il d'une mise � jour ?
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
	 * � la cr�ation d'un PreparedStatement.
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
	 * Cr�e une PreparedStatement servant � la cr�ation ou � la mise � jour
	 * d'une ligne.
	 * 
	 * @param connection
	 *            La connexion au SGBD
	 * @param query
	 *            La requ�te SQL
	 * @param returnGeneratedKeys
	 *            Faut-il retourner l'ID g�n�r� automatiquement
	 * @param fields
	 *            Les attributs
	 * @param bean
	 *            Le Bean
	 * @return Le PreparedStatement cr��
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static PreparedStatement initializePreparedStatement(
			Connection connection, String query, boolean returnGeneratedKeys,
			ArrayList<Field> fields, Bean bean) throws SQLException,
			IllegalArgumentException, IllegalAccessException {
		// Pr�paration de la requ�te, avec ou non retour de la cl� g�n�r�e
		PreparedStatement preparedStatement = connection.prepareStatement(
				query, returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS
						: Statement.NO_GENERATED_KEYS);

		// Mise en place des param�tres de la requ�te
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
