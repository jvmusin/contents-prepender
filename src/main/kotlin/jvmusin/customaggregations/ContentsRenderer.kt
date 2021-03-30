package jvmusin.customaggregations

/**
 * Contents renderer.
 *
 * Used to extract contents from the `Markdown` text and add its contents to the beginning.
 *
 * @property indent Indentation for sub-sections.
 */
class ContentsRenderer(private val indent: Int = 4) {

    /**
     * Tree node representing a single contents section and it's children.
     *
     * @property root Section root header.
     * @property children Children nodes of this section.
     */
    private data class ContentNode(val root: Header, val children: MutableList<ContentNode> = mutableListOf())

    /**
     * Renders a [Header] prepending it with [indent] * [depth] spaces and adding an [index] to the beginning.
     *
     * The rendered header looks like
     * ```
     * 1. [Root header](#root-header)
     * ```
     *
     * @receiver A [Header] to render.
     * @param index Index of this header in the contents section.
     * @param depth Depth of this header in the contents.
     * @return A string representation of a rendered header.
     */
    private fun Header.render(index: Int, depth: Int): String {
        val id = title.toLowerCase().replace(' ', '-')
        val prefix = " ".repeat(maxOf(depth, 0) * indent)
        return "$prefix$index. [$title](#$id)"
    }

    /**
     * Renders a [ContentNode] prepending it with [indent] * [depth] spaces, adding an [index] to the beginning
     * and it's children recursively.
     *
     * The rendered ContentNode looks like
     * ```
     * 1. [Root header](#root-header)
     *     2. [First section](#first-section)
     *     3. [Second section](#second-section)
     * ```
     *
     * @receiver A [ContentNode] to render.
     * @param index Index of this ContentNode's root in the contents section.
     * @param depth Depth of this ContentNode.
     * @return A list of string representations of this [ContentNode] and it's [children][ContentNode.children].
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun ContentNode.render(index: Int, depth: Int): List<String> = buildList {
        add(root.render(index, depth))
        addAll(children.flatMapIndexed { index, child -> child.render(index + 1, depth + 1) })
    }

    /**
     * Builds a tree on all [Header]-s laying on the same level as the root at the moment of call.
     *
     * @param contents [Contents] to build a tree from.
     * @return A list of [ContentNode]-s laying on the same level as the root at the moment of call.
     */
    private fun buildTree(contents: Contents): List<ContentNode> {
        var index = 0
        fun peek() = contents.getOrNull(index)
        fun poll() = contents.getOrNull(index++)
        fun build(): List<ContentNode> {
            val first = poll() ?: return emptyList()
            val result = mutableListOf(ContentNode(first))
            val currentLevel = first.level
            while (peek().let { it != null && it.level >= first.level }) {
                when (peek()!!.level) {
                    currentLevel -> result += ContentNode(poll()!!)
                    currentLevel + 1 -> result.last().children += build()
                    else -> break
                }
            }
            return result
        }
        return build()
    }

    /**
     * Renders the given [Contents] prepending sub-sections with spaces equal to `depth` * [indent].
     *
     * @param contents [Contents] to render.
     * @return A list of string representations of the [contents]' headers.
     */
    fun render(contents: Contents): List<String> {
        val tree = buildTree(contents)
        val pseudoRootNode = ContentNode(Header(0, ""), tree.toMutableList())
        return pseudoRootNode.render(0, -1).drop(1)
    }
}
