
package GUI;

import AeropuertoProyecto.Airport;
import AeropuertoProyecto.Buffer;
import AeropuertoProyecto.Plane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.PriorityQueue;

public class AirportGUI extends JFrame {
    private Airport airport;
    private JPanel gatesPanel;
    private JPanel runwaysPanel;
    private JButton[][] gateButtons;
    private JButton[] runwayButtons;
    private DefaultListModel<String> landingListModel;
    private JList<String> landingList;
    private Timer timer;
    private boolean running = false;
    private JButton playPauseButton;

    public AirportGUI(Airport airport) {
        this.airport = airport;

        setTitle("Airport Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout(20, 20));

        // === Panel principal (centro): Gates y Runways ===
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));

        gatesPanel = new JPanel();
        runwaysPanel = new JPanel();
        gatesPanel.setBorder(BorderFactory.createTitledBorder("Gates"));
        runwaysPanel.setBorder(BorderFactory.createTitledBorder("Runways"));

        // Cuadrícula dinámica de gates
        int gates = airport.gates.length;
        int gridSize = (int) Math.ceil(Math.sqrt(gates));
        gatesPanel.setLayout(new GridLayout(gridSize, gridSize, 3, 3));

        gateButtons = new JButton[gates][1];
        for (int i = 0; i < gates; i++) {
            JButton btn = new JButton("Gate " + (i + 1));
            btn.setBackground(Color.GREEN);
            btn.addActionListener(new GateButtonListener(i));
            gateButtons[i][0] = btn;
            gatesPanel.add(btn);
        }

        // Lista vertical de runways
        int runways = airport.runways.length;
        runwaysPanel.setLayout(new GridLayout(runways, 1, 3, 3));
        runwayButtons = new JButton[runways];
        for (int i = 0; i < runways; i++) {
            JButton btn = new JButton("Runway " + (i + 1));
            btn.setBackground(Color.CYAN);
            btn.addActionListener(new RunwayButtonListener(i));
            runwayButtons[i] = btn;
            runwaysPanel.add(btn);
        }

        mainPanel.add(gatesPanel);
        mainPanel.add(runwaysPanel);
        add(mainPanel, BorderLayout.CENTER);

        // === Panel lateral derecho ===
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setPreferredSize(new Dimension(250, 0));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Control de aviones"));

        // Botón para agregar avión
        JButton addPlaneButton = new JButton("Agregar avión");
        // === Botón de pausa/play ===
        playPauseButton = new JButton("▶ Iniciar Simulación");
        playPauseButton.addActionListener(e -> toggleSimulacion());

        // Agregamos ambos botones al panel superior derecho
        JPanel controlButtons = new JPanel(new GridLayout(2, 1, 5, 5));
        controlButtons.add(addPlaneButton);
        controlButtons.add(playPauseButton);
        rightPanel.add(controlButtons, BorderLayout.NORTH);

        // === Temporizador para avanzar el tiempo cada segundo ===
        timer = new Timer(1000, e -> avanzarTiempo());
        addPlaneButton.addActionListener(e -> agregarAvion());

        // Lista desplazable de aviones en landingQueue
        landingListModel = new DefaultListModel<>();
        landingList = new JList<>(landingListModel);
        JScrollPane scrollPane = new JScrollPane(landingList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.EAST);

        actualizarListaLanding();
    }

    // === Métodos de listeners ===

    
    private class GateButtonListener implements ActionListener {
        int index;
        GateButtonListener(int index) { this.index = index; }

        @Override
        public void actionPerformed(ActionEvent e) {
            Buffer gate = airport.gates[index];
            String info;
            if (gate.available) {
                info = "Gate " + (index + 1) + " está libre.";
            } else if (gate.plane != null) {
                info = "Gate " + (index + 1) + " ocupado por avión " + gate.plane.toString();
            } else {
                info = "Gate " + (index + 1) + " ocupado.";
            }
            JOptionPane.showMessageDialog(AirportGUI.this, info);
        }
    }

    private class RunwayButtonListener implements ActionListener {
        int index;
        RunwayButtonListener(int index) { this.index = index; }

        @Override
        public void actionPerformed(ActionEvent e) {
            Buffer runway = airport.runways[index];
            String info;
            if (runway.available) {
                info = "Runway " + (index + 1) + " está libre.";
            } else if (runway.plane != null) {
                info = "Runway " + (index + 1) + " ocupado por avión " + runway.plane.toString();
            } else if (airport.airspace[index] != null) {
                info = "Runway " + (index + 1) + " Espera del avion " + airport.airspace[index].toString();
            } else {
                info = "Runway " + (index + 1) + " Espera de un avion";
            }
            JOptionPane.showMessageDialog(AirportGUI.this, info);
        }
    }

    // === Método para agregar un nuevo avión ===
    private void agregarAvion() {
    String[] opciones = {"Manual", "Aleatorio", "Cancelar"};
    int eleccion = JOptionPane.showOptionDialog(
            this,
            "¿Cómo quieres agregar el avión?",
            "Agregar Avión",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
    );

    if (eleccion == 2 || eleccion == JOptionPane.CLOSED_OPTION) {
        return; // cancelar
    }

    if (eleccion == 0) { // Manual
        try {
            String priStr = JOptionPane.showInputDialog(this, "Prioridad del avión (1–5):");
            String speStr = JOptionPane.showInputDialog(this, "Velocidad (1-10):");
            String pasStr = JOptionPane.showInputDialog(this, "Pasajeros a bordo:");

            if (priStr == null || speStr == null || pasStr == null) return;

            int pri = Integer.parseInt(priStr);
            int spe = Integer.parseInt(speStr);
            int pas = Integer.parseInt(pasStr);

            Plane nuevo = new Plane(pri, spe, pas);
            airport.addPlaneLandingQueue(nuevo);

            JOptionPane.showMessageDialog(this,
                    "✈️ Avión agregado exitosamente:\n" +
                    "ID: " + nuevo.id + "\n" +
                    "Prioridad: " + nuevo.priority + "\n" +
                    "Velocidad: " + nuevo.speed + " km/h\n" +
                    "Pasajeros: " + nuevo.passengers,
                    "Avión creado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    } else if (eleccion == 1) { // Aleatorio
        try {
            String numStr = JOptionPane.showInputDialog(this, "¿Cuántos aviones aleatorios deseas generar?");
            if (numStr == null) return;

            int num = Integer.parseInt(numStr);
            airport.generarAvionesAleatorios(num);

            JOptionPane.showMessageDialog(this,
                    "✈️ " + num + " aviones generados aleatoriamente y añadidos a la cola.",
                    "Generación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // Actualizar la lista visual
    actualizarListaLanding();
    }

    // === Actualiza los colores de los botones ===
    public void updateDisplay() {
        for (int i = 0; i < airport.gates.length; i++) {
            if (!airport.gates[i].available){
                gateButtons[i][0].setBackground(
                    airport.gates[i].plane != null ? Color.red : Color.ORANGE
                );
                gateButtons[i][0].setText("Runway " + i + (airport.gates[i].plane != null ? " Tarea:" + airport.gates[i].plane.getTask() + ", Tiempo en completar: " + airport.gates[i].plane.tasktime : ", Esperando por un avion"));
                
            } else {
                gateButtons[i][0].setBackground(Color.GREEN);
                gateButtons[i][0].setText("Gate " + i + "\nLibre");
            }
        }
        for (int i = 0; i < airport.runways.length; i++) {
            if (!airport.runways[i].available){
                runwayButtons[i].setBackground(
                    airport.runways[i].plane != null ? Color.red : Color.ORANGE
                );
                runwayButtons[i].setText("Runway " + i + (airport.runways[i].plane != null ? " Tarea:" + airport.runways[i].plane.getTask() + ", Tiempo en completar: " + airport.runways[i].plane.tasktime : ", Esperando por un avion"));
            } else {
                runwayButtons[i].setBackground(Color.GREEN);
                runwayButtons[i].setText("Runway " + i + "\nLibre");
            }
        }
    }

    // === Actualiza la lista con los aviones en la landingQueue ===
    
    private void actualizarListaLanding() {
        landingListModel.clear();
        PriorityQueue<Plane> pq = new PriorityQueue<>(airport.landingQueue);
        while (!pq.isEmpty()) {
            Plane p = pq.poll();
            landingListModel.addElement("Plane " + p.id + " (Prioridad " + p.priority + ")");
        }
    }
    // Alternar simulación (pausa/play)
    private void toggleSimulacion() {
    if (running) {
        timer.stop();
        playPauseButton.setText("▶ Reanudar");
        running = false;
    } else {
        timer.start();
        playPauseButton.setText("⏸ Pausar");
        running = true;
    }
    }

// Un tick de simulación
    private void avanzarTiempo() {
    airport.time();           // usa tu método original
    updateDisplay();          // actualiza colores de botones
    actualizarListaLanding(); // actualiza la cola visual

    // Puedes mostrar el tiempo en el título
    setTitle("Airport Simulation - Tiempo: " + airport.time);
    }
}