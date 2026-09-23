package com.example.curso.service;

import com.example.curso.entity.Curso;
import com.example.curso.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public Curso criar(Curso curso) {
        curso.setId(null);
        curso.setDeletado(false);

        return cursoRepository.save(curso);
    }

    public List<Curso> listar(String nome) {
        if (nome == null || nome.isEmpty()) {
            return cursoRepository.findByDeletadoFalse();
        }

        return cursoRepository.findByNomeStartingWithAndDeletadoFalse(nome);
    }
}