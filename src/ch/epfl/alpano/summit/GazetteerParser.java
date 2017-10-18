package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Class represents a file-reader describing a list of Summits
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public class GazetteerParser {

	private GazetteerParser() {
	}

	/**
	 * Compute a list of Summit from a file
	 *
	 * @param file list of Summits in text format
	 * @return the list of all summits registered in the file
	 * @throws IOException if there is any input problem
	 */
	public static List<Summit> readSummitsFrom(File file) throws IOException {

		List<Summit> summits = new ArrayList<>();

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
				US_ASCII))) {

				String l;
			while ((l = bufferedReader.readLine()) != null) {
				Summit summit = extractLine(l);
				summits.add(summit);
			}
		}

		return Collections.unmodifiableList(summits);

	}

	/**
	 * @param line String
	 * @return the summit registered in the specified line
	 */
	private static Summit extractLine(String line) throws IOException {

		try {
			double longitude = fromStringToRad(line.substring(0, 9).trim());
			double latitude = fromStringToRad(line.substring(10, 18).trim());
			int elevation = Integer.parseInt(line.substring(20, 26).trim());
			String name = line.substring(36);

			return new Summit(name, new GeoPoint(longitude, latitude),
					elevation);

		} catch ( NumberFormatException e) {
			throw new IOException("Format Invalide");

		} catch (StringIndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IOException("Format Invalide");
		}
	}

	private static double fromStringToRad(String line) {

		//if (!line.contains(":"))
		//	throw new IllegalArgumentException("String does not contain ':'");

		String[] angle = line.split(":");

		double result = Integer.parseInt(angle[0])
				+ Integer.parseInt(angle[1]) / 60d
				+ Integer.parseInt(angle[2]) / 3600d;
		return Math.toRadians(result);

	}

}
