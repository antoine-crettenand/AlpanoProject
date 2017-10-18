package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.Preconditions;

import java.util.function.DoubleUnaryOperator;

/**
 * A functional interface representing a Channel Painter.
 * A Channel Painter map a value {@code float} on every pixel of a two-dimensional grid.
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 */

@FunctionalInterface public interface ChannelPainter {

	/**
	 * @param x x-axis
	 * @param y y-axis
	 * @return the value of the canal at (x ,y)
	 */
	float valueAt(int x, int y);

	/**
	 * Given a {@code Panorama}, creates a {@code ChannelPainter} that calculates the distance
	 * between the given point and his furthest neighbour (edge-to-edge)
	 *
	 * @param p instance of {@code Panorama}
	 * @return the value of ChannelPainter
	 */
	static ChannelPainter maxDistanceToNeighbors(Panorama p) {
		return (x, y) -> Math.max(Math.max(p.distanceAt(x + 1, y, 0), p.distanceAt(x - 1, y, 0)),
				Math.max(p.distanceAt(x, y + 1, 0), p.distanceAt(x, y - 1, 0))) - p.distanceAt(x, y);
	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Adding a float to the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int)
	 *
	 * @param f the float to add
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter add(float f) {
		return (x, y) -> valueAt(x, y) + f;

	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * but subtracting a float to the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int)
	 *
	 * @param f the float to subtract
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter sub(float f) {
		return (x, y) -> valueAt(x, y) - f;

	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Multiplying the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int) by a float
	 *
	 * @param f the float that multiplies
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter mul(float f) {
		return (x, y) -> valueAt(x, y) * f;

	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Dividing the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int) by a float
	 *
	 * @param f the float that divides (not null !)
	 * @return the new ChannelPainter
	 * @throws IllegalArgumentException if f = 0, because divide by 0 is forbidden.
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter div(float f) {
		Preconditions.checkArgument(f != 0, "An error occurred : attempt to divide by 0.");
		return (x, y) -> valueAt(x, y) / f;

	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Applying a function to the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int)
	 *
	 * @param f the function
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter map(DoubleUnaryOperator f) {
		return (x, y) -> (float) f.applyAsDouble((double) valueAt(x, y));
	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Changing the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int) to 1 - its previous value
	 *
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter invert() {
		return (x, y) -> 1 - valueAt(x, y);
	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Changing the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int) to 0 if <0 or to 1 if >1
	 *
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter clamp() {
		return (x, y) -> Math.max(0, Math.min(1, valueAt(x, y)));
	}

	/**
	 * Generates a new {@code ChannelPainter}, based on current instance of {@code ChannelPainter}
	 * Changing the result of ch.epfl.alpano.gui.ChannelPainter#valueAt(int, int) its previous value modulus 1
	 *
	 * @return the new ChannelPainter
	 * @see #valueAt(int, int)
	 */
	default ChannelPainter cycle() {
		return (x, y) -> valueAt(x, y) % 1;
	}

}
