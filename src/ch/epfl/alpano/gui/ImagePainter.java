package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * A functional interface representing an image painter.
 * An Image Painter map a {@code Color} to every pixel of a two-dimensional grid
 * @see Color
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */
@FunctionalInterface public interface ImagePainter {

	/**
	 * Compute the color assigned to a specific pixel of coordinates (x, y)
	 *
	 * @param x x-axis
	 * @param y y-axis
	 * @return the associated color
	 */
	Color colorAt(int x, int y);

	/**
	 * Compute an ImagePainter based on the hsb Color system.
	 * The ImagePainter uses hue, saturation, brightness and opacity values at a given pixel to compute it's {@code
	 * Color}
	 *
	 * @param hue        - from 0 to 360
	 * @param saturation from 0.0 to 1.0
	 * @param brightness from 0.0 to 1.0
	 * @param opacity    from 0.0 to 1.0
	 * @return the image painter
	 */
	static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness,
			ChannelPainter opacity) {
		return (x, y) -> Color
				.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), brightness.valueAt(x, y), opacity.valueAt(x, y));
	}

	/**
	 * Compute an ImagePainter based on a black/white Color system.
	 * The ImagePainter uses gray and opacity values at a given pixel to compute it's {@code Color}
	 *
	 * @param gray    - from 0.0 to 1.0
	 * @param opacity from 0.0 to 1.0
	 * @return the image painter
	 */
	static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity) {
		return (x, y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
	}

	static ImagePainter airPhotoWithoutBounds(Panorama p, Image i, int Xmin, int Ymin){

		return new ImagePainter() {
			PixelReader pixelReader = i.getPixelReader();
			@Override

			public Color colorAt(int x, int y) {

				//Gestion des cas où le point visé est dans le ciel
				if(p.distanceAt(x,y,Float.POSITIVE_INFINITY) == Float.POSITIVE_INFINITY){
					return Color.TRANSPARENT;
				}

				int X = (int)(p.longitudeAt(x,y)*DiscreteElevationModel.SAMPLES_PER_RADIAN-Xmin);
				int Y = (int)(p.latitudeAt(x,y)*DiscreteElevationModel.SAMPLES_PER_RADIAN-Ymin);

				//Gestion des cas où le point visé n'est pas sur le hgt
				if(X < 0 || Y < 0){ //add upper bound
					if((x+y)%30>15){
						return new Color(1,0,1,1);
					}else {
						return new Color(0,1,0,1);
					}
				}
				return pixelReader.getColor(X,Y);
			}
		};
	}

	static ImagePainter airPhoto(Panorama p, Image i, int Xmin, int Ymin, ChannelPainter invert){


		return new ImagePainter() {
			@Override
			public Color colorAt(int x, int y) {

				if(invert.valueAt(x,y)>0.5){
					return airPhotoWithoutBounds(p,i,Xmin,Ymin).colorAt(x,y);
				}else{
					return airPhotoWithoutBounds(p,i,Xmin,Ymin).colorAt(x,y).invert();
				}
			}
		};
	}
}
