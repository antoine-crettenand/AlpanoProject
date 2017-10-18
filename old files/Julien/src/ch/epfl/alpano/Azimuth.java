package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * Cette interface est utilisée pour traiter les azimuts, en radians.
 *
 * @author Julien Sahli
 * @date 21.02.2017
 */
public interface Azimuth {

    /**
     * détermine si l'azimut donné est canonique ou non
     * @param azimuth input
     * @return true si l'azimut est canonique, false sinon
     */
    static boolean isCanonical(double azimuth) {
        return (0 <= azimuth) && (azimuth < PI2);
    }

    /**
     * convertit l'angle ou l'azimut dans l'angle ou l'azimut canonique correspondant.
     * @param azimuth input
     * @return angle canonique correspondant
     */
    static double canonicalize(double azimuth) {
        return floorMod(azimuth, PI2);
    }

    /**
     * Convertit un azimut canonique en angle mathématique canonique
     * @param azimuth input
     * @return angle mathématique canonique correspondant
     */
    static double toMath(double azimuth) {
        checkArgument(isCanonical(azimuth), "L'argument n'est pas un azimut canonique");
        return floorMod(-azimuth, PI2);
    }

    /**
     * Convertit un angle mathématique canonique en azimut canonique
     * @param azimuth input
     * @return azimut canonique correspondant
     */
    static double fromMath(double azimuth) {
        checkArgument(isCanonical(azimuth), "L'argument n'est pas un azimut canonique");
        return floorMod(-azimuth, PI2);
    }

    /**
     * Retourne l'octant dans lequel se trouve l'azimuth donné, avec moyen de personnaliser les chaînes correspondant aux différents octants.
     *
     * @param azimuth l'azimuth dont l'octant doit être déterminé
     * @param n       La chaîne de caractères correspondant au nord
     * @param e       La chaîne de caractères correspondant à l'est
     * @param s       La chaîne de caractères correspondant au sud
     * @param w       La chaîne de caractères correspondant à l'ouest
     * @return Chaîne contenant l'octant contenant l'azimuth donné
     */
    static String toOctantString(double azimuth, String n, String e, String s, String w) {
        checkArgument(isCanonical(azimuth), "L'argument n'est pas un azimut canonique");
        if (azimuth < Math.PI * 1 / 8) {
            return n;
        } else if (azimuth < Math.PI * 3 / 8) {
            return n + e;
        } else if (azimuth < Math.PI * 5 / 8) {
            return e;
        } else if (azimuth < Math.PI * 7 / 8) {
            return s + e;
        } else if (azimuth < Math.PI * 9 / 8) {
            return s;
        } else if (azimuth < Math.PI * 11 / 8) {
            return s + w;
        } else if (azimuth < Math.PI * 13 / 8) {
            return w;
        } else if (azimuth < Math.PI * 15 / 8) {
            return n + w;
        } else {
            return n;
        }
    }

        /**
         * Retourne l'octant dans lequel se trouve l'azimuth donné sous la forme des abréviations francophones usuelles (N,E,S,O)
         * @param azimuth l'azimuth dont l'octnat doit être déterminé
         * @return Chaîne contenant l'octant contenant l'azimuth donné
         */

    static String toOctantString(double azimuth) {
        return toOctantString(azimuth, "N", "E", "S", "O");
    }
}
