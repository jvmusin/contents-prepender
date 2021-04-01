package jvmusin.customaggregations

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class ContentsRendererTests : BehaviorSpec() {
    private val contentsRenderer = ContentsRenderer(4)

    init {
        Given("render") {
            When("contents is empty") {
                Then("returns empty list") {
                    contentsRenderer.render(Contents()).shouldBeEmpty()
                }
            }
            When("contents consist only of first-level headers") {
                Then("renders them") {
                    contentsRenderer.render(
                        Contents(
                            Header(1, "First"),
                            Header(1, "Second"),
                            Header(1, "Third"),
                        )
                    ) shouldBe listOf(
                        "1. [First](#first)",
                        "2. [Second](#second)",
                        "3. [Third](#third)"
                    )
                }
            }
            When("contents consist of deep headers") {
                Then("renders them") {
                    contentsRenderer.render(
                        Contents(
                            Header(1, "First"),
                            Header(2, "First sub 1"),
                            Header(3, "First sub sub"),
                            Header(1, "Second"),
                            Header(1, "Third"),
                            Header(2, "Third sub 1"),
                            Header(3, "Third sub 1 sub"),
                            Header(4, "Third sub 1 sub sub"),
                            Header(2, "Third sub 2")
                        )
                    ) shouldBe listOf(
                        "1. [First](#first)",
                        "    1. [First sub 1](#first-sub-1)",
                        "        1. [First sub sub](#first-sub-sub)",
                        "2. [Second](#second)",
                        "3. [Third](#third)",
                        "    1. [Third sub 1](#third-sub-1)",
                        "        1. [Third sub 1 sub](#third-sub-1-sub)",
                        "            1. [Third sub 1 sub sub](#third-sub-1-sub-sub)",
                        "    2. [Third sub 2](#third-sub-2)"
                    )
                }
            }
            When("headers have multiple spaces") {
                Then("they all are replaces with - in links") {
                    contentsRenderer.render(Contents(Header(1, "This is   the spaced  header")))
                        .shouldBe(listOf("1. [This is   the spaced  header](#this-is---the-spaced--header)"))
                }
            }
        }
    }
}
