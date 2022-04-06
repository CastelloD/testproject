package com.example.demo.alumni;

import com.example.demo.alumni.controller.AlumniController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void noAuthorizedExpectStatus401() throws Exception {
        this.mockMvc.perform(get("/ex-1/alumni?name=carlo"))
                .andDo(print())
                .andExpect(status().is(401));
    }


    @Test
    public void contentNotFoundExpectStatus204() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");
        this.mockMvc.perform(get("/ex-1/alumni?name=carlo")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print()).andExpect(status().is(204));
    }

    @Test
    public void findByNameWithPaginationExpectStatus200() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");
        mockMvc.perform(get("/ex-1/alumni?name=nome&size=1&page=0")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void findByNameAndEducationExpectStatus200() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");
        this.mockMvc.perform(get("/ex-1/alumni?name=nome&eduLevel=master&size=1&page=0")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }


    private String obtainAccessToken(String username, String password) throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        String body = new ObjectMapper().writeValueAsString(params);

        ResultActions result
                = mockMvc.perform(post("/authenticate")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }

}
