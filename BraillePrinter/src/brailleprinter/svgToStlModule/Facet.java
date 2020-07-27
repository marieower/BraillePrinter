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
public class Facet {
    
    /** Точки, задающие плоскость */
    private Vertex A;
    private Vertex B;
    private Vertex C;
    
    /** Нормаль? плоскости */
    private Vertex N;
    
    public Facet(Vertex A, Vertex B, Vertex C) {
        this.A = A;
        this.B = B;
        this.C = C;
        
        Vertex ba = Vertex.substractVectors(B, A);
        Vertex ca = Vertex.substractVectors(C, A);
        
        this.N = Vertex.multiplyVectors(ba, ca);
        
        double max = 
                Math.max(
                        Math.max(
                                Math.abs(this.N.getX()), 
                                Math.abs(this.N.getY())), 
                                Math.abs(this.N.getZ()));
        
        if (max != 0) {
            this.N = new Vertex(this.N.getX() / max, 
                                this.N.getY() / max, 
                                this.N.getZ() / max);
        }
    }
    
    public Vertex getA() {
        return this.A;
    }
    
    public Vertex getB() {
        return this.B;
    }
    
    public Vertex getC() {
        return this.C;
    }
    
    public Vertex getN() {
        return this.N;
    }
    
}
