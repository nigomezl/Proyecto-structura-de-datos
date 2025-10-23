/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AeropuertoProyecto;

public class Buffer {
    public Plane plane;
    public boolean available;

    public Buffer() {
        this.plane = null;
        this.available = true;
    }
    
    public Plane getPlane(){
        return plane;
    }
}