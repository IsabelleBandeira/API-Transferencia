package br.com.banco.consignadofgts_isabellebandeira;

import br.com.banco.consignadofgts_isabellebandeira.dto.ClienteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.ResultMatcher;



@SpringBootTest
@AutoConfigureMockMvc
public class ClienteTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_cadastrarClienteSucesso() throws Exception {
        ClienteDTO clientedto = new ClienteDTO();
        clientedto.setNome("Isabelle Bandeira");

        mockMvc.perform(post("/api/v1/cliente/cadastracliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientedto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente cadastrado com sucesso."));


    }
}
