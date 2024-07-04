package com.company.literatura.modelos;

import com.company.literatura.registros.DatosAutor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro = new ArrayList<>();

    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.nacimiento = datosAutor.nacimiento();
        this.fallecimiento = datosAutor.fallecimiento();
    }

    public Autor(){}

    @Override
    public String toString(){
        StringBuilder libroTitulo = new StringBuilder();
        for (Libro libro : libro){
            libroTitulo.append(libro.getTitulo()).append(", ");
        }

        if(libroTitulo.length() > 0){
            libroTitulo.setLength(libroTitulo.length() - 2);
        }

        return "********* Datos del autor *********" + '\'' +
                "Nombre del autor ='" + nombre + '\'' +
                ", Fecha de nacimiento =" + nacimiento +
                ", Fecha de muerte =" + fallecimiento +
                ", Libros escritos ='" + libroTitulo + '\'' +
                "*****************************+*****";
    }

    public List<Libro> getLibros() {
        return libro;
    }

    public void setLibros(List<Libro> libros) {
        this.libro = libros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getFallecimiento() {
        return fallecimiento;
    }

    public void setFallecimiento(Integer fallecimiento) {
        this.fallecimiento = fallecimiento;
    }
}
