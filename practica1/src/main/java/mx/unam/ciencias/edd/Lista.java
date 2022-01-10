package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            // Aquí va su código.
	    this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            // Aquí va su código.
	    anterior = null;
	    siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            // Aquí va su código.
	    if (siguiente == null) throw new NoSuchElementException();
	    Nodo s = siguiente;
	    anterior = s;
	    siguiente = s.siguiente;
	    return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            // Aquí va su código.
	    return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            // Aquí va su código.
	    if (anterior == null) throw new NoSuchElementException();
	    Nodo a = anterior;
	    siguiente = a;
	    anterior = a.anterior;
	    return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            // Aquí va su código.
	    anterior = null;
	    siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            // Aquí va su código.
	    anterior = rabo;
	    siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        // Aquí va su código.
	return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	if (cabeza == null) return true;
	else return false;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException();
	Nodo n = new Nodo(elemento);
	longitud++;
	if (rabo == null){
	    cabeza = rabo = n;
	} else {
	    rabo.siguiente = n;
	    n.anterior = rabo;
	    rabo = n;
	}
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException();
	Nodo n = new Nodo(elemento);
	longitud++;
	if (rabo == null){
	    cabeza = rabo = n;
	} else {
	    rabo.siguiente = n;
	    n.anterior = rabo;
	    rabo = n;
	}
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException();
	Nodo n = new Nodo(elemento);
	longitud++;
	if (cabeza == null){
	    cabeza = rabo = n;
	} else {
	    cabeza.anterior = n;
	    n.siguiente = cabeza;
	    cabeza = n;
	}
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException();
	if (i <= 0){
	    agregaInicio(elemento);
	    return;
	}
	if (i >= longitud){
	    agregaFinal(elemento);
	    return;
	}
	longitud++;
	Nodo m = getNodo(i);
	Nodo n = new Nodo(elemento);
	m.anterior.siguiente = n;
	n.anterior = m.anterior;
	n.siguiente = m;
	m.anterior = n;
    }
     /**
     * Regresa el <em>i</em>-ésimo nodo de la lista.
     * @param i el índice del nodo que queremos.
     * @return el <em>i</em>-ésimo nodo de la lista, o <code>null</code> si
     *         <em>i</em> es menor que cero o mayor o igual que el número de
     *         nodos en la lista.
     */
    private Nodo getNodo(int i){
	if((i<0) || (i>=longitud))
	    return null;
	int c = 0;
	Nodo n = cabeza;
	while(c != i){
	    n = n.siguiente;
	    c++;
	}
	return n;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if (getNodo(elemento) == null)
	    return;
	Nodo n = getNodo(elemento);
	eliminaNodo(n);
    }

    /**
     * Regresa el nodo del elemento que manda llamar el método.
     * @return el nodo del elemento que manda llamar el método.
     */
    private Nodo getNodo(Object elemento){
	Nodo n = cabeza;
	while (n != null){
	    if (n.elemento.equals(elemento))
		return n;
	    n = n.siguiente;
	}
	return null;
    }

    /**
     * Elimina el nodo de la lista.
     * @param nodo el nodo a eliminar.
     */
    private void eliminaNodo(Nodo node){
	if (node == null || cabeza == null)
	    return;
	longitud--;
	if (cabeza.equals(rabo)) limpia();
	else if (cabeza.equals(node)){
	    node.siguiente.anterior = null;
	    cabeza = node.siguiente;
	} else if (rabo.equals(node)){
	    node.anterior.siguiente = null;
	    rabo = node.anterior;
	} else {
	    node.anterior.siguiente = node.siguiente;
	    node.siguiente.anterior = node.anterior;
	}
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        // Aquí va su código.
	if (cabeza == null) throw new NoSuchElementException();
	Nodo primero = cabeza;
	eliminaNodo(cabeza);
	return primero.elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        // Aquí va su código.
	if(rabo == null) throw new NoSuchElementException();
	Nodo ultimo = rabo;
	eliminaNodo(rabo);
	return ultimo.elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	if (getNodo(elemento) != null)
	    return true;
	return false;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        // Aquí va su código.
	Lista<T> lista = new Lista<T>();
	Nodo n = cabeza;
	while (n != null){
	    lista.agregaInicio(n.elemento);
	    n = n.siguiente;
	}
	return lista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        // Aquí va su código.
	Lista<T> lista = new Lista<T>();
	Nodo n = cabeza;
	while (n != null){
	    lista.agregaFinal(n.elemento);
	    n = n.siguiente;
	}
	return lista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
	cabeza = null;
	rabo = null;
	longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        // Aquí va su código.
	if (cabeza == null) throw new NoSuchElementException();
	return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        // Aquí va su código.
	if (rabo == null) throw new NoSuchElementException();
	return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        // Aquí va su código.
	if((i < 0) || (i >= longitud)) throw new ExcepcionIndiceInvalido();
	int c = 0;
	Nodo n = cabeza;
	while(n != null){
	    if (c == i) return n.elemento;
	    n = n.siguiente;
	    c++;
	}
	return null;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        // Aquí va su código.
	int c = 0;
	int a = -1;
	Nodo n = cabeza;
	while (n != null){
	    if (n.elemento.equals(elemento))
		return c;
	    c++;
	    n = n.siguiente;
	}
	return a;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        // Aquí va su código.
        String s = "[]";
	if (cabeza == null) return s;
	
	Nodo n = cabeza;
	String cadena = "[" + n.elemento.toString();
	
	while (n.siguiente != null){
	    cadena = cadena + ", " + n.siguiente.elemento.toString();
	    n = n.siguiente;
	}
	return cadena + "]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        // Aquí va su código.
	if (lista == null) return false;
	else if (lista.longitud != longitud) return false;
	else {
	    Nodo m = cabeza;
	    Nodo n = lista.cabeza;
	while (m != null){
	    if (m.elemento.equals(n.elemento)){
		m = m.siguiente;
		n = n.siguiente;
	    }
	    else return false;
	}
	return true;
	}
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
}
