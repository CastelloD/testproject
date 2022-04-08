package com.example.demo.alumni;

import com.example.demo.alumni.controller.AlumniController;
import com.example.demo.alumni.dto.AlumnoDTO;
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
        mockMvc.perform(get("/ex-1/alumni?name=giovanni&size=1&page=0")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void findByNameAndEducationExpectStatus200() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");
        this.mockMvc.perform(get("/ex-1/alumni?name=nome&eduLevel=master&size=1&page=0")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void setNewAlumniWithFullDataExpectStatus200() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");

        String alumnoJson = "{ " +
                "\"name\":\"daniele\",\"addresses\":[{ \"street\":\"via acerbi\",\"number\":\"29\", \"country\":\"genova\"}" +
                "], \"education\":{\"master\":{\"university\":\"DISI\", \"year\": 2008}}}";

        this.mockMvc.perform(post("/ex-1/alumni")
                .content(alumnoJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

    }

    @Test
    public void newAlumniNameWithNumbersExpectStatus400() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");

        String alumnoJson = "{ " +
                "\"name\":\"dani123\",\"addresses\":[{ \"street\":\"via acerbi\",\"number\":\"29\", \"country\":\"genova\"}" +
                "], \"education\":{\"master\":{\"university\":\"DISI\", \"year\": 2008}}}";

        this.mockMvc.perform(post("/ex-1/alumni")
                .content(alumnoJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().is(400));

    }

    @Test
    public void newAlumniStreetNumberWithLettersExpectStatus400() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");

        String alumnoJson = "{ " +
                "\"name\":\"dani\",\"addresses\":[{ \"street\":\"via acerbi\",\"number\":\"TTT\", \"country\":\"genova\"}" +
                "], \"education\":{\"master\":{\"university\":\"DISI\", \"year\": 2008}}}";

        this.mockMvc.perform(post("/ex-1/alumni")
                .content(alumnoJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().is(400));

    }

    @Test
    public void setNewAlumniWithoutAddressExpectStatus400() throws Exception {
        String accessToken = obtainAccessToken("user1", "password");

        String alumnoJson = "{ " +
                "\"name\":\"daniele\", \"education\":{\"master\":{\"university\":\"DISI\", \"year\": 2008}}}";

        this.mockMvc.perform(post("/ex-1/alumni")
                .content(alumnoJson)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().is(400));

    }



    private String obtainAccessToken(String username, String password) throws Exception {

        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        String json = new ObjectMapper().writeValueAsString(body);

        ResultActions result
                = mockMvc.perform(post("/authenticate")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }

}
