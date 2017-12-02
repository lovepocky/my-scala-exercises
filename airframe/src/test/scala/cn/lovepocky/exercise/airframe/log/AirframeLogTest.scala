package cn.lovepocky.exercise.airframe.log

import org.scalatest.FlatSpec
import wvlet.log.{LogFormatter, LogRecord, LogSupport, Logger}
import pprint.pprintln
import wvlet.log.LogTimestampFormatter.formatTimestamp


class AirframeLogTest extends FlatSpec with LogSupport {

  "airframe log" should "work" in {

    Logger.scheduleLogLevelScan

    pprintln("logger level can be set in log.properties and log-test.properties")

    pprint.pprintln("logger.getLogLevel = " + logger.getLogLevel)
    pprint.pprintln(logger.getHandlers.toList)
    pprint.pprintln(logger.getName)

  }

  it should "log various formats" in {
    /**
      * this is best formatter in my opinion
      */
    pprintln("LogFormatter.SourceCodeLogFormatter")
    logger.setFormatter(LogFormatter.SourceCodeLogFormatter)
    logMessages()

    pprintln("LogFormatter.TSVLogFormatter")
    logger.setFormatter(LogFormatter.TSVLogFormatter)
    logMessages()

    pprintln("LogFormatter.AppLogFormatter")
    logger.setFormatter(LogFormatter.AppLogFormatter)
    logMessages()
  }

  it should "use custom formatter" in {
    import wvlet.log.LogFormatter._
    /**
      * [[AppLogFormatter]]
      * [[TSVLogFormatter]]
      * [[SourceCodeLogFormatter]]
      */
    object CustomLogFormatter extends LogFormatter {
      override def formatLog(r: LogRecord): String = {
        val loc =
          r.source
            .map(source => s" ${withColor(Console.BLUE, s"- (${source.fileLoc})")}")
            .getOrElse("")

        val logTag = highlightLog(r.level, r.level.name)
        val log =
          f"${withColor(Console.BLUE, formatTimestamp(r.getMillis))} ${logTag}%14s [${withColor(Console.WHITE, r.leafLoggerName)}] ${highlightLog(r.level, r.getMessage)} - ${currentThreadName}${loc}"
        appendStackTrace(log, r)
      }
    }

    pprintln("CustomLogFormatter")
    //Logger.setDefaultFormatter(CustomLogFormatter)
    logger.setFormatter(CustomLogFormatter)
    logMessages()

  }

  it should "use custom handler" in {
    /**
      * TODO
      * https://github.com/wvlet/log#writing-and-rotating-logs-with-files
      */
  }

  def logMessages(): Unit = {
    trace("this is trace message")
    debug("this is debug message")
    info("this is info message")
    warn("this is warn message")
    error("this is error message", new Exception("cause reason"))
  }
}


