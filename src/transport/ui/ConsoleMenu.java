package transport.ui;

import transport.model.*;
import transport.service.PassengerTrain;
import transport.sort.RailcarComfortComparator;

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Консольный интерфейс. Ввод и вывод отделены от предметной модели. */
public final class ConsoleMenu {
    private final PassengerTrain train;
    private final Scanner input;
    private final PrintStream output;

    public ConsoleMenu(PassengerTrain train, Scanner input, PrintStream output) {
        this.train = train;
        this.input = input;
        this.output = output;
    }

    public void run() {
        output.println("Лабораторная работа №5. Интерфейсы. Вариант 8");
        output.println("Маршрут: " + train.getRouteId());
        output.println("Загружен пример состава. Изменения сохраняются до выхода из программы.");
        try {
            while (true) {
                printMenu();
                int choice = readInt("Выберите действие: ", 0, 7);
                if (choice == 0) {
                    output.println("Работа завершена.");
                    return;
                }
                try {
                    switch (choice) {
                        case 1 -> printCars("Состав поезда", train.getCars());
                        case 2 -> printTotals();
                        case 3 -> {
                            train.sortByComfort();
                            printCars("Состав по возрастанию комфорта (Comparable)", train.getCars());
                        }
                        case 4 -> printCars("Копия состава по возрастанию комфорта (Comparator)",
                                train.sortedCars(new RailcarComfortComparator()));
                        case 5 -> findCars();
                        case 6 -> addCar();
                        case 7 -> printCapabilities();
                        default -> throw new IllegalStateException("Неизвестное действие");
                    }
                } catch (IllegalArgumentException | ArithmeticException error) {
                    output.println("Ошибка: " + error.getMessage());
                }
            }
        } catch (NoSuchElementException endOfInput) {
            output.println("\nВвод завершён. Работа завершена.");
        }
    }

    private void printMenu() {
        output.println("\n=== Главное меню ===");
        output.println("1. Показать состав поезда");
        output.println("2. Подсчитать пассажиров и багаж");
        output.println("3. Сортировать состав по комфорту");
        output.println("4. Показать состав по массе (без изменения порядка)");
        output.println("5. Найти вагоны по числу пассажиров");
        output.println("6. Добавить единицу подвижного состава");
        output.println("7. Показать возможности вагона через интерфейсы");
        output.println("0. Выход");
    }

    private void printTotals() {
        output.println("Всего пассажиров: " + train.getPassengerCount());
        output.printf(Locale.ROOT, "Всего багажа: %.1f кг%n", train.getLuggageWeight());
    }

    private void printCars(String title, List<Railcar> cars) {
        output.println("\n" + title + ":");
        if (cars.isEmpty()) {
            output.println("Вагоны не найдены.");
        } else {
            cars.forEach(car -> output.println("  " + car.getInfo()));
        }
    }

    private void findCars() {
        int min = readInt("Минимальное число пассажиров: ", 0, Integer.MAX_VALUE);
        int max = readInt("Максимальное число пассажиров: ", min, Integer.MAX_VALUE);
        printCars("Результаты поиска [" + min + ", " + max + "]", train.findByPassengerRange(min, max));
    }

    private void addCar() {
        output.println("\n1. Пассажирский вагон\n2. Спальный вагон\n3. Вагон-ресторан");
        output.println("4. Грузовой вагон для багажа\n5. Почтовый вагон\n6. Локомотив\n0. Отмена");
        int type = readInt("Выберите тип: ", 0, 6);
        if (type == 0) { return; }
        int number = readInt("Номер: ", 1, Integer.MAX_VALUE);
        if (train.getCars().stream().anyMatch(car -> car.getNumber() == number)) {
            output.println("Вагон с таким номером уже есть в составе. Добавление отменено.");
            return;
        }
        double weight = readDouble("Собственная масса (т): ", true);
        int comfort = readInt("Комфорт (0–5): ", 0, 5);
        Railcar car = switch (type) {
            case 1 -> new PassengerCar(number, weight, comfort,
                    readInt("Число отделений: ", 1, Integer.MAX_VALUE),
                    readInt("Число пассажиров: ", 0, Integer.MAX_VALUE),
                    readDouble("Багаж (кг): ", false));
            case 2 -> createSleepingCar(number, weight, comfort);
            case 3 -> createDiningCar(number, weight, comfort);
            case 4 -> {
                double capacity = readDouble("Грузоподъёмность (кг): ", false);
                double luggage = readDouble("Багаж (кг): ", false, capacity);
                yield new CargoCar(number, weight, capacity, luggage);
            }
            case 5 -> {
                double capacity = readDouble("Максимальная масса почты (кг): ", false);
                double mail = readDouble("Масса почты (кг): ", false, capacity);
                yield new MailCar(number, weight, comfort, capacity, mail);
            }
            case 6 -> new Locomotive(number, weight, comfort, readDouble("Мощность (кВт): ", true));
            default -> throw new IllegalStateException("Неизвестный тип");
        };
        train.addCar(car);
        output.println("Добавлено: " + car.getInfo());
    }

    private SleepingCar createSleepingCar(int number, double weight, int comfort) {
        int compartments = readInt("Число отделений: ", 1, Integer.MAX_VALUE);
        int beds = readInt("Число спальных мест: ", 1, Integer.MAX_VALUE);
        int passengers = readInt("Число пассажиров: ", 0, beds);
        return new SleepingCar(number, weight, comfort, compartments, beds, passengers,
                readDouble("Багаж (кг): ", false));
    }

    private DiningCar createDiningCar(int number, double weight, int comfort) {
        int tables = readInt("Число столов (по 4 места): ", 1, Integer.MAX_VALUE);
        int capacity = (int) Math.min((long) tables * 4, Integer.MAX_VALUE);
        int passengers = readInt("Число пассажиров: ", 0, capacity);
        return new DiningCar(number, weight, comfort, tables, passengers,
                readDouble("Багаж (кг): ", false));
    }

    private void printCapabilities() {
        int number = readInt("Номер вагона или локомотива: ", 1, Integer.MAX_VALUE);
        Railcar car = train.getCars().stream().filter(item -> item.getNumber() == number)
                .findFirst().orElse(null);
        if (car == null) {
            output.println("Единица подвижного состава с таким номером не найдена.");
            return;
        }
        output.println(car.getInfo());
        if (car instanceof PassengerCarrier carrier) {
            output.println("PassengerCarrier: пассажиров " + carrier.getPassengerCapacity());
            output.printf(Locale.ROOT, "Багаж %.1f кг%n", carrier.getLuggageCapacity());
        } else {
            output.println("Перевозка пассажиров не предусмотрена.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            output.print(prompt);
            output.flush();
            try {
                int value = Integer.parseInt(input.nextLine().trim());
                if (value >= min && value <= max) { return value; }
            } catch (NumberFormatException invalidNumber) {
                // Повторяем запрос при неправильном формате или переполнении.
            }
            output.println("Введите целое число от " + min + " до " + max + ".");
        }
    }

    private double readDouble(String prompt, boolean positive) {
        return readDouble(prompt, positive, Double.MAX_VALUE);
    }

    private double readDouble(String prompt, boolean positive, double max) {
        while (true) {
            output.print(prompt);
            output.flush();
            try {
                double value = Double.parseDouble(input.nextLine().trim().replace(',', '.'));
                if (Double.isFinite(value) && (positive ? value > 0 : value >= 0) && value <= max) {
                    return value;
                }
            } catch (NumberFormatException invalidNumber) {
                // Повторяем запрос, не прерывая работу меню.
            }
            output.println("Введите конечное число " + (positive ? "больше 0" : "не меньше 0")
                    + " и не больше " + max + ".");
        }
    }
}
