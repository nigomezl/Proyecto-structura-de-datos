
package AeropuertoProyecto;

public class Plane {
    public static int numero = 0;
    public int id;
    public int priority;
    public int tasktime = -1;   // >=0 : tareas en curso; -1 : sin tarea/esperando asignación
    public int runwayIndex = -1;
    public int gateIndex = -1;
    public int speed;
    public int passengers;
    public int action = -1;


    public Plane(int pri, int spe, int pas) {
        this.id = numero++;
        this.priority = pri;
        this.tasktime = -1;
        this.runwayIndex = -1;
        this.gateIndex = -1;
        this.speed = spe;
        this.passengers = pas;
        this.action = -1;
        System.out.println("Plane created ID = " + id + " priority=" + priority);
    }

    @Override
    public String toString() {
        return "Plane{id=" + id + ",Reserved runaway=" + runwayIndex + ",Reserved gate=" + gateIndex +  ", tasktime=" + tasktime + ", task="+ action + "}";
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String getTask() {
        if (action == 1){
            return "Aterrizando";
        } else if (action == 2){
            return "Moviendose a gates";
        } else if (action == 3){
            return "Recogiendo y dejando gente";
        } else if (action == 4){
            return "Moviendose a la pista";
        } else if (action == 5){
            return "Despegue";
        } else {
            return "En espera";
        }
    }
}