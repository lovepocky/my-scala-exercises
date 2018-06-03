package cn.lovepocky.exercise.flink

import com.typesafe.scalalogging.LazyLogging
import org.apache.flink.streaming.api.scala._
import ammonite.ops.Extensions._

object SocketStreamCount extends App with LazyLogging {

  if (args.length != 2) {
    System.err.println("USAGE:\nSocketTextStreamWordCount <hostname> <port>")
    sys.exit(0)
  }

  val hostName = args(0)
  val port     = args(1).toInt

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val text = env.socketTextStream(hostName, port)
  val counts = text
    .flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
    .map { (_, 1) }
    .keyBy(0)
    .sum(1)

  counts print

  env.execute()

}
