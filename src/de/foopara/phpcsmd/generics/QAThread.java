/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.exec.phpcs.Phpcs;
import de.foopara.phpcsmd.exec.phpmd.Phpmd;
import java.util.LinkedHashMap;
import org.openide.filesystems.FileObject;

/**
 *
 * @author n.specht
 */
public class QAThread extends Thread {
        private static LinkedHashMap<String, QAThread> instances = new LinkedHashMap<String, QAThread>();

        private FileObject fo =null;

        private boolean interupted = false;

        public void setFileObject(FileObject fo) {
            this.fo = fo;
        }

        /*
         * Nur damit Netbeans die Klappe hällt
         */
        @Override
        public void run() {
            this.qarun();
        }

        public void qarun() {
            if (QAThread.instances.containsKey(this.fo.getPath())) {
                QAThread.instances.get(this.fo.getPath()).interupt();
            }
            QAThread.instances.put(this.fo.getPath(), this);

            if (!this.interupted) new Phpcs().execute(this.fo);
            if (!this.interupted) new Phpmd().execute(this.fo);
            if (!this.interupted) ViolationRegistry.getInstance().reprintTasks(this.fo);


        }

        public void interupt() {
            this.interupted = true;
            QAThread.instances.remove(this.fo.getPath());
        }
}
