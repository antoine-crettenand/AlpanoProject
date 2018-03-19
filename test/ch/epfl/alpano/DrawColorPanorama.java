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
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * TODO : Description
 *
 * @author Julien Sahli
 * @date 27/03/2017
 */

final class DrawColorPanorama {
	final static String id = "N46E007";
	final static File HGT_FILE = new File(id + ".hgt");

	final static PanoramaParameters PARAMS = PredefinedPanoramas.ALPES_DU_JURA.panoramaParameters();
    static long start;

	public static void main(String[] as) throws Exception {
        start = System.currentTimeMillis();
        System.out.print("Load Elevation Model : ");


		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(
				HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);


			nextStep("Compute Panorama");

			Panorama p = new PanoramaComputer(cDEM).computePanorama(PARAMS);


			nextStep("Create ChannelPainter");

            ImagePainter imagePainter;
            if(new File(id + ".png").isFile()) {
                InputStream inputStream = new FileInputStream(id + ".png");
                Image airphoto = new Image(inputStream, dDEM.extent().iX().size(), dDEM.extent().iY().size(), false, false); //TODO : tester avec smooth true et false
                ChannelPainter invert = ChannelPainter.maxDistanceToNeighbors(p)
                        .sub(500)
                        .div(4500)
                        .clamp()
                        .invert();
                imagePainter = ImagePainter.airPhoto(p, airphoto, dDEM.extent().iX().includedFrom(), dDEM.extent().iY().includedFrom(), invert);

            } else {
                ChannelPainter saturation = ChannelPainter.maxDistanceToNeighbors(p).div(200_000).clamp().invert();
                ChannelPainter brightness = (x, y) -> (1 - (2 * p.slopeAt(x, y) / (float) Math.PI)) * 0.7f + 0.3f;
                ChannelPainter distance = p::distanceAt;
                ChannelPainter hue = distance.div(50_000f).cycle().mul(360);
                hue = (x, y) -> (p.elevationAt(x, y) / 50);
                ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
                imagePainter = ImagePainter.hsb(hue, saturation, brightness, opacity);

            }
			nextStep("Generate Image");

			Image i = PanoramaRenderer.renderPanorama(p, imagePainter);
			ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png",new File(
					"testOutput/PredefinedPanorama-color-profile.png"));
			System.out.println((float)(System.currentTimeMillis() - start) / 1000f + " sec");
		}
	}

	private static void nextStep(String s){
        System.out.println((float)(System.currentTimeMillis() - start) / 1000f + " sec");
        start = System.currentTimeMillis();
        System.out.print(s + " : ");
    }
}

