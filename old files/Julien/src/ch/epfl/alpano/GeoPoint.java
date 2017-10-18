package ch.epfl.alpano;

import java.util.Locale;

import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Classe représentant un point à la surface de la terre
 *
 * @author Julien Sahli
 * @date 22.02.2017
 */
public final class GeoPoint {
    private final double longitude;
    private final double latitude;

    /**
     * Crée une nouvelle instance de Geopoint
     *
     * @param longitude - Longitude du point, en radians
     * @param latitude - Latitude du point, en radians
     * @throws IllegalArgumentException - si la valeur absolue de la longitude excède PI ou si celle de la latitude excède PI/2
     */
    public GeoPoint(double longitude, double latitude) {
        checkArgument(longitude <= Math.PI && longitude >= -Math.PI && latitude <= Math.scalb(Math.PI, -1) && latitude >= -Math.scalb(Math.PI, -1), "La longitude n'appartient pas à [-pi ; pi] ou la latitude n'appartient pas à [-pi/2 ; pi/2]");
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Acesseur simples
     *
     * @return la variable correspondante
     */
    public double longitude() {
        return longitude;
    }

    public double latitude() {
        return latitude;
    }

    /**
     * Calcule l'azimut d'un point visé par rapport au point courant
     *
     * @param that point visé
     * @return azimuth du point visé par rapport au point courant
     */
    public double azimuthTo(GeoPoint that) {
        return Azimuth.fromMath(Azimuth.canonicalize(Math.atan((Math.sin(this.longitude() - that.longitude()) * Math.cos(that.latitude())) / (Math.cos(this.latitude()) * Math.sin(that.latitude()) - Math.sin(this.latitude()) * Math.cos(that.latitude()) * Math.cos(this.longitude() - that.longitude())))));
    }

    /**
     * Calcula la distance entre le point courant et un autre point
     *
     * @param that l'autre point
     * @return distance entre ce point et l'autre point
     */
    public double distanceTo(GeoPoint that) {
        return Distance.EARTH_RADIUS * 2 * Math.asin(Math.sqrt(Math2.haversin(this.latitude() - that.latitude()) + Math.cos(this.latitude()) * Math.cos(that.latitude) * Math2.haversin(this.longitude() - that.longitude())));
    }

    @Override
    public String toString() {
        Locale l = null;
        return String.format(l, "(%.4f,%.4f)", Math.toDegrees(longitude), Math.toDegrees(latitude));
    }

}
