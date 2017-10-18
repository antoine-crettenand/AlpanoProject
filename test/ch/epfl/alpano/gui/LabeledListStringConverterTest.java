package ch.epfl.alpano.gui;

import org.junit.Test;

/**
 * Created by AntoineCrettenand on 04/05/2017.
 */
public class LabeledListStringConverterTest {
	@Test public void toStringWorks() throws Exception {
		LabeledListStringConverter c =
				new LabeledListStringConverter("zéro", "un", "deux");

		System.out.println(c.toString(0));
	}

	@Test public void fromStringWorks() throws Exception {
		LabeledListStringConverter c =
				new LabeledListStringConverter("zéro", "un", "deux");

		System.out.println(c.fromString("deux"));
	}

}