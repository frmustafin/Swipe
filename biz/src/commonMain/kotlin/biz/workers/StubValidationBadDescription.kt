package biz.workers

import ICorChainDsl
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplError
import ru.otus.otuskotlin.marketplace.common.models.MkplState
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import worker

fun ICorChainDsl<MkplContext>.stubValidationBadDescription(title: String) = worker {
    this.title = title
    on { stubCase == MkplStubs.BAD_DESCRIPTION && state == MkplState.RUNNING }
    handle {
        state = MkplState.FAILING
        this.errors.add(
            MkplError(
                group = "validation",
                code = "validation-description",
                field = "description",
                message = "Wrong description field"
            )
        )
    }
}
