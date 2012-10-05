package de.foopara.phpcsmd.debug;

import de.foopara.phpcsmd.option.GeneralOptions;

/**
 *
 * @author nspecht
 */
public class Logger {

    private static Logger instance = null;

    public static Logger getInstance() {
        if (Logger.instance == null) {
            Logger.instance = new Logger();
        }
        return Logger.instance;
    }

    protected StringBuilder buff = new StringBuilder();

    public void log(String str) {
        if (GeneralOptions.getDebugLog()) {
            this.buff.append(str).append("<br />\n");
        }
    }

    public void log(Exception ex) {
        this.log("--- Exception START ---");
        this.log(ex.getMessage());
        for(StackTraceElement element : ex.getStackTrace()) {
            this.log(element.toString());
        }
        this.log("--- Exception END ---");
    }

    public void logPre(String caption, String str) {
        str = str
                .replaceAll("[^A-Za-z0-9\\?\\.\\n\\r\\s\\w\\\\\\/=\"'<>]", "&otimes;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\n", "<br />");
        this.log("<b>" + caption + "</b><pre style=\"border:#444444 1px solid;padding:5px;\">" + str + "</pre>");
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("<html>")
            .append("<body style=\"font-size:10px\">")
            .append(this.buff.toString())
            .append("</body>")
            .append("</html>")
                ;
        return ret.toString();
    }

    public void clear() {
        this.buff = new StringBuilder();
    }
}
