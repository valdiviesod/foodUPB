// Cola de despacho de cocina

// Una vez finalizada una llamada, el pedido se pasa a la cola de despacho en la cocina. La cola de despacho se presenta al
//personal en los monitores de la cocina y esta se ordena con las siguientes prioridades: Siempre un pedido de un cliente
//premium encolará de primero. En la cocina hay cuatro bancos de trabajo, tres con prioridad para comidas rápidas (cocc ión
//entre 1 y 5 minutos) y uno para comidas más complejas (cocción superior a 5 minutos). La cola de despacho debe priorizar
//los pedidos por cuadrante y asignar su cocción de acuerdo con el banco de trabajo, optimizando el tiempo de entrega.
public class ColaCocina {
    class Pedido {
        int numero;
        int prioridad; // 1 para premium, 2 para normal
        int tiempoCoccion;

        Pedido(int numero, int prioridad, int tiempoCoccion) {
            this.numero = numero;
            this.prioridad = prioridad;
            this.tiempoCoccion = tiempoCoccion;
        }
    }

    private Pedido[] cola;
    private int capacidad;
    private int size;
    private int frente;
    private int fin;

    ColaCocina(int capacidad) {
        this.capacidad = capacidad;
        this.cola = new Pedido[capacidad];
        this.size = 0;
        this.frente = 0;
        this.fin = -1;
    }

    boolean estaVacia() {
        return size == 0;
    }

    boolean estaLlena() {
        return size == capacidad;
    }

    void insertar(int numero, int prioridad, int tiempoCoccion) {
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

    Pedido extraer() {
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


//Una vez están lista la comida se entrega a los agentes domiciliarios. Los agentes domiciliarios estarán asignados a los
//pedidos que se puedan acumular en rango de 5 minutos. En caso acumular más de un pedido en los 5 minutos, se indicará
//al domiciliario la ruta de acuerdo con los cuadrantes (barrios de Bucaramanga) el orden de entrega


    public static void main(String[] args) {
        ColaCocina colaCocina = new ColaCocina(10);

        colaCocina.insertar(1, 1, 7);
        colaCocina.insertar(2, 2, 3);
        colaCocina.insertar(3, 1, 5);
        colaCocina.insertar(4, 2, 6);

        colaCocina.imprimirCola();

        Pedido pedidoDespachado = colaCocina.extraer();
        if (pedidoDespachado != null) {
            System.out.println("Pedido " + pedidoDespachado.numero + " despachado.");
        }

        colaCocina.imprimirCola();
    }
}
