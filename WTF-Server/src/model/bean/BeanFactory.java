package model.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import utility.Inflector;

/**
 * Cette classe est une fabrique de Bean.
 * 
 * @author Philémon Bouzy
 * @version 1.0
 */
public class BeanFactory {

	/**
	 * Retourne un Bean
	 * 
	 * @return une instance de Bean
	 */
	public static Bean getBean() {
		return new Bean();
	}

	/**
	 * Retourne un Bean ou un héritier de Bean à partir de la classe dudit Bean.
	 * 
	 * @param beanClass
	 *            La classe du Bean
	 * @return Une instance de Bean ou d'un héritier de Bean
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static <T extends Bean> T getBean(Class<T> beanClass)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return beanClass.newInstance();
	}

	/**
	 * Retourne un Bean ou un héritier de Bean à partir de la classe dudit Bean
	 * et d'un ResultSet (servant à renseigné les attributs dudit Bean).
	 * 
	 * @param beanClass
	 *            La classe du Bean
	 * @param resultSet
	 *            Le ResultSet
	 * @param getAssociations
	 *            Doit-on retourner les associations ?
	 * @param fieldNamesList
	 *            Chaine de caractère contenant la liste des champs de la
	 *            requête. Ce paramètre pallie, en l'occurence, l'absence d'une
	 *            implémentation correcte de ResultSetMetaData dans les drivers
	 *            de PostgreSQL.
	 * @return Une instance de Bean ou d'un héritier de Bean
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	public static <T extends Bean> T getBean(Class<T> beanClass,
			ResultSet resultSet, String fieldNamesList)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SQLException, NoSuchMethodException,
			SecurityException, ClassNotFoundException,
			InvocationTargetException {
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int i = 0;
		int columnAssociationNumber;
		int columnNumber = resultSetMetaData.getColumnCount();

		if (resultSet.next()) {
			int id = resultSet.getInt("id");
			
			T bean = getBean(beanClass);
			i = bean.setAttributes(resultSet, i);
			columnAssociationNumber = i;
			
			if (!fieldNamesList.isEmpty()) {
				Bean associatedBean;
				ArrayList<String> fieldNames = new ArrayList<String>(
						Arrays.asList(fieldNamesList.split(", ")));
				
				String associatedBeanSimpleClassName;
				String associatedBeanClassName;
				String addAssociatedBeanMethodName;
				
				Method addAssociatedBeanMethod;
				
				do {
					while (i < columnNumber) {
						associatedBeanSimpleClassName = Inflector.classify(
								fieldNames.get(i).split("\\.")[0]);
						
						associatedBeanClassName = "model.bean."
						+ associatedBeanSimpleClassName;
						
						associatedBean = (Bean) Class.forName(
								associatedBeanClassName).newInstance();
						
						i = associatedBean.setAttributes(resultSet, i);

						addAssociatedBeanMethodName = "add"
						+ associatedBeanSimpleClassName;
						
						addAssociatedBeanMethod = bean.getClass().getMethod(
								addAssociatedBeanMethodName, 
								associatedBean.getClass());
						
						addAssociatedBeanMethod.invoke(bean, associatedBean);
					}
					i = columnAssociationNumber;
				} while (resultSet.next() && id == resultSet.getInt("id"));
			}

			return bean;
		}
		return null;
	}

	/**
	 * Retourne une liste de Bean ou d'héritiers de Bean à partir de la classe
	 * dudit Bean et d'un ResultSet.
	 * 
	 * @param beanClass
	 *            La classe du Bean
	 * @param resultSet
	 *            Le ResultSet
	 * @return La liste de Bean ou d'héritiers de ce dernier
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws InvocationTargetException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static <T extends Bean> ArrayList<T> getBeanList(Class<T> beanClass,
			ResultSet resultSet, String fieldNamesList) 
					throws IllegalArgumentException, IllegalAccessException,
					SQLException, InstantiationException, NoSuchMethodException,
					SecurityException, ClassNotFoundException,
					InvocationTargetException {
		ArrayList<T> beanList = new ArrayList<T>();
		T bean;

		do {
			bean = getBean(beanClass, resultSet, fieldNamesList);
			
			if (bean != null)
				beanList.add(bean);
		} while (bean != null && resultSet.previous());
		
		return beanList;
	}

}
