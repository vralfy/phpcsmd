package de.foopara.phpcsmd.debug;

import de.foopara.phpcsmd.option.GeneralOptions;

/**
 *
 * @author nspecht
 */
public class Logger
{

    private static volatile Logger instance = null;

    public static Logger getInstance() {
        if (Logger.instance == null) {
            Logger.instance = new Logger();
        }
        return Logger.instance;
    }

    protected StringBuilder buff = new StringBuilder();

    public void log(String str) {
        this.log(str, null);
    }

    public void log(String str, String caption) {
        if ((Boolean)GeneralOptions.loadOriginal(GeneralOptions.Settings.DEBUGLOG) == true) {
            this.buff.append("<tr>");
            if (caption != null) {
                this.buff.append("<td><b>").append(caption).append("</b></td>");
            }
            this.buff.append("<td>").append(str).append("</td>");
            this.buff.append("</tr>");
        }
    }

    public void log(Exception ex) {
        StringBuilder exStr = new StringBuilder();
//        exStr.append(ex.getMessage()).append("\n");
        for (StackTraceElement element : ex.getStackTrace()) {
            exStr.append(element.toString()).append("\n");
        }
        this.log("<pre style=\"padding:5px\">" + exStr.toString() + "</pre>", "Exception:<br />" + ex.getMessage());
    }

    public void logPre(String str, String caption) {
        str = str
                .replaceAll("[^A-Za-z0-9\\.\\n\\r\\w\\\\\\/\\!\\$%\\(\\)=\\?\"'<>-]", "&otimes;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\n", "<br />");
        this.log("<pre style=\"border:#444444 1px solid;padding:5px;\">" + str + "</pre>", caption);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("<html>")
                .append("<body style=\"font-size:10px\">")
                .append("<table cellspacing=\"0\" cellpadding=\"1\">")
                .append(this.buff.toString())
                .append("</table>")
                .append("</body>")
                .append("</html>");
        return ret.toString();
    }

    public void clear() {
        this.buff = new StringBuilder();
    }

}
