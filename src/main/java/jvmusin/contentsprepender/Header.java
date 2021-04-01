package jvmusin.contentsprepender;

import lombok.Data;

/** Represents a single header in a document. */
@Data
public class Header {

    /** Indentation level of {@code this} header. */
    private final int level;

    /** Title of {@code this} header. */
    private final String title;
}
