/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.textToSvgModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Мария
 */
public class BrailleToken {
//    /** Порядковый номер символа в строке */
//    private int index;
    
    /** Символ, кодируемый глифом */
    private String symbol;
    
    /** Точки, входящие в глиф */
    private List<CirclePosition> circlePositions;
    
    public BrailleToken(String symbol) throws IOException {
        this.symbol = symbol;
        this.circlePositions = new ArrayList<CirclePosition>();
        
        // Добавляю точки из словаря
        String circlesPos;
        try {
            circlesPos = Utils.symbolsCirclesSets.get(this.symbol);
        } catch (Exception ex) {
            throw new IOException("No such symbol in dictionary");
        }
        
        int col, row;
        
        // Преобразую в CirclePosition все точки, присутствующие в глифе:
        
        // Для каждой из возможных шести точек
        for (int i = 0; i < 6; ++i) {
            // Если на этой позиции есть точка
            if (circlesPos.charAt(i) != '0') {
                // Вычислим, в каком она столбце
                col = (i) / 3;
                // Вычислим, в какой она строке
                row = (i) % 3;
                // Добавим в глиф точку с полученными координатами
                this.circlePositions.add(new CirclePosition(row, col));
            }
        }
        
        
    }

    public String getSymbol() {
        return this.symbol;
    }
    
    public List<CirclePosition> getCirclesPositions() {
        return Collections.unmodifiableList(this.circlePositions);
    }

    public class CirclePosition {
        private int row;
        private int column;
        
        public CirclePosition(int row, int column) {
            this.row = row;
            this.column = column;
        }
        
        public int getRow() {
            return this.row;
        }
        
        public int getColumn() {
            return this.column;
        }
    }
}
