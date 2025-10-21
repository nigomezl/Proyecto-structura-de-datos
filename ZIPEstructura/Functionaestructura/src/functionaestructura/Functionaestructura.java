package functionaestructura;
import java.util.*;

public class Functionaestructura {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("gates number: ");
        int gates = s.nextInt();
        System.out.print("runways number: ");
        int runways = s.nextInt();
        Airport AP = new Airport(gates, runways);
        System.out.print("running ticks: ");
        int time = s.nextInt();
        System.out.print("Number of random planes: ");
        int number = s.nextInt();

        AP.generarAvionesAleatorios(number);
        AP.simulate(time, 300); // 300 ms por tick (visual)
    }
}