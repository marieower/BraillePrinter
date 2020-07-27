/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.svgToStlModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;

/**
 *
 * @author Мария
 */
public class StlGenerator {
    
    private String stl = "";
    private String svg = "";
    private List<Pair<Double, Double>> circleCenters = new ArrayList<Pair<Double, Double>>();;
    private final String svgCirclePattern = "<circle cx=\"([0-9]+(\\.[0-9]+)?(mm)?)\" cy=\"([0-9]+(\\.[0-9]+)?(mm)?)\".*>";
    private final int pageHeight = 5;
    
    public StlGenerator(String svg) {
        this.svg = svg;
        this.parseSvg();
        convertToStl();
    }
    
    private void parseSvg() {
        Pattern circlePattern = Pattern.compile(svgCirclePattern);
        List<String> list = new ArrayList<String>();
        Matcher m = circlePattern.matcher(this.svg);
        while (m.find()) {
            list.add(m.group());
        }
        
        double cx = 0;
        double cy = 0;
        
        for (String circle : list) {
            Pattern cxPattern = Pattern.compile("cx=\\\"([0-9]+(\\.[0-9]+)?)(mm)?\\\"");
            Matcher cxMatcher = cxPattern.matcher(circle);

            Pattern cyPattern = Pattern.compile("cy=\\\"([0-9]+(\\.[0-9]+)?)(mm)?\\\"");
            Matcher cyMatcher = cyPattern.matcher(circle);

            if (cxMatcher.find() && cyMatcher.find()) {
                cx = Double.parseDouble(cxMatcher.group(1));
                cy = Double.parseDouble(cyMatcher.group(1));
                
                //отзеркалить cy, так как в stl и svg разные системы координат
                cy = -cy;
                this.circleCenters.add(new Pair<Double, Double>(cx, cy));
            }
        }
        
    }
    
    public void convertToStl() {
        this.stl = "";
        this.stl += "solid OpenSCAD_Model\n";
        this.stl += buildBase(pageHeight);
        
        for ( Pair<Double, Double> circle : this.circleCenters) {
            stl += createStlSpherical(circle.getKey(), circle.getValue(), pageHeight);
        }
        
        this.stl += "endsolid OpenSCAD_Model";
        
    }
    
    public String getStl() {
        return this.stl;
    }
    
    public String buildBase(int height) {
        
        List<Vertex> verticesBottomRect = new ArrayList<Vertex>();
        List<Vertex> verticesTopRect = new ArrayList<Vertex>();
        
        int rectWidth = 230;
        int rectHeight = 310;
        
        verticesBottomRect.add(new Vertex(0, 0, 0));
        verticesBottomRect.add(new Vertex(rectWidth, 0, 0));
        verticesBottomRect.add(new Vertex(rectWidth, -rectHeight, 0));
        verticesBottomRect.add(new Vertex(0, -rectHeight, 0));
        
        verticesTopRect.add(new Vertex(0, 0, height));
        verticesTopRect.add(new Vertex(rectWidth, 0, height));
        verticesTopRect.add(new Vertex(rectWidth, -rectHeight, height));
        verticesTopRect.add(new Vertex(0, -rectHeight, height));
        
        List<Facet> rectFacets = new ArrayList<Facet>();

	//Треугольники площадки
	for (int indexDown = 0; indexDown < verticesBottomRect.size() - 1; indexDown++) {
            
            Facet facet = new Facet(
                    verticesBottomRect.get(indexDown), 
                    verticesBottomRect.get(indexDown + 1),
                    verticesTopRect.get(indexDown));
            rectFacets.add(facet);
	}

	for (int indexUp = verticesTopRect.size()-1; indexUp > 0; indexUp--)
	{
		Facet facet = new Facet(
                        verticesTopRect.get(indexUp), 
                        verticesTopRect.get(indexUp - 1),
                        verticesBottomRect.get(indexUp));
                rectFacets.add(facet);
	}

        rectFacets.add(new Facet (
                verticesTopRect.get(0),
                verticesTopRect.get(3),
                verticesBottomRect.get(0)));
	
	rectFacets.add(new Facet(
                verticesBottomRect.get(0), 
                verticesBottomRect.get(3), 
                verticesTopRect.get(3)));
	
	rectFacets.add(new Facet(
                verticesBottomRect.get(0), 
                verticesBottomRect.get(1), 
                verticesBottomRect.get(2)));
	
	rectFacets.add(new Facet(
                verticesBottomRect.get(2), 
                verticesBottomRect.get(3), 
                verticesBottomRect.get(0)));
	
	rectFacets.add(new Facet(
                verticesTopRect.get(0), 
                verticesTopRect.get(1), 
                verticesTopRect.get(2)));

	rectFacets.add(new Facet(
                verticesTopRect.get(2), 
                verticesTopRect.get(3), 
                verticesTopRect.get(0)));
	
        String shape = "";
        
        for (Facet facet : rectFacets) {
            shape += "  facet normal ";
            shape += facet.getN().toString();
            shape += "\n";
            shape += "    outer loop\n";
            shape += "      vertex ";
            shape += facet.getA().toString();
            shape += "\n";
            shape += "      vertex ";
            shape += facet.getB().toString();
            shape += "\n";
            shape += "      vertex ";
            shape += facet.getC().toString();
            shape += "\n";
            shape += "    endloop\n";
            shape += "  endfacet\n";
        }
        
        return shape;
    }

    public String createStlSpherical(double cx, double cy, double cz) {
        
        Pair<Double, Double> center = new Pair<Double, Double>(cx, cy);
	double radius = 0.6;
	double height = 0.5;
	double heightStep = 0.5/3;
	int fullRad = 360;
	int angleStep = 30;
        
        double sphereRadius = 0.61;
        
        double sectionRadius = 0;
        
	List<Base> bases = new ArrayList<Base>();
        
        for (int i = 0; i < height/heightStep + 1; i++) {
            double h = i*heightStep;
            sectionRadius = calculateSphereSectionRadius(sphereRadius, height, h);
            
            Base base = new Base();
            
            for (int angle = 0; angle <= fullRad; angle += angleStep) {
                Vertex vertex = new Vertex(
                        center.getKey() + sectionRadius*Math.cos(angle * Math.PI / 180), 
                        center.getValue() + sectionRadius*Math.sin(angle * Math.PI / 180),
                        h + cz);
                base.addVertex(vertex);
            }
            
            bases.add(base);
            
        }
	
	List<List<Facet>> allFacets = new ArrayList<>();
	List<Facet> facetOne = new ArrayList<Facet>();
	
        for (int indBase = 0; indBase < bases.size()-1; indBase++) {
            List<Vertex> verticesBottom = bases.get(indBase).getVertices();
            List<Vertex> verticesTop = bases.get(indBase+1).getVertices();

            int verticesCountBottom = verticesBottom.size();
            int verticesCountTop = verticesTop.size();

            if (verticesCountTop != 1) {
                for (int indexDown = 0; indexDown < verticesCountBottom - 1; indexDown++) {
                    facetOne.add(new Facet(
                            verticesBottom.get(indexDown), 
                            verticesBottom.get(indexDown + 1), 
                            verticesTop.get(indexDown)));
                }

                for (int indexUp = verticesCountBottom - 1; indexUp > 0; indexUp--) {
                    facetOne.add(new Facet(
                            verticesTop.get(indexUp), 
                            verticesTop.get(indexUp - 1), 
                            verticesBottom.get(indexUp)));
                }

                allFacets.add(facetOne);
            } else {
                for (int indexDown = 0; indexDown < verticesCountBottom - 1; indexDown++) {
                    facetOne.add(new Facet(
                            verticesBottom.get(indexDown), 
                            verticesBottom.get(indexDown + 1), 
                            verticesTop.get(0)));
                }
            }
	}
        
        String shape = "";
        
        //Вычисленные нормали
        for (List<Facet> facets : allFacets) {
            for (Facet facet : facets) {
                shape += "  facet normal ";
			shape += facet.getN().toString();
			shape += "\n";
			shape += "    outer loop\n";
			shape += "      vertex ";
			shape += facet.getA().toString();
			shape += "\n";
			shape += "      vertex ";
			shape += facet.getB().toString();
			shape += "\n";
			shape += "      vertex ";
			shape += facet.getC().toString();
			shape += "\n";
			shape += "    endloop\n";
			shape += "  endfacet\n";
            }
        }
	
	return shape;
    }
    
    private double calculateSphereSectionRadius(double sphereRadius, double pointHeight, double sectionHeight) {
        
        double result = sphereRadius*sphereRadius - 
                (sphereRadius - pointHeight + sectionHeight)*(sphereRadius - pointHeight + sectionHeight);
                
        result = Math.sqrt(result);
        return result;
    }
}

