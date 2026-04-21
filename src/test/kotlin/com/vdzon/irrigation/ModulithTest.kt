package com.vdzon.irrigation

import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ModulithTest {

    @Test
    fun verifyModulith() {
        val modules = ApplicationModules.of(IrrigationApplication::class.java)
        modules.verify()
        modules.forEach { println(it) }
    }
}
