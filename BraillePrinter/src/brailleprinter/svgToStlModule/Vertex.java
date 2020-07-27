/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.svgToStlModule;

/**
 *
 * @author Мария
 */
public class Vertex {
    private double x, y, z;
    
//    public Vertex() {
//        
//    }
    
    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public static Vertex substractVectors(Vertex left, Vertex right) {
        return new Vertex(left.x - right.x, left.y - right.y, left.z - right.z);
    }
    
    public static Vertex multiplyVectors(Vertex left, Vertex right) {
        return new Vertex(
            left.y * right.z - left.z * right.y,
            left.z * right.x - left.x * right.z,
            left.x * right.y - left.y * right.x
        );
    }
    
    @Override
    public String toString() {
        return (String.valueOf(this.x) + " " + 
                String.valueOf(this.y) + " " + 
                String.valueOf(this.z));
    }
}
