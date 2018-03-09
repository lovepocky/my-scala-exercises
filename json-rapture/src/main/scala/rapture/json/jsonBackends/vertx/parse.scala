/*
  Rapture, version 2.0.0. Copyright 2010-2016 Jon Pretty, Propensive Ltd.

  The primary distribution site is

    http://rapture.io/

  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
  compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software distributed under the License is
  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and limitations under the License.
 */

package rapture.json.jsonBackends.vertx

import rapture.core._
import rapture.json._
import rapture.data._

//import com.fasterxml.jackson.databind._
import io.vertx.scala.core.parsetools.JsonParser
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.{Json}

private[vertx] object VertxJsonParser extends Parser[String, JsonAst] {

  val ast = VertxJsonAst

  override def toString = "<VertxJsonParser>"
  val parser            = JsonParser.newParser()

  /*private val mapper = new ObjectMapper()
    .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
    .enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)*/

  def parse(s: String): Option[Any] = {
    import io.vertx.core.parsetools.JsonEventType._

    parser.handle(Buffer.buffer(s))
    parser.end()
    parser.handler(event => {
      event.`type`() match {
//        case START_OBJECT =>
        case VALUE =>
          event.value()
      }
    })
    ???
  }
}
