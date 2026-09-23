package com.example.curso.service;

import com.example.curso.entity.Curso;
import com.example.curso.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));

        curso.setDeletado(true);
        cursoRepository.save(curso);
    }
}