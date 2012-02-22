/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.util.Exceptions;

/**
 *
 * @author nspecht
 */
public class GenericProcess {
    public static GenericOutputReader run(ExternalProcessBuilder builder) {
        GenericOutputReader output = new GenericOutputReader();

        ExecutionDescriptor descriptor = new ExecutionDescriptor()
                .frontWindow(false)
                .controllable(false)
                .outProcessorFactory(output);

        ExecutionService service = ExecutionService
                .newService(builder, descriptor, "PHP Violations");
        Future<Integer> task = service.run();

        try {
            task.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }

        return output;
    }

    public static GenericOutputReader run(String cmd, File outputFile) {
        try {
            Process child = Runtime.getRuntime().exec(cmd);
            StringBuilder tmp = new StringBuilder();
            InputStream in = child.getInputStream();
            int c;
            if (outputFile == null) {
                while((c = in.read()) != -1) {
                    tmp.append((char)c);
                }
            } else {
                if (!GenericHelper.isDesirableFile(outputFile)) return new GenericOutputReader();
                InputStream err = child.getErrorStream();
                while((c = err.read()) != -1) {} //Ich muss das auslesen, damit der Prozess wartet
                while((c = in.read()) != -1) {}  //Ich muss das auslesen, damit der Prozess wartet
                child.waitFor();

                FileInputStream fis = new FileInputStream(outputFile);
                while((c = fis.read()) != -1) {
                    tmp.append((char)c);
                }
            }
            return new GenericOutputReader(tmp);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return new GenericOutputReader();
    }
}
