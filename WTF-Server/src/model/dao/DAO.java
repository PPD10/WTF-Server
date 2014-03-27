package model.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import utility.Inflector;
import config.ConnectionDB;
import model.bean.*;

/**
 * Cette classe gère la création, la suppression et la récupération de données à
 * partir de la base de données.
 * 
 * @author Philémon Bouzy
 * @version 1.1
 * @param <T>
 *            Un héritier de Bean
 */
public class DAO<T extends Bean> {

	private T bean;
	private String query;

	/**
	 * Construit l'objet DAO.
	 * 
	 * @param bean
	 *            Un Bean
	 */
	public DAO(T bean) {
		this.bean = bean;
		this.query = new String();
	}

	/**
	 * Définit l'attribut bean.
	 * 
	 * @param bean
	 *            Le nouveau Bean
	 */
	public void setBean(T bean) {
		this.bean = bean;
	}
	
	public T getBean() {
		return this.bean;
	}

	/**
	 * Sauvegarde une ligne dans une table. Si l'ID n'est pas renseigné, la
	 * méthode crée une ligne, sinon, elle met à jour la ligne correspondant à
	 * l'ID renseigné.
	 * 
	 * @return true si les données ont bien été enregistrées, false sinon
	 */
	public boolean save() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;

		// On récupère les attributs/champs de la classe héritière de Bean
		// et de Bean elle-même
		ArrayList<Field> fields = Bean.getAttributesWithoutAssociations(
				this.bean.getClass());

		// Génération de la requête
		if (isIdSet(this.bean)) {
			this.query = "UPDATE "
					+ Inflector.tableize(this.bean.getClass().getSimpleName())
					+ " SET " + getFieldNamesList(fields, true)
					+ " WHERE id = " + this.bean.getId();
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
					this.query, true, fields, this.bean);
			
			// Exécution de la requête
			int statut = preparedStatement.executeUpdate();

			// Si aucune ligne insérée, lancement d'une exception
			if (statut == 0) {
				throw new SQLException("Problème lors de la création du "
						+ Inflector.tableize(this.bean.getClass()
								.getSimpleName()));
			}
			
			generatedKeys = preparedStatement.getGeneratedKeys();
			
			if (generatedKeys != null && generatedKeys.next())
				bean.setId(generatedKeys.getInt(1));
			else
				throw new SQLException("Problème lors de la génération de la"
						+ " clé primaire");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close(preparedStatement, connection);
		}
	}

	/**
	 * Récupère une ligne dans une table à partir de l'ID du Bean préalablement
	 * renseigné.
	 * 
	 * @return Le Bean obtenu
	 */
	@SuppressWarnings("unchecked")
	public T find(boolean getAssociations) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		String joinsList = new String();
		String fieldNamesList = new String();
		
		try {
			if (!isIdSet(bean)) {
				throw new Exception(
						"ID manquant. Merci de l'indiquer en faisant"
								+ " un setId(...) de la classe Bean.");
			}

			if (getAssociations) {
				HashMap<String, ArrayList<Class<? extends Bean>>> associations = T
						.getAssociations();

				ArrayList<Class<? extends Bean>> hasOne = associations
						.get("hasOne");
				ArrayList<Class<? extends Bean>> hasMany = associations
						.get("hasMany");

				fieldNamesList = getFieldNamesList(
						Bean.getAttributesWithoutAssociations(this.bean.getClass()),
						this.bean.getClass());

				if (!hasOne.isEmpty()) {
					joinsList += this.getJoinsList(hasOne);
					for (int i = 0; i < hasOne.size(); ++i) {
						fieldNamesList += ", " + getFieldNamesList(
								Bean.getAttributesWithoutAssociations(
										hasOne.get(i)), hasOne.get(i));
					}
				}
				if (!hasMany.isEmpty()) {
					joinsList += this.getJoinsList(hasMany);
					for (int i = 0; i < hasMany.size(); ++i) {
						fieldNamesList += ", " + getFieldNamesList(
								Bean.getAttributesWithoutAssociations(
										hasMany.get(i)), hasMany.get(i));
					}
				}

				this.query = "SELECT " + fieldNamesList + " FROM "
						+ Inflector.tableize(bean.getClass().getSimpleName())
						+ joinsList;
			} else {
				this.query = "SELECT * FROM "
						+ Inflector.tableize(bean.getClass().getSimpleName());
			}

			this.query += " WHERE "
					+ Inflector.tableize(this.bean.getClass().getSimpleName())
					+ ".id = " + this.bean.getId();

			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);

			// Exécution de la requête
			resultSet = statement.executeQuery(this.query);

			// Création de l'objet bean
			T bean = (T) BeanFactory.getBean(this.bean.getClass(), resultSet, 
					fieldNamesList);

			return bean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(resultSet, statement, connection);
		}
	}

	/**
	 * Récupère toutes les lignes d'une table.
	 * 
	 * @return La liste des Bean obtenus
	 */
	public ArrayList<T> findAll(boolean getAssociations) {
		return findAll(getAssociations, "");
		
//		Connection connection = null;
//		Statement statement = null;
//		ResultSet resultSet = null;
//		
//		String joinsList = new String();
//		String fieldNamesList = new String();
//
//		// Génération de la requête		
//		if (getAssociations) {
//			HashMap<String, ArrayList<Class<? extends Bean>>> associations = T
//					.getAssociations();
//
//			ArrayList<Class<? extends Bean>> hasOne = associations
//					.get("hasOne");
//			ArrayList<Class<? extends Bean>> hasMany = associations
//					.get("hasMany");
//
//			fieldNamesList = getFieldNamesList(
//					T.getAttributesWithoutAssociations(this.bean.getClass()),
//					this.bean.getClass());
//
//			if (!hasOne.isEmpty()) {
//				joinsList += this.getJoinsList(hasOne);
//				for (int i = 0; i < hasOne.size(); ++i) {
//					fieldNamesList += ", " + getFieldNamesList(
//							Bean.getAttributesWithoutAssociations(
//									hasOne.get(i)), hasOne.get(i));
//				}
//			}
//			if (!hasMany.isEmpty()) {
//				joinsList += this.getJoinsList(hasMany);
//				for (int i = 0; i < hasMany.size(); ++i) {
//					fieldNamesList += ", " + getFieldNamesList(
//							Bean.getAttributesWithoutAssociations(
//									hasMany.get(i)), hasMany.get(i));
//				}
//			}
//
//			this.query = "SELECT " + fieldNamesList + " FROM "
//					+ Inflector.tableize(bean.getClass().getSimpleName())
//					+ joinsList;
//		} else {
//			this.query = "SELECT * FROM "
//					+ Inflector.tableize(bean.getClass().getSimpleName());
//		}
//
//		try {
//			// Récupération d'une connexion
//			connection = ConnectionDB.getConnection();
//
//			// Initialisation de la requête
//			statement = connection.createStatement();
//
//			// Exécution de la requête
//			resultSet = statement.executeQuery(this.query);
//
//			 On récupère les attributs/champs de la classe héritière de Bean
//			// et de la classe Bean elle-même
//			ArrayList<T> beans = (ArrayList<T>) BeanFactory.getBeanList(
//					this.bean.getClass(), resultSet, fieldNamesList);
//
//			return beans;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			close(resultSet, statement, connection);
//		}
	}

	/**
	 * Récupère plusieurs lignes dans une table en fonction d'une condition.
	 * 
	 * @param condition
	 *            Ladite condition
	 * @return La liste des Bean obtenus
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> findAll(boolean getAssociations, String... conditions) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String joinsList = new String();
		String fieldNamesList = new String();

		try {
			// Génération de la requête
			if (getAssociations) {
				HashMap<String, ArrayList<Class<? extends Bean>>> associations =
						new HashMap<String, ArrayList<Class<? extends Bean>>>(
								T.getAssociations());

				ArrayList<Class<? extends Bean>> hasOne = 
						new ArrayList<Class<? extends Bean>>(associations
								.get("hasOne"));
				ArrayList<Class<? extends Bean>> hasMany = 
						new ArrayList<Class<? extends Bean>>(associations
								.get("hasMany"));
	
				fieldNamesList = getFieldNamesList(
						Bean.getAttributesWithoutAssociations(this.bean.getClass()),
						this.bean.getClass());

				if (!hasOne.isEmpty()) {
					joinsList += this.getJoinsList(hasOne);
					
					for (int i = 0; i < hasOne.size(); ++i) {
						fieldNamesList += ", " + getFieldNamesList(
								Bean.getAttributesWithoutAssociations(
										hasOne.get(i)), hasOne.get(i));
					}
				}
				if (!hasMany.isEmpty()) {
					joinsList += this.getJoinsList(hasMany);
					
					for (int i = 0; i < hasMany.size(); ++i) {
						fieldNamesList += ", " + getFieldNamesList(
								Bean.getAttributesWithoutAssociations(
										hasMany.get(i)), hasMany.get(i));
					}
				}
				
				this.query = "SELECT " + fieldNamesList + " FROM "
						+ Inflector.tableize(bean.getClass().getSimpleName())
						+ joinsList;
			} else {
				this.query = "SELECT * FROM "
						+ Inflector.tableize(bean.getClass().getSimpleName());
			}
			if (conditions.length > 0 && conditions[0] != "") {
				this.query += " WHERE ";
		
				for (int i = 0; i < conditions.length; ++i) {
					this.query += conditions[i];
		
					if (i != conditions.length - 1) {
						this.query += " AND ";
					}
				}
			}
			
			System.out.println(this.query);
			
			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête
			statement = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, 
					ResultSet.CONCUR_READ_ONLY);

			// Exécution de la requête
			resultSet = statement.executeQuery(this.query);

			// On récupère les attributs/champs de la classe héritière de Bean
			// et de Bean elle-même
			ArrayList<T> beans = (ArrayList<T>) BeanFactory.getBeanList(
					this.bean.getClass(), resultSet, fieldNamesList);

			return beans;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(resultSet, statement, connection);
		}
	}

	public boolean delete() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			if (!isIdSet(this.bean)) {
				throw new Exception(
						"ID manquant. Merci de l'indiquer en faisant"
								+ " un setId(...) de la classe Bean.");
			}

			// Génération de la requête
			this.query = "DELETE FROM "
					+ Inflector.tableize(this.bean.getClass().getSimpleName())
					+ " WHERE id = " + this.bean.getId();

			// Récupération d'une connexion
			connection = ConnectionDB.getConnection();

			// Initialisation de la requête
			statement = connection.createStatement();

			// Exécution de la requête
			resultSet = statement.executeQuery(this.query);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close(resultSet, statement, connection);
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
	 * table pour une insertion ou une mise à jour.
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
	 * Retourne une chaine de caractère contenant la liste des champs d'une
	 * table.
	 * 
	 * @param fields
	 *            Les attributs
	 * @param beanClass
	 *            La classe du Bean
	 * @return La chaine contenant la liste des champs
	 */
	private static String getFieldNamesList(ArrayList<Field> fields,
			Class<? extends Bean> beanClass) {
		String fieldNamesList = new String();

		for (int i = 0; i < fields.size(); ++i) {
			fieldNamesList += Inflector.tableize(beanClass.getSimpleName())
					+ "." + Inflector.underscore(fields.get(i).getName());
			if (i != fields.size() - 1)
				fieldNamesList += ", ";
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

	private static String getTablesList(
			ArrayList<Class<? extends Bean>> associations) {
		String tablesList = new String();

		for (int i = 0; i < associations.size(); ++i) {
			tablesList += Inflector.tableize(associations.get(i)
					.getSimpleName());

			if (i != associations.size() - 1) {
				tablesList += ", ";
			}
		}

		return tablesList;
	}

	private String getJoinsList(ArrayList<Class<? extends Bean>> associations) {
		String joinsList = new String();

		for (int i = 0; i < associations.size(); ++i) {
			joinsList += " LEFT JOIN "
					+ Inflector.tableize(associations.get(i).getSimpleName())
					+ " ON "
					+ Inflector.tableize(this.bean.getClass().getSimpleName())
					+ ".id = "
					+ Inflector.tableize(associations.get(i).getSimpleName())
					+ ".id_"
					+ Inflector.tableize(this.bean.getClass().getSimpleName());
		}

		return joinsList;
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
	 *            Faut-il retourner l'ID généré automatiquement ?
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
		for (int i = 0; i < fields.size(); ++i) {
			if (fields.get(i).getName() != "id") {
				fields.get(i).setAccessible(true);
				preparedStatement.setObject(i, fields.get(i).get(bean));
			}
		}

		return preparedStatement;
	}

	/**
	 * Fermeture d'un ResultSet
	 * 
	 * @param resultSet
	 *            Le ResultSet
	 */
	private static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("Échec de la fermeture du ResultSet : "
						+ e.getMessage());
			}
		}
	}

	/**
	 * Fermeture d'un Statement
	 * 
	 * @param statement
	 *            Le Statement
	 */
	private static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("Échec de la fermeture du Statement : "
						+ e.getMessage());
			}
		}
	}

	/**
	 * Fermeture d'une Connection
	 * 
	 * @param connection
	 *            La Connection
	 */
	private static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Échec de la fermeture de la connexion : "
						+ e.getMessage());
			}
		}
	}

	/**
	 * Fermeture d'un Statement et d'une Connection
	 * 
	 * @param statement
	 *            Le Statement
	 * @param connection
	 *            La Connection
	 */
	private static void close(Statement statement, Connection connection) {
		close(statement);
		close(connection);
	}

	/**
	 * Fermeture d'un ResultSet, d'un Statement et d'une Connection
	 * 
	 * @param resultSet
	 *            Le ResultSet
	 * @param statement
	 *            Le Statement
	 * @param connection
	 *            La Connection
	 */
	private static void close(ResultSet resultSet, Statement statement,
			Connection connection) {
		close(resultSet);
		close(statement);
		close(connection);
	}

}
