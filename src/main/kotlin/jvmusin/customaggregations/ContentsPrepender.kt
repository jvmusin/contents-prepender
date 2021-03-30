package jvmusin.customaggregations

/**
 * Contents prepender.
 *
 * Used to extract contents from the text and prepend it with extracted contents.
 *
 * @property extractor Extractor used to extract [Contents].
 * @property renderer Renderer used to render [Contents].
 * @property validator Validator used to validate [Contents].
 */
class ContentsPrepender(
    private val extractor: ContentsExtractor = ContentsExtractor(),
    private val renderer: ContentsRenderer = ContentsRenderer(),
    private val validator: ContentsValidator = ContentsValidator()
) {

    /**
     * Returns [text] prepended with its contents.
     *
     * From this
     * ```
     * # My Project
     * ## Idea
     * content
     * ## Implementation
     * ### Step 1
     * content
     * ### Step 2
     * content
     * ```
     *
     * Makes this
     * ```
     * 1. [My Project](#my-project)
     *     1. [Idea](#idea)
     * 2. [Implementation](#implementation)
     *     1. [Step 1](#step-1)
     *     2. [Step 2](#step-2)
     *
     * # My Project
     * ## Idea
     * content
     * ## Implementation
     * ### Step 1
     * content
     * ### Step 2
     * content
     * ```
     *
     * @param text Text to prepend with its content.
     * @return [text] prepended with it's content.
     * @throws ContentsInvalidException if the contents is invalid.
     */
    fun prependWithContents(text: String): String {
        val lines = text.lines()
        val contents = extractor.extract(lines)
        if (contents.isEmpty()) return text
        validator.validate(contents)
        val renderedContents = renderer.render(contents)
        return (renderedContents + "" + lines).joinToString("\n")
    }
}
