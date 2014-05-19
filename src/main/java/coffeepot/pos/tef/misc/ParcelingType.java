/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.misc;

/**
 * Tipo de parcelamento.
 *
 * @author Jeandeson O. Merelis
 */
public enum ParcelingType {

    /**
     * Parcelado pela loja.
     */
    STORE,
    /**
     * Parcelado pela administradora.
     */
    ADM;
    private static ParcelingType[] values = null;

    public static ParcelingType fromInt(int i) {
        if (ParcelingType.values == null) {
            ParcelingType.values = ParcelingType.values();
        }
        return ParcelingType.values[i];
    }
}
