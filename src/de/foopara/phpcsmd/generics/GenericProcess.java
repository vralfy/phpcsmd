package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.debug.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

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
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        }

        return output;
    }

    public static GenericOutputReader[] run(String cmd, String outputFile, GenericTopComponent topComponent, Lookup lkp) {
        if (outputFile.compareTo("") == 0 || outputFile == null) {
            return GenericProcess.run(cmd, new File[] {}, topComponent, lkp);
        }
        return GenericProcess.run(cmd, new File[] {new File(outputFile)}, topComponent, lkp);

    }

    public static GenericOutputReader[] run(String cmd, File[] outputFiles, GenericTopComponent topComponent, Lookup lkp) {
        if (outputFiles.length == 0) {
            outputFiles = null;
        }

        try {
            Process child = Runtime.getRuntime().exec(cmd);
            StringBuilder tmp = new StringBuilder();
            InputStream in = child.getInputStream();
            int c;
            if (outputFiles == null) {
                while((c = in.read()) != -1) {
                    tmp.append((char)c);
                }
                return new GenericOutputReader[]{new GenericOutputReader(tmp)};
            } else {
                InputStream err = child.getErrorStream();

                while((c = in.read()) != -1) {
                    if (topComponent != null) {
                        tmp.append((char)c);
                        topComponent.setCommandOutput(tmp.toString());
                    }
                }
                while((err.read()) != -1) {} //Ich muss das auslesen, damit der Prozess wartet

                child.waitFor();
                child.exitValue();

                HashSet<GenericOutputReader> reader = new HashSet<GenericOutputReader>();
                for (File outputFile : outputFiles) {
                    if (GenericHelper.isDesirableFile(outputFile, false, lkp)) {
                        tmp = new StringBuilder();
                        FileInputStream fis = new FileInputStream(outputFile);
                        while((c = fis.read()) != -1) {
                            tmp.append((char)c);
                        }
                        reader.add(new GenericOutputReader(tmp));
                    }
                }

                Object[] tmpReader = reader.toArray();
                GenericOutputReader[] res = new GenericOutputReader[tmpReader.length];
                for (int i=0;i<tmpReader.length;i++) {
                    res[i] = (GenericOutputReader)tmpReader[i];
                }

                return res;
            }
        } catch (InterruptedException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Logger.getInstance().log(ex);
            Exceptions.printStackTrace(ex);
        }
        return new GenericOutputReader[]{};
    }
}
