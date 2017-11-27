package library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author INT
 */

public class ToolsLibrary {

    public static final DateFormat dateTimeFormat;
    static {
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH", Locale.US);
    }

}
