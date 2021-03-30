package jvmusin.customaggregations

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty

class ContentsAppenderTests : BehaviorSpec() {
    private val contentsAppender = ContentsPrepender()

    init {
        Given("prependWithContents") {
            When("text is empty") {
                Then("returns empty text") {
                    contentsAppender.prependWithContents("").shouldBeEmpty()
                }
            }
            When("text does not have headers") {
                Then("returns the same text") {
                    val text = """
                        when we all fall asleep,
                        where do we go?
                    """.trimIndent()
                    contentsAppender.prependWithContents(text) shouldBe text
                }
            }
            When("text has only headers") {
                Then("prepends contents correctly") {
                    val text = """
                    # My Project
                    ## Idea
                    ## Implementation
                    ### Step 1
                    ### Step 2
                """.trimIndent()
                    val expect = """
                    1. [My Project](#my-project)
                        1. [Idea](#idea)
                        2. [Implementation](#implementation)
                            1. [Step 1](#step-1)
                            2. [Step 2](#step-2)

                    # My Project
                    ## Idea
                    ## Implementation
                    ### Step 1
                    ### Step 2
                """.trimIndent()
                    contentsAppender.prependWithContents(text) shouldBe expect
                }
            }
            When("test has headers and regular lines") {
                Then("prepends contents correctly") {
                    val text = """
                    # My Project
                    ## Idea
                    content
                    ## Implementation
                    ### Step 1
                    content
                    ### Step 2
                    content
                """.trimIndent()
                    val expect = """
                    1. [My Project](#my-project)
                        1. [Idea](#idea)
                        2. [Implementation](#implementation)
                            1. [Step 1](#step-1)
                            2. [Step 2](#step-2)

                    # My Project
                    ## Idea
                    content
                    ## Implementation
                    ### Step 1
                    content
                    ### Step 2
                    content
                """.trimIndent()
                    contentsAppender.prependWithContents(text) shouldBe expect
                }
            }
            When("text has indentation level jumps by more than 1") {
                Then("throws ContentsInvalidException") {
                    val text = """
                        # My Project
                        ### And the jump
                    """.trimIndent()
                    shouldThrow<ContentsInvalidException> {
                        contentsAppender.prependWithContents(text)
                    }
                }
            }
        }
    }
}
