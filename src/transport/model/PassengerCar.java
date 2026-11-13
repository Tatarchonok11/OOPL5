package transport.model;

import java.util.Locale;

public class PassengerCar extends Railcar implements PassengerCarrier, Comparable<Railcar> {
    private static final int DEFAULT_CAPACITY = 36;
    private static final int DEFAULT_PASSENGERS = 1;
    private static final double DEFAULT_LUGGAGE = 0.0;

    private final int capacity;
    private final int passengerCount;
    private final double luggageWeight;

    public PassengerCar(int number, double weight, int comfortLevel, int capacity,
                        int passengerCount, double luggageWeight) {
        super(number, weight, comfortLevel);

        this.capacity = positive(capacity, DEFAULT_CAPACITY);

        int validPassengers = positive(passengerCount, DEFAULT_PASSENGERS);
        if (validPassengers > this.capacity) {
            System.out.printf("Число пассажиров (%d) превышает вместимость (%d). Установлен максимум.%n",
                    validPassengers, this.capacity);
            validPassengers = this.capacity;
        }
        this.passengerCount = validPassengers;

        this.luggageWeight = positive(luggageWeight, DEFAULT_LUGGAGE);
    }

    public final int getCapacity() {
        return capacity;
    }

    @Override
    public final int getPassengerCapacity() {
        return passengerCount;
    }

    @Override
    public final double getLuggageCapacity() {
        return luggageWeight;
    }

    @Override
    public final int compareTo(Railcar other) {
        return compareComfort(other);
    }

    @Override
    public String getInfo() {
        return baseInfo("Пассажирский вагон") + passengerInfo();
    }

    protected final String passengerInfo() {
        return String.format(Locale.ROOT, ", пассажиров %d / %d, багаж %.1f кг",
                passengerCount, capacity, luggageWeight);
    }
}
