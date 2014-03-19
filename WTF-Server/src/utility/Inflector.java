package utility;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Cette classe permet, grâce à des méthodes statiques, de manipuler des chaines
 * de caractère afin qu'elles puissent être utilisées par les autres classes de
 * l'application
 * 
 * @author Philémon Bouzy
 * @version 1.0
 */
public class Inflector {

	/**
	 * Crée une chaine séparée par des underscores à partir d'une chaine
	 * camelCase.
	 * 
	 * @param camelized
	 *            La chaine camelCase
	 * @return La chaine séparée par des underscores
	 */
	public static String underscore(String camelized) {
		return camelized.replaceAll("\\B([A-Z])", "_$1").toLowerCase();
	}

	/**
	 * Crée une chaine camerCase à partir d'une chaine séparée par des
	 * underscores.
	 * 
	 * @param underscored
	 *            La chaine séparée par des underscored
	 * @return La chaine camelCase
	 */
	public static String camelize(String underscored) {
		String capitalized = WordUtils
				.capitalize(underscored.replace('_', ' '))
				.replaceAll("\\s", "");
		return Character.toLowerCase(capitalized.charAt(0))
				+ capitalized.substring(1);
	}

	/**
	 * Crée un nom de table à partir d'un nom de classe.
	 * 
	 * @param className
	 *            Le nom de la classe
	 * @return Le nom de la table
	 */
	public static String tableize(String className) {
		return Inflector.underscore(className);
	}

	/**
	 * Crée un nom de classe à partir d'un nom de table.
	 * 
	 * @param tableName
	 *            Le nom de la table
	 * @return Le nom de la classe
	 */
	public static String classify(String tableName) {
		return Inflector.camelize(tableName);
	}

	/**
	 * Crée un nom de classe à partir d'un nom de table et d'un suffixe.
	 * 
	 * @param tableName
	 *            Le nom de la table
	 * @param suffix
	 *            Le suffixe
	 * @return Le nom de la classe
	 */
	public static String classify(String tableName, String suffix) {
		return Inflector.camelize(tableName) + WordUtils.capitalize(suffix);
	}

}
