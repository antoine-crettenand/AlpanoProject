package ch.epfl.alpano;

/**
 * Cette interface est utilisée pour convertir des distances à la surface de la Terre de mètres en radians et vice-versa.
 *
 * @author Julien Sahli
 * @date 21.02.2017
 */
public interface Distance {

    double EARTH_RADIUS = 6371000; //Le Rayon de la terre


    /**
     * Retourne l'angle d'un arc de cercle dont on donne la longueur.
     * @param distanceInMeters longueur de l'arc à convertir.
     * @return angle de l'arc converti.
     */
    static double toRadians(double distanceInMeters) {
        return distanceInMeters / EARTH_RADIUS;
    }

    /**
     * Retourne la longueur d'un arc de cercle dont on donne la l'angle.
     * @param distanceInRadians angle de l'arc à convertir.
     * @return longueur de l'arc converti.
     */
    static double toMeters(double distanceInRadians) {
        return distanceInRadians * EARTH_RADIUS;
    }
}
