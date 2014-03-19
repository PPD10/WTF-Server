package utility;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Cette classe permet, gr�ce � des m�thodes statiques, de manipuler des chaines
 * de caract�re afin qu'elles puissent �tre utilis�es par les autres classes de
 * l'application
 * 
 * @author Phil�mon Bouzy
 * @version 1.0
 */
public class Inflector {

	/**
	 * Cr�e une chaine s�par�e par des underscores � partir d'une chaine
	 * camelCase.
	 * 
	 * @param camelized
	 *            La chaine camelCase
	 * @return La chaine s�par�e par des underscores
	 */
	public static String underscore(String camelized) {
		return camelized.replaceAll("\\B([A-Z])", "_$1").toLowerCase();
	}

	/**
	 * Cr�e une chaine camerCase � partir d'une chaine s�par�e par des
	 * underscores.
	 * 
	 * @param underscored
	 *            La chaine s�par�e par des underscored
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
	 * Cr�e un nom de table � partir d'un nom de classe.
	 * 
	 * @param className
	 *            Le nom de la classe
	 * @return Le nom de la table
	 */
	public static String tableize(String className) {
		return Inflector.underscore(className);
	}

	/**
	 * Cr�e un nom de classe � partir d'un nom de table.
	 * 
	 * @param tableName
	 *            Le nom de la table
	 * @return Le nom de la classe
	 */
	public static String classify(String tableName) {
		return Inflector.camelize(tableName);
	}

	/**
	 * Cr�e un nom de classe � partir d'un nom de table et d'un suffixe.
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
