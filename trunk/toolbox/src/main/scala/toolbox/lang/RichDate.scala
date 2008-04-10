package toolbox.lang

import java.text.{SimpleDateFormat}
import java.util.{Date}

/**
 * Some convinient date methods
 * 
 * @author thebugslayer
 */
object RichDate {
  def currentTime = System.currentTimeMillis
  def now = new Date()
  def datef(format :String, date :Date) = new SimpleDateFormat(format).format(date)
  
  def mkdate :Date = mkdate(datef("yyyy-MM-dd", now))
  def mkdatetime :Date = mkdate(datef("yyyy-MM-dd HH:mm:ss", now))
  def mkdate(yyyyMMdd :String) :Date = mkdatetime("yyyy-MM-dd", yyyyMMdd)
  def mkdatetime(yyyyMMddHHmmss :String) :Date = mkdatetime("yyyy-MM-dd HH:mm:ss", yyyyMMddHHmmss)
  def mkdatetime(format :String, date :String) :Date = new SimpleDateFormat(format).parse(date)
}
