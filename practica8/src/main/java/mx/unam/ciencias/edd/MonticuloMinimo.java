package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return (indice<elementos) && arbol[indice] != null;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
	    if (indice >= elementos) {
		throw new NoSuchElementException();
	    }
	    indice += 1;	    
	    return arbol[indice-1];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            // Aquí va su código.
	    this.elemento = elemento;
	    this.indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            // Aquí va su código.
	    return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
	    this.indice = indice; 
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            // Aquí va su código.
	    return this.elemento.compareTo(adaptador.elemento); 
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        // Aquí va su código.
	this.arbol = nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        // Aquí va su código.
	this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        // Aquí va su código.
	this.arbol = this.nuevoArreglo(n);
	int i = 0;
	for (T e: iterable) {
	    this.arbol[i] = e;
	    e.setIndice(i);
	    i += 1;
	}
	elementos = n;
	
	for(int j = (n-1)/2; j>=0; j--) {
	    acomodaHaciaAbajo(this.arbol[j]);
	}

    }

    private void intercambia(T u, T v) {
	int aux = v.getIndice();
	arbol[u.getIndice()] = v;
	arbol[v.getIndice()] = u;
	v.setIndice(u.getIndice());
	u.setIndice(aux);
    }
    
    private void acomodaHaciaArriba(T elemento) {
	int padre;
	int v;
	if (elemento == null)
	    return;
	v = elemento.getIndice();
	padre = (v-1)/2;

	if (!indiceValido(padre) ||
	    this.arbol[padre].compareTo(this.arbol[v]) <= 0)
	    return;

	intercambia(elemento, this.arbol[padre]);
	acomodaHaciaArriba(elemento);
    }

    private void acomodaHaciaAbajo(T elemento) {
	int izq, der;
	int indice;
	int indiceDeMenor;
	if (elemento == null)
	    return;
	indice = elemento.getIndice();
	izq = 2*indice + 1;
	der = 2*indice + 2;
	
	if (indiceValido(der) && indiceValido(izq)) {
	    if (this.arbol[izq].compareTo(this.arbol[der])<0){
		indiceDeMenor = izq;
	    } else {
		indiceDeMenor = der;
	    }
	    if (this.arbol[indiceDeMenor].compareTo(this.arbol[indice])<0) {
		intercambia(elemento, this.arbol[indiceDeMenor]);
		acomodaHaciaAbajo(elemento);
	    }	    
	}

	if (indiceValido(der) && !indiceValido(izq)) {
	    if (this.arbol[der].compareTo(this.arbol[indice])<0) {
		intercambia(elemento, this.arbol[der]);
		acomodaHaciaAbajo(elemento);
	    }	    
	}
	
	if (indiceValido(izq) && !indiceValido(der)) {
	    if (this.arbol[izq].compareTo(this.arbol[indice])<0) {
		intercambia(elemento, this.arbol[izq]);
		acomodaHaciaAbajo(elemento);
	    }	    
	}
    }

    private boolean indiceValido(int n) {
        return !(n <0 || n >= this.arbol.length || arbol[n] == null);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	T[] arreglo;
	int i = 0;
	if (elementos >= arbol.length) {
	    arreglo = this.arbol;
	    this.arbol = nuevoArreglo(2*elementos);
	    for (T e:arreglo)
		this.arbol[i++] = e;
	}
	arbol[elementos] = elemento;
	elemento.setIndice(elementos++);
	acomodaHaciaArriba(elemento);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        // Aquí va su código.
	if (this.esVacia())
	    throw new IllegalStateException();
	T raiz = arbol[0];
	intercambia(raiz, arbol[--elementos]);
	raiz.setIndice(-1);
	arbol[elementos] = null;
	acomodaHaciaAbajo(arbol[0]);
	return raiz;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	int indice = elemento.getIndice();
	if (indice < 0 || indice >= elementos)
	    return;
	intercambia(elemento, this.arbol[--elementos]);
	arbol[elementos] = null;
	elemento.setIndice(-1);
	reordena(this.arbol[indice]);
	
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	int indice; 
	if (elemento == null)
	    return false;
	indice = elemento.getIndice();
	if (indice <0 || indice >= elementos)
	    return false;
	if (arbol[indice].equals(elemento))
	    return true;
	return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
	elementos = 0;
	for (T e:arbol) {
	    e = null;
	}
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        // Aquí va su código.
	if (elemento == null)
	    return;
	acomodaHaciaArriba(elemento);
	acomodaHaciaAbajo(elemento);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        // Aquí va su código.
	if (i <0 || i>= elementos)
	    throw new NoSuchElementException();
	return this.arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        // Aquí va su código.
	String s = "";
	for (int i = 0; i < elementos; i++)
	    s += get(i).toString() + ", "; 
		//s += String.format("%s, ", get(i).toString());
	return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        // Aquí va su código.
	if (monticulo.elementos == this.elementos) {
	    for (int i = 0; i < elementos; i++) {
		if (!arbol[i].equals(monticulo.arbol[i]))
		    return false;
	    }
	    return true;
	}
	return false;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        // Aquí va su código.
	Lista<Adaptador<T>> lista1 = new Lista<Adaptador<T>>();
	Lista<T> lista2 = new Lista<T>();
	Adaptador<T> adaptador;
	int indice = 0;
	for (T c:coleccion) {
	    adaptador = new Adaptador<T>(c);
	    adaptador.indice = indice++;
	    lista1.agrega(adaptador);
	}

	MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo(lista1);
	while(monticulo.elementos > 0) {
	    lista2.agrega(monticulo.get(0).elemento);
	    monticulo.elimina();
	}
	return lista2;
    }
}
