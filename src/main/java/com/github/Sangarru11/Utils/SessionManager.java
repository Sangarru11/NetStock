package com.github.Sangarru11.Utils;

import com.github.Sangarru11.Model.Entity.Usuario;

public class SessionManager {

    private static Usuario usuarioLogueado;

    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}