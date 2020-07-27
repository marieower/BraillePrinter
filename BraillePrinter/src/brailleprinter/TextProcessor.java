/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter;

import brailleprinter.svgToStlModule.StlGenerator;
import brailleprinter.textToSvgModule.SvgGenerator;
import com.sun.java.accessibility.util.Translator;

/**
 *
 * @author Мария
 */
public class TextProcessor {
    
    String text = "";
    String preparedText = "";
    String svg = "";
    String stl = "";
    
    private final char numberServiceSymbol = '&';
    private final char upperCaseServiceSymbol = '^';
    
    public TextProcessor(String text) {
        this.text = text;
        prepareForTranslation();
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getPreparedText() {
        return this.preparedText;
    }
    
    public String getSvg() {
        return this.svg;
    }
    
    public String getStl() {
        return this.stl;
    }
    
    private void prepareForTranslation() {
        putServiceSymbols();
        deleteSpaces();
    }
    
    public void translateToSvg() {
        SvgGenerator svgGenerator = new SvgGenerator(preparedText);
        try {
            this.svg = svgGenerator.getSvg();
        } catch (NullPointerException ex) {
            // TODO
        }
    }
    
    public void translateToStl() {
        StlGenerator stlGenerator = new StlGenerator(svg);
        
        this.stl = stlGenerator.getStl();
        
    }
    
    private void putServiceSymbols() {
        String buffer = "";
        
        for (int i = 0; i < this.text.length(); i++) {
            if ( Character.isDigit(this.text.charAt(i)) ) {
                if ( i == 0 ||
                        i != 0 && !(Character.isDigit(this.text.charAt(i - 1)))) {
                    buffer += numberServiceSymbol;
                }
            }
            if (Character.isLetter(this.text.charAt(i)) && Character.isUpperCase(this.text.charAt(i))) {
                    buffer += upperCaseServiceSymbol;
                }
            buffer += this.text.charAt(i);
        }
        
        this.preparedText = buffer.toLowerCase();
    }
    
    private void deleteSpaces() {
        String buffer = this.preparedText;
        
        buffer = buffer.replace(", ", ",");
        buffer = buffer.replace(" -", "-");
        
        this.preparedText = buffer;
    }
    
    
    
}