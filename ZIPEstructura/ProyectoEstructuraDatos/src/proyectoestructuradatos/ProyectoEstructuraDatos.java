package proyectoestructuradatos;
import java.util.*;

public class ProyectoEstructuraDatos {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("gates number:");
        int gates = s.nextInt();
        System.out.print("gates runaway:");
        int runaways = s.nextInt();
        Airport AP = new Airport(gates, runaways);
        System.out.print("running time:");
        int time = s.nextInt();
        int i = 0;
        System.out.print("Number of random planes:");
        int number = s.nextInt();
        AP.generarAvionesAleatorios(number);
        while (time > i){
            AP.time();
            i++;
        }
    }
}


