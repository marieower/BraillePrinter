/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.textToSvgModule;

import brailleprinter.textToSvgModule.BrailleToken.CirclePosition;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Мария
 */
public class SvgGenerator {
    
    private String inputText;
    private String svg = "";

    public SvgGenerator(String inputText) {
        this.inputText = inputText;
        convertToSvg();
    }
    /**
     * Создает строку с svg описанием таблички на основе inputText
     */
    private void convertToSvg() {
        // Создаем брайлевскую таблицу на основе полученного текста
        BrailleTable table = null;
        try {
            table = new BrailleTable(this.inputText);
        } catch (IOException ex) { }
        
        // Создаем описание точек для отображения в SVG 
        double cx, cy;     // Координаты центра точки
        for (int i = 0; i < table.getBrailleLines().size(); ++i) {
            BrailleLine currentLine = table.getBrailleLines().get(i);
            
            for (int j = 0; j < currentLine.getBrailleTokens().size(); ++j) {
                BrailleToken currentToken = currentLine.getBrailleTokens().get(j);
                
                    for (int k = 0; k < currentToken.getCirclesPositions().size(); ++k) {
                        CirclePosition currentCircle = currentToken.getCirclesPositions().get(k);
                        
                        cx = calculateCX(j, currentCircle.getColumn());
                        cy = calculateCY(i, currentCircle.getRow());
                        
                        this.svg += createSvgCircle(cx, cy);
                    }
            }
        }
        
        // Добавляем необходимые теги
        this.svg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<svg  version=\"1.1\" width=\"1280\" height=\"1024\"\n" +
                    "        viewBox=\"0 0 1280 1024\"\n" +
                    "     baseProfile=\"full\"\n" +
                    "     xmlns=\"http://www.w3.org/2000/svg\"\n" +
                    "     xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" +
                    "     xmlns:ev=\"http://www.w3.org/2001/xml-events\">\n" +
                    "                  \n" +
                    this.svg +

                    "\n</svg>";
    }
    
    public String getSvg() {
        return this.svg;
    }
    
    /**
     * Вычисляет координату Х центра точки
     * @param tokenIndex индекс глифа, которому принадлежит точка, в строке
     * @param circleColumn колонка, в которой находится точка (внутри глифа)
     * @return координата Х точки
     */
    private double calculateCX(int tokenIndex, int circleColumn) {
        return Utils.HORIZONTAL_PAGE_PADDING                        // Отступ от края таблички

                + tokenIndex *                                      // Отступ от предыдущих глифов
                (Utils.HORIZONTAL_SPACE_INSIDE_GLYPH + 
                    Utils.HORIZONTAL_SPACE_BETWEEN_GLYPHS)
                
                + circleColumn*Utils.HORIZONTAL_SPACE_INSIDE_GLYPH; // Отступ внутри глифа (если точка во втором столбце)
    }
    
    /**
     * Вычисляет координату Y центра точки
     * @param lineIndex индекс строки, в которой находится глиф с данной точкой, в табличке
     * @param circleRow ряд, в котором находится точка (внутри глифа)
     * @return координата Y точки
     */
    private double calculateCY(int lineIndex, int circleRow) {
        return Utils.VERTICAL_PAGE_PADDING                          // Отступ от края таблички
                
                + lineIndex *                                       // Отступ от предыдущих глифов
                (Utils.VERTICAL_SPACE_INSIDE_GLYPH * 2 + 
                Utils.VERTICAL_SPACE_BETWEEN_GLYPHS)
        
                + circleRow*Utils.VERTICAL_SPACE_INSIDE_GLYPH;    // Отступ внутри глифа (если точка во 2 или 3 строке)
    }
    
    /**
     * Создает строку с SVG описанием одной точки по заданным координатам центра
     * @param cx координата X центра
     * @param cy координата Y центра
     * @return SVG с описанием точки
     */
    private String createSvgCircle(double cx, double cy) {
        return ("\n<circle " +
                "cx=\"" + cx + "mm\" " +
                "cy=\"" + cy + "mm\" " +
                "r=\"" + Utils.CIRCLE_RADIUS + ""
                + "mm\" " +
                "stroke=\"black\" " +
                "stroke-width=\"1\" " +
                "fill=\"black\" />");
    }
    
}
