package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;

/**
 * A one dimensional integer interval is a non-empty set of integers containing
 * every integer between two endpoints, a smallest integer (= minimum of the interval)
 * and a biggest integer (= maximum of the interval)
 * Example : [5,8] contains 5,6,7 and 8. Endpoints are 5 and 8
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */
public final class Interval1D {
    private final int includedFrom;
    private final int includedTo;

    /**
     * Integer interval
     *
     * @param includedFrom integer, minimum of the interval
     * @param includedTo   integer, maximum of the interval
     * @throws IllegalArgumentException if minimum > maximum
     */
    public Interval1D(int includedFrom, int includedTo) {

        Preconditions.checkArgument(includedFrom <= includedTo);
        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    public int includedFrom() {
        return includedFrom;
    }

    public int includedTo() {
        return includedTo;
    }

    /**
     * Compute if the interval contains given integer
     *
     * @param v given integer
     * @return true if so, false otherwise
     */
    public boolean contains(int v) {
        return (v >= includedFrom() && v <= includedTo());
    }

    /**
     * @return number of integer in the interval (or "size" of the interval)
     */
    public int size() {
        return includedTo() - includedFrom() + 1;
    }

    /**
     * @param that interval to intersect with
     * @return integer, the size of the intersection of the interval {this} and interval {@param that}
     */
    public int sizeOfIntersectionWith(Interval1D that) {
        int lowerBound = Math.max(includedFrom(), that.includedFrom());
        int upperBound = Math.min(includedTo(), that.includedTo());

		// if lowerBound > upperBound then the interval is empty
		if (lowerBound > upperBound)
			return 0;

        return new Interval1D(lowerBound, upperBound).size();

    }

    /**
     * Compute the bounding union of the two intervals {this} and {@param that}
     *
     * @param that interval
     * @return the union of the two intervals
     */
    public Interval1D boundingUnion(Interval1D that) {
        int lowerBound = Math.min(includedFrom(), that.includedFrom());
        int upperBound = Math.max(includedTo(), that.includedTo());

		return new Interval1D(lowerBound, upperBound);
	}

    /**
     * Compute if the two intervals are Unionable
     *
     * @param that second interval to union
     * @return false if the intervals are disjoint
     */
    public boolean isUnionableWith(Interval1D that) {
        return (size() + that.size() - sizeOfIntersectionWith(that)
                == boundingUnion(that).size());
    }

    /**
     * Compute the union of the two intervals {this} and {@param that}
     *
     * @param that interval, not null
     * @return interval union of intervals
     * @throws IllegalArgumentException if the intervals are not unionable
	 * @see #isUnionableWith(Interval1D)
	 */
    public Interval1D union(Interval1D that) {
        Preconditions.checkArgument(isUnionableWith(that));
        //if they are unionable then, the union and bounding union are the same
        return boundingUnion(that);

    }

    @Override
    public String toString() {
        Locale l = null;
        return String.format(l, "[%d..%d]", includedFrom(), includedTo());
    }

    @Override
    public boolean equals(Object thatO) {
        if (this == thatO)
            return true;
        if (thatO == null || getClass() != thatO.getClass())
            return false;

        Interval1D that = (Interval1D) thatO;

		return includedFrom() == that.includedFrom() && includedTo() == that
				.includedTo();
	}

    @Override
    public int hashCode() {

        return Objects.hash(includedFrom(), includedTo());
    }
}




