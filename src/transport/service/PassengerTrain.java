package transport.service;

import transport.model.LuggageCarrier;
import transport.model.PassengerCarrier;
import transport.model.Railcar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Агрегирует подвижной состав, вычисляя характеристики через интерфейсы возможностей. */
public final class PassengerTrain implements PassengerCarrier, LuggageCarrier {
    private final String routeId;
    private final List<Railcar> railcars = new ArrayList<>();

    public PassengerTrain(String routeId) {
        if (routeId == null || routeId.isBlank()) {
            throw new IllegalArgumentException("Маршрут не должен быть пустым");
        }
        this.routeId = routeId;
    }

    public String getRouteId() { return routeId; }

    public void addCar(Railcar car) {
        Objects.requireNonNull(car, "Вагон не должен быть null");
        if (railcars.stream().anyMatch(existing -> existing.getNumber() == car.getNumber())) {
            throw new IllegalArgumentException("Вагон с таким номером уже есть в составе");
        }
        railcars.add(car);
    }

    /** Неизменяемый снимок защищает коллекцию от изменения извне. */
    public List<Railcar> getCars() { return List.copyOf(railcars); }

    @Override
    public int getPassengerCount() {
        int total = 0;
        for (Railcar car : railcars) {
            if (car instanceof PassengerCarrier carrier) {
                total = Math.addExact(total, carrier.getPassengerCount());
            }
        }
        return total;
    }

    @Override
    public double getLuggageWeight() {
        double total = 0;
        for (Railcar car : railcars) {
            if (car instanceof LuggageCarrier carrier) {
                total += carrier.getLuggageWeight();
            }
        }
        return total;
    }

    // Названия операций из исходной диаграммы оставлены как удобные делегирующие методы.
    public int getTotalPassengers() { return getPassengerCount(); }
    public double getTotalLuggage() { return getLuggageWeight(); }

    /** Использует Comparable<Railcar>; при равном комфорте порядок сохраняется. */
    public void sortByComfort() { railcars.sort(Comparator.naturalOrder()); }

    /** Возвращает отсортированный снимок, не меняя исходный состав. */
    public List<Railcar> sortedCars(Comparator<? super Railcar> comparator) {
        Objects.requireNonNull(comparator, "Компаратор не должен быть null");
        List<Railcar> sorted = new ArrayList<>(railcars);
        sorted.sort(comparator);
        return List.copyOf(sorted);
    }

    /** Границы включены. Непассажирские вагоны не попадают даже в диапазон [0, 0]. */
    public List<Railcar> findByPassengerRange(int min, int max) {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Диапазон должен удовлетворять 0 <= min <= max");
        }
        return railcars.stream()
                .filter(car -> car instanceof PassengerCarrier carrier
                        && carrier.getPassengerCount() >= min && carrier.getPassengerCount() <= max)
                .toList();
    }
}
