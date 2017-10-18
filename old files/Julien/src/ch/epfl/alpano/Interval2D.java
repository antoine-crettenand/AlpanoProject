package ch.epfl.alpano;

import java.util.Objects;

/**
 * Chaque instance de cette classe représente un intervalle bidimentionnel fermé.
 *
 * @author Julien Sahli
 * @date 28.02.2017
 */
public final class Interval2D {
    private final Interval1D iX;
    private final Interval1D iY;

    /**
     * Crée un nouvel intervalle bidimentionnel
     *
     * @param iX - premier intervalle
     * @param iY - deuxième intervalle
     * @throws IllegalArgumentException - Si un des intervalles est nul
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        if (iX == null && iY == null) {
            throw new NullPointerException("Un des intervalles est null");
        }
        this.iX = iX;
        this.iY = iY;

    }

    /**
     * Acesseurs simples
     *
     * @return la variable correspondante
     */
    public Interval1D iX() {
        return iX;
    }
    public Interval1D iY() {
        return iY;
    }

    /**
     * Vérifie si deux valeurs appartient à l'intervalle 2D
     *
     * @param x - la valeur en X à contrôler
     * @param y - la valeur en Y à contrôler
     * @return true si v appartient à l'intervalle.
     */
    public boolean contains(int x, int y) {
        return (iX.contains(x) && iY.contains(y));
    }

    /**
     * Calcule la taille de l'intervalle 2D
     *
     * @return la taille de l'intervalle 2D
     */
    public int size() {
        return iX.size() * iY.size();
    }

    /**
     * Calcule la taille de cet intervalle 2D avec un autre intervalle 2D
     *
     * @param that - l'autre intervalle 2D
     * @return la taille de l'intersection des deux intervalles 2D.
     */
    public int sizeOfIntersectionWith(Interval2D that) {
        return this.iX().sizeOfIntersectionWith(that.iX()) * this.iY().sizeOfIntersectionWith(that.iY());
    }

    /**
     * Union englobante de cet intervalle 2D avec un autre.
     *
     * @param that - l'autre intervalle 2D
     * @return l'intervalle 2D né de l'union englobante entre les deux intervalles 2D
     */
    public Interval2D boundingUnion(Interval2D that) {
        return new Interval2D(this.iX().boundingUnion(that.iX()), this.iY().boundingUnion(that.iY()));
    }

    /**
     * Contrôle si cet intervalle 2D est unionable avec un autre.
     *
     * @param that - l'autre intervalle 2D
     * @return true si les deux intervalles sont unionables
     */
    public boolean isUnionableWith(Interval2D that) {
        return (this.boundingUnion(that).size() == (this.size() + that.size() - this.sizeOfIntersectionWith(that)));
    }

    /**
     * Union de cet intervalle 2D avec un autre.
     *
     * @param that - l'autre intervalle 2D
     * @return l'intervalle 2D né de l'union entre les deux intervalles 2D
     * @throws IllegalArgumentException - Si les intervalles ne sont pas unionables
     */
    public Interval2D union(Interval2D that) {
        Preconditions.checkArgument(this.isUnionableWith(that), "Les intervalles ne sont pas unionables");
        return this.boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO) {
        return (thatO != null) && (thatO instanceof Interval2D) && this.iX().equals(((Interval2D) thatO).iX()) && this.iY().equals(((Interval2D) thatO).iY());
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}
        if (o == null || getClass() != o.getClass()){ return false;}

        Interval2D that = (Interval2D) o;

        if (iX != null ? !iX.equals(that.iX) : that.iX != null) return false;
        return iY != null ? iY.equals(that.iY) : that.iY == null;

    }*/

    @Override
    public int hashCode() {
        return Objects.hash(iX(), iY());
    }

    @Override
    public String toString() {
        return iX.toString() + "x" + iY.toString();
    }

}
