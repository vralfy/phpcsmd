/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.foopara.phpcsmd.generics;

import de.foopara.phpcsmd.PHPCSMD;
import de.foopara.phpcsmd.ViolationRegistry;
import de.foopara.phpcsmd.option.GeneralOptions;
import java.net.URL;
import javax.swing.ImageIcon;
import org.openide.awt.Notification;
import org.openide.awt.NotificationDisplayer;
import org.openide.filesystems.FileObject;

/**
 *
 * @author n.specht
 */
public class GenericNotification {
    private static Notification notify = null;

    public static void displayNotification(FileObject fo) {
        if (GenericNotification.notify != null) {
            GenericNotification.notify.clear();
        }

        if (!GeneralOptions.getNotification()) return;

        GenericResult phpcs = ViolationRegistry.getInstance().getPhpcs(fo);
        GenericResult phpmd = ViolationRegistry.getInstance().getPhpmd(fo);
        GenericResult phpcpd = ViolationRegistry.getInstance().getPhpcpd(fo);

        int phpcsError = phpcs.getErrors().size();
        int phpcsWarn = phpcs.getWarnings().size();

        int phpmdError = phpmd.getErrors().size();
        int phpmdWarn = phpmd.getWarnings().size();

        int phpcpdError = phpcpd.getErrors().size();
        int phpcpdWarn = phpcpd.getWarnings().size();

        int sum = 0
                + phpcsError + phpcsWarn
                + phpmdError + phpmdWarn
                + phpcpdError + phpcpdWarn
                ;
        if (sum > 0) {
            StringBuilder text = new StringBuilder();
            text.append("<html><body style=\"font-size:8px;\"><table style=\"width:100%;\">");
            GenericNotification.appendDetail(text, "phpcs", phpcsError, phpcsWarn);
            GenericNotification.appendDetail(text, "phpmd", phpmdError, phpmdWarn);
            GenericNotification.appendDetail(text, "phpcpd", phpcpdError, phpcpdWarn);
            text.append("</table></body></html>");

            URL iconRes = PHPCSMD.class.getResource("resources/phpcs/violation.jpg");
            ImageIcon icon;
            if (iconRes != null) {
                icon = new ImageIcon(iconRes);
            } else {
                icon = new ImageIcon();
            }

            GenericNotification.notify = NotificationDisplayer.getDefault().notify(sum + " violations", icon, text.toString(), null);
        }
    }

    private static void appendDetail(StringBuilder text, String caption, int errors, int warnings) {
        if (errors + warnings > 0) {
            text.append("<tr><td colspan=\"2\">").append(caption).append("</td></tr>");
            if (errors > 0) {
                text
                        .append("<tr>")
                        .append("<td style=\"width:10px\">&nbsp;</td>")
                        .append("<td>errors:</td>")
                        .append("<td style=\"color:#ff0000;\"><b>").append(errors).append("</b></td>")
                        .append("</tr>");
            }
            if (warnings > 0) {
                text
                        .append("<tr>")
                        .append("<td style=\"width:10px\">&nbsp;</td>")
                        .append("<td>warnings:</td>")
                        .append("<td style=\"color:#CCCC00;\"><b>").append(warnings).append("</b></td>")
                        .append("</tr>");
            }
        }
    }
}
