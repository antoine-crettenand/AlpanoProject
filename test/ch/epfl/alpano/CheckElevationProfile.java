package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

public class CheckElevationProfile {
	final static File HGT_FILE = new File("N46E006.hgt");
	final static double MAX_ELEVATION = 1_500;
	final static int LENGTH = 111_000;
	final static double AZIMUTH = toRadians(27.97);
	final static double LONGITUDE = toRadians(6.15432);
	final static double LATITUDE = toRadians(46.20562);
	final static int WIDTH = 800, HEIGHT = 100;

	@Test public void checkElevationProfile() throws Exception {
		BufferedImage i = ImageIO
				.read(new File("TestRessources/profile_prof.png"));

		DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
		ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
		GeoPoint o = new GeoPoint(LONGITUDE, LATITUDE);
		ElevationProfile p = new ElevationProfile(cDEM, o, AZIMUTH, LENGTH);

		int BLACK = 0x00_00_00, WHITE = 0xFF_FF_FF;

		for (int x = 0; x < WIDTH; ++x) {
			double pX = x * (double) LENGTH / (WIDTH - 1);
			double pY = p.elevationAt(pX);
			int yL = (int) ((pY / MAX_ELEVATION) * (HEIGHT - 1));
			for (int y = 0; y < HEIGHT; ++y) {
				int color = y < yL ? BLACK : WHITE;
				assertEquals("Incorrect value at " + x + " " + (HEIGHT - 1 - y),
						color, i.getRGB(x, HEIGHT - 1 - y) & 0xFFFFFF);
			}
		}
		dDEM.close();

	}
}