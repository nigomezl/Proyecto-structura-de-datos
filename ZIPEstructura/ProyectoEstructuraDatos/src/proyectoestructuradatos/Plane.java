
package proyectoestructuradatos;

import java.util.Random;

public class Plane {
    static int numero = 0;
    int id;
    int priority;
    public int tasktime = -1;
    public int index = -1;
    public int index1 = -1;
    public int action = -1;
    
    public Plane(int pri){
        id = numero;
        priority = pri;
        numero++;
        System.out.println("Plane created ID = " + id);
    }
    
    
    
    
    public void decrementTime(){
        tasktime =- 1;
    }
    
    public void index(int i){
        this.index = i;
    }
}
