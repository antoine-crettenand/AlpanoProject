package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.util.regex.Pattern;

import static java.nio.channels.FileChannel.MapMode;

/**
 * This class represents a discrete elevation model computed from an .hgt file.
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
	private static final Pattern pattern = Pattern
			.compile("[NS][0-9]{2}[EW][0-9]{3}\\.hgt");
	private final int FILE_LENGTH = 25_934_402;
	private final ShortBuffer buffer;
	private final String fileName;
	private final Interval2D extent;

	/**
	 * creates a new instance of the class based on a .hgt file
	 *
	 * @param file .hgt
	 * @throws IllegalArgumentException if fileName is not in the right format {@see pattern} or fileLength is not
	 * equal to FILE_LENGTH
	 */
	public HgtDiscreteElevationModel(File file) {

		fileName = file.getName();

		Preconditions.checkArgument(checkFileName(fileName), "nom incorrect");
		Preconditions
				.checkArgument(checkFileLength(file), "longueur incorrect");

		try (FileInputStream stream = new FileInputStream(file)) {
			buffer = stream.getChannel()
					.map(MapMode.READ_ONLY, 0, file.length()).asShortBuffer();
		} catch (IOException e) {
			throw new IllegalArgumentException("file not found");
		}

		//compute extent
		char northSouth = fileName.charAt(0);
		int latitude = (northSouth == 'N' ? 1 : -1) * Integer
				.parseInt(fileName.substring(1, 3)) * SAMPLES_PER_DEGREE;

		char eastWest = fileName.charAt(3);
		int longitude = (eastWest == 'E' ? 1 : -1) * Integer
				.parseInt(fileName.substring(4, 7)) * SAMPLES_PER_DEGREE;

		Interval1D nS = new Interval1D(longitude,
				longitude + SAMPLES_PER_DEGREE);
		Interval1D eW = new Interval1D(latitude, latitude + SAMPLES_PER_DEGREE);

		extent = new Interval2D(nS, eW);

	}

	private boolean checkFileName(String fileName) {
		return pattern.matcher(fileName).find();
	}

	private boolean checkFileLength(File file) {
		return (file.length() == FILE_LENGTH);
	}

	@Override
	public Interval2D extent() {
		return extent;
	}

	@Override
    public double elevationSample(int x, int y) {
		Preconditions.checkArgument(extent().contains(x, y));

		int index =
				(extent().iY().includedTo() - y) * (SAMPLES_PER_DEGREE + 1) + (x
						- extent().iX().includedFrom());

		return buffer.get(index);
	}

	@Override
    public void close() throws Exception {
	}
}
