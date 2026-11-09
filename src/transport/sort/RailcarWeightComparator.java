package transport.sort;

import transport.model.Railcar;
import java.util.Comparator;

/** Альтернативный порядок по полю weight родительского класса Railcar. */
public final class RailcarWeightComparator implements Comparator<Railcar> {
    @Override
    public int compare(Railcar first, Railcar second) {
        return Double.compare(first.getWeight(), second.getWeight());
    }
}
