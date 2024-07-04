package com.company.literatura.servicios;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class <T> clase);
}
