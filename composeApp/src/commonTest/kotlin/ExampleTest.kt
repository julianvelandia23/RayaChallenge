package org.julianvelandia.raya

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleTest {

    @Test
    fun `it works`() {
        assertEquals(4, 2 + 2)
    }

    suspend fun fetchData(): String {
        // Fake suspend logic
        return "result"
    }

    @Test
    fun `test suspend function`() = runTest {
        val result = fetchData()
        assertEquals("result", result)
    }
}
