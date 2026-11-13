package transport.model;

import java.util.Locale;

public final class Locomotive extends Railcar implements Comparable<Railcar> {
    private static final double DEFAULT_POWER = 750.0;

    private final double power;

    public Locomotive(int number, double weight, int comfortLevel, double power) {
        super(number, weight, comfortLevel);
        this.power = positive(power, DEFAULT_POWER);
    }

    public double getPower() {
        return power;
    }

    @Override
    public int compareTo(Railcar other) {
        return compareComfort(other);
    }

    @Override
    public String getInfo() {
        return baseInfo("Локомотив") + String.format(Locale.ROOT, ", мощность %.1f кВт", power);
    }
}
