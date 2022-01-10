package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
	    cola = new Cola<Vertice>();
	    if (raiz !=null) cola.mete(raiz);	    
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return !cola.esVacia();	    
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
	    Vertice v = cola.saca();
	    if (v.hayIzquierdo()) cola.mete(v.izquierdo);
	    if (v.hayDerecho()) cola.mete(v.derecho);
	    return v.elemento;		    
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if (elemento == null) throw new IllegalArgumentException(); 
	Vertice vertice = nuevoVertice(elemento);
	elementos += 1;

	if (raiz == null) raiz = vertice;
	else {
	    Cola<Vertice> cola = new Cola<Vertice>();
	    cola.mete(raiz);
	    while(!cola.esVacia()) {
		Vertice v = cola.saca();
		if (!v.hayIzquierdo()) {
		    v.izquierdo  = vertice;
		    vertice.padre = v;
		    break;
		} else if (!v.hayDerecho()) {
		    v.derecho = vertice;
		    vertice.padre = v;
		    break;
		}
		cola.mete(v.izquierdo);
		cola.mete(v.derecho);
	    }
	}	
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if (busca(elemento) == null) return;
	Vertice v = vertice(this.busca(elemento));
	Vertice v2 = raiz;
	if (v == null) return;
	elementos -= 1;
	if (elementos == 0) {
	    raiz = null;
	} else {
	    Cola<Vertice> cola = new Cola<Vertice>();
	    cola.mete(raiz);

	    while(!cola.esVacia()) {
		v2 = cola.saca();
		if (v2.hayIzquierdo()) cola.mete(v2.izquierdo);
		if (v2.hayDerecho()) cola.mete(v2.derecho);
	    }
	    T temp = v2.elemento;
	    v2.elemento = elemento;
	    v.elemento = temp;
	    Vertice vpadre = v2.padre;
	    if (v2.padre.izquierdo.equals(v2)){
		v2.padre.izquierdo = null;
	    } else if (v2.padre.derecho.equals(v2)){
		v2.padre.derecho = null;
	    } 
	}	    
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
	if (elementos == 0) return -1;
	
	int n = elementos;
	if (n == 1) return 0;
	
	int c = 0;
	while (n > 1) {
	    n =  n/2;
	    c += 1;
	}
	return c;	
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	if (raiz == null) return;
	else {
	    Cola<Vertice> c = new Cola<Vertice>();
	    c.mete(raiz);
	    while (!c.esVacia()) {
		Vertice v = c.saca();
		accion.actua(v);
		if (v.izquierdo != null) c.mete(v.izquierdo);
		if (v.derecho != null) c.mete(v.derecho);	
	    }
	}	
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
