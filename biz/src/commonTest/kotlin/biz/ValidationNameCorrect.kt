package biz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplProfile
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.models.MkplVisibility
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.stubs.MkplProfileStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = MkplProfileStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationNameCorrect(command: MkplCommand, processor: MkplProfileProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        profileRequest = MkplProfile(
            id = stub.id,
            name = "Ivan",
            description = "abc",
            visibility = MkplVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("Ivan", ctx.profileValidated.name)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: MkplCommand, processor: MkplProfileProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        profileRequest = MkplProfile(
            id = stub.id,
            name = " \n\t abc \t\n ",
            description = "abc",
            visibility = MkplVisibility.VISIBLE_PUBLIC
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MkplState.FAILING, ctx.state)
    assertEquals("abc", ctx.profileValidated.name)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: MkplCommand, processor: MkplProfileProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        profileRequest = MkplProfile(
            id = stub.id,
            name = "",
            description = "abc",
            visibility = MkplVisibility.VISIBLE_PUBLIC,
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: MkplCommand, processor: MkplProfileProcessor) = runTest {
    val ctx = MkplContext(
        command = command,
        state = MkplState.NONE,
        workMode = MkplWorkMode.TEST,
        profileRequest = MkplProfile(
            id = MkplUserId("123"),
            name = "!@#$%^&*(),.{}",
            description = "abc",
            visibility = MkplVisibility.VISIBLE_PUBLIC
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MkplState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}