package com.company.literatura.repositorios;

import com.company.literatura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro,Long> {

    boolean existByTitulo(String titulo);

    Libro findByTituloContainsIgnoreCase(String titulo);

    List<Libro> findTop10ByOrderByNumeroDescargasDesc();

    List<Libro> findByIdiomaContaining(String idioma);
}
