package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import ch.epfl.alpano.gui.PredefinedPanoramas;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * TODO : Description
 *
 * @author Julien Sahli
 * @date 27/03/2017
 */

final class DrawGrayPanorama {
	final static File HGT_FILE = new File("N46E007.hgt");

	final static PanoramaParameters PARAMS = PredefinedPanoramas.NIESEN.panoramaParameters();

	public static void main(String... as) throws Exception {
		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(
				HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);

			Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);

			ChannelPainter gray =
					ChannelPainter.maxDistanceToNeighbors(p)
							.sub(500)
							.div(4500)
							.clamp()
							.invert();

			ChannelPainter distance = p::distanceAt;
			ChannelPainter opacity =
					distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

			ImagePainter l = ImagePainter.gray(gray, opacity);

			Image i = PanoramaRenderer.renderPanorama(p, l);
			ImageIO.write(SwingFXUtils.fromFXImage(i, null),
					"png",
					new File("niesen-profile.png"));
		}
	}
}

