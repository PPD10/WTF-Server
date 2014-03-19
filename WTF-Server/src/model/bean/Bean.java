package model.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Cette classe est la classe JavaBean principale
 * 
 * @author Philémon Bouzy
 * @version 1.0
 */
public class Bean {

	private Integer id;

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
	 * Retourne la liste des attributs du Bean
	 * 
	 * @return La liste des attributs
	 */
	public ArrayList<Field> getAttributes() {
		Field[] superclassFields = this.getClass().getSuperclass()
				.getDeclaredFields();
		Field[] classFields = this.getClass().getDeclaredFields();
		ArrayList<Field> fields = new ArrayList<Field>(
				Arrays.asList(superclassFields));
		fields.addAll(Arrays.asList(classFields));

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

}