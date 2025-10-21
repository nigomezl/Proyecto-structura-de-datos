
package proyectoestructuradatos;

import java.util.*;

public class Airport {
    PriorityQueue<Plane> landingQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority)) ;
    Queue<Plane> takeoffQueue = new LinkedList<>();
    Buffer[] gates;
    Buffer[] runaways;
    
    public Airport(int num_gates,int num_runaways){
        gates = new Buffer[num_gates];
        runaways = new Buffer[num_runaways];
        for (int i = 0; i < num_gates; i++) {
            gates[i] = new Buffer(null);
        }

        for (int i = 0; i < num_runaways; i++){
            runaways[i] = new Buffer(null);
        }
    }
    
    
    public void addPlaneLandingQueue(Plane ob_plane){
        landingQueue.add(ob_plane);
        System.out.println("Plane "+ ob_plane.id + " con prioridad " + ob_plane.priority);
    }
    
    public int runawayEmpty(){
        int i = 0;
        while (i < runaways.length){
            if (gates[i].available){
                return i;
            }
            i++;
        }
        return -1;
    }
    
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
    
    public boolean Landing(){
        if ((landingQueue.peek().tasktime == 0) && (landingQueue.peek().index != -1)){
            landingTorunaway(landingQueue.peek().index);
            return false;
        } else if ((landingQueue.peek().tasktime == -1) ){
            int Rindex = runawayEmpty();
            int Gindex = gateEmpty();
            if ((Rindex != -1) && (Gindex != -1)){
                runaways[Rindex].available = false;
                gates[Gindex].available = false;
                landingQueue.peek().index = Rindex;
                landingQueue.peek().index1 = Gindex;
                landingQueue.peek().action = 1;
                landingQueue.peek().tasktime = 15;
                return true;
            }
            return false;
        } else {
            landingQueue.peek().tasktime -= 1;
            return true;
        }
    }
    
    public void landingTorunaway(int ind_runaway){
        landingQueue.peek().index = -1;
        landingQueue.peek().tasktime = -1;
        landingQueue.peek().action = -1;
        runaways[ind_runaway].plane = landingQueue.poll();
    }
    
    public boolean Togates(int ind_runaway){
        if ((runaways[ind_runaway].plane.tasktime == 0) && (runaways[ind_runaway].plane.index1 != -1)){
            runawayTogates(ind_runaway, runaways[ind_runaway].plane.index1);
            return false;
        } else if ((landingQueue.peek().tasktime == -1) ){
            runaways[ind_runaway].plane.tasktime = 10;
            runaways[ind_runaway].plane.action = 2;
            if (runaways[ind_runaway].plane.index1 == -1){
                int Gindex = gateEmpty();
                runaways[ind_runaway].plane.index1 = Gindex;
                gates[Gindex].available = false;
                return true;
            }
            return false;
        } else {
            runaways[ind_runaway].plane.tasktime -= 1;
            return true;
        }
    }
    
    public void runawayTogates(int ind_runaway, int ind_gate){
        runaways[ind_runaway].plane.tasktime = -1;
        runaways[ind_runaway].plane.action = -1;
        gates[ind_gate].plane = runaways[ind_runaway].plane;
        runaways[ind_runaway].plane = null;
    }
    
    public boolean PickUp(int ind_gate){
        if (landingQueue.peek().tasktime == 0){
            gatesPickUp(ind_gate);
            return false;
        }  else if ((landingQueue.peek().tasktime == -1) ){
            gates[ind_gate].plane.tasktime = 60;
            gates[ind_gate].plane.action = 3;
            return true;
        } else {
            gates[ind_gate].plane.tasktime -= 1;
            return true;
        }
    }
    
    public void gatesPickUp(int ind_gate){
        gates[ind_gate].plane.tasktime = -1;
        gates[ind_gate].plane.action = -1;
        addPlanetakeoffQueue(gates[ind_gate].plane);
    }
    
    public void addPlanetakeoffQueue(Plane ob_plane){
        takeoffQueue.add(ob_plane);
    }
    
    public boolean Torunaway(){
        if (landingQueue.peek().tasktime == 0){
            gatesTorunaway(landingQueue.peek().index1, landingQueue.peek().index);
            return false;
        } else if ((landingQueue.peek().tasktime == -1) ){
            int Rindex = runawayEmpty();
            if (Rindex != -1){
                runaways[Rindex].available = false;
                landingQueue.peek().index = Rindex;
                landingQueue.peek().action = 4;
                landingQueue.peek().tasktime = 15;
                return true;
            }
            return false;
        } else {
            landingQueue.peek().tasktime -= 1;
            return true;
        }
    }
    
    public void gatesTorunaway(int ind_gate, int ind_runaway){
        landingQueue.peek().index1 = -1;
        landingQueue.peek().action = -1;
        runaways[ind_runaway].plane = takeoffQueue.poll();
        gates[ind_gate].plane = null;
        takeoff(ind_runaway);
    }
    
    public boolean takeoff(int ind_runaway){
        if (runaways[ind_runaway].plane.tasktime == 0){
            runaways[ind_runaway].plane = null;
            return false;
        } else if (landingQueue.peek().tasktime == -1){
            runaways[ind_runaway].plane.tasktime = 5;
            runaways[ind_runaway].plane.action = 5;
            return false;
        } else {
            runaways[ind_runaway].plane.tasktime -= 1;
            return true;
        }
    }
    
    public void time(){
        if (!landingQueue.isEmpty()){
            Landing();
        }
        int i = 0;
        while (i < runaways.length){
            if (runaways[i].plane != null){
                if (runaways[i].plane.action == 2){
                    Togates(i);
                } else if (runaways[i].plane.action == 5){
                    takeoff(i);
                }
            }
            i++;
        }
        i = 0;
        while (i < gates.length){
            if (gates[i].plane != null){
                 PickUp(i);
            }
            i++;
        }
        if (!takeoffQueue.isEmpty()){
            Torunaway();
        }
        printStatus();
    }
    
    public void generarAvionesAleatorios(int cantidad) {
        Random rand = new Random();
        
        for (int i = 0; i < cantidad; i++) {
            int prioridad = rand.nextInt(10) + 1; // Prioridad entre 1 y 10
            Plane nuevo = new Plane(prioridad);

        // Puedes decidir si va a aterrizar o despegar, por ejemplo:
            if (rand.nextBoolean()) {
                addPlaneLandingQueue(nuevo);
            } else {
                addPlanetakeoffQueue(nuevo);
            }
        }
    }
    
    public void printStatus() {
    System.out.println("===== ESTADO DEL AEROPUERTO =====");
    System.out.println("Runways:");
    for (int i = 0; i < runaways.length; i++) {
        if (runaways[i].plane != null) {
            System.out.println("  Runway " + i + " → Plane " + runaways[i].plane.id + 
                               " (action: " + runaways[i].plane.action + 
                               ", tasktime: " + runaways[i].plane.tasktime + ")");
        } else {
            System.out.println("  Runway " + i + " → libre");
        }
    }
    System.out.println("Gates:");
    for (int i = 0; i < gates.length; i++) {
        if (gates[i].plane != null) {
            System.out.println("  Gate " + i + " → Plane " + gates[i].plane.id +
                               " (action: " + gates[i].plane.action +
                               ", tasktime: " + gates[i].plane.tasktime + ")");
        } else {
            System.out.println("  Gate " + i + " → libre");
        }
    }
    System.out.println("=================================\n");
    }
}