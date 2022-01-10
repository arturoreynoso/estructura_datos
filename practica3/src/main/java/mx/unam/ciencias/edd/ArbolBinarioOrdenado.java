package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
	    pila = new Pila<Vertice>();
	    Vertice v = raiz;
	    if (v != null) {
		pila.mete(v);
		
		while(v.hayIzquierdo()){
		    pila.mete(v.izquierdo);
		    v = v.izquierdo;
		}
	    }
        }
        

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
	    Vertice v = pila.saca();
	    T e = v.elemento;
	    v = v.derecho;
	    while (v != null) {
		pila.mete(v);
		v = v.izquierdo;
	    }
	    return e;
	}
    }	    

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    private void compara(Vertice v1, Vertice v2) {
	if (v1 == null) return;
	if (v2.get().compareTo(v1.get())<=0) {
	    if (v1.izquierdo == null) {
		v1.izquierdo = ultimoAgregado = v2;
		v2.padre = v1;
	    } else {
		compara(v1.izquierdo, v2);
	    }
	} else {
	    if (v1.derecho == null) {
		v1.derecho = ultimoAgregado = v2;
		v2.padre = v1;
	    } else {
		compara(v1.derecho, v2);
	    }
	}
    } 

    
    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException();
	Vertice vertice = nuevoVertice(elemento);
	elementos += 1;
	if (raiz == null) raiz = ultimoAgregado = vertice;
	else {
	    compara(raiz, vertice);
	}
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if (busca(elemento) == null || elemento == null) return;
	Vertice v = vertice(busca(elemento));
	elementos -= 1;

	// Si no tiene hijos
	if (!v.hayIzquierdo() && !v.hayDerecho()){
	    if (v.equals(raiz))
		raiz = null;
	    else {
		if (esIzquierdo(v)) 
		    v.padre.izquierdo = v.padre = null;
		else v.padre.derecho = v.padre = null;
	    }
	}
	// Si hay hijo izquierdo pero no derecho
	else if (v.hayIzquierdo() && !v.hayDerecho()) {
	    if (v.equals(raiz)) {
		raiz = v.izquierdo;
		raiz.padre = null;
	    } else {
		v.izquierdo.padre = v.padre;
		if (esIzquierdo(v))
		    v.padre.izquierdo = v.izquierdo;
		else 
		    v.padre.derecho = v.izquierdo;
	    }
	}
	// Si hay hijo derecho pero no izquierdo 
	else if (!v.hayIzquierdo() && v.hayDerecho()) {
	    if (v.equals(raiz)) {
		raiz = v.derecho;
		raiz.padre = null;
	    } else {
		v.derecho.padre = v.padre;
		if (esIzquierdo(v))
		    v.padre.izquierdo = v.derecho;
		else
		    v.padre.derecho = v.derecho;
	    }   
	} else {
	    // Si sus dos hijos son distintos de cero
	    Vertice u = intercambiaEliminable(v);
	    eliminaVertice(u);
	}
    }

    protected Vertice maximoEnSubarbol(Vertice v) {
	if (v.derecho == null) return v;
	return maximoEnSubarbol(v.derecho);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
	Vertice u =  maximoEnSubarbol(vertice.izquierdo);
	T e1 = u.elemento;
	T e = vertice.elemento;
	vertice.elemento = e1;
	u.elemento = e;
	return u;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
	Vertice u;
	if (vertice.hayIzquierdo())
	    u = vertice.izquierdo;
	else if (vertice.hayDerecho())
	    u = vertice.derecho;
	else u = vertice.izquierdo;
	
	if (vertice.padre != null) {
	    if (esIzquierdo(vertice)) {
		vertice.padre.izquierdo = u;
	    } else if (esDerecho(vertice)) {
		vertice.padre.derecho = u;
	    }
	} else {
	    raiz = u;
	    raiz.padre = null;
	}

	if (u != null) u.padre = vertice.padre;
    }

    protected boolean esIzquierdo(Vertice v) {
	if (v.padre.hayIzquierdo())
	    return v.padre.izquierdo.equals(v);
	return false;
    }

    protected boolean esDerecho(Vertice v) {
	if (v.padre.hayDerecho())
	    return v.padre.derecho.equals(v);
	return false;
    }

    protected VerticeArbolBinario<T> busca(T elemento, Vertice vertice) {
	if (vertice == null || elemento == null) return null;
	if (vertice.elemento.equals(elemento)) return vertice;
	if (vertice.elemento.compareTo(elemento)>0) {
	    return busca(elemento, vertice.izquierdo);
	} else return busca(elemento, vertice.derecho);
    }
    
    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
	return busca(elemento, this.raiz);	
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	if (vertice == null || !vertice.hayIzquierdo()) return;

	Vertice v = vertice(vertice);
      	Vertice vIzq = v.izquierdo;
	vIzq.padre = v.padre;
	
	if (v.equals(raiz)) {
	    raiz = vIzq;
	} else {
	    if (esIzquierdo(v)) {
		v.padre.izquierdo = vIzq;
	    } else v.padre.derecho = vIzq;
	}
	
	v.izquierdo = vIzq.derecho;
	if (vIzq.hayDerecho()) v.izquierdo.padre = v;
	vIzq.derecho = v;
	v.padre = vIzq;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	if (vertice == null || !vertice.hayDerecho()) return;

	Vertice v = vertice(vertice);
	Vertice vDer = v.derecho;
	vDer.padre = v.padre;

	if (v.equals(raiz)) {
	    raiz = vDer;
	} else {
	    if (esIzquierdo(v)) {
		v.padre.izquierdo = vDer;
	    } else v.padre.derecho = vDer;
	}

	v.derecho = vDer.izquierdo;
	if (vDer.hayIzquierdo()) v.derecho.padre = v;
	vDer.izquierdo = v;
	v.padre = vDer;
    }
    
    public void dfsPreOrder(Vertice v, AccionVerticeArbolBinario<T> accion) {
	if (v == null) return;
	accion.actua(v);
	dfsPreOrder(v.izquierdo, accion);
	dfsPreOrder(v.derecho, accion);
    }
    
    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	dfsPreOrder(raiz, accion);
    }

    public void dfsInOrder(Vertice v, AccionVerticeArbolBinario<T> accion) {
	if (v == null ) return;
	dfsInOrder(v.izquierdo, accion);
	accion.actua(v);
	dfsInOrder(v.derecho, accion);
    }
    
    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	dfsInOrder(raiz, accion);
    }

    public void dfsPostOrder(Vertice v, AccionVerticeArbolBinario<T> accion) {
	if (v == null) return;
	dfsPostOrder(v.izquierdo, accion);
	dfsPostOrder(v.derecho, accion);
	accion.actua(v);
    }
   
    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	dfsPostOrder(raiz, accion);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
