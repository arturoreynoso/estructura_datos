package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
	    iterador = vertices.iterator();
	    
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
	    Vertice v = iterador.next();
	    return v.elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
	    this.elemento = elemento;
	    this.color = Color.NINGUNO;
	    this.vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
	    return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
	    return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
	    return this.color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
	    return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
	this.vertices = new Lista<Vertice>();
	this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
	return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if (elemento == null || contiene(elemento)) throw new IllegalArgumentException();
	Vertice v = new Vertice(elemento);
	vertices.agrega(v);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
	if (!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	if (a.equals(b) || sonVecinos(a,b)) throw new IllegalArgumentException();
	Vertice av = (Vertice) vertice(a);
	Vertice bv = (Vertice) vertice(b);
	av.vecinos.agrega(bv);
	bv.vecinos.agrega(av);
	aristas += 1;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
	if (!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	if (!sonVecinos(a,b)) throw new IllegalArgumentException();
	Vertice av = (Vertice) vertice(a);
	Vertice bv = (Vertice) vertice(b);
	av.vecinos.elimina(bv);
	bv.vecinos.elimina(av);
	aristas -= 1;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	for (Vertice v:vertices) {
	    if (v.elemento.equals(elemento))
		return true;
	}
	return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	if (!this.contiene(elemento)) throw new NoSuchElementException();
	Vertice v = (Vertice) vertice(elemento);
	vertices.elimina(v);
	for (Vertice w:v.vecinos){
	    if (w.vecinos.contiene(v)){
		w.vecinos.elimina(v);
		aristas -= 1;
	    }   
	}
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
	if (!contiene(a) || !contiene(b)) throw new NoSuchElementException();
	Vertice av = (Vertice) vertice(a);
	Vertice bv = (Vertice) vertice(b);
	return (av.vecinos.contiene(bv) && bv.vecinos.contiene(av));
    }

    private Vertice busca(T elemento) {
	for (Vertice v:vertices) {
	    if (v.elemento.equals(elemento))
		return v;
	}
	throw new NoSuchElementException();
    } 
    
    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
	return this.busca(elemento);
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        // Aquí va su código.	
	if(vertice.getClass() != Vertice.class)
	    throw new IllegalArgumentException();
	Vertice v = (Vertice) vertice;
	v.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
	Cola<Vertice> cola = new Cola<Vertice>();
	Vertice v = null;
	for (Vertice u:vertices) {
	    u.color = Color.ROJO;
	}

	for (Vertice u:vertices) {
	    v = u;
	    break;
	}

	v.color = Color.NEGRO;
	cola.mete(v);

	while(!cola.esVacia()) {
	    Vertice u = cola.saca();
	    u.color = Color.NEGRO;
	    for (Vertice vecino: u.vecinos) {
		if (vecino.color == Color.ROJO) {
		    vecino.color = Color.NEGRO;
		    cola.mete(vecino);
		}
	    }
	}

	for (Vertice u:vertices) {
	    if (u.color == Color.ROJO)
		return false;
	}
	return true;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
	for (Vertice v:vertices)
	    accion.actua(v);
    }


    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
	Cola<Vertice> cola = new Cola<Vertice>();
	buscaActua(elemento, accion, cola);
    }

    private void buscaActua(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> metesaca) {
	if (!this.contiene(elemento))
	    throw new NoSuchElementException();
	Vertice w = (Vertice) vertice(elemento);
	for (Vertice v:vertices)
	    v.color = Color.ROJO;
	w.color = Color.NEGRO;
	metesaca.mete(w);

	while(!metesaca.esVacia()) {
	    Vertice u = metesaca.saca();
	    accion.actua(u);
	    for (Vertice vertice:u.vecinos) {
		if (vertice.color == Color.ROJO) {
		    vertice.color = Color.NEGRO;
		    metesaca.mete(vertice);
		}
	    }
	}
	paraCadaVertice(v -> setColor(v, Color.NINGUNO));
    }
	
    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
	Pila<Vertice> pila = new Pila<Vertice>();
	buscaActua(elemento, accion, pila);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
	vertices.limpia();
	aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
	String cadena = "{";
	for (Vertice v:vertices) {
	    cadena += v.elemento.toString() + ", ";
	}
	cadena += "}, {";

	for (Vertice v:vertices) {
	    for (Vertice w:v.vecinos) {
		if (vertices.indiceDe(v) < vertices.indiceDe(w)) {
		    cadena += "(" + v.elemento.toString() + ", " + w.elemento.toString() + "), "; 
		}
	    }
	}
	cadena += "}";
	return cadena;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
	Vertice x = null;
	Vertice y = null;
	if ((this.vertices.getLongitud() == grafica.vertices.getLongitud()) && this.aristas == grafica.aristas) {
	    for (Vertice v:vertices) {
		for (Vertice w:v.vecinos) {
		    if (sonVecinos(v.elemento, w.elemento)) {
			x = (Vertice) grafica.vertice(v.elemento);
			y = (Vertice) grafica.vertice(w.elemento);
			if (!grafica.sonVecinos(x.elemento, y.elemento))
			    return false;
		    }
		}
	    }
	    return true;
	}
	return false;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
