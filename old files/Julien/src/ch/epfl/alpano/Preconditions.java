package ch.epfl.alpano;

/**
 * Cette interface est utilisée pour contrôler que les imputs d'une méthode satifont des conditions.
 *
 * @author Julien Sahli
 * @date 21.02.2017
 */
public interface Preconditions {

    /**
     * Renvoie une IllegalArgumentException si une condition n'est pas respectée.
     * @param b la condition qui doit être vraie.
     * @param message le message attaché à l'IllegalArgumentException (facultatif).
     */
    static void checkArgument(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * Renvoie une IllegalArgumentException si une condition n'est pas respectée.
     * @param b la condition qui doit être vraie.
     */
    static void checkArgument(boolean b) {
        checkArgument(b, "no message");
    }
}
