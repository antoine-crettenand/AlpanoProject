package ch.epfl.alpano;

import ch.epfl.alpano.gui.PredefinedPanoramas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by AntoineCrettenand on 27/04/2017.
 */
public class CompareImages {

	public static void main(String[] as) throws IOException {

		PanoramaParameters PARAMS = PredefinedPanoramas.NIESEN.panoramaParameters();

		BufferedImage theirs = ImageIO.read(new File("test/testRessources/louis_niesen.png"));
		BufferedImage ours = ImageIO.read(new File("test/testOutput/niesen-gray-profile.png"));

		int wrong = 0, good = 0;
		for (int x = 0; x < PARAMS.width(); x++){
			for (int y = 0; y < PARAMS.height(); y++ ){
				if (theirs.getRGB(x,y) == ours.getRGB(x,y)) good++;
				else {
					wrong++;
					System.out.print("wrong pixel at ( " + x + ", " + y + " ) " );
					System.out.println("difference of : " + Math.abs(theirs.getRGB(x,y) - ours.getRGB(x,y)) );
				}

			}
		}

		System.out.println("wrong pixels : " + ((wrong) / (float) (wrong + good)) * 100);


	}
}
