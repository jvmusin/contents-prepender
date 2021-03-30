package jvmusin.customaggregations

/**
 * Contents validator.
 *
 * Used to validate [Contents].
 *
 * Basically it checks that the first [Header] has level `1`
 * and it's possible to build a single table of contents from the given [Contents] instance.
 */
class ContentsValidator {

    /**
     * Validates the [contents].
     *
     * Throws [ContentsInvalidException] if:
     * * the first header has indentation level other than `1`, or
     * * indentation level increases by more than `1` in some pair of two consecutive headers, or
     * * indentation level of some header is less than `1`.
     *
     * @param contents [Contents] to validate.
     * @throws ContentsInvalidException if the [contents] is invalid.
     */
    fun validate(contents: Contents) {
        if (contents.isNotEmpty() && contents.first().level != 1) {
            throw ContentsInvalidException(
                "First header of the document should have indentation level 1 (a single hash symbol): " +
                        "'${contents.first()}'"
            )
        }
        for ((prev, cur) in contents.zipWithNext()) {
            if (cur.level !in 1..prev.level + 1) {
                throw ContentsInvalidException("Headers '$prev' and '$cur' can't go after each other")
            }
        }
    }
}
