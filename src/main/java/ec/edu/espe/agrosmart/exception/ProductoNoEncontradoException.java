package ec.edu.espe.agrosmart.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException() {
        super("Producto no encontrado");
    }
}