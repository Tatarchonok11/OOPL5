package transport.model;

import java.util.Locale;

/** Грузовой вагон, используемый для перевозки багажа пассажирского поезда. */
public final class CargoCar extends Railcar implements LuggageCarrier {
    private static final double DEFAULT_MAX_LOAD = 2500.0;
    private static final double DEFAULT_LUGGAGE_WEIGHT = 1000.0;

    private final double maxLoad;
    private final double luggageWeight;

    public CargoCar(int number, double weight, int comfortLevel, double maxLoad, double luggageWeight) {
        super(number, weight, comfortLevel);

        double validMaxLoad = positive(maxLoad, DEFAULT_MAX_LOAD);
        double validLuggage = positive(luggageWeight, DEFAULT_LUGGAGE_WEIGHT);

        this.maxLoad = validMaxLoad;
        this.luggageWeight = Math.min(validLuggage, validMaxLoad);
    }

    public double getMaxLoad() {
        return maxLoad;
    }

    @Override
    public double getLuggageWeight() {
        return luggageWeight;
    }

    @Override
    public String getInfo() {
        return baseInfo("Грузовой вагон")
                + String.format(Locale.ROOT, ", багаж %.1f / %.1f кг", luggageWeight, maxLoad);
    }
}