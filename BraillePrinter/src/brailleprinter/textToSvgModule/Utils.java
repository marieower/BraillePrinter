/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter.textToSvgModule;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Мария
 */
public class Utils {

    /** Максимальное количество глифов в строке */
    static final int MAX_LINE_LENGTH = 30;
    /** Максимальное количество строк в табличке */
    static final int MAX_LINES_COUNT = 25;
    
    /** Радиус одной брайлевской точки */
    static final double CIRCLE_RADIUS = 0.6;
    /** Расстояние между центрами соседних точек в одном глифе по горизонтали */
    static final double HORIZONTAL_SPACE_INSIDE_GLYPH = 2.5;
    /** Расстояние между центрами соседних точек в одном глифе по вертикали */
    static final double VERTICAL_SPACE_INSIDE_GLYPH = 2.5;
    /** Расстояние между центрами соседних точек разных глифов по горизонтали */
    static final double HORIZONTAL_SPACE_BETWEEN_GLYPHS = 3.5;
    /** Расстояние между центрами соседних точек разных глифов по вертикали */
    static final double VERTICAL_SPACE_BETWEEN_GLYPHS = 3.8;
    
    /** Расстояние от края страницы до первого символа по вертикали */
    static final double VERTICAL_PAGE_PADDING = 31.75;
    /** Расстояние от края страницы до первого символа по горизонтали */
    static final double HORIZONTAL_PAGE_PADDING = 26;
    
    
    /** Символы и наборы точек, которыми они кодируются (на позициях от 1 до 6)*/
    static Map<String, String> symbolsCirclesSets = new HashMap<String, String>() {{
        // English
        put("a", "100000");
        put("b", "110000");
        put("c", "100100");
        put("d", "100110");
        put("e", "100010");
        put("f", "110100");
        put("g", "110110");
        put("h", "110010");
        put("i", "010100");
        put("j", "010110");
        put("k", "101000");
        put("l", "111000");
        put("m", "101100");
        put("n", "101110");
        put("o", "101010");
        put("p", "111100");
        put("q", "111110");
        put("r", "111010");
        put("s", "011100");
        put("t", "011110");
        put("u", "101001");
        put("v", "111001");
        put("w", "010111");
        put("x", "101101");
        put("y", "101111");
        put("z", "101011");
        // Russian
        put("а", "100000");
        put("б", "110000");
        put("в", "010111");
        put("г", "110110");
        put("д", "100110");
        put("е", "100010");
        put("ё", "100001");
        put("ж", "010110");
        put("з", "101011");
        put("и", "010100");
        put("й", "111101");
        put("к", "101000");
        put("л", "111000");
        put("м", "101100");
        put("н", "101110");
        put("о", "101010");
        put("п", "111100");
        put("р", "111010");
        put("с", "011100");
        put("т", "011110");
        put("у", "101001");
        put("ф", "110100");
        put("х", "110010");
        put("ц", "100100");
        put("ч", "111110");
        put("ш", "100011");
        put("щ", "101101");
        put("ъ", "111011");
        put("ы", "011101");
        put("ь", "011111");
        put("э", "010101");
        put("ю", "110011");
        put("я", "110101");
        // Numbers
        put("0", "010110");
        put("1", "100000");
        put("2", "110000");
        put("3", "100100");
        put("4", "100110");
        put("5", "100010");
        put("6", "110100");
        put("7", "110110");
        put("8", "110010");
        put("9", "010100");
        // service symbol for number
        put("&", "001111");
        // service symbol for capital letter
        put("^", "000110");
        // Symbols
        put(".", "010011");
        put(",", "010000");
        put(":", "010010");
        put(";", "011000");
        put("-", "001001");
        put("!", "011010");
        put("?", "010001");
        put(" ", "000000");
    }};
}

