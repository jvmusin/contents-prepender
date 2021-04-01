package jvmusin.customaggregations;

import static java.util.Collections.emptyList;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Contents renderer.
 *
 * <p>Used to extract contents from the given {@code Markdown} text and add the contents to the
 * beginning of the text.
 */
@RequiredArgsConstructor
public class ContentsRenderer {

    /** Indentation for sub-sections. */
    private final int indent;

    /**
     * Renders a {@link Header} prepending it with {@code indent} * {@code depth} spaces and adding
     * an {@code index} to the beginning.
     *
     * <p>The rendered header looks like
     *
     * <pre>
     * 1. [Root header](#root-header)
     * </pre>
     *
     * @param header A {@link Header} to render.
     * @param index Index of this header in the contents section.
     * @param depth Depth of this header in the contents.
     * @return A string representation of a rendered header.
     */
    private String render(Header header, int index, int depth) {
        String id = header.getTitle().toLowerCase().replace(' ', '-');
        String prefix = " ".repeat(Math.max(depth, 0) * indent);
        return String.format("%s%s. [%s](#%s)", prefix, index, header.getTitle(), id);
    }

    /**
     * Renders a {@link ContentNode} prepending it with {@code indent} * {@code depth} spaces,
     * adding an {@code index} to the beginning and its children recursively.
     *
     * <p>The rendered ContentNode looks like
     *
     * <pre>
     * 1. [Root header](#root-header)
     *     2. [First section](#first-section)
     *     3. [Second section](#second-section)
     * </pre>
     *
     * @param node A {@link ContentNode} to render.
     * @param index Index of this ContentNode's root in the contents section.
     * @param depth Depth of this ContentNode.
     * @return A list of string representations of this {@link ContentNode} and it's {@link
     *     ContentNode#children}.
     */
    private List<String> render(ContentNode node, int index, int depth) {
        List<String> result = new ArrayList<>();
        result.add(render(node.root, index, depth));
        List<ContentNode> children = node.children;
        for (int i = 0; i < children.size(); i++) {
            ContentNode child = children.get(i);
            result.addAll(render(child, i + 1, depth + 1));
        }
        return result;
    }

    /**
     * Builds a tree on all {@link Header}-s laying on the same level as the root at the moment of
     * call.
     *
     * @param contents {@link Contents} to build a tree from.
     * @return A list of {@link ContentNode}-s laying on the same level as the root at the moment of
     *     call.
     */
    private List<ContentNode> buildTree(Contents contents) {
        class Builder {
            int index = 0;

            Header peek() {
                if (index >= contents.size()) return null;
                return contents.get(index);
            }

            Header poll() {
                if (index >= contents.size()) return null;
                return contents.get(index++);
            }

            List<ContentNode> build() {
                Header first = poll();
                if (first == null) return emptyList();
                List<ContentNode> result = new ArrayList<>();
                result.add(new ContentNode(first));
                int currentLevel = first.getLevel();
                while (true) {
                    Header next = peek();
                    int nextLevel;
                    if (next == null || (nextLevel = next.getLevel()) < currentLevel) break;
                    if (nextLevel == currentLevel) result.add(new ContentNode(poll()));
                    else if (nextLevel == currentLevel + 1)
                        result.get(result.size() - 1).children.addAll(build());
                    else break;
                }
                return result;
            }
        }
        return new Builder().build();
    }

    /**
     * Renders the given {@link Contents} prepending sub-sections with spaces equal to {@code depth}
     * * {@code indent}.
     *
     * @param contents {@link Contents} to render.
     * @return A list of string representations of the {@code contents}' headers.
     */
    public List<String> render(Contents contents) {
        var tree = buildTree(contents);
        var pseudoRootNode = new ContentNode(new Header(0, ""));
        pseudoRootNode.children.addAll(tree);
        final List<String> result = render(pseudoRootNode, 0, -1);
        result.remove(0);
        return result;
    }

    /** Tree node representing a single contents section and it's children. */
    @Data
    private static class ContentNode {
        /** Section root header. */
        private final Header root;
        /** Children nodes of this section. */
        private final List<ContentNode> children = new ArrayList<>();
    }
}
