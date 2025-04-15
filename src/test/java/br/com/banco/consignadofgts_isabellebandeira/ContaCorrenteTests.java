package br.com.banco.consignadofgts_isabellebandeira;

import br.com.banco.consignadofgts_isabellebandeira.dto.ContaCorrenteDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContaCorrenteTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @BeforeEach
    void setup() {
        ContaCorrente conta = new ContaCorrente();
        conta.setSaldo(0.00);
        contaCorrenteRepository.save(conta);
    }

    //Testa endpoint para depositar saldo na conta-corrente
    @Test
    @Transactional
    public void test_depositarSaldoComSucesso() throws Exception {
        //Prepara contaCorrenteDTO para enviar para o endpoint
        ContaCorrenteDTO contaCorrenteDTO = new ContaCorrenteDTO();
        contaCorrenteDTO.setNumContaCorrente(1L);
        contaCorrenteDTO.setSaldo(0.00);

        //Chama a API, converte o DTO pra uma string JSON e espera resposta de sucesso
        mockMvc.perform(post("/api/v1/conta-corrente/depositar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contaCorrenteDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Conta corrente atualizada com sucesso."));

    }
}
