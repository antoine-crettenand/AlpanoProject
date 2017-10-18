package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.Math.toRadians;

public final class CheckNiesen {
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

	@Test
	public void testDrawPanorama() throws Exception {
		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(
				HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
			Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

			ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p)
					.sub(500).div(4500).clamp().invert();

			System.out.println(PARAMS.toString());
			ChannelPainter distance = p::distanceAt;
			ChannelPainter opacity = distance
					.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

			ImagePainter l = ImagePainter.gray(gray, opacity);

			Image i = PanoramaRenderer.renderPanorama(p, l);
			ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png",
					new File("niesen-profile.png"));

			BufferedImage img = ImageIO.read(new File("TestRessources/niesen-profile-prof.png"));
			BufferedImage target = SwingFXUtils.fromFXImage(i, null);

			for (int x = 0; x < IMAGE_WIDTH; ++x) {
				for (int y = 0; y < IMAGE_HEIGHT; ++y) {
					Assert.assertEquals(img.getRGB(x, y), target.getRGB(x, y));
				}
			}
		}
	}
}
