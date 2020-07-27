/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.svgToStlModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Мария
 */
public class Base {
    private List<Vertex> vertices = new ArrayList<Vertex>();
    
    public void addVertex(Vertex vertex) {
        this.vertices.add(vertex);
    }
    
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(this.vertices);
    }
}
