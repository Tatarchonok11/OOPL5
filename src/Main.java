import transport.model.*;
import transport.service.PassengerTrain;
import transport.sort.RailcarComfortComparator;
import transport.ui.ConsoleMenu;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PassengerTrain train = new PassengerTrain("Уфа — Москва");
        train.addCar(new Locomotive(1, 120, 0, 6000));
        train.addCar(new PassengerCar(2, 50, 2, 54, 54, 810));
        train.addCar(new SleepingCar(3, 52, 4, 9, 36, 30, 600));
        train.addCar(new DiningCar(4, 48, 3, 8, 20, 100));
        train.addCar(new CargoCar(5, 30, 20000, 5000));
        train.addCar(new MailCar(6, 28, 0, 10000, 2500));

        if (args.length == 0 || !args[0].equals("--demo")) {
            new ConsoleMenu(train, new Scanner(System.in), System.out).run();
            return;
        }

        System.out.println("Лабораторная работа №5. Интерфейсы. Вариант 8");
        System.out.println("Маршрут: " + train.getRouteId());
        printCars("Исходный состав", train.getCars());
        System.out.println("Всего пассажиров: " + train.getPassengerCount());
        System.out.printf(Locale.ROOT, "Всего багажа: %.1f кг%n", train.getLuggageWeight());

        train.sortByComfort();
        printCars("По уровню комфорта (Comparable, по возрастанию)", train.getCars());
        printCars("По комфорту (Comparator)",
                train.sortedCars(new RailcarComfortComparator()));
        printCars("Пассажирские вагоны с числом пассажиров от 20 до 40 включительно",
                train.findByPassengerRange(20, 40));

        // Восходящее преобразование к интерфейсу и безопасное сужение типа.
        Railcar sleepingCar = train.getCars().stream()
                .filter(car -> car instanceof SleepingCar).findFirst().orElseThrow();
        PassengerCarrier carrier = (PassengerCarrier) sleepingCar;
        System.out.println("Пассажиры спального вагона через интерфейс: " + carrier.getPassengerCapacity());
        System.out.printf(Locale.ROOT, "Багаж того же вагона через интерфейс: %.1f кг%n",
                carrier.getLuggageCapacity());
    }

    private static void printCars(String title, List<Railcar> cars) {
        System.out.println("\n" + title + ":");
        cars.forEach(car -> System.out.println("  " + car.getInfo()));
    }
}
