package Estructuras;

public class ColaCocina {

    public class Pedido {
        public int numero;
        public int prioridad;
        public int tiempoCoccion;

        Pedido(int numero, int prioridad, int tiempoCoccion) {
            this.numero = numero;
            this.prioridad = prioridad;
            this.tiempoCoccion = tiempoCoccion;
        }
    }

    private static Pedido[] cola;
    private int capacidad;
    private static int size;
    private static int frente;
    private int fin;

    public ColaCocina(int capacidad) {
        this.capacidad = capacidad;
        this.cola = new Pedido[capacidad];
        this.size = 0;
        this.frente = 0;
        this.fin = -1;
    }

    static boolean estaVacia() {
        return size == 0;
    }

    boolean estaLlena() {
        return size == capacidad;
    }

    public void insertar(int numero, int prioridad, int tiempoCoccion) {
        if (!estaLlena()) {
            Pedido nuevoPedido = new Pedido(numero, prioridad, tiempoCoccion);
            if (estaVacia()) {
                cola[0] = nuevoPedido;
                fin = 0;
            } else {
                int i;
                for (i = fin; i >= 0; i--) {
                    if (cola[i].prioridad <= nuevoPedido.prioridad)
                        break;
                    cola[i + 1] = cola[i];
                }
                cola[i + 1] = nuevoPedido;
                fin++;
            }
            size++;
            System.out.println("Pedido " + numero + " encolado con prioridad " + prioridad + " y tiempo de cocción " + tiempoCoccion + " minutos.");
        } else {
            System.out.println("La cola de despacho está llena, no se puede encolar el pedido " + numero);
        }
    }

    public static Pedido extraer() {
        if (!estaVacia()) {
            Pedido pedido = cola[frente];
            frente++;
            size--;
            return pedido;
        }
        return null;
    }

    void imprimirCola() {
        if (!estaVacia()) {
            System.out.println("Cola de despacho de cocina:");
            for (int i = frente; i <= fin; i++) {
                System.out.println("Pedido " + cola[i].numero + " - Prioridad " + cola[i].prioridad +
                        " - Tiempo de cocción " + cola[i].tiempoCoccion + " minutos");
            }
        } else {
            System.out.println("La cola de despacho está vacía.");
        }
    }
}
