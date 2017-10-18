package ch.epfl.alpano;

/**
 * Set of methods checking for validity of input
 */

public interface Preconditions  {

	/**
	 * throw IllegalArgumentException if {@param b} is false, otherwise do nothing
	 */
	static void checkArgument(boolean b) throws IllegalArgumentException{
		if (!b) throw new IllegalArgumentException();
	}

	/**
	 * throw IllegalArgumentException with {@param message} if {@param b} is false
	 */
	static void checkArgument(boolean b, String message){
		if (!b) throw new IllegalArgumentException(message);
	}
}
