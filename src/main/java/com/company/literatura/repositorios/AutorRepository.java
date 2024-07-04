package com.company.literatura.repositorios;

import com.company.literatura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {

    List<Autor> findByNacimientoLessThanEqualOrFallecimientoGreaterThanEqual(int añoBuscado, int añoBuscado2);

    Autor findByNombreIgnoreCase(String nombre);
}
