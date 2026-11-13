package transport.model;

/** Общий контракт вагона, участвующего в расчёте состава. */
public interface PassengerCarrier {
    int getPassengerCapacity();
    double getLuggageCapacity();
}
