package ch.epfl.alpano.gui;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public final class Alpano extends Application {
	private final PanoramaParametersBean paraBean = new PanoramaParametersBean(PredefinedPanoramas.JUNGFRAU);
	private final PanoramaComputerBean computerBean = new PanoramaComputerBean(buildContinuousModel());
	private TextArea textArea = buildParametersInformation();
	private final static float KILOMETERS = 1000f;

	public static void main(String... args) {
		Application.launch(args);
	}

	@Override public void start(Stage primaryStage) {

		//Define Main Window
		final BorderPane root = new BorderPane(buildPanoramaImage(), null, null, buildUserParameters(), null);

		final Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Alpano");
		primaryStage.show();
	}

	private String formatValue(double value, boolean toDegrees, int precision) {
		return String.format((Locale) null, "%." + precision + "f", (toDegrees ? Math.toDegrees(value) : value));
	}

	/**
	 * Build Window {@code StackPane} with displayed Panorama
	 *
	 * @return built window, StackPane
	 */
	private StackPane buildPanoramaImage() {

		final ImageView panoView = new ImageView();
		panoView.fitWidthProperty().bind(paraBean.widthProperty());
		panoView.fitHeightProperty().bind(paraBean.heightProperty());
		panoView.imageProperty().bind(computerBean.imageProperty());
		panoView.preserveRatioProperty().setValue(true);
		panoView.smoothProperty().setValue(true);

		//Setup Displayed Informations on cursor
		panoView.setOnMouseMoved((event) -> {

			int sampling = paraBean.getSuperSamplingExponent();
			int x = (int) Math.round(Math.scalb(event.getX(), sampling));
			int y = (int) Math.round(Math.scalb(event.getY(), sampling));

			Panorama panorama = computerBean.getPanorama();
			double longitude = panorama.longitudeAt(x, y);
			double latitude = panorama.latitudeAt(x, y);
			double altitude = panorama.elevationAt(x, y);
			double distance = panorama.distanceAt(x, y) / KILOMETERS;
			double azimuth = new GeoPoint(computerBean.getPanorama().parameters().observerPosition().longitude(),
					computerBean.getPanorama().parameters().observerPosition().latitude())
					.azimuthTo(new GeoPoint(longitude, latitude));
			double elevation = computerBean.getParameters().panoramaParameters().altitudeForY(y);

			textArea.setText(
					"Position : " + formatValue(latitude, true, 4) + "°N " + formatValue(longitude, true, 4) + "°E"
							+ '\n' + "Distance : " + formatValue(distance, false, 1) + " km" + '\n' + "Altitude : "
							+ formatValue(altitude, false, 0) + " m" + '\n' + "Azimut : " + formatValue(azimuth,
							true,
							1) + "° " + "" + "(" + Azimuth.toOctantString(azimuth, "N", "E", "S", "W") + ")" + ""
							+ "     " + "Elévation : " + formatValue(elevation, true, 1) + "°");
		});

		//Setup Displayed Open Maps Navigator on click
		panoView.setOnMouseClicked((event -> {
			int sampling = paraBean.getSuperSamplingExponent();
			int x = (int) Math.round(Math.scalb(event.getX(), sampling)); //getX() return the x position on
			// panoramaParameters
			int y = (int) Math.round(Math.scalb(event.getY(), sampling));

			Panorama panorama = computerBean.getPanorama();
			double lambda = Math.toDegrees(panorama.longitudeAt(x, y));
			double phi = Math.toDegrees(panorama.latitudeAt(x, y));
			String lambdaString = String.format((Locale) null, "%.2f", lambda);
			String phiString = String.format((Locale) null, "%.2f", phi);

			String qy = "mlat=" + phiString + "&mlon=" + lambdaString;
			String fg = "map=15/" + phiString + "/" + lambdaString;

			URI osmURI;
			try {
				osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
				java.awt.Desktop.getDesktop().browse(osmURI);
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}

		}));

		//Setup labels and labels.properties of the displayed Summits
		Pane labelsPane = new Pane();
		labelsPane.setMouseTransparent(true);
		labelsPane.prefWidthProperty().bind(paraBean.widthProperty());
		labelsPane.prefHeightProperty().bind(paraBean.heightProperty());
		Bindings.bindContent(labelsPane.getChildren(), computerBean.getLabels());

		StackPane panoGroup = new StackPane(new StackPane(panoView), labelsPane);
		ScrollPane panoScrollPane = new ScrollPane(panoGroup);


		//Setup Text and Screen to display if parameters are changed
		//TEXT
		Text updateText = new Text(
				"Les paramètres du panorama ont changé. \n Cliquez ici pour mettre le " + "dessin à jour");
		updateText.setFont(new Font(40));
		updateText.setTextAlignment(TextAlignment.CENTER);
		//SCREEN
		StackPane updateNotice = new StackPane(updateText);
		updateNotice.setVisible(false);
		updateNotice.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		updateNotice.setOpacity(0.9);
		//Setup displaying condition of the screen + text
		BooleanExpression parametersAreNotSame = paraBean.parametersProperty()
				.isNotEqualTo(computerBean.parametersProperty());
		updateNotice.visibleProperty().bind(parametersAreNotSame);
		//Setup computing the new Panorama on click after modifying parameters/When updateNotice is visible
		updateNotice.setOnMouseClicked((event -> {
			computerBean.setParameters(paraBean.getParameters());
		}));

		//Window displaying the whole
		StackPane panoPane = new StackPane(panoScrollPane, updateNotice);

		return panoPane;
	}

	/**
	 * Build Window {@code GridPane} displaying parameters of the Panorama and Informations on mouse
	 *
	 * @return GridPane, built window
	 */
	private GridPane buildUserParameters() {

		GridPane paramsGrid = new GridPane();

		//Define Labels and TextFields foreach parameter
		Label longitude = new Label(" Longitude (°) : ");
		TextField longitudeField = new TextField();
		setTextFieldAndBinding(longitudeField, paraBean.observerLongitudeProperty(), 7, 4);

		Label latitude = new Label(" Latitude (°) : ");
		TextField latitudeField = new TextField();
		setTextFieldAndBinding(latitudeField, paraBean.observerLatitudeProperty(), 7, 4);

		Label elevation = new Label(" Altitude (m) : ");
		TextField elevationField = new TextField();
		setTextFieldAndBinding(elevationField, paraBean.observerElevationProperty(), 4, 0);

		Label azimuth = new Label(" Azimuth (°) : ");
		TextField azimuthField = new TextField();
		setTextFieldAndBinding(azimuthField, paraBean.centerAzimuthProperty(), 3, 0);

		Label horizontalView = new Label(" Angle de vue (°) : ");
		TextField horizontalViewField = new TextField();
		setTextFieldAndBinding(horizontalViewField, paraBean.horizontalFieldOfViewProperty(), 3, 0);

		Label distance = new Label(" Visibilité (km) : ");
		TextField distanceField = new TextField();
		setTextFieldAndBinding(distanceField, paraBean.maxDistanceProperty(), 3, 0);

		Label width = new Label(" Largeur (px) : ");
		TextField widthField = new TextField();
		setTextFieldAndBinding(widthField, paraBean.widthProperty(), 4, 0);

		Label height = new Label(" Hauteur (px) : ");
		TextField heightField = new TextField();
		setTextFieldAndBinding(heightField, paraBean.heightProperty(), 4, 0);

		Label sampling = new Label(" Suréchantillonnage : ");
		StringConverter converter = new LabeledListStringConverter("non", "2x", "4x");
		ChoiceBox<Integer> samplingBox = new ChoiceBox<>(FXCollections.observableArrayList(0, 1, 2));
		samplingBox.setConverter(converter);
		samplingBox.valueProperty().bindBidirectional(paraBean.superSamplingExponentProperty());

		paramsGrid.addColumn(1, latitude, azimuth, width);
		paramsGrid.addColumn(2, latitudeField, azimuthField, widthField);
		paramsGrid.addColumn(3, longitude, horizontalView, height);
		paramsGrid.addColumn(4, longitudeField, horizontalViewField, heightField);
		paramsGrid.addColumn(5, elevation, distance, sampling);
		paramsGrid.addColumn(6, elevationField, distanceField, samplingBox);
		paramsGrid.add(textArea, 7, 0, 1, 3);

		paramsGrid.setVgap(5);
		paramsGrid.setHgap(10);

		return paramsGrid;
	}

	private void setTextFieldAndBinding(TextField field, Property<Integer> property, int columns, int precision) {

		//SETTINGS
		FixedPointStringConverter stringConverter = new FixedPointStringConverter(precision);
		TextFormatter<Integer> formatter = new TextFormatter<>(stringConverter);
		field.setAlignment(Pos.BASELINE_RIGHT);
		field.setPrefColumnCount(columns);
		field.setTextFormatter(formatter);

		//BINDING TO PARAMBEAN
		formatter.valueProperty().bindBidirectional(property);

	}

	private TextArea buildParametersInformation() {

		TextArea data = new TextArea();

		data.setPrefRowCount(4);
		data.setPrefColumnCount(20);
		data.setEditable(false);

		return data;
	}

	private ContinuousElevationModel buildContinuousModel() {

		DiscreteElevationModel model = null;
		for (int i = 6; i < 10; i++) {
			DiscreteElevationModel column = null;
			for (int j = 45; j < 47; j++) {
				DiscreteElevationModel dem = new HgtDiscreteElevationModel(new File("N" + j + "E" + "00" + i + ".hgt"));
				column = (column == null ? dem : column.union(dem));
			}
			model = (model == null ? column : model.union(column));
		}
		return new ContinuousElevationModel(model);

	}

}