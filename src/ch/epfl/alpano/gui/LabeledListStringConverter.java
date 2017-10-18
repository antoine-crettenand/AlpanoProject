package ch.epfl.alpano.gui;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

/**
 * Converter from Strings to Integers and vice versa
 *
 * @see StringConverter
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {
	private final String[] strings;

	/**
	 * Create a "map" : each string is "mapped" to its position in {field strings}
	 * @param strings any number of strings
	 */
	public LabeledListStringConverter(String... strings) {
		this.strings = strings;
	}

	/**
	 * Compute the string mapped to {@param integer}
	 * @param integer {@code Integer} to convert
	 * @return matching {@code string}
	 */
	@Override public String toString(Integer integer) {
		Preconditions.checkArgument(0 <= integer  && integer < strings.length, "invalid index : " + integer);
		return strings[integer];
	}

	/**
	 * Compute the Integer mapped to {@param string}
	 * @param string {@code string} to convert
	 * @return matching {@code Integer}
	 * @throws IllegalArgumentException if {@param string} isn't mapped to an integer
	 */
	@Override public Integer fromString(String string) {
		for(int i = 0; i < strings.length; i++){
			if (strings[i].equals(string)) return i;
		}
		throw new IllegalArgumentException("string not found : " + string);
	}
}
