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
public class BrailleTable {
    
    /**Строки, содержащиеся на этой странице*/
    private List<BrailleLine> lines;
    
    /**
     * Конструктор таблички Брайля из текста
     * @param input Текст для перефода в брайлевские глифы
     * @throws IOException если символов в тексте больше максимального количества
     */
    public BrailleTable(String input) throws IOException {
        if (input.length() > Utils.MAX_LINE_LENGTH * Utils.MAX_LINES_COUNT) {
            throw new IOException("Too many symbols in input string");
        }
        
        this.lines = new ArrayList<BrailleLine>();
        
        String currentLine;
        
        while(input.length() > 30) {
            // Создаем строку из первых 30 символов 
            currentLine = input.substring(0, Utils.MAX_LINE_LENGTH);
            // Добавляем в таблицу
            this.addLine(new BrailleLine(currentLine));
            
            // Переходим к обработке следующей части строки
            input = input.substring(Utils.MAX_LINE_LENGTH);
        }
        
        this.addLine(new BrailleLine(input));
        
    }
    
    public void addLine(BrailleLine line) throws IOException {
        if (this.lines.size() >= Utils.MAX_LINES_COUNT) {
            throw new IOException("Too many lines in table");
        }
        
        this.lines.add(line);
    }
    
    public List<BrailleLine> getBrailleLines() {
        return Collections.unmodifiableList(this.lines);
    }
}
