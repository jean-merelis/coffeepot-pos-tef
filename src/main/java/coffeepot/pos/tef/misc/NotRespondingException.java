/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.misc;

/**
 * Exceção lançada quando o gerenciador padrão não confirma o recebimento do
 * comando enviado.
 *
 * @author Jeandeson O. Merelis
 */
public class NotRespondingException extends Exception {

    /**
     * Creates a new instance of
     * <code>NotRespondingException</code> without detail message.
     */
    public NotRespondingException() {
    }

    /**
     * Constructs an instance of
     * <code>NotRespondingException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NotRespondingException(String msg) {
        super(msg);
    }
}
