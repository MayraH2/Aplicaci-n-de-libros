package com.company.literatura.modelos;

import com.company.literatura.registros.DatosLibros;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne
    @JoinColumn(name="autor_id")
    private Autor autor;

    @Column(name="nombre_autor")
    private String nombreAutor;

    @Column(name="idiomas")
    private String idioma;

    private Long numeroDescargas;


    public Libro(DatosLibros datosLibro, Autor autor){
        this.titulo = datosLibro.titulo();
        this.autor = autor;
        this.nombreAutor = autor.getNombre();
        setIdioma(datosLibro.idioma());
        this.numeroDescargas = datosLibro.cantidadDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<String> getIdioma() {
        return Arrays.asList(idioma.split(","));
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = String.join(",",idioma);
    }

    public Long getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Long numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return  "********* Datos del libro *********" + '\'' +
                "titulo ='" + titulo + '\'' +
                ", Autor(es) =" + nombreAutor +
                ", Idioma(s) =" + idioma +
                ", Total de descargas ='" + numeroDescargas + '\'' +
                "*****************************+*****";

    }
}
