package gitlet.utils;


import gitlet.entity.Commit;
import gitlet.exception.GitletException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class should implement function of the command "log".
 *
 * @author jeffrey-zhang
 * @version 1.0
 */
public class Log {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(Commit.DATE_FORMAT);

    /** Print all ancestor logs of given commit. */
    public static void printLogStack(Commit commit) {
        if (commit.getParent() == null) {
            System.out.println(getLog(commit));
            return;
        }
        System.out.println(getLog(commit));
        Commit firstParent = commit.getParent().get(0);
        printLogStack(firstParent);
    }

    public static String getLog(Commit commit) {
        String commitId = commit.getId();
        String commitTimestamp = commit.getTimestamp();
        String logDateTimestamp = timestampConverter(commitTimestamp);
        String message = commit.getMessage();
        String log = String.format("===\n" +
                "commit %s\n" +
                "Date: %s\n" +
                "%s\n",
                commitId,
                logDateTimestamp,
                message);
        return log;
    }

    /**
     * Convert timestamp in commit to proper format which is needed by log command.
     *
     * @param commitTimestamp the timestamp of given commit.
     * @return the proper formatted timestamp for git log command.
     * @throws GitletException if timestamp of given commit is failed to parse to the Date type.
     */
    private static String timestampConverter(String commitTimestamp) throws GitletException {
        SimpleDateFormat toDateFormatter = new SimpleDateFormat(Commit.DATE_FORMAT);
        Date commitDate;
        try {
            commitDate = toDateFormatter.parse(commitTimestamp);
        } catch (Exception e) {
            throw new GitletException("日期从string字符串转换成Date类型失败!");
        }
        String logTimestampPattern = "E dd MMM HH:mm:ss yyyy";
        SimpleDateFormat toTimestampFormatter = new SimpleDateFormat(logTimestampPattern);
        String logTimestamp = String.format("%s", toTimestampFormatter.format(commitDate));
        return logTimestamp;
    }
}
