package com.company.literatura.registros;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadoLibros(
        @JsonAlias("results") List<DatosLibros> resultados
        ) {
}
