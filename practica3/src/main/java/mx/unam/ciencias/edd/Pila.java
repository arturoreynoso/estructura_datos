package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        // Aquí va su código.
	String s = "";
	if (cabeza == null) return s;

	Nodo n = cabeza;
	String cadena = n.elemento.toString();

	while(n.siguiente != null){
	    cadena = cadena + "\n" + n.siguiente.elemento.toString();
	    n = n.siguiente;
	}
	return cadena + "\n";
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException();
	Nodo n = new Nodo(elemento);
	if (cabeza == null) {
	    cabeza = n;
	    rabo = n;
	} else {
	    n.siguiente = cabeza;
	    cabeza = n;
	}
    }
}
