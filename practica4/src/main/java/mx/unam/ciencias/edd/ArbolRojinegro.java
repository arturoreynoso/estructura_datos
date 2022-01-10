package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
	    super(elemento);
	    this.color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            // Aquí va su código.
	    return String.format("%s{%s}", color == Color.ROJO ? "R" : "N", elemento.toString());
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro) objeto;
            // Aquí va su código.
	    return (this.color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        // Aquí va su código.
	super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
	return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	VerticeRojinegro v = (VerticeRojinegro) vertice;
	return v.color;
    }

    private boolean esRojo(VerticeRojinegro vertice) {
	return vertice != null && vertice.color == Color.ROJO;
    }

    private boolean esNegro(VerticeRojinegro vertice) {
	return vertice == null || vertice.color == Color.NEGRO;
    }

    private boolean hasTio(VerticeRojinegro v) {
	return getTio(v) != null;
    }
    
    private VerticeRojinegro getTio(VerticeRojinegro v) {
	if (v.padre == null) return null;
	VerticeRojinegro p = (VerticeRojinegro) v.padre;
	if (p.padre == null) return null;
	VerticeRojinegro a = (VerticeRojinegro) p.padre;
	if (esDerecho(p))
	    return (VerticeRojinegro) a.izquierdo;
	else
	    return (VerticeRojinegro) a.derecho;
    }

    private VerticeRojinegro getAbuelo(VerticeRojinegro v) {
	if (v.padre == null) return null;
	VerticeRojinegro p = (VerticeRojinegro) v.padre;
	return p.padre == null ? null : (VerticeRojinegro) p.padre; 
    }
    
    protected void rebalanceaAgrega(VerticeRojinegro v) {
	// Caso 1. EL vértice tiene padre null.
	if (v.padre == null) {
	    v.color = Color.NEGRO;
	    return;
	}

	// Caso 2. El vértice padre es negro.
	if (getColor(v.padre) == Color.NEGRO) return;
	
	VerticeRojinegro p = (VerticeRojinegro) v.padre;
	VerticeRojinegro a = getAbuelo(v);
	VerticeRojinegro t =  getTio(v);
	// Caso 3. EL vertice t (tío de v) es rojo.;
	if (hasTio(v) && esRojo(t)) {
		t.color = Color.NEGRO;
		p.color = Color.NEGRO;
		a.color = Color.ROJO;
		rebalanceaAgrega(a);
		return;
	}
	// Caso 4. El vértice v y su padre p están cruzados
	if (esIzquierdo(v) && esDerecho(p)) {
		super.giraDerecha(p);
		VerticeRojinegro temp = v;
		v = p;
		p = temp;
	} else if (esIzquierdo(p) && esDerecho(v)) {
		super.giraIzquierda(p);
		VerticeRojinegro temp = v;
		v = p;
		p = temp;		
	}
	// Caso 5. El vértice v y su padre p no están cruzados
	p.color = Color.NEGRO;
	a.color = Color.ROJO;
	if (esIzquierdo(v)) {
	    super.giraDerecha(a);
	} else {
	    super.giraIzquierda(a);
	}
	return;
    }


    
    
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	VerticeRojinegro v = (VerticeRojinegro) ultimoAgregado;
	v.color = Color.ROJO;
	rebalanceaAgrega(v);
    }

    private VerticeRojinegro getHermano(VerticeRojinegro v) {
	if (esIzquierdo(v))
	    return (VerticeRojinegro) v.padre.derecho;
	return (VerticeRojinegro) v.padre.izquierdo;
    }


    private void rebalanceaElimina(VerticeRojinegro v) {
	// Caso 1. El vértice v tiene padre = null
	if (!v.hayPadre()) return;
	VerticeRojinegro p = (VerticeRojinegro) v.padre;
	VerticeRojinegro h = getHermano(v);

	// Caso 2. EL vértice h es rojo y por lo tanto p es negro.
	if (h.color == Color.ROJO) {
	    p.color = Color.ROJO;
	    h.color = Color.NEGRO;
	    if (esIzquierdo(v)) super.giraIzquierda(p);
	    else super.giraDerecha(p);
	}

	h = getHermano(v);
	VerticeRojinegro hIzq = (VerticeRojinegro) h.izquierdo;
	VerticeRojinegro hDer = (VerticeRojinegro) h.derecho;

	// Caso 3. El vértice v, h, hIzq y hDer son negros.
	// Coloreamos a h de rojo, hacemos recursión sobre p y terminamos.
	if (esNegro(p) && esNegro(h) && esNegro(hIzq) && esNegro(hDer)) {
	    h.color = Color.ROJO;
	    rebalanceaElimina(p);
	    return;
	}
	// Caso 4. Los vértices h, hIzq, hDer son negros y p es rojo.
	// Coloreamos h de rojo, a p de negro y terminamos.
	if (esNegro(h) && esNegro(hIzq) && esNegro(hDer) && esRojo(p)) {
	    h.color = Color.ROJO;
	    p.color = Color.NEGRO;
	    return;
	}

	// Caso 5. El vértice v es izquierdo, hIzq es rojo y hDer es negro;
	// o v es derecho, hIzq es negro y hDer es rojo. Coloreamos h de rojo,
	// al hijo rojo de h de negro y giramos sobre h en la dirección
	// contraria a v;
	if (esIzquierdo(v) && esRojo(hIzq) && esNegro(hDer) ||
	    esDerecho(v) && esNegro(hIzq) && esRojo(hDer)) {
	    h.color = Color.ROJO;
	    if (esRojo(hIzq)) hIzq.color = Color.NEGRO;
	    if (esRojo(hDer)) hDer.color = Color.NEGRO;
	    if (esIzquierdo(v)) super.giraDerecha(h);
	    if (esDerecho(v)) super.giraIzquierda(h);
	    h = getHermano(v);
	    hIzq = (VerticeRojinegro) h.izquierdo;
	    hDer = (VerticeRojinegro) h.derecho;
	}

	// Caso 6. El vértice v es izquierdo y hDer es rojo o el vértice v
	// es derecho y hIzq es rojo. No hay que verificar nada. COloreamos
	// Coloreamos h con el mismo color de p, a p de negro, al hijo de h
	// con dirección contraria a v de negro y giramos sobre p en la
	// dirección de v.
	if (esNegro(p)) h.color = Color.NEGRO;
	else h.color = Color.ROJO;
	p.color = Color.NEGRO;
	if (esIzquierdo(v)) {
	    hDer.color = Color.NEGRO;
	    super.giraIzquierda(p);
	} 
	else {
	    hIzq.color = Color.NEGRO;
	    super.giraDerecha(p);
	} 
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	VerticeRojinegro fantasma = null;
	VerticeRojinegro hijo = null;
	
	if (busca(elemento) == null || elemento == null) return;
	VerticeRojinegro v = (VerticeRojinegro) vertice(busca(elemento));
	elementos -=1;

	// Si no tiene hijos
	if (!v.hayIzquierdo() && !v.hayDerecho()) {
	    if (v.equals(raiz))
		raiz = null;
	    else {
		fantasma = (VerticeRojinegro) nuevoVertice(null);
		fantasma.color = Color.NEGRO;
		fantasma.padre = v;
		v.izquierdo = fantasma;
		hijo = getUnicoHijo(v);
		eliminaVertice(v);
		if (getColor(hijo) == Color.ROJO) {
		    hijo.color = Color.NEGRO;
		    return;
		}
		if (getColor(v) == Color.ROJO) {}
		if (getColor(v) == Color.NEGRO &&
		    getColor(hijo) == Color.NEGRO) {
		    rebalanceaElimina(hijo);
		}
		eliminaVertice(fantasma);
	    }
	}
	// Si hay un solo hijo
	else if ((v.hayIzquierdo() && !v.hayDerecho()) || !v.hayIzquierdo() && v.hayDerecho()) {
	    VerticeRojinegro h = getUnicoHijo(v);
	    eliminaVertice(v);
	    if (getColor(h) == Color.ROJO){
		h.color = Color.NEGRO;
		return;
	    }	
	    if (getColor(v) == Color.ROJO) {}
	    if (getColor(v) == Color.NEGRO &&
		getColor(h) == Color.NEGRO) {
		rebalanceaElimina(h);
	    }
	    return;
	}
	// Cuando ambos hijos son distintos de null
	else {
	    VerticeRojinegro u = (VerticeRojinegro) intercambiaEliminable(v);
            //Si no tiene hijos 
	    if (!u.hayIzquierdo() && !u.hayDerecho()) {
		if (u.equals(raiz))
		    raiz = null;
		else {
		    fantasma = (VerticeRojinegro) nuevoVertice(null);
		    fantasma.color = Color.NEGRO;
		    fantasma.padre = u;
		    u.izquierdo = fantasma;
		    hijo = getUnicoHijo(u);
		    eliminaVertice(u);
		    if (getColor(hijo) == Color.ROJO) {
			hijo.color = Color.NEGRO;
			return;
		    }
		    if (getColor(u) == Color.ROJO) {}
		    if (getColor(u) == Color.NEGRO &&
			getColor(hijo) == Color.NEGRO) {
			rebalanceaElimina(hijo);
		    }
		    eliminaVertice(fantasma);
		}
	    }
	    // Si tiene un solo hijo

	    else if ((u.hayIzquierdo() && !u.hayDerecho()) || !u.hayIzquierdo() && u.hayDerecho()) {
		VerticeRojinegro h = getUnicoHijo(u);
		eliminaVertice(u);
		if (getColor(h) == Color.ROJO){
		    h.color = Color.NEGRO;
		    return;
		}	
		if (getColor(u) == Color.ROJO) {}
		if (getColor(u) == Color.NEGRO &&
		    getColor(h) == Color.NEGRO) {
		    rebalanceaElimina(h);
		}
		return;
	    }
	}
    }
    
    
    private VerticeRojinegro getUnicoHijo(VerticeRojinegro v) {
	if (v.hayIzquierdo()) return (VerticeRojinegro) v.izquierdo;
	else return (VerticeRojinegro) v.derecho;
    }

    // Sube único hijo distinto de null
    private void subeUnicoHijo(VerticeRojinegro v) {
	if (v.hayIzquierdo() && !v.hayDerecho()) {
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
	}
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
