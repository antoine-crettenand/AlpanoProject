package ch.epfl.alpano;

import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Louis Vialar
 */
public class GazetteerParserTest {
	private static List<Summit> list;
	private static Map<String, Summit> summits;

	private static Map<String, Summit> map(List<Summit> summitList) {
		return summitList.stream().<Map<String, Summit>>collect(HashMap::new, (map, e) -> map.put(e.name(), e), Map::putAll);
	}

	@BeforeClass public static void load() throws IOException {
		File file = new File("alps.txt");
		list = GazetteerParser.readSummitsFrom(file);
		summits = map(list);
	}

	private static boolean listContains(String name) {
		for (Summit summit : list) {
			if (summit.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

        /*
			7:56:53 46:35:33  2472  H1 C02 D0 LAUBERHORN
            8:00:19 46:34:39  3970  H1 C02 E0 EIGER
            7:59:01 46:34:38  2663  H1 C02 E0 ROTSTOCK
         */

	private static double radians(int deg, int min, int sec) {
		double degrees = ((double) deg) + ((double) min) / 60D + ((double) sec) / 3600D;
		return Math.toRadians(degrees);
	}

	@Test public void sizeTest() throws Exception {
		assertEquals(21_069, list.size());
	}

	@Test public void mapContainsTest() throws Exception {
		assertEquals(true, summits.containsKey("LAUBERHORN"));
		assertEquals(true, summits.containsKey("EIGER"));
		assertEquals(true, summits.containsKey("ROTSTOCK"));
	}

	@Test public void containsTest() throws Exception {
		assertEquals(true, listContains("LAUBERHORN"));
		assertEquals(true, listContains("EIGER"));
		assertEquals(true, listContains("ROTSTOCK"));
	}

	@Test public void heightTest() throws Exception {
		assertEquals(2472, summits.get("LAUBERHORN").elevation());
		assertEquals(3970, summits.get("EIGER").elevation());
	}

	@Test public void latitudeTest() throws Exception {
		assertEquals(radians(7, 56, 53),
				summits.get("LAUBERHORN").position().longitude(), 0.00001);
		assertEquals(radians(8, 0, 19),
				summits.get("EIGER").position().longitude(), 0.00001);
	}

	@Test public void longitudeTest() throws Exception {
		assertEquals(radians(46, 35, 33),
				summits.get("LAUBERHORN").position().latitude(), 0.00001);
		assertEquals(radians(46, 34, 39),
				summits.get("EIGER").position().latitude(), 0.00001);
	}
}