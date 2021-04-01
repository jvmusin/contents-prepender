package jvmusin.customaggregations;

import lombok.Data;

@Data
public class Header {

    /** Indentation level of {@code this} header. */
    private final int level;

    /** Title of {@code this} header. */
    private final String title;
}
