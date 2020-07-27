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
public class BrailleLine {
    
    /** Символы, содержащиеся в строке */
    private List<BrailleToken> tokens;
    
    /**
     * Конструктор из строки
     * @param input строка для перевода в брайлевские глифы (0-30 символов)
     * @throws IOException если передана слишком длинная строка
     */
    public BrailleLine(String input) throws IOException {
        if (input.length() > Utils.MAX_LINE_LENGTH) {
            throw new IOException("Too many symbols in input string");
        }
        
        this.tokens = new ArrayList<BrailleToken>();
        
        // Создать и добавить глиф для каждого символа входной строки
        for (int i = 0; i < input.length(); ++i) {
            this.addToken(new BrailleToken(input.substring(i, i+1)));
        }
    }
    
    
    public void addToken(BrailleToken token) throws IOException {
        if (this.tokens.size() >= Utils.MAX_LINE_LENGTH) {
            throw new IOException("Too many symbols in line");
        }
        
        this.tokens.add(token);
    }
    
    public List<BrailleToken> getBrailleTokens() {
        return Collections.unmodifiableList(this.tokens);
    }
}
