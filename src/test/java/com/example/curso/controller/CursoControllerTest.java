package com.example.curso.controller;

import com.example.curso.entity.Curso;
import com.example.curso.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    void limparBanco() {
        cursoRepository.deleteAll();
    }

    @Test
    void deveCriarCurso() throws Exception {
        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "Java básico",
                                    "descricao": "Introdução ao Java"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Java básico"))
                .andExpect(jsonPath("$.descricao").value("Introdução ao Java"))
                .andExpect(jsonPath("$.deletado").value(false));

        List<Curso> cursos = cursoRepository.findAll();

        assertEquals(1, cursos.size());

        Curso salvo = cursos.getFirst();

        assertNotNull(salvo.getId());
        assertEquals("Java básico", salvo.getNome());
        assertEquals("Introdução ao Java", salvo.getDescricao());
        assertFalse(salvo.isDeletado());
    }

    @Test
    void deveListarSomenteCursosNaoDeletados() throws Exception {
        salvarCurso("Java básico", false);
        salvarCurso("Python básico", false);
        salvarCurso("Curso deletado", true);

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].nome").value(
                        containsInAnyOrder("Java básico", "Python básico")));
    }

    @Test
    void deveFiltrarPeloInicioDoNomeSemMostrarDeletados() throws Exception {
        salvarCurso("Java básico", false);
        salvarCurso("Java avançado", false);
        salvarCurso("Introdução ao Java", false);
        salvarCurso("Python básico", false);
        salvarCurso("Java deletado", true);

        mockMvc.perform(get("/cursos")
                        .param("nome", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].nome").value(
                        containsInAnyOrder("Java básico", "Java avançado")));
    }

    private Curso salvarCurso(String nome, boolean deletado) {
        Curso curso = new Curso();
        curso.setNome(nome);
        curso.setDescricao("Descrição do curso");
        curso.setDeletado(deletado);

        return cursoRepository.save(curso);
    }
}