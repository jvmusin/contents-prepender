package jvmusin.customaggregations

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe

class ContentsExtractorTests : BehaviorSpec() {
    private val extractor = ContentsExtractor()

    init {
        Given("extract") {
            When("text is empty") {
                Then("no headers produced") {
                    extractor.extract(emptyList()).shouldBeEmpty()
                }
            }
            When("text has no headers") {
                Then("no headers produced") {
                    extractor.extract(listOf("Hello", "it's me")).shouldBeEmpty()
                }
            }
            When("text has a single header of level 1") {
                Then("it's extracted correctly") {
                    extractor.extract(listOf("# The Header")) shouldBe Contents(Header(1, "The Header"))
                }
            }
            When("text has several headers") {
                Then("they're extracted correctly") {
                    extractor.extract(listOf("# H1", "## H2", "## H2_2", "# H1_2")) shouldBe Contents(
                        Header(1, "H1"),
                        Header(2, "H2"),
                        Header(2, "H2_2"),
                        Header(1, "H1_2")
                    )
                }
            }
            When("headers have several spaces") {
                Then("the ones in the middle are not removed, but the line is trimmed") {
                    extractor.extract(listOf("#   Hello   it's me  ")) shouldBe Contents(Header(1, "Hello   it's me"))
                }
            }
            When("header has no space after hash symbol") {
                Then("it's extracted correctly") {
                    extractor.extract(listOf("#Header")) shouldBe Contents(Header(1, "Header"))
                }
            }
            When("line has spaces before leading hashes") {
                Then("it's extracted correctly") {
                    extractor.extract(listOf("  # Hi")) shouldBe Contents(Header(1, "Hi"))
                }
            }
            When("line contains only hashes") {
                Then("not extracts it as a header") {
                    extractor.extract(listOf("##")).shouldBeEmpty()
                }
            }
        }
    }
}
