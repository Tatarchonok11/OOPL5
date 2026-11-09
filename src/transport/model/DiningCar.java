package transport.model;

public final class DiningCar extends PassengerCar {
    private static final int SEATS_PER_TABLE = 4;
    private static final int DEFAULT_TABLE_COUNT = 10;

    private final int tableCount;

    /** Стол рассчитан на четыре человека. */
    public DiningCar(int number, double weight, int comfortLevel, int tableCount,
                     int passengerCount, double luggageWeight) {
        // Проверяем tableCount сразу при передаче в super
        super(
                number,
                weight,
                comfortLevel,
                positive(tableCount, DEFAULT_TABLE_COUNT) * SEATS_PER_TABLE,
                Math.min(
                        positive(passengerCount, 0),
                        positive(tableCount, DEFAULT_TABLE_COUNT) * SEATS_PER_TABLE
                ),
                luggageWeight
        );

        this.tableCount = positive(tableCount, DEFAULT_TABLE_COUNT);
    }

    public int getTableCount() {
        return tableCount;
    }

    @Override
    public String getInfo() {
        return baseInfo("Вагон-ресторан") + ", столов " + tableCount + passengerInfo();
    }
}