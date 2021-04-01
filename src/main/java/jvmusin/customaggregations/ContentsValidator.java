package jvmusin.customaggregations;

import org.jetbrains.annotations.NotNull;

import static java.text.MessageFormat.format;

/**
 * Contents validator.
 *
 * <p>Used to validate {@link Contents}.
 *
 * <p>Basically it checks that the first {@link Header} has level {@code 1}
 * and it's possible to build a single table of contents from the given contents instance.
 */
public class ContentsValidator {

    /**
     * Validates the {@code contents}.
     *
     * <p>Throws {@link ContentsInvalidException} if:
     * <ul>
     *     <li>the first header has indentation level other than `1`, or</li>
     *     <li>indentation level increases by more than `1` in some pair of two consecutive headers, or</li>
     *     <li>indentation level of some header is less than `1`.</li>
     * </ul>
     *
     * @param contents {@link Contents} to validate.
     * @throws ContentsInvalidException if the {@code contents} is invalid.
     */
    public void validate(@NotNull Contents contents) {
        if (!contents.isEmpty() && contents.get(0).getLevel() != 1) {
            throw new ContentsInvalidException(
                    "First header of the document should have indentation level 1 (a single hash symbol): " + contents.get(0)
            );
        }
        for (int i = 1; i < contents.size(); i++) {
            Header prev = contents.get(i - 1);
            Header cur = contents.get(i);
            if (cur.getLevel() < 1 || cur.getLevel() > prev.getLevel() + 1) {
                throw new ContentsInvalidException(
                        format("Headers '{0}' and '{1}' can't go after each other", prev, cur)
                );
            }
        }
    }
}
