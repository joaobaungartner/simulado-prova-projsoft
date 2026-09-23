package com.example.curso.controller;

import com.example.curso.entity.Curso;
import com.example.curso.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<Curso> criar(@RequestBody Curso curso) {
        Curso criado = cursoService.criar(curso);

        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public List<Curso> listar(
            @RequestParam(name = "nome", required = false) String nome) {
        return cursoService.listar(nome);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        cursoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}