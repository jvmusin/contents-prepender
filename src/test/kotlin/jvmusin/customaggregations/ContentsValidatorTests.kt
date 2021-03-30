package jvmusin.customaggregations

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec

class ContentsValidatorTests : BehaviorSpec() {
    private val contentsValidator = ContentsValidator()

    init {
        Given("validate") {
            When("contents is empty") {
                Then("validation passes") {
                    contentsValidator.validate(emptyList())
                }
            }
            When("contents has one header") {
                And("the header has level 1") {
                    Then("validation passes") {
                        contentsValidator.validate(listOf(Header(1, "Header")))
                    }
                }
                And("the header has level 2") {
                    Then("validation fails") {
                        shouldThrow<ContentsInvalidException> {
                            contentsValidator.validate(listOf(Header(2, "Header")))
                        }
                    }
                }
                And("the header has level 0") {
                    Then("validation fails") {
                        shouldThrow<ContentsInvalidException> {
                            contentsValidator.validate(listOf(Header(0, "Header")))
                        }
                    }
                }
            }
            When("contents has several headers") {
                And("everything is all right") {
                    Then("validation passes") {
                        contentsValidator.validate(
                            listOf(
                                Header(1, "The Root"),
                                Header(2, "First section"),
                                Header(3, "First subsection 1"),
                                Header(3, "First subsection 2"),
                                Header(2, "Second section"),
                                Header(1, "The second root"),
                                Header(2, "First section 2")
                            )
                        )
                    }
                }
                And("There is an indentation level jump of more than 1") {
                    Then("validation fails") {
                        shouldThrow<ContentsInvalidException> {
                            contentsValidator.validate(
                                listOf(
                                    Header(1, "Header1"),
                                    Header(3, "Header2")
                                )
                            )
                        }
                    }
                }
            }
            When("Some of the headers has indentation level 0") {
                Then("validation fails") {
                    shouldThrow<ContentsInvalidException> {
                        contentsValidator.validate(
                            listOf(
                                Header(1, "Header1"),
                                Header(2, "Header2"),
                                Header(0, "Header0")
                            )
                        )
                    }
                }
            }
        }
    }
}
