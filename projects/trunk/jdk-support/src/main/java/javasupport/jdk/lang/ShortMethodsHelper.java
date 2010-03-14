package javasupport.jdk.lang;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Typical java lang support methods for easy and quick coding.
 * 
 * @author Zemian Deng
 * @since Wed Feb 20 00:39:43 EST 2008
 */
public class ShortMethodsHelper {
    //IO
    public static void println(Object... things) {
        for (Object obj : things) {
            System.out.println(obj);
        }
    }

    public static void printf(String format, Object... things) {
        System.out.printf(format, things);
    }

    public static void copyStream(InputStream ins, OutputStream outs) {
        int MB = 1024 * 1024;
        byte[] buf = new byte[MB];
        int len = 0;
        try {
            while ((len = ins.read(buf, 0, MB)) != -1) {
                outs.write(buf, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sprintf(String format, Object... things) {
        return String.format(format, things);
    }

    //dates
    public static Date now() {
        return new Date();
    }
    
    public static String datef(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }
    
    public static Date mkdate() {
        return mkdate(datef("yyyy-MM-dd", new Date()));
    }

    public static Date mkdatetime() {
        return mkdate(datef("yyyy-MM-dd HH:mm:ss", new Date()));
    }

    public static Date mkdate(String yyyyMMdd) {
        return mkdatetime("yyyy-MM-dd", yyyyMMdd);
    }

    public static Date mkdatetime(String yyyyMMddHHmmss) {
        return mkdatetime("yyyy-MM-dd HH:mm:ss", yyyyMMddHHmmss);
    }

    public static Date mkdatetime(String format, String date) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //collections
    public static <T> List<T> mklist(T... things) {
        List<T> res = new ArrayList<T>();
        for (T obj : things) {
            res.add(obj);
        }
        return res;
    }

    public static <T> List<T> slice(List<T> ls, int start, int end) {
        return slice(ls, start, end, 1);
    }

    public static <T> List<T> slice(List<T> ls, int start, int end, int step) {
        int lsize = ls.size();
        if (start < 0) {
            start = lsize + start;
        } //count neg elem from back of list
        if (end < 0) {
            end = lsize + end;
        }
        List<T> newls = new ArrayList<T>();
        for (int i = start; i <= end; i += step) {
            newls.add(ls.get(i));
        }
        return newls;
    }

    //file IO
    public static void writeText(String filename, String text) {
        writeLines(filename, mklist(text));
    }

    public static void writeLines(String filename, List<String> lines) {
        try {
            FileWriter writer = new FileWriter(filename);
            for (String ln : lines) {
                writer.write(ln);
            }
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readText(String filename) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            copyStream(new FileInputStream(filename), output);
            return output.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLines(String filename) {
        try {
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String ln = null;
            while ((ln = reader.readLine()) != null) {
                lines.add(ln);
            }
            return lines;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //strings
    public static String join(List<String> lines, String sep) {
        StringBuilder sb = new StringBuilder();
        for (String ln : lines) {
            sb.append(ln + sep);
        }
        if (lines.size() > 0) {
            sb.delete(sb.length() - sep.length(), sb.length());
        } //remove last extra sep
        return sb.toString();
    }

    public static Matcher regexMatches(String pattern, String input) {
        Matcher m = Pattern.compile(pattern).matcher(input);
        if (m.matches()) {
            return m;
        } else {
            throw new RuntimeException("No input matches.");
        }
    }

    public static Matcher regexFind(String pattern, String input) {
        Matcher m = Pattern.compile(pattern).matcher(input);
        if (m.find()) {
            return m;
        } else {
            throw new RuntimeException("No input found.");
        }
    }

    // testing
    public static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("Expected " + expected + ", but got " + actual);
        }
    }

    //system/kernel
    public static void exit(String msg) {
        println(msg);
        System.exit(0);
    }

    public static String exec(String... cmdArgs) {
        try {
            ProcessBuilder pb = new ProcessBuilder(cmdArgs);
            Process p = pb.redirectErrorStream(true).start();
            p.waitFor();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            copyStream(p.getInputStream(), output);
            return output.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
