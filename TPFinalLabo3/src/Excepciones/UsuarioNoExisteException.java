package Excepciones;

public class UsuarioNoExisteException extends Exception {
    public UsuarioNoExisteException(){
    super("El usuario no existe") ;
    }
}

