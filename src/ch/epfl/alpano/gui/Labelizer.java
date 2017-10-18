package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * Labelizer computes the visible Summits on a {@code Panorama} given it's {@code PanoramaParameters} and
 * create a JavaFX {@code Nodes} for each of them.
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 * @see Node
 */
public final class Labelizer {
	private final ContinuousElevationModel cDem;
	private final List<Summit> summitList;

	private static final int TAILLE_INTERVAL = 64;
	private static final int PRECISION = 200;
	private static final int PIXEL_SPACE = 2;
	private static final int PIXEL_BORDER = 20;
	private static final int PIXEL_MIN = 170;
	private static final int ROTATION_ANGLE = -60; //-60

	public Labelizer(ContinuousElevationModel cDem, List<Summit> summitList) {
		this.cDem = cDem;
		this.summitList = summitList;
	}

	/**
	 * Compute a List of JavaFX Node from a List of Summit
	 *
	 * @param params PanoramaParameters
	 * @return List of Node
	 * @see Text
	 * @see Line
	 */
	public List<Node> labels(PanoramaParameters params) {

		//initialisation
		BitSet positions = new BitSet(params.width());
		int setSize = positions.size();
		positions.flip(0, PIXEL_BORDER - 1);
		positions.flip(setSize - PIXEL_BORDER, setSize - 1);
		// 1/true = used, 0/false = available

		List<Node> nodes = new ArrayList<>();
		List<SummitWrapper> visibleSummits = visibleSummits(params);

		int minY = 0;
		for (SummitWrapper s : visibleSummits) {
			//get the coordinates (x, y) of the summit on the image
			int x = (int) Math.round(params.xForAzimuth(s.azimuth()));
			int y = (int) Math.round(params.yForAltitude(s.raySlope()));

			if (positions.get(x, x + PIXEL_BORDER).isEmpty() && y > PIXEL_MIN) {
				if (minY == 0)
					minY = y; // get the vertical pixel of the highest Summit
				Text t = new Text(x, minY - PIXEL_BORDER, s.summit().name() + " (" + s.s.elevation() + " m)");
				t.getTransforms().addAll(new Translate(0, -PIXEL_SPACE), new Rotate(ROTATION_ANGLE, x, minY -
						PIXEL_BORDER));
				Line l = new Line(x, minY - PIXEL_BORDER, x, y);
				nodes.add(t);
				nodes.add(l);
				positions.set(x, x + PIXEL_BORDER - 1, true);
			}
		}
		return nodes;
	}

	/**
	 * @param params
	 * @return
	 */
	private List<SummitWrapper> visibleSummits(PanoramaParameters params) {

		List<SummitWrapper> wrapperList = new ArrayList<>();

		for (Summit s : summitList) {

			GeoPoint observerPosition = params.observerPosition();
			GeoPoint summitPosition = s.position();
			double distBetween = observerPosition.distanceTo(summitPosition);
			double azimuthBetween = observerPosition.azimuthTo(summitPosition);
			double summitAzimuth = azimuthBetween - params.centerAzimuth();

			ElevationProfile profile = new ElevationProfile(cDem, observerPosition, azimuthBetween, distBetween);
			DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(profile, params.observerElevation(), 0);
			double y = -f.applyAsDouble(distBetween);
			double ray0 = params.observerElevation();
			double raySlope = Math.atan2(y, distBetween);

			//Condition
			if ((summitAzimuth < params.horizontalFieldOfView() / 2
					&& summitAzimuth > -params.horizontalFieldOfView() / 2) && (distBetween < params.maxDistance()) && (
					raySlope < params.verticalFieldOfView() / 2 && raySlope > -params.verticalFieldOfView() / 2)) {

				f = PanoramaComputer.rayToGroundDistance(profile, ray0, raySlope);

				double x = Math2.firstIntervalContainingRoot(f, 0, distBetween, TAILLE_INTERVAL);

				if (x >= distBetween - PRECISION)
					wrapperList.add(new SummitWrapper(s, y + ray0, raySlope, azimuthBetween));
			}
		}

		//Sort list
		wrapperList.sort((SummitWrapper s1, SummitWrapper s2) -> {

			double azimuth1 = params.yForAltitude(s1.raySlope());
			double azimuth2 = params.yForAltitude(s2.raySlope());

			int comparison = Double.compare(azimuth1, azimuth2);
			return (comparison == 0 ? Double.compare(s1.realElevation(), s2.realElevation()) : comparison);
		});

		return Collections.unmodifiableList(wrapperList);
	}

	/**
	 * Class used to stock computed information about a Summit
	 */
	private final class SummitWrapper {
		private final Summit s;
		private final double raySlope;
		private final double azimuth;
		private final double realElevation;

		private SummitWrapper(Summit s, double realElevation, double raySlope, double azimuth) {
			this.s = s;
			this.raySlope = raySlope;
			this.azimuth = azimuth;
			this.realElevation = realElevation;
		}

		Summit summit() {
			return s;
		}

		private double raySlope() {
			return raySlope;
		}

		private double azimuth() {
			return azimuth;
		}

		private double realElevation() {
			return realElevation;
		}
	}

}


