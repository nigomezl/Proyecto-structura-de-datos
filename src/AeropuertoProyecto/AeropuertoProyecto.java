package AeropuertoProyecto;
import GUI.AirportGUI;
import java.util.*;
import javax.swing.JOptionPane;

public class AeropuertoProyecto {
    public static void main(String[] args) {
        // Pedir cantidad de gates y runways antes de crear la simulación
        int gates = 0;
        int runways = 0;

        try {
            gates = Integer.parseInt(
                JOptionPane.showInputDialog(null, "Ingrese la cantidad de gates:", "Configuración Inicial", JOptionPane.QUESTION_MESSAGE)
            );
            runways = Integer.parseInt(
                JOptionPane.showInputDialog(null, "Ingrese la cantidad de runways:", "Configuración Inicial", JOptionPane.QUESTION_MESSAGE)
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Validar que sean positivos
        if (gates <= 0 || runways <= 0) {
            JOptionPane.showMessageDialog(null, "Los valores deben ser mayores que 0.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
         Airport airport = new Airport(gates, runways);
    }
}