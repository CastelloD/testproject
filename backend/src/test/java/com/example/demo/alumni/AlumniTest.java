package com.example.demo.alumni;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AlumniTest {

    @Autowired
    AlumniController alumniController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contentNotFound() throws Exception {
        this.mockMvc.perform(get("/ex-1/alumni?name=carlo")).andDo(print()).andExpect(status().is(204));
    }

    @Test
    public void paginationIsOk() throws Exception {
        this.mockMvc.perform(get("/ex-1/alumni?name=nome&size=1&page=0")).andDo(print()).andExpect(status().isOk());
    }

}
