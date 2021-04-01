package jvmusin.contentsprepender;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * Contents extractor.
 *
 * <p>Used to extract {@link Contents} from the text.
 */
public class ContentsExtractor {

    /**
     * Pattern that describes how a single {@link Header} looks like in a text.
     *
     * <p>It ignores all the spaces between hash signs and a title, trailing spaces are also
     * ignored.
     */
    private static final Pattern pattern = Pattern.compile(" *(?<level>#+) *(?<title>[^#].*?) *");

    /**
     * Takes all the {@link Header headers} from the {@code lines} and returns them as {@link Contents} instance.
     *
     * @param lines Lines to extract {@link Contents} from.
     * @return {@link Contents} with all the headers form the text.
     * @throws ContentsInvalidException if the contents is invalid.
     */
    public Contents extract(List<String> lines) {
        List<Header> headers = lines.stream()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> {
                    int level = matcher.group("level").length();
                    String title = matcher.group("title");
                    return new Header(level, title);
                })
                .collect(toList());
        return new Contents(headers);
    }
}
