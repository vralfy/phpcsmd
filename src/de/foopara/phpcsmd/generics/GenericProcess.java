/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import java.io.Reader;
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
    public static Reader run(ExternalProcessBuilder builder) {
        GenericOutputReader output = new GenericOutputReader();

        ExecutionDescriptor descriptor = new ExecutionDescriptor()
                .frontWindow(false)
                .controllable(false)
                .outProcessorFactory(output);

        ExecutionService service = ExecutionService
                .newService(builder, descriptor, "PHPCSMD");
        Future<Integer> task = service.run();
        try {
            task.get();
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }

        return output.getReader();
    }
}
