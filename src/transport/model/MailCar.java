package transport.model;

import java.util.Locale;

/** Почтовый груз учитывается отдельно от пассажирского багажа. */
public final class MailCar extends Railcar {
    private static final double DEFAULT_MAX_MAIL_WEIGHT = 5000.0;
    private static final double DEFAULT_MAIL_WEIGHT = 0.0;

    private final double maxMailWeight;
    private final double mailWeight;

    public MailCar(int number, double weight, int comfortLevel, double maxMailWeight, double mailWeight) {
        super(number, weight, comfortLevel);

        double validMaxMail = positive(maxMailWeight, DEFAULT_MAX_MAIL_WEIGHT);
        double validMail = positive(mailWeight, DEFAULT_MAIL_WEIGHT);

        if (validMail > validMaxMail) {
            System.out.printf(Locale.ROOT,
                    "Масса почты (%.1f кг) превышает вместимость вагона (%.1f кг). Установлен максимум.%n",
                    validMail, validMaxMail);
            validMail = validMaxMail;
        }

        this.maxMailWeight = validMaxMail;
        this.mailWeight = validMail;
    }

    public double getMaxMailWeight() {
        return maxMailWeight;
    }

    public double getMailWeight() {
        return mailWeight;
    }

    @Override
    public String getInfo() {
        return baseInfo("Почтовый вагон")
                + String.format(Locale.ROOT, ", почта %.1f / %.1f кг", mailWeight, maxMailWeight);
    }
}