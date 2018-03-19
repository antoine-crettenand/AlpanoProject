package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <description>
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

public class PanoramaComputerBean {

	private ObjectProperty<PanoramaUserParameters> parameters = new SimpleObjectProperty<>();
	private ObjectProperty<Panorama> panorama = new SimpleObjectProperty<>();
	private ObjectProperty<Image> image = new SimpleObjectProperty<>();
	private ObservableList<Node> labels;

	/**
	 * Creates a new instance of the class
	 * @param cDem TODO
	 */
	public PanoramaComputerBean(ContinuousElevationModel cDem) {
		ObservableList<Node> labels = FXCollections.observableArrayList();
		this.labels = FXCollections.unmodifiableObservableList(labels);

		List<Summit> summitList = null;
		try {
			summitList = GazetteerParser.readSummitsFrom(new File("ressources/alps.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}


		Labelizer labelizer = new Labelizer(cDem, summitList);
		PanoramaComputer computer = new PanoramaComputer(cDem);

		parameters.addListener((prop, oV, nV) -> {
			System.out.println("Program is running...");
			long start = System.currentTimeMillis();
			panorama.set(computer.computePanorama(parameters.get().panoramaParameters()));
			image.set(PanoramaRenderer.defaultRenderPanorama(panorama.get()));
			labels.setAll(labelizer.labels(parameters.get().panoramaDisplayParameters()));
			System.out.println("Computation time : " + (System.currentTimeMillis() - start) / 1000l + " sec");
		});
	}

	/**
	 * Getter
	 * @return the parameters
	 */
	public PanoramaUserParameters getParameters() {
		return parameters.get();
	}

	/**
	 * Getter
	 * @return the parameter's property
	 */
	public ObjectProperty<PanoramaUserParameters> parametersProperty() {
		return parameters;
	}

	/**
	 * Setter
	 * @param parameters the parameters to assign
	 */
	public void setParameters(PanoramaUserParameters parameters) {
		this.parameters.set(parameters);
	}

	/**
	 * Getter
	 * @return the panorama
	 */
	public Panorama getPanorama() {
		return panorama.get();
	}

	/**
	 * Getter
	 * @return the
	 */
	public ReadOnlyObjectProperty<Panorama> panoramaProperty() {
		return panorama;
	}

	public Image getImage() {
		return image.get();
	}

	public ReadOnlyObjectProperty<Image> imageProperty() {
		return image;
	}

	public ObservableList<Node> getLabels() {
		return labels;
	}

	private void synchronizeParams() {

	}

}
