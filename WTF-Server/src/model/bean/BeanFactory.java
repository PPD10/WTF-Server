package model.bean;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import utility.Inflector;

/**
 * Cette classe est une fabrique de Bean.
 * 
 * @author Philémon
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
	@SuppressWarnings("unchecked")
	public static <T extends Bean> T getBean(Class<T> beanClass)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return (T) Class.forName(beanClass.getName()).newInstance();
	}

	/**
	 * Retourne un Bean ou un héritier de Bean à partir de la classe dudit Bean
	 * et d'un ResultSet (servant à renseigné les attributs dudit Bean).
	 * 
	 * @param beanClass
	 *            La classe du Bean
	 * @param resultSet
	 *            Le ResultSet
	 * @return Une instance de Bean ou d'un héritier de Bean
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	public static <T extends Bean> T getBean(Class<T> beanClass,
			ResultSet resultSet) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, SQLException {
		if (resultSet.next()) {
			T bean = beanClass.newInstance();
			ArrayList<Field> fields = bean.getAttributes();
			for (Field field : fields) {
				bean.setAttribute(field, resultSet.getObject(Inflector
						.underscore(field.getName())));
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
	 */
	public static <T extends Bean> ArrayList<T> getBeanList(Class<T> beanClass,
			ResultSet resultSet) throws IllegalArgumentException,
			IllegalAccessException, SQLException, InstantiationException {
		ArrayList<T> beanList = new ArrayList<T>();

		while (resultSet.next()) {
			T bean = beanClass.newInstance();
			ArrayList<Field> fields = bean.getAttributes();
			for (Field field : fields) {
				bean.setAttribute(field, resultSet.getObject(Inflector
						.underscore(field.getName())));
			}

			beanList.add(bean);
		}

		return beanList;
	}

}
