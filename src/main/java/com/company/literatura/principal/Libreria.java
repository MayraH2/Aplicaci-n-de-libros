package com.company.literatura.principal;

import com.company.literatura.modelos.Autor;
import com.company.literatura.modelos.Libro;
import com.company.literatura.registros.DatosAutor;
import com.company.literatura.registros.DatosLibros;
import com.company.literatura.registros.ResultadoLibros;
import com.company.literatura.repositorios.AutorRepository;
import com.company.literatura.repositorios.LibroRepository;
import com.company.literatura.servicios.ConsumoAPI;
import com.company.literatura.servicios.ConvierteDatos;
import jakarta.transaction.Transactional;

import java.util.*;

public class Libreria {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro> datosSeries = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Libreria(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void mostrarMenu() {
        var opcion = 0;
        while (opcion != 9) {
            var menu = """
                    ///////////////////////////////////////////////////////////
                           Te damos la bienvenida a la libreria virtual
                    ///////////////////////////////////////////////////////////
                                        
                    Eliga una opción de nuestro menú a continuación
                                        
                    1 - Agregar Libro por su Nombre 📕
                    2 - Lista de libros buscados ✍️
                    3 - Buscar Libro por su Nombre 👨‍🏫
                    4 - Mostrar Autores de libros en la base ✍️
                    5 - Buscar Autores por Año ⌛
                    6 - Buscar Libros por idiomas traducidos ℹ️
                    7 - Top 10 de Libros más descargados 📊
                    9 - Salir de la librería 👋
                    """;

            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Ingrese una opción valida porfavor :)");
                teclado.nextLine();
            }

            switch (opcion) {
                case 1:
                    agregarLibroPorNombre();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    buscarLibroPorNombre();
                    break;
                case 4:
                    mostrarAutoresDeLibros();
                    break;
                case 5:
                    buscarAutorPorAno();
                    break;
                case 6:
                    buscarLibroPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 9:
                    System.out.println("Saliendo de la librería, nos vemos luego...");
                    break;
                default:
                    System.out.println("Ingrese una opción válida por favor :)");

            }
        }
    }

    private ResultadoLibros getDatosLibro() {
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        //System.out.println(json);
        ResultadoLibros datos = conversor.obtenerDatos(json, ResultadoLibros.class);
        return datos;
    }

    private Libro libroCreado(DatosLibros datosLibros, Autor autor){
        if (autor !=null){
            return new Libro(datosLibros,autor);
        } else {
            System.out.println("No se pudo crear el libro porque no hay datos del autor");
            return null;
        }
    }

    private void agregarLibroPorNombre(){
        System.out.println("Escribe el nombre del libro que deseas buscar");
        ResultadoLibros datos= getDatosLibro();
        if(!datos.resultados().isEmpty()){
            DatosLibros datosLibros = datos.resultados().get(0);
            DatosAutor datosAutor = datosLibros.autor().get(0);
            Libro libro = null;
            Libro libroRepositorio = libroRepository.findByTituloContainsIgnoreCase(datosLibros.titulo());
            if(libroRepositorio != null){
                System.out.println("El libro ya se encuentra en la base de datos");
                System.out.println(libroRepositorio.toString());
            } else {
                Autor autorRepositorio = autorRepository.findByNombreIgnoreCase(datosLibros.autor().get(0).nombre());
                if(autorRepositorio != null){
                    libro = libroCreado(datosLibros,autorRepositorio);
                    libroRepositorio.save(libro);
                    System.out.println("Libro agregado a la base de datos");
                    System.out.println(libro);
                } else{
                   Autor autor = new Autor((datosAutor));
                   autor = autorRepositorio.save(autor);
                   libro = libroCreado(datosLibros,autor);
                   datosLibros.save(libro);
                    System.out.println("Libro agregado a la base de datos");
                    System.out.println(libro);
                }
            }
        }else {
            System.out.println("El libro no existe dentro de la API, por favor ingresa otro");
        }
    }

    private void mostrarLibrosBuscados(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos");
            return;
        } else {

        System.out.println("******** Libros registrados en la base ******** \n");
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
        }
    }

    private void buscarLibroPorNombre(){
        System.out.println("Escriba el nombre del libro");
        var titulo = teclado.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if(libroBuscado != null){
            System.out.println("El libro encontrado es: " + libroBuscado);
        } else {
            System.out.println("El libro " + libroBuscado + " no se encuentra en la base de datos");
        }
    }

    private void mostrarAutoresDeLibros(){
        List<Autor> autores = autorRepository.findAll();

        if(autores.isEmpty()){
            System.out.println("No se encontraron libros con el autor en la base de datos \n");
        } else {
            System.out.println("Libros encontrados con el autor en la base de datos \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores){
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre() + "\n");
                }
            }
        }
    }

    private void buscarLibroPorIdioma(){
        System.out.println("Ingrese el idioma de los libros que desea buscar (es (español) o en (ingles)): \n");
        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdiomaContaining(idioma);

        if(librosPorIdioma.isEmpty()){
            System.out.println("No se encontraron libros con ese idioma en la base de datos");
        }else {
            System.out.println("Libros encontrados en el idioma escogido ");
            for (Libro libro: librosPorIdioma){
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarAutorPorAno(){
        System.out.println("Indica el año donde desea buscar autores: ");
        var añoBuscado = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autoresEnAño = autorRepository.findByNacimientoLessThanEqualOrFallecimientoGreaterThanEqual(añoBuscado,añoBuscado);

        if(autoresEnAño.isEmpty()){
            System.out.println("Nose encontraron autores en el año " + añoBuscado);
        } else {
            System.out.println("Los autores encontrados en el año " + añoBuscado );
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor: autoresEnAño){
                if(autor.getNacimiento() != null && autor.getFallecimiento() !=null){
                    if(autor.getNacimiento() <= añoBuscado && autor.getFallecimiento() >= añoBuscado){
                        if(autoresUnicos.add(autor.getNombre())){
                            System.out.println("Autor - " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }

    private void top10LibrosMasDescargados(){
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();
        if(!top10Libros.isEmpty()){
            top10Libros.forEach(l-> System.out.printf("Libro: %s Autor: %s Descargas: %s\n",
                           l.getTitulo(), l.getAutor().getNombre(), l.getNumeroDescargas()));
        }
    }

}
