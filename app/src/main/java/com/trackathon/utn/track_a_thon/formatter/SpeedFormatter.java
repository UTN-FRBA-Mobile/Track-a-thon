package com.trackathon.utn.track_a_thon.formatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by erwincdl on 7/1/17.
 */

public class SpeedFormatter {
    static public String format(Float speed){
        DecimalFormat speedFormat = new DecimalFormat("#.## m/s");
        speedFormat.setRoundingMode(RoundingMode.CEILING);
        return speedFormat.format(speed);
    }
}
