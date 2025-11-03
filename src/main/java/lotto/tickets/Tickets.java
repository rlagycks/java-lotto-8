package lotto.tickets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lotto.domain.Lotto;

public final class Tickets implements Iterable<Lotto> {
    private final List<Lotto> values;

    public Tickets(List<Lotto> source) {
        List<Lotto> copy = new ArrayList<>(source);
        this.values = Collections.unmodifiableList(copy);
    }

    public int size() {
        return values.size();
    }

    public List<Lotto> asList() {
        return values;
    }

    @Override
    public Iterator<Lotto> iterator() {
        return values.iterator();
    }
}