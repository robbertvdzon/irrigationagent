package com.vdzon.irrigation.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class DashboardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `test index page returns 200 and correct view`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("index"))
    }

    @Test
    fun `test index page with htmx fragment returns 200`() {
        mockMvc.perform(get("/advice-fragment"))
            .andExpect(status().isOk)
            .andExpect(view().name("fragments/advice :: advice-section"))
    }
}
