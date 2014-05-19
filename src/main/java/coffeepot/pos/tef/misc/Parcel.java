/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.misc;

import java.math.BigDecimal;
import org.joda.time.DateTime;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class Parcel {

    private DateTime date;
    private BigDecimal value;
    private Long nsu;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getNsu() {
        return nsu;
    }

    public void setNsu(Long nsu) {
        this.nsu = nsu;
    }
}
