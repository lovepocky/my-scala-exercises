package com.xyz10.base.log.ExecuteContext

import com.typesafe.scalalogging.LazyLogging
import org.slf4j.MDC

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutor

class CustomMdcExecuteContext(mdcContext: Map[String, String], delegate: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global)
    extends ExecutionContextExecutor
    with LazyLogging {
  override def execute(command: Runnable): Unit =
    delegate.execute(new Runnable {
      override def run(): Unit = {
        import scala.collection.JavaConverters._
        val originContextMap = MDC.getCopyOfContextMap
        logger.debug(
          s"merge new context = $mdcContext, originContextMap = ${if (originContextMap == null) "null" else originContextMap.asScala.toMap}")
        setContextMap(
          if (originContextMap != null)
            (originContextMap.asScala.toMap ++ mdcContext).asJava
          else mdcContext.asJava
        )
        try {
          command.run()
        } finally {
          logger.debug(
            s"set origin, current context = $mdcContext, originContextMap = ${if (originContextMap == null) "null" else originContextMap.asScala.toMap}")
          setContextMap(originContextMap)
        }
      }
    })

  private[this] def setContextMap(context: java.util.Map[String, String]): Unit = {
    if (context == null) {
      MDC.clear()
    } else {
      MDC.setContextMap(context)
    }
  }

  override def reportFailure(cause: Throwable): Unit = delegate.reportFailure(cause)
}

object CustomMdcExecuteContext {

  import scala.collection.JavaConverters._

  def fromThread(delegate: ExecutionContext): ExecutionContextExecutor =
    new CustomMdcExecuteContext(MDC.getCopyOfContextMap.asScala.toMap, delegate)

  def fromThread(mergeContext: Map[String, String], delegate: ExecutionContext): ExecutionContextExecutor =
    new CustomMdcExecuteContext({
      val current = MDC.getCopyOfContextMap
      if (current == null) Map[String, String]() else current.asScala.toMap
    } ++ mergeContext, delegate)
}
