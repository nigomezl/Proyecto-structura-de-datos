
package functionaestructura;

import java.util.*;

public class Airport {
    PriorityQueue<Plane> landingQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority)) ;
    Queue<Plane> takeoffQueue = new LinkedList<>();
    Buffer[] gates;
    Buffer[] runways;
    Plane[] airspace;
    int time = 0;
    
    public Airport(int num_gates,int num_runaways){
        gates = new Buffer[num_gates];
        runways = new Buffer[num_runaways];
        airspace = new Plane[num_runaways];
        for (int i = 0; i < num_gates; i++) {
            gates[i] = new Buffer();
        }

        for (int i = 0; i < num_runaways; i++){
            runways[i] = new Buffer();
        }

    }
    
    
    public void addPlaneLandingQueue(Plane ob_plane){
        landingQueue.add(ob_plane);
        System.out.println("Plane "+ ob_plane.id + " con prioridad " + ob_plane.priority);
    }
    
    public int runwayEmptySpaces(){
        int i = 0;
        int counter = 0;
        while (i < runways.length){
            if (runways[i].available){
                counter++;
            }
            i++;
        }
        return counter;
    }
    
    public int runwayEmpty(){
        int i = 0;
        while (i < runways.length){
            if (runways[i].available){
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
        if ((landingQueue.peek().tasktime == 0) && (landingQueue.peek().runwayIndex != -1)){
            landingTorunway(landingQueue.peek().runwayIndex);
            return false;
        } else if ((landingQueue.peek().tasktime == -1)){
            int Rindex = runwayEmpty();
            int Gindex = gateEmpty();
            System.out.println(Rindex + "y" + Gindex);
            if ((Rindex != -1) && (Gindex != -1)){
                runways[Rindex].available = false;
                gates[Gindex].available = false;
                landingQueue.peek().runwayIndex = Rindex;
                landingQueue.peek().gateIndex = Gindex;
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
    
    public void landingTorunway(int ind_runaway){
        landingQueue.peek().runwayIndex = -1;
        landingQueue.peek().tasktime = -1;
        landingQueue.peek().action = 2;
        runways[ind_runaway].plane = landingQueue.poll();
    }
    
    public boolean Togates(int ind_runway){
        if ((runways[ind_runway].plane.tasktime == 0) && (runways[ind_runway].plane.gateIndex != -1)){
            runwayTogates(ind_runway, runways[ind_runway].plane.gateIndex);
            return false;
        } else if ((runways[ind_runway].plane.tasktime == -1) ){
            runways[ind_runway].plane.tasktime = 10;
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
    
    public void runwayTogates(int ind_runway, int ind_gate){
        runways[ind_runway].plane.tasktime = -1;
        runways[ind_runway].plane.action = 3;
        gates[ind_gate].plane = runways[ind_runway].plane;
        runways[ind_runway].plane = null;
        runways[ind_runway].available = true;
    }
    
    public boolean PickUp(int ind_gate){
        if (gates[ind_gate].plane.tasktime == 0){
            gatesPickUp(ind_gate);
            return false;
        }  else if ((gates[ind_gate].plane.tasktime == -1) ){
            gates[ind_gate].plane.tasktime = 20;
            gates[ind_gate].plane.action = 3;
            return true;
        } else {
            gates[ind_gate].plane.tasktime -= 1;
            return true;
        }
    }
    
    public void gatesPickUp(int ind_gate){
        gates[ind_gate].plane.tasktime = -1;
        gates[ind_gate].plane.action = 4;
        addPlanetakeoffQueue(gates[ind_gate].plane);
    }
    
    public void addPlanetakeoffQueue(Plane ob_plane){
        takeoffQueue.add(ob_plane);
    }
    
    public boolean Torunway(){
        if (takeoffQueue.peek().tasktime == 0){
            gatesTorunway(takeoffQueue.peek().gateIndex, takeoffQueue.peek().runwayIndex);
            return false;
        } else if ((takeoffQueue.peek().tasktime == -1) ){
            int Rindex = runwayEmpty();
            if (Rindex != -1){
                runways[Rindex].available = false;
                takeoffQueue.peek().runwayIndex = Rindex;
                takeoffQueue.peek().action = 5;
                takeoffQueue.peek().tasktime = 15;
                return true;
            }
            return false;
        } else {
            takeoffQueue.peek().tasktime -= 1;
            return true;
        }
    }
    
    public void gatesTorunway(int ind_gate, int ind_runway){
        takeoffQueue.peek().tasktime = -1;
        takeoffQueue.peek().gateIndex = -1;
        takeoffQueue.peek().action = 5;
        runways[ind_runway].plane = takeoffQueue.poll();
        gates[ind_gate].plane = null;
        gates[ind_gate].available = true;
    }
    
    public boolean takeoff(int ind_runway){
        if (runways[ind_runway].plane.tasktime == 0){
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
        if (!landingQueue.isEmpty()){
            Landing();
        }
        int i = 0;
        while (i < runways.length){
            if (runways[i].plane != null){
                
                if (runways[i].plane.action == 2){
                    Togates(i);
                } else if (runways[i].plane.action == 5){
                    takeoff(i);
                    System.out.println("mmm");
                }
            }
            i++;
        }
        i = 0;
        while (i < gates.length){
            if (gates[i].plane != null){
                if (gates[i].plane.action == 3){
                    PickUp(i);
                } 
            }
            i++;
        }
        if (!takeoffQueue.isEmpty()){
            Torunway();
        }
        time++;
    }
    // imprime estado
    public void printStatus() {
        System.out.println("\n===== ESTADO t=" + this.time + " =====");
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
                System.out.println("  Gate " + i + " -> [LIBRE]");
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
            Plane nuevo = new Plane(priority, speed);
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