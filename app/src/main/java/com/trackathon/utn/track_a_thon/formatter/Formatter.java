package com.trackathon.utn.track_a_thon.formatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Formatter {
    static public String format(Float speed, String format){
        DecimalFormat speedFormat = new DecimalFormat(format);
        speedFormat.setRoundingMode(RoundingMode.CEILING);
        return speedFormat.format(speed);
    }
}
