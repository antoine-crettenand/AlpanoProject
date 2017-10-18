package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Compute a Panorama into an JavaFX Image
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public interface PanoramaRenderer {

	/**
	 * Compute a Java FX image from a Panorama and an ImagePainter
	 * Each pixel of the image is colored depending on the ImagePainter
	 *
	 * @param p Panorama to compute
	 * @param i ImagePainter
	 * @return JavaFX Image
	 * @see Image
	 */
	static Image renderPanorama(Panorama p, ImagePainter i) {

		int IMAGE_WIDTH = p.parameters().width();
		int IMAGE_HEIGHT = p.parameters().height();

		WritableImage writableImage = new WritableImage(IMAGE_WIDTH, IMAGE_HEIGHT);

		PixelWriter writer = writableImage.getPixelWriter();

		for (int a = 0; a < IMAGE_WIDTH; a++) {
			for (int b = 0; b < IMAGE_HEIGHT; b++) {
				writer.setColor(a, b, i.colorAt(a, b));
			}
		}

		return writableImage;
	}


	static Image defaultRenderPanorama(Panorama p){

		ChannelPainter saturation = ChannelPainter.maxDistanceToNeighbors(p).div(200_000).clamp().invert();
		ChannelPainter brightness = (x, y) -> (1 - (2 * p.slopeAt(x, y) / (float) Math.PI)) * 0.7f + 0.3f;

		ChannelPainter distance = p::distanceAt;
		ChannelPainter hue = distance.div(100_000f).cycle().mul(360);


		ChannelPainter opacity =
				distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

		ImagePainter i = ImagePainter.hsb(hue, saturation, brightness, opacity);


		return renderPanorama(p, i);

	}

}
