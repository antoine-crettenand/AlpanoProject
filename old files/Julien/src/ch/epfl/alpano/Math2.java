package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;


/**
 * Cette interface sert à compléter la classe java.lang.Math .
 *
 * @author Julien Sahli
 * @date 21.02.2017
 */
public interface Math2 {
    double PI2 = 2 * Math.PI; //2*PI (Explicite)

    /**
     * Fonction carré.
     * @param x input.
     * @return x^2.
     */
    static double sq(double x) {
        return Math.pow(x, 2);
    }

    /**
     * Fonction reste de la division par défaut (modulo positif).
     * @param x numérateur.
     * @param y dénominateur.
     * @return x - y * (Math.floor(x / y)).
     */
    static double floorMod(double x, double y) {
        return x - y * (Math.floor(x / y));
    }

    /**
     * Fonction demi sinus verse
     * @param x input
     * @return sin(x/2)^2
     */
    static double haversin(double x) {
        return sq(Math.sin(x / 2));
    }

    /**
     * Fonction différence angulaire
     * @param a1 angle 1
     * @param a2 angle 2
     * @return ((a2 - a1 + pi)modF 2pi)-pi
     */
    static double angularDistance(double a1, double a2) {
        return (floorMod((a2 - a1 + Math.PI), PI2)) - Math.PI;
    }

    /**
     * Fonction Interpolation linéaire
     * @param y0 f(0)
     * @param y1 f(1)
     * @param x coordonnée en x du point à calculer
     * @return f(x)
     */
    static double lerp(double y0, double y1, double x) {
        return y0 + (y1 - y0) * x;
    }

    /**
     * Fonction interpolation billinéaire
     * @param z00 f(0,0)
     * @param z10 f(1,0)
     * @param z01 f(0,1)
     * @param z11 f(1,1)
     * @param x coordonnée en x du point à calculer
     * @param y coordonnée en y du point à calculer
     * @return f(x,y)
     */
    static double bilerp(double z00, double z10, double z01, double z11, double x, double y) {
        double z1 = lerp(z00, z10, x);
        double z2 = lerp(z01, z11, x);
        return lerp(z1, z2, y);
    }

    /**
     * Divise un intervalle en plusieurs sous-intervalles et retourne la borne inférieure du premier sous-intervalle contenant un 0 de la fonction continue.
     * @param f la fonction continue
     * @param minX borne inférieure (inclue)
     * @param maxX borne supérieure (inclue)
     * @param dX la taille des sous-intervalles
     * @return la borne inférieure du premier sous-intervalle contenant un 0 de la fonction.
     */
    static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX) {
        double currentMinX = minX;
        double currentMaxX = minX + dX;
        while (currentMaxX <= maxX) {
            if (f.applyAsDouble(currentMinX) * f.applyAsDouble(currentMaxX) <= 0) {
                return currentMinX;
            }
            currentMinX += dX;
            currentMaxX += dX;
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Avec la méthode de la dichotomie, détecte un 0 d'une fonction continue dans un intervalle donné
     * @param f la fonction continue
     * @param x1 borne inférieure (inclue)
     * @param x2 borne extérieure (inclue)
     * @param epsilon taille maximale de  l'intervalle réduit par dichotomie
     * @return la borne inférieure du nouvel intervalle calculé
     */
    static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon) { //TODO? : Contrôler que la fonction est continue
        if (f.applyAsDouble(x1) == 0) {
            return x1;
        }
        if (f.applyAsDouble(x2) == 0) {
            return x2;
        }
        Preconditions.checkArgument(f.applyAsDouble(x1) * f.applyAsDouble(x2) <= 0, "les bornes de l'intervlles sont de même signe.");
        double currentX1 = x1;
        double currentX2 = x2;
        double x;

        do {
            x = (currentX1 + currentX2) / 2;
            if (f.applyAsDouble(currentX1) * f.applyAsDouble(x) > 0) {
                currentX1 = x;
            } else {
                currentX2 = x;
            }
        } while (Math.abs(currentX2 - currentX1) > epsilon);
        return x;
    }

}
