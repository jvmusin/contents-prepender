package jvmusin.customaggregations;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Table of contents.
 *
 * <p>Represented as a list of {@link Header}-s.
 */
public class Contents extends ArrayList<Header> {
    public Contents(int initialCapacity) {
        super(initialCapacity);
    }

    public Contents() {}

    public Contents(@NotNull Collection<? extends Header> c) {
        super(c);
    }

    public Contents(@NotNull Header... headers) {
        this(Arrays.asList(headers));
    }
}
