package transport.model;

import java.util.Locale;
import java.io.PrintStream;

/** Общая часть подвижного состава. Естественный порядок — возрастание комфорта. */
public abstract class Railcar implements Comparable<Railcar> {
    private final int number;
    private final double weight;
    private final int comfortLevel;

    protected Railcar(int number, double weight, int comfortLevel) {
        this.number = positive(number,1);
        this.weight = positive(weight, 100);
        this.comfortLevel = Math.max(comfortLevel, 1);
    }

    public final int getNumber() { return number; }
    /** Собственная масса в тоннах, без пассажиров и груза. */
    public final double getWeight() { return weight; }
    /** 0 — без пассажирского комфорта, 5 — наивысший уровень. */
    public final int getComfortLevel() { return comfortLevel; }

    @Override
    public final int compareTo(Railcar other) {
        return Integer.compare(comfortLevel, other.comfortLevel);
    }

    public abstract String getInfo();

    protected final String baseInfo(String type) {
        return String.format(Locale.ROOT, "%s №%d, масса %.1f т, комфорт %d",
                type, number, weight, comfortLevel);
    }

    protected static int positive(int value, int defaultValue){
        if(value<=0){
            System.out.printf("Значение должно быть положительным. Выбрано значение по умолчанию (%.1f)%n", defaultValue);
            return defaultValue;
        } else return value;
    }

    protected static double positive(double value, double defaultValue){
        if(value<=0){
            System.out.printf("Значение должно быть положительным. Выбрано значение по умолчанию (%.1f)%n", defaultValue);
            return defaultValue;
        } else return value;
    }
}
