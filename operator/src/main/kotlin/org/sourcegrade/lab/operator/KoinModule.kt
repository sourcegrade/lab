package org.sourcegrade.lab.operator

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val koinModule = module {
    single<Logger> { LogManager.getLogger("AutoGD Operator") }
    singleOf(::Operator)
}
