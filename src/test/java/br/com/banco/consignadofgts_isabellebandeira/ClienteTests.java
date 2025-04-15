package br.com.banco.consignadofgts_isabellebandeira;

import br.com.banco.consignadofgts_isabellebandeira.dto.ClienteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.Cliente;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ClienteRepository;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
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

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setup() {
        Cliente cliente = new Cliente();
        cliente.setNome("Isabelle Bandeira");
        clienteRepository.save(cliente);
        ClienteDTO clientedto = new ClienteDTO();
        clientedto.setNome("Isabelle Bandeira");
    }

    //Testa endpoint para cadastro de clientes
    @Test
    @Transactional
    public void test_cadastrarClienteSucesso() throws Exception {
        //Prepara um clienteDTO para enviar para o endpoint
        ClienteDTO clientedto = new ClienteDTO();
        clientedto.setNome("Isabelle Bandeira");

        //Chama a API, converte o DTO pra uma string JSON e espera resposta de sucesso
        mockMvc.perform(post("/api/v1/cliente/cadastracliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientedto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Cliente cadastrado com sucesso."));


    }

    //Testa endpoint para atualizar cadastro de clientes
    @Test
    @Transactional
    public void test_atualizarClienteSucesso() throws Exception {
        //Prepara clienteDTO para enviar para o endpoint
        ClienteDTO clientedto = new ClienteDTO();
        clientedto.setNome("Isabelle Bandeira");
        clientedto.setIdCliente(1L);

        //Chama a API, converte o DTO pra uma string JSON e espera resposta de sucesso
        mockMvc.perform(post("/api/v1/cliente/atualizacliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientedto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente atualizado com sucesso."));


    }
}
