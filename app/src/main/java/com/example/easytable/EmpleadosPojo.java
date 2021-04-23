package com.example.easytable;

public class EmpleadosPojo {

    private String Apellidos, Password, Correo, ID, Nombre, Telefono, Usuario, tipoDeUsuario;

    public EmpleadosPojo(){

    }

    public EmpleadosPojo(String Apellidos, String Password, String Correo, String ID,
                         String Nombre, String Telefono, String Usuario, String tipoDeUsuario)
    {
        this.Apellidos = Apellidos;
        this.Password = Password;
        this.Correo = Correo;
        this.ID = ID;
        this.Nombre = Nombre;
        this.Telefono = Telefono;
        this.Usuario = Usuario;
        this.tipoDeUsuario = tipoDeUsuario;
    }


    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        this.Apellidos = apellidos;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        this.Correo = correo;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        this.Usuario = usuario;
    }

    public String getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public void setTipoDeUsuario(String tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
    }
}
