package transport.model;

public final class SleepingCar extends PassengerCar {
    private static final int DEFAULT_COMPARTMENTS = 9;
    private static final int DEFAULT_BED_COUNT = 36;

    private final int compartments;
    private final int bedCount;

    public SleepingCar(int number, double weight, int comfortLevel, int compartments,
                       int bedCount, int passengerCount, double luggageWeight) {
        // Передаем bedCount в качестве вместимости (capacity) родителя.
        // PassengerCar сам провалидирует passengerCount и ограничит его до bedCount.
        super(
                number,
                weight,
                comfortLevel,
                positive(bedCount, DEFAULT_BED_COUNT),
                passengerCount,
                luggageWeight
        );

        this.compartments = positive(compartments, DEFAULT_COMPARTMENTS);
        this.bedCount = positive(bedCount, DEFAULT_BED_COUNT);
    }

    public int getCompartments() {
        return compartments;
    }

    public int getBedCount() {
        return bedCount;
    }

    @Override
    public String getInfo() {
        return baseInfo("Спальный вагон") +
                ", купе " + compartments +
                ", спальных мест " + bedCount +
                passengerInfo();
    }
}