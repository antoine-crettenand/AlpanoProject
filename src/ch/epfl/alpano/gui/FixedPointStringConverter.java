package ch.epfl.alpano.gui;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

import java.math.BigDecimal;

/**
 * Converter from Integer To String and vice-versa
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 * @see StringConverter
 */
public final class FixedPointStringConverter extends StringConverter<Integer> {
	private final int precision;

	public FixedPointStringConverter(int precision) {
		Preconditions.checkArgument(precision >= 0);
		this.precision = precision;
	}

	/**
	 * From Integer to string method
	 *
	 * @param object value of {@code Integer} to convert
	 * @return matching {@code string}
	 */
	@Override public String toString(Integer object) {
		if (object == null) return "";
		return "" + BigDecimal.valueOf(object).movePointLeft(precision);
	}

	/**
	 * From string to Integer method
	 *
	 * @param string value of {@code string} to convert
	 * @return matching {@code Integer}
	 * @throws IllegalArgumentException if string does not containt valid Integer
	 */
	@Override public Integer fromString(String string) {

		try {
			double value = Double.parseDouble(string);

			BigDecimal integer = BigDecimal.valueOf(value);
			integer = integer.movePointRight(precision);

			return (int) Math.round(integer.doubleValue());

		} catch (NumberFormatException e){
			throw new IllegalArgumentException("input string is not valid : " + string);
		}
	}
}
