package jvmusin.contentsprepender;

import static java.util.stream.Collectors.toList;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Contents prepender.
 *
 * <p>Used to extract contents from the text and prepend the text with extracted contents.
 */
@RequiredArgsConstructor
public class ContentsPrepender {

    /** Extractor used to extract {@link Contents}. */
    private final ContentsExtractor extractor;

    /** Renderer used to render {@link Contents}. */
    private final ContentsRenderer renderer;

    /** Validator used to validate {@link Contents}. */
    private final ContentsValidator validator;

    /**
     * Returns {@code} prepended with its table of contents.
     *
     * <p>From this
     *
     * <pre>
     * # My Project
     * ## Idea
     * content
     * ## Implementation
     * ### Step 1
     * content
     * ### Step 2
     * content
     * </pre>
     *
     * <p>Makes this
     *
     * <pre>
     * 1. [My Project](#my-project)
     *     1. [Idea](#idea)
     *     2. [Implementation](#implementation)
     *         1. [Step 1](#step-1)
     *         2. [Step 2](#step-2)
     *
     * # My Project
     * ## Idea
     * content
     * ## Implementation
     * ### Step 1
     * content
     * ### Step 2
     * content
     * </pre>
     *
     * @param text Text to prepend with its content.
     * @return {@code text} prepended with it's content.
     * @throws ContentsInvalidException if the contents is invalid.
     */
    public String prependWithContents(String text) {
        List<String> lines = text.lines().collect(toList());
        Contents contents = extractor.extract(lines);
        if (contents.isEmpty()) return text;
        validator.validate(contents);
        List<String> result = new ArrayList<>(renderer.render(contents));
        result.add("");
        result.addAll(lines);
        return String.join("\n", result);
    }
}
