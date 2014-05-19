/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.dispatch;

import coffeepot.pos.tef.dial.TefDial;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class ATV extends DispatchMessage{

    public ATV() {
        operation = TefDial.Operation.ATV;
    }
    
}
