package jvmusin.customaggregations

/**
 * Contents extractor.
 *
 * Used to extract [Contents] from the text.
 */
class ContentsExtractor {

    companion object {
        /**
         * Pattern that describes how a single [Header] looks like in a text.
         *
         * It ignores all the spaces between hash signs and a title, trailing spaces are also ignored.
         */
        private val pattern = " *(?<level>#+) *(?<title>[^#].*?) *".toRegex()
    }

    /**
     * Takes all the [headers][Header] from the [lines] and returns them as [Contents] instance.
     *
     * @param lines Lines to extract [Contents] from.
     * @return [Contents] with all the headers form the text.
     * @throws ContentsInvalidException if the contents is invalid.
     */
    fun extract(lines: List<String>): Contents = lines.mapNotNull { line ->
        val groups = pattern.matchEntire(line)?.groups ?: return@mapNotNull null
        val level = groups["level"]!!.value.length
        val title = groups["title"]!!.value
        Header(level, title)
    }
}
