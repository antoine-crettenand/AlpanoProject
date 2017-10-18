package ch.epfl.alpano;

import java.util.Objects;

/**
 * Chaque instance de cette classe représente un intervalle unidimentionnel fermé.
 *
 * @author Julien Sahli
 * @date 27.02.2017
 */
public final class Interval1D {

    private final int includedFrom;
    private final int includedTo;

    /**
     * Crée un nouvel intervalle unidimentionnel
     *
     * @param includedFrom - Borne inférieure (inclue)
     * @param includedTo - Borne supérieure (inclue)
     * @throws IllegalArgumentException - Si la borne inférieure est supérieure à la borne supérieure
     */
    public Interval1D(int includedFrom, int includedTo) {
        Preconditions.checkArgument(includedFrom <= includedTo, "Borne  supérieure a une valeur inférieure à la borne inférieure.");
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    /**
     * Acesseur simples
     *
     * @return la variable correspondante
     */
    public int includedFrom() {
        return includedFrom;
    }
    public int includedTo() {
        return includedTo;
    }


    /**
     * Vérifie si une valeur appartient à l'intervalle
     *
     * @param v - la valeur à contrôler
     * @return true si v appartient à l'intervalle.
     */
    public boolean contains(int v) {
        return (v >= includedFrom) && (v <= includedTo);
    }

    /**
     * Taille de l'intervalle
     *
     * @return la taille de l'intervalle
     */
    public int size() {
        return 1 + includedTo - includedFrom;
    }

    /**
     * Taille de l'intersection entre cet intervalle et un autre
     *
     * @param that - l'autre intervalle
     * @return la taille de l'intersection de cet intervalle avec l'autre.
     */
    public int sizeOfIntersectionWith(Interval1D that) {
        return Math.max(0, Math.min(this.includedTo(), that.includedTo()) - Math.max(this.includedFrom(), that.includedFrom()) + 1);
    }

    /**
     * Union englobante de cet intervalle avec un autre
     *
     * @param that - l'autre intervalle
     * @return l'intervalle né de l'union englobante de cet intervalle et de l'autre.
     */
    public Interval1D boundingUnion(Interval1D that) {
        return new Interval1D(Math.min(this.includedFrom(), that.includedFrom()), Math.max(this.includedTo(), that.includedTo()));
    }

    /**
     * Contrôle si cet intervalle est unionable avec un autre.
     *
     * @param that - l'autre intervalle
     * @return true si les deux intervalles sont unionables.
     */
    public boolean isUnionableWith(Interval1D that) {
        return this.boundingUnion(that).size() == (this.size() + that.size() - this.sizeOfIntersectionWith(that));
    }

    /**
     * Union de cet intervalle avec un autre
     *
     * @param that un intervalle unionable avec celui-ci
     * @return l'intervalle né de l'union de cet intervalle et de l'autre.
     * @throws IllegalArgumentException - Si les deux intervalles ne sont pas unionables.
     */
    public Interval1D union(Interval1D that) {
        Preconditions.checkArgument(this.isUnionableWith(that), "Intervalles non unionables");
        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO) {
        return (thatO != null) && (thatO instanceof Interval1D) && ((Interval1D) thatO).includedFrom() == this.includedFrom() && ((Interval1D) thatO).includedTo() == this.includedTo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    @Override
    public String toString() {
        return "[" + includedFrom() + ".." + includedTo() + "]";
    }
}
