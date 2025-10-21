
package functionaestructura;

public class Plane {
    public static int numero = 0;
    public int id;
    public int priority;
    public int tasktime = -1;   // >=0 : tareas en curso; -1 : sin tarea/esperando asignación
    public int runwayIndex = -1;
    public int gateIndex = -1;
    public int speed;
    public int action = -1;

    public Plane(int pri, int spe) {
        this.id = numero++;
        this.priority = pri;
        this.tasktime = -1;
        this.runwayIndex = -1;
        this.gateIndex = -1;
        this.speed = spe;
        this.action = -1;
        System.out.println("Plane created ID = " + id + " priority=" + priority);
    }

    @Override
    public String toString() {
        return "Plane{" + id + ", runaway=" + runwayIndex + ", gate=" + gateIndex +  ", tasktime=" + tasktime + ", task="+ action + "}";
    }
}