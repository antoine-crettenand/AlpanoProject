package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static ch.epfl.alpano.DrawDEM.gray;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.toRadians;

/**
 * Created by AntoineCrettenand on 27/03/2017.
 */

final class DrawPanorama {
	final static File HGT_FILE = new File("N46E007.hgt");

	final static int IMAGE_WIDTH = 500;
	final static int IMAGE_HEIGHT = 200;

	final static double ORIGIN_LON = toRadians(7.65);
	final static double ORIGIN_LAT = toRadians(46.73);
	final static int ELEVATION = 600;
	final static double CENTER_AZIMUTH = toRadians(180);
	final static double HORIZONTAL_FOV = toRadians(60);
	final static int MAX_DISTANCE = 100_000;

	final static PanoramaParameters PARAMS = new PanoramaParameters(
			new GeoPoint(ORIGIN_LON, ORIGIN_LAT), ELEVATION, CENTER_AZIMUTH,
			HORIZONTAL_FOV, MAX_DISTANCE, IMAGE_WIDTH, IMAGE_HEIGHT);

	public static void main(String[] as) throws Exception {
		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(
				HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
			Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

			BufferedImage i = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
					TYPE_INT_RGB);

			for (int x = 0; x < IMAGE_WIDTH; ++x) {
				for (int y = 0; y < IMAGE_HEIGHT; ++y) {
					float d = p.distanceAt(x, y);
					int c = (d == Float.POSITIVE_INFINITY) ?
							0x87_CE_EB :
							gray((d - 2_000) / 15_000);
					i.setRGB(x, y, c);
				}
			}

			ImageIO.write(i, "png", new File("testOutput/niesen_prof.png"));
		}
	}
}
