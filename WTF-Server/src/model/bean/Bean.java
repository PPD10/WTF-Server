package model.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import utility.Inflector;

/**
 * Cette classe est la classe JavaBean principale.
 * 
 * @author Philémon Bouzy
 * @version 1.1
 */
public class Bean {

	private Integer id;
	private static ArrayList<Class<? extends Bean>> belongsTo = new ArrayList<Class<? extends Bean>>();
	private static ArrayList<Class<? extends Bean>> hasOne = new ArrayList<Class<? extends Bean>>();
	private static ArrayList<Class<? extends Bean>> hasMany = new ArrayList<Class<? extends Bean>>();

	/**
	 * Ajoute des relations belongsTo (N, 1)
	 * 
	 * @param beans
	 *            Les types de Bean
	 */
	@SafeVarargs
	protected static void belongsTo(Class<? extends Bean>... beans) {
		belongsTo.addAll(Arrays.asList(beans));
	}

	/**
	 * Ajoute des relations hasOne (1, 1)
	 * 
	 * @param beans
	 *            Les types de Bean
	 */
	@SafeVarargs
	protected static void hasOne(Class<? extends Bean>... beans) {
		hasOne.addAll(Arrays.asList(beans));
	}

	/**
	 * Ajoute des relations hasMany (1, N)
	 * 
	 * @param beans
	 *            Les types de Bean
	 */
	@SafeVarargs
	protected static void hasMany(Class<? extends Bean>... beans) {
		hasMany.addAll(Arrays.asList(beans));
	}

	/**
	 * Retourne la liste des associations du Bean
	 * 
	 * @return La liste des associations
	 */
	public static HashMap<String, ArrayList<Class<? extends Bean>>> getAssociations() {
		HashMap<String, ArrayList<Class<? extends Bean>>> associations = new HashMap<String, ArrayList<Class<? extends Bean>>>();
		associations.put("belongsTo", belongsTo);
		associations.put("hasOne", hasOne);
		associations.put("hasMany", hasMany);

		return associations;
	}

	/**
	 * Retourne l'ID.
	 * 
	 * @return L'ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Définit l'ID
	 * 
	 * @param id
	 *            L'ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Définit un attribut du Bean
	 * 
	 * @param field
	 *            L'attribut
	 * @param value
	 *            La valeur
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void setAttribute(Field field, Object value)
			throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		field.set(this, value);
	}

	/**
	 * Définit tous les attributs (sauf les associations) du Bean à partir d'un
	 * ResultSet.
	 * 
	 * @param resultSet
	 *            Le ResultSet
	 * @param columnPosition
	 *            La position de la colonne dans le ResultSet
	 * @return La nouvelle position de la colonne dans le ResultSet
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	protected int setAttributes(ResultSet resultSet, int columnPosition)
			throws IllegalArgumentException, IllegalAccessException,
			SQLException {
		ArrayList<Field> fields = getAttributes(this.getClass());

		for (Field field : fields) {
			if (!field.getType().isAssignableFrom(ArrayList.class)
					&& !field.getType().isAssignableFrom(Set.class)) {
				this.setAttribute(field, resultSet.getObject(columnPosition + 1));
				++columnPosition;
			}
		}

		return columnPosition;
	}

	/**
	 * Retourne la liste des attributs du Bean
	 * 
	 * @return La liste des attributs
	 */
	public static <T extends Bean> ArrayList<Field> getAttributes(
			Class<T> beanClass) {
		Field[] superclassFields = beanClass.getSuperclass().getDeclaredFields();
		Field[] classFields = beanClass.getDeclaredFields();
		ArrayList<Field> fields = new ArrayList<Field>(
				Arrays.asList(superclassFields));
		fields.addAll(Arrays.asList(classFields));

		return fields;
	}

	public static <T extends Bean> ArrayList<Field> 
	getAttributesWithoutAssociations(Class<T> beanClass) {
		ArrayList<Field> fields = getAttributes(beanClass);
//		Field[] superclassFields = beanClass.getSuperclass()
//				.getDeclaredFields();
//		Field[] classFields = beanClass.getDeclaredFields();
//		ArrayList<Field> fields = new ArrayList<Field>(
//				Arrays.asList(superclassFields));
//		fields.addAll(Arrays.asList(classFields));

		for (int i = fields.size() - 1; i > 0; --i) {
			if (fields.get(i).getName().equals("belongsTo")
					|| fields.get(i).getName().equals("hasOne")
					|| fields.get(i).getName().equals("hasMany")
					|| fields.get(i).getName().contains("Associations")) {
				fields.remove(i);
			}
		}

		return fields;
	}

	/**
	 * Retourne un objet Field (un attribut), s'il existe, null sinon.
	 * 
	 * @param attributeName
	 *            Le nom de l'attribut
	 * @param fields
	 *            La liste
	 * @return L'objet Field ou null
	 */
	public static Field getAttribute(String attributeName,
			ArrayList<Field> fields) {
		for (Field field : fields) {
			if (field.getName().equals(attributeName))
				return field;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Bean> boolean isNewAssociation(T bean) 
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException {
		String beanSimpleClassName;
		String getAssociatedBeanMethodName;

		Method getAssociatedBeanMethod;

		beanSimpleClassName = this.getClass().getSimpleName();

		getAssociatedBeanMethodName = "get" + beanSimpleClassName
				+ "Associations";

		getAssociatedBeanMethod = bean.getClass().getMethod(
				getAssociatedBeanMethodName);

		ArrayList<? extends Bean> associationsList = (ArrayList<? extends Bean>)
				getAssociatedBeanMethod.invoke(bean);
		
		for (Bean associatedBean : associationsList) {
			System.out.println(associatedBean.getId() + " = " + this.getId());
			if (associatedBean.getId() == this.getId())
				return false;
		}
		
		return true;
	}

}