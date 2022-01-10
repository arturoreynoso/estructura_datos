package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
	if (arreglo.length == 0) return;
	sort(arreglo, comparador, 0, arreglo.length - 1);
    }
        private static <T> void sort (T[] arreglo, Comparator<T> comparador, int a, int b){
	if (b <= a) return;
	int i; int j;
	i = a + 1;
	j = b;
	while (i < j){
	    if (comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[j], arreglo[a]) <= 0){
		intercambia(arreglo, i, j);
		i = i+1;
		j = j-1;
	    } else if (comparador.compare(arreglo[i], arreglo[a])<=0){
		i = i+1;
	    } else {
		j = j-1;
	    }
	}
	if(comparador.compare(arreglo[i], arreglo[a]) > 0) i = i-1;
	intercambia(arreglo, a, i);
	sort(arreglo, comparador, a, i-1);
	sort(arreglo, comparador, i+1, b);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
	for (int i = 0; i < arreglo.length; i++){
	    int m = i;
	    for (int j = i + 1; j < arreglo.length; j++) {
		if (comparador.compare(arreglo[j], arreglo[m]) < 0) {
		    m = j;
		}
	    }
	    intercambia(arreglo, i, m);
	}
    }

    private static <T> void intercambia(T[] arreglo, int i, int j) {
	T temp = arreglo[j];
	arreglo[j] = arreglo[i];
	arreglo[i] = temp;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
	return busqueda(arreglo, elemento, comparador, 0, arreglo.length-1);
    }

    private static <T> int busqueda(T[] arreglo, T elemento, Comparator<T> comparador, int low, int high){
	if (low > high) return -1;
	int mid = (low + high)/2;
	if (comparador.compare(arreglo[mid], elemento) == 0) return mid;
	if (comparador.compare(arreglo[mid], elemento) < 0){
	    return busqueda(arreglo, elemento, comparador, mid + 1, high);
	} else {
	    return busqueda(arreglo, elemento, comparador, low, mid-1);
	}
    }
     
    
    

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
