/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brailleprinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Мария
 */
public class FileProcessor {
    File directory;
    
    public FileProcessor(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IOException("Path is not a directory");
        } else {
            this.directory = directory;
        }
    }
    
    public File createFile(String fileName, String fileExtension) throws IOException {
        String extendedName = fileName + "." + fileExtension;
        File dest = new File(directory, extendedName);
        int numOfTry = 0;
        while (dest.exists()) {
            numOfTry++;
            extendedName = fileName + "(" + Integer.toString(numOfTry) + ")." + fileExtension;
            dest = new File(directory, extendedName);
        }
        
        dest.createNewFile();
        
        return dest;
    }
    
    public void writeToFile(File dest, String text) {
        if (dest.exists() && dest.isFile()) {
            try{
                    FileWriter fw = new FileWriter(dest);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(text);
                    bw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
            }
        }
    }
}
