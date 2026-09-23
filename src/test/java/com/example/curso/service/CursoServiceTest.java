package com.example.curso.service;

import com.example.curso.entity.Curso;
import com.example.curso.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceTest {

    private CursoRepository cursoRepository;
    private CursoService cursoService;

    @BeforeEach
    void configurar() {
        cursoRepository = mock(CursoRepository.class);
        cursoService = new CursoService(cursoRepository);
    }

    @Test
    void deveCriarCursoSemIdEComDeletadoFalse() {
        Curso entrada = new Curso();
        entrada.setId(99L);
        entrada.setNome("Java básico");
        entrada.setDescricao("Introdução ao Java");
        entrada.setDeletado(true);

        Curso salvo = new Curso();
        salvo.setId(1L);
        salvo.setNome("Java básico");
        salvo.setDescricao("Introdução ao Java");

        when(cursoRepository.save(entrada)).thenReturn(salvo);

        Curso resultado = cursoService.criar(entrada);

        assertNull(entrada.getId());
        assertFalse(entrada.isDeletado());
        assertEquals("Java básico", entrada.getNome());
        assertEquals("Introdução ao Java", entrada.getDescricao());
        assertSame(salvo, resultado);

        verify(cursoRepository).save(entrada);
        verifyNoMoreInteractions(cursoRepository);
    }

    @Test
    void deveListarQuandoNomeForNull() {
        Curso curso = new Curso();
        curso.setNome("Java básico");
        List<Curso> esperados = List.of(curso);

        when(cursoRepository.findByDeletadoFalse())
                .thenReturn(esperados);

        List<Curso> resultado = cursoService.listar(null);

        assertEquals(esperados, resultado);

        verify(cursoRepository).findByDeletadoFalse();
        verifyNoMoreInteractions(cursoRepository);
    }

    @Test
    void deveListarQuandoNomeForVazio() {
        when(cursoRepository.findByDeletadoFalse())
                .thenReturn(List.of());

        List<Curso> resultado = cursoService.listar("");

        assertTrue(resultado.isEmpty());

        verify(cursoRepository).findByDeletadoFalse();
        verifyNoMoreInteractions(cursoRepository);
    }

    @Test
    void deveListarComFiltroPeloInicioDoNome() {
        Curso curso = new Curso();
        curso.setNome("Java básico");
        List<Curso> esperados = List.of(curso);

        when(cursoRepository.findByNomeStartingWithAndDeletadoFalse("Java"))
                .thenReturn(esperados);

        List<Curso> resultado = cursoService.listar("Java");

        assertEquals(esperados, resultado);

        verify(cursoRepository)
                .findByNomeStartingWithAndDeletadoFalse("Java");
        verifyNoMoreInteractions(cursoRepository);
    }
}