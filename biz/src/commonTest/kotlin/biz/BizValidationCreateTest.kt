package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import kotlin.test.Test

// TODO-validation-5: смотрим пример теста валидации, собранного из тестовых функций-оберток
@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = MkplCommand.CREATE
    private val processor by lazy { MkplProfileProcessor() }

    @Test
    fun correctTitle() = validationNameCorrect(command, processor)
    @Test
    fun trimTitle() = validationTitleTrim(command, processor)
    @Test
    fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test
    fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test
    fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test
    fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test
    fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test
    fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

}