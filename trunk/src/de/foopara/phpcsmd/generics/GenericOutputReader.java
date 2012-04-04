/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.input.InputProcessor;

/**
 *
 * @author nspecht
 */
public class GenericOutputReader implements InputProcessor, ExecutionDescriptor.InputProcessorFactory {
    
    private StringBuilder output = new StringBuilder();
    
    public GenericOutputReader(StringBuilder s) {
        this.output.append(s);
    }
    
    public GenericOutputReader() {
        
    }
    
    @Override
    public InputProcessor newInputProcessor(InputProcessor ip) {
        return this;
    }
    
    @Override
    public void processInput(char[] chars) throws IOException {
        this.output.append(chars);
    }

    @Override
    public void reset() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
    
    public Reader getReader() {
        char[] c = new char[this.output.length()];
        this.output.getChars(0, this.output.length(), c, 0);

        return new CharArrayReader(c);
    }
    
    @Override
    public String toString() {
        return this.output.toString();
    }
    
}
