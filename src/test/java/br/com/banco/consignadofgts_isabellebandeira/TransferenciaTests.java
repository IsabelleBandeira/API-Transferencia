package br.com.banco.consignadofgts_isabellebandeira;

import br.com.banco.consignadofgts_isabellebandeira.dto.TransferenciaRequestDTO;
import br.com.banco.consignadofgts_isabellebandeira.model.ContaCorrente;
import br.com.banco.consignadofgts_isabellebandeira.model.Transferencia;
import br.com.banco.consignadofgts_isabellebandeira.repository.ContaCorrenteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
public class TransferenciaTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    private ContaCorrente contaOrigem;
    private ContaCorrente contaDestino;

    @BeforeEach
    void setup() {
        contaOrigem = ContaCorrente.builder().build();
        contaCorrenteRepository.save(contaOrigem);

        contaDestino = ContaCorrente.builder().build();
        contaCorrenteRepository.save(contaDestino);
    }

    @Test
    @Transactional
    public void test_transferirComSucesso() throws Exception {
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setValorTransferencia(100.00);
        contaOrigem.setSaldo(100.00);
        contaCorrenteRepository.save(contaOrigem);
        transferenciaRequestDTO.setIdContaDestino(contaDestino.getNumContaCorrente());
        transferenciaRequestDTO.setIdContaOrigem(contaOrigem.getNumContaCorrente());

        mockMvc.perform(post("/api/v1/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferenciaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência realizada com sucesso!"));

    }
}
