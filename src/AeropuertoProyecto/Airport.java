
package AeropuertoProyecto;

import GUI.AirportGUI;
import java.util.*;

public class Airport {
    public PriorityQueue<Plane> landingQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority)) ;
    public Queue<Plane> takeoffQueue = new LinkedList<>();
    public Buffer[] gates;
    public Buffer[] runways;
    public Plane[] airspace;
    public int time = 0;
    public AirportGUI gui;
    
    public Airport(int num_gates,int num_runways){
        gates = new Buffer[num_gates];
        runways = new Buffer[num_runways];
        airspace = new Plane[num_runways];
        for (int i = 0; i < num_gates; i++) {
            gates[i] = new Buffer();
        }

        for (int i = 0; i < num_runways; i++){
            runways[i] = new Buffer();
        }
        AirportGUI gui = new AirportGUI(this);
        gui.setVisible(true);
    }
    
    //Agrega aviones para aterrizar
    public void addPlaneLandingQueue(Plane ob_plane){
        landingQueue.add(ob_plane);
        System.out.println("Plane "+ ob_plane.id + " con prioridad " + ob_plane.priority);
    }
    
    //Busca una ubicacion en runways que este disponible
    public int runwayEmpty(){
        int i = 0;
        while (i < runways.length){
            if (runways[i].available){
                return i;
            }
            i++;
        }
        return -1; //Devuelve -1 en caso de no encontrar
    }
    
    //Lo mismo pero para los gates
    public int gateEmpty(){
        int i = 0;
        while (i < gates.length){
            if (gates[i].available){
                return i;
            }
            i++;
        }
        return -1;
    }
    
    //Esta se encarga de mover de la fila a el Espacio de descenso airspace donde se va a gastar su tiempo en descender al runway
    public boolean Landing(){
        int i = 0;
        while (i < runways.length && !landingQueue.isEmpty()){
            if (runways[i].available){
                int G_index = gateEmpty();//Busca una ubicacion en runways  para aterrizar
                if (G_index != -1){
                    landingQueue.peek().runwayIndex = i;
                    runways[i].available = false;
                    landingQueue.peek().gateIndex = G_index;
                    gates[G_index].available = false; //Reserva espacio en los gates y runways para evitar que varios aviones lleguen al mismo o que se sature la pista sin poder parquear en gates
                    airspace[i] = landingQueue.poll(); //Lo elimina para comprobar si el siguiente tambien puede comenzar a descender
                    return true;
                }
            }
            i++;
        }
        return false;
    }
    
    //Esta se encarga de hacer el conteo en el tiempo para los que se encuentren en descenso cuando llega a 0 se agregan a runway
    public boolean aTorunway(int ind_runway){
        if (airspace[ind_runway].tasktime == 0){
            airTorunway(ind_runway);
            return false;
        } else if ((airspace[ind_runway].tasktime == -1) ){
            airspace[ind_runway].tasktime = 20/airspace[ind_runway].speed;
            airspace[ind_runway].action = 1;
            return false;
        } else {
            airspace[ind_runway].tasktime -= 1;
            return true;
        }
    }
    
    //Adicion a runways desde aire
    public void airTorunway(int ind_runway){
        airspace[ind_runway].runwayIndex = -1;
        airspace[ind_runway].tasktime = -1;
        airspace[ind_runway].action = 2;
        runways[ind_runway].plane = airspace[ind_runway];
        airspace[ind_runway] = null;
    }
    
    //Esta se encarga de hacer el conteo en el tiempo para los que se encuentren en runway
    public boolean Togates(int ind_runway){
        if ((runways[ind_runway].plane.tasktime == 0) && (runways[ind_runway].plane.gateIndex != -1)){
            runwayTogates(ind_runway, runways[ind_runway].plane.gateIndex);
            return false;
        } else if ((runways[ind_runway].plane.tasktime == -1) ){
            runways[ind_runway].plane.tasktime = 5;
            runways[ind_runway].plane.action = 2;
            if (runways[ind_runway].plane.gateIndex == -1){
                int Gindex = gateEmpty();
                runways[ind_runway].plane.gateIndex = Gindex;
                gates[Gindex].available = false;
                return true;
            }
            return false;
        } else {
            runways[ind_runway].plane.tasktime -= 1;
            return true;
        }
    }
    
    //Adicion a gates desde runways
    public void runwayTogates(int ind_runway, int ind_gate){
        runways[ind_runway].plane.tasktime = -1;
        runways[ind_runway].plane.action = 3;
        gates[ind_gate].plane = runways[ind_runway].plane;
        runways[ind_runway].plane = null;
        runways[ind_runway].available = true;
    }
    
    //Esta se encarga de hacer el conteo en el tiempo para los que se demore en bajar y subir personas
    public boolean PickUp(int ind_gate){
        if (gates[ind_gate].plane.tasktime == 0){
            gatesPickUp(ind_gate);
            return false;
        }  else if ((gates[ind_gate].plane.tasktime == -1) ){
            gates[ind_gate].plane.tasktime = gates[ind_gate].plane.passengers/2;
            gates[ind_gate].plane.action = 3;
            return true;
        } else {
            gates[ind_gate].plane.tasktime -= 1;
            return true;
        }
    }
    
    //Luego de completar el subir y bajar personas lo agrega a una fila para el despegue
    public void gatesPickUp(int ind_gate){
        gates[ind_gate].plane.tasktime = -1;
        gates[ind_gate].plane.action = 4;
        takeoffQueue.add(gates[ind_gate].plane);
    }
    
    //Contador de tiempo en dezplasarse de gates hasta la runway
    public boolean Torunway(int ind_gate){
        if (gates[ind_gate].plane.tasktime == 0){
            gatesTorunway(ind_gate, gates[ind_gate].plane.runwayIndex);
            return false;
        } else if (gates[ind_gate].plane.tasktime != -1){
            gates[ind_gate].plane.tasktime -= 1;
            return true;
        } else if ((!takeoffQueue.isEmpty()) && (takeoffQueue.peek().tasktime == -1)){
            int Rindex = runwayEmpty();
            if (Rindex != -1){
                runways[Rindex].available = false;
                takeoffQueue.peek().runwayIndex = Rindex;
                takeoffQueue.peek().action = 4;
                takeoffQueue.peek().tasktime = 5;
                takeoffQueue.remove();
                return true;
            }
            return false;
        }
        return false;
    }
    
    //Adicion a runways desde gates
    public void gatesTorunway(int ind_gate, int ind_runway){
        gates[ind_gate].plane.tasktime = -1;
        gates[ind_gate].plane.gateIndex = -1;
        gates[ind_gate].plane.action = 5;
        runways[ind_runway].plane = gates[ind_gate].plane;
        gates[ind_gate].plane = null;
        gates[ind_gate].available = true;
    }
    
    //Despegue y tiempo gastado en este luego de despegar se elimina el objeto
    public boolean takeoff(int ind_runway){
        if (runways[ind_runway].plane.tasktime == 0){
            Plane p = runways[ind_runway].getPlane();
            p = null;
            runways[ind_runway].plane = null;
            runways[ind_runway].available = true;
            return false;
        } else if (runways[ind_runway].plane.tasktime == -1){
            runways[ind_runway].plane.tasktime = 5;
            runways[ind_runway].plane.action = 5;
            return false;
        } else {
            runways[ind_runway].plane.tasktime -= 1;
            return true;
        }
    }
    
    public void time(){
        int i = 0;
        
        
        
        while (i < runways.length){
            if (airspace[i] != null){
                aTorunway(i);
            }
            i++;
        }
        i = 0;
        
        while (i < runways.length){
            if (runways[i].plane != null){
                if (runways[i].plane.action == 2){
                    Togates(i);
                } else if (runways[i].plane.action == 5){
                    takeoff(i);
                }
            }
            i++;
        }
        
        if (!landingQueue.isEmpty()){
            Landing();
        }

        i = 0;
        
        while (i < gates.length){
            if (gates[i].plane != null){
                if (gates[i].plane.action == 3){
                    PickUp(i);
                } else if (gates[i].plane.action == 4){
                    Torunway(i);
                }
            }
            i++;
        }
        time++;
    }
    // imprime estado
    public void printStatus() {
        System.out.println("\n===== ESTADO t=" + this.time + " =====");
        System.out.println("Landing:");
        for (int i = 0; i < runways.length; i++) {
            Plane r = airspace[i];
            if (r != null) {
                System.out.println("  airspace " + i + " -> " + r);
            } else {
                System.out.println("  airspace " + i + r);
            }
        }
        System.out.println("Runways:");
        for (int i = 0; i < runways.length; i++) {
            Buffer r = runways[i];
            if (r.plane != null) {
                System.out.println("  Runway " + i + " -> " + r.plane + (r.available ? " [available]" : " [busy]"));
            } else {
                System.out.println("  Runway " + i + (r.available ? " [available]" : " [busy]"));
            }
        }
        System.out.println("Gates:");
        for (int i = 0; i < gates.length; i++) {
            Buffer g = gates[i];
            if (g.plane != null) {
                System.out.println("  Gate " + i + " -> " + g.plane);
            } else {
                System.out.println("  Gate " + i + " -> [available]");
            }
        }
        System.out.println("Queues: landing=" + landingQueue.size() + " takeoff=" + takeoffQueue.size());
        System.out.println(landingQueue.peek());
        System.out.println("=============================\n");
    }
    
    public void generarAvionesAleatorios(int number) {
        Random rand = new Random();
        for (int i = 0; i < number; i++) {
            int priority = rand.nextInt(5) + 1; // 1..5
            int speed = rand.nextInt(10) + 1;
            int passengers = rand.nextInt(100) + 20;
            Plane nuevo = new Plane(priority, speed, passengers);
            addPlaneLandingQueue(nuevo);
        }
    }

    // simulación con sleep opcional
    public void simulate(int ticks, long sleepMillis) {
        for (int i = 0; i < ticks; i++) {
            time();
            printStatus();
            if (sleepMillis > 0) {
                try { Thread.sleep(sleepMillis); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }
    
    
}