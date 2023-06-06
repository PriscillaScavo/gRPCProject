import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.google.protobuf.timestamp.Timestamp
import com.scavo.http.example.{MessageRequestExample, MessageResponseExample, ZioExample}
import io.grpc.Status
import zio.stream.ZStream
import zio.{ZEnv, ZIO}

import java.time.Instant
import java.util.UUID

class MessageExchangeServiceExampleImpl extends ZioExample.ZMessageExchangeServiceExample[ZEnv, Any]{
  private def getTimestamp: Timestamp = {
    val time = Instant.now()
    Timestamp.of(time.getEpochSecond, time.getNano)
  }

  override def sendMessage(receivingMessage: MessageRequestExample): ZIO[Any, Status, MessageResponseExample] = {
    receivingMessage match{
      case receiveMex if receiveMex.message.isEmpty => ZIO.fail(Status.INVALID_ARGUMENT)
      case _ => ZIO.succeed(MessageResponseExample(
        id = UUID.randomUUID().toString,
        responseMessage = s"Responding to ${receivingMessage.message}",
        timestamp = Some(getTimestamp),
        extraInfo = receivingMessage.extraInfo
      ))
    }
  }

  override def streamMessagesSingleResponse(receivingMessageStream: ZStream[Any, Status, MessageRequestExample]) = {
    val aggregated: ZIO[Any, Status, (List[String], List[String])] =
      receivingMessageStream.fold((List.empty[String], List.empty[String]))((result, message) => (
      result._1.appended(message.message),
      result._2.appendedAll(message.extraInfo)))

    aggregated.map(aggregated => {
      val message = aggregated._1.mkString(",")
      println(s"Message => ${message}")
      MessageResponseExample(
      id = UUID.randomUUID().toString,
      responseMessage = s"Responding to the stream ${message}",
      timestamp = Some(getTimestamp),
      extraInfo = aggregated._2
    )
    })
  }

  override def sendMessageStreamResponse(receivingMessage: MessageRequestExample): ZStream[Any, Status, MessageResponseExample] =
    ZStream(
        MessageResponseExample(
          id = UUID.randomUUID().toString,
          responseMessage = s"Stream responding to ${receivingMessage.message}",
          timestamp = Some(getTimestamp),
          extraInfo = receivingMessage.extraInfo
        )
      ).mapM(_ => ZIO.fail(Status.NOT_FOUND))


  override def streamMessages(receivingMessageStream: ZStream[Any, Status, MessageRequestExample]): ZStream[Any, Status, MessageResponseExample] =
    receivingMessageStream.map(receivingMessage =>
      MessageResponseExample(
        id = UUID.randomUUID().toString,
        responseMessage = s"Stream responding to ${receivingMessage.message}",
        timestamp = Some(getTimestamp),
        extraInfo = receivingMessage.extraInfo
      )
    )

}