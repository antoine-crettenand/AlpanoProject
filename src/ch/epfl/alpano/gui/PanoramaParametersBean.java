package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import static javafx.application.Platform.runLater;

/**
 * Created by AntoineCrettenand on 04/05/2017.
 */
public class PanoramaParametersBean {

	private ObjectProperty<PanoramaUserParameters> parameters;
	private ObjectProperty<Integer> observerLongitude;
	private ObjectProperty<Integer> observerLatitude;
	private ObjectProperty<Integer> observerElevation;
	private ObjectProperty<Integer> centerAzimuth;
	private ObjectProperty<Integer> horizontalFieldOfView;
	private ObjectProperty<Integer> maxDistance;
	private ObjectProperty<Integer> width;
	private ObjectProperty<Integer> height;
	private ObjectProperty<Integer> superSamplingExponent;

	public PanoramaParametersBean(PanoramaUserParameters params){
		this.parameters = new SimpleObjectProperty<>(params);
		observerLongitude = setSynchronizer(new SimpleObjectProperty<>(params.observerLongitude()));
		observerLatitude = setSynchronizer(new SimpleObjectProperty<>(params.observerLatitude()));
		observerElevation = setSynchronizer(new SimpleObjectProperty<>(params.observerElevation()));
		centerAzimuth = setSynchronizer(new SimpleObjectProperty<>(params.centerAzimuth()));
		horizontalFieldOfView = setSynchronizer(new SimpleObjectProperty<>(params.horizontalFieldOfView()));
		maxDistance = setSynchronizer(new SimpleObjectProperty<>(params.maxDistance()));
		width = setSynchronizer(new SimpleObjectProperty<>(params.width()));
		height = setSynchronizer(new SimpleObjectProperty<>(params.height()));
		superSamplingExponent = setSynchronizer(new SimpleObjectProperty<>(params.superSamplingExponent()));
	}

	/**
	 * List of GETTERS
	 */

	public PanoramaUserParameters getParameters() {
		return parameters.get();
	}

	public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty() {
		return parameters;
	}

	public Integer getObserverLongitude() {
		return observerLongitude.get();
	}

	public ObjectProperty<Integer> observerLongitudeProperty() {
		return observerLongitude;
	}

	public Integer getObserverLatitude() {
		return observerLatitude.get();
	}

	public ObjectProperty<Integer> observerLatitudeProperty() {
		return observerLatitude;
	}

	public Integer getObserverElevation() {
		return observerElevation.get();
	}

	public ObjectProperty<Integer> observerElevationProperty() {
		return observerElevation;
	}

	public Integer getCenterAzimuth() {
		return centerAzimuth.get();
	}

	public ObjectProperty<Integer> centerAzimuthProperty() {
		return centerAzimuth;
	}

	public Integer getHorizontalFieldOfView() {
		return horizontalFieldOfView.get();
	}

	public ObjectProperty<Integer> horizontalFieldOfViewProperty() {
		return horizontalFieldOfView;
	}

	public Integer getMaxDistance() {
		return maxDistance.get();
	}

	public ObjectProperty<Integer> maxDistanceProperty() {
		return maxDistance;
	}

	public Integer getWidth() {
		return width.get();
	}

	public ObjectProperty<Integer> widthProperty() {
		return width;
	}

	public Integer getHeight() {
		return height.get();
	}

	public ObjectProperty<Integer> heightProperty() {
		return height;
	}

	public Integer getSuperSamplingExponent() {
		return superSamplingExponent.get();
	}

	public ObjectProperty<Integer> superSamplingExponentProperty() {
		return superSamplingExponent;
	}

	/**
	 * List of SETTERS
	 */

	public void setObserverLongitude(Integer observerLongitude) {
		this.observerLongitude.set(observerLongitude);
	}

	public void setObserverLatitude(Integer observerLatitude) {
		this.observerLatitude.set(observerLatitude);
	}

	public void setObserverElevation(Integer observerElevation) {
		this.observerElevation.set(observerElevation);
	}

	public void setCenterAzimuth(Integer centerAzimuth) {
		this.centerAzimuth.set(centerAzimuth);
	}

	public void setHorizontalFieldOfView(Integer horizontalFieldOfView) {
		this.horizontalFieldOfView.set(horizontalFieldOfView);
	}

	public void setMaxDistance(Integer maxDistance) {
		this.maxDistance.set(maxDistance);
	}

	public void setWidth(Integer width) {
		this.width.set(width);
	}

	public void setHeight(Integer height) {
		this.height.set(height);
	}

	public void setSuperSamplingExponent(Integer superSamplingExponent) {
		this.superSamplingExponent.set(superSamplingExponent);
	}

	private ObjectProperty<Integer> setSynchronizer(ObjectProperty<Integer> property){
		property.addListener((b, o, n) ->
				runLater(this::synchronizeParameters));
		return property;
	}

	private void synchronizeParameters() {

		PanoramaUserParameters p1 = new PanoramaUserParameters(getObserverLongitude(), getObserverLatitude(),
				getObserverElevation(), getCenterAzimuth(), getHorizontalFieldOfView(), getMaxDistance(), getWidth(),
				getHeight(), getSuperSamplingExponent());

		this.parameters.setValue(p1);

		setObserverLongitude(p1.observerLongitude());
		setObserverLatitude(p1.observerLatitude());
		setObserverElevation(p1.observerElevation());
		setCenterAzimuth(p1.centerAzimuth());
		setHorizontalFieldOfView(p1.horizontalFieldOfView());
		setMaxDistance(p1.maxDistance());
		setWidth(p1.width());
		setHeight(p1.height());
	}
}
