package ch.epfl.alpano.gui;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import javafx.scene.Node;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by AntoineCrettenand on 26/04/2017.
 */
public class LabelizerTest {

	@Test public void visibleSummitsWorksWithExemples() throws Exception {

		File HGT_FILE = new File("ressources/N46E007.hgt");
		PanoramaParameters PARAMS = PredefinedPanoramas.NIESEN
				.panoramaParameters();

		PanoramaParameters Params = PARAMS;
		try (DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE)) {
			ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);

			Labelizer label = new Labelizer(cDEM,
					GazetteerParser.readSummitsFrom(new File("ressources/alps.txt")));

		//	List<Labelizer.SummitWrapper> visibleSummits = label.visibleSummits(PARAMS);

		//	visibleSummits.forEach((Labelizer.SummitWrapper s) -> System.out.println(s.summit()));

			System.out.println(" ");

			List<Node> nodes = label.labels(Params);

			nodes.forEach(System.out::println);

		}

	}
}