package digitalphotoframe;

import java.util.Date;
import java.util.Formattable;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * LogFormatter
 */
public class LogFormatter extends Formatter {

    @Override
    public synchronized String format(LogRecord logRecord) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(String.format("[%1$tF %1$tT] [%2$-7s] %3$s %n",
                new Date(logRecord.getMillis()),
                logRecord.getLevel().getLocalizedName(),
                logRecord.getMessage()));
        return resultBuilder.toString();
    }

}