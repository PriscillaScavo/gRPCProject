import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.google.protobuf.timestamp.Timestamp

import java.time.Instant
import java.util.UUID
import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.scavo.http.{MessageExchangeServiceExample, MessageExchangeServiceExampleHandler, MessageRequestExample, MessageResponseExample}


class MessageExchangeServiceExampleImpl(implicit val mat: Materializer, actorSystem: ActorSystem)
  extends MessageExchangeServiceExample{
  implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  private def getTimestamp: Timestamp = {
    val time = Instant.now()
    Timestamp.of(time.getEpochSecond, time.getNano)
  }

  override def sendMessage(receivingMessage: MessageRequestExample):
  Future[MessageResponseExample] = {
    val response = MessageResponseExample(
      id = UUID.randomUUID().toString,
      responseMessage = s"Responding to ${receivingMessage.message}",
      timestamp = Some(getTimestamp),
      extraInfo = receivingMessage.extraInfo
    )
    Future.successful(response)
  }

  override def streamMessagesSingleResponse(receivingMessageStream: Source[MessageRequestExample, NotUsed]): Future[MessageResponseExample] =
    receivingMessageStream
      .runWith(Sink.seq)
      .map(messages =>
        MessageResponseExample(
          id = UUID.randomUUID().toString,
          responseMessage =
            s"Responding to stream ${messages.map(_.message).mkString(", ")}",
          timestamp = Some(getTimestamp),
          extraInfo = messages.flatMap(_.extraInfo)
        )
      )

  override def sendMessageStreamResponse(receivingMessage: MessageRequestExample): Source[MessageResponseExample, NotUsed] =
    Source(
      List(
        MessageResponseExample(
          id = UUID.randomUUID().toString,
          responseMessage = s"Stream responding to ${receivingMessage.message}",
          timestamp = Some(getTimestamp),
          extraInfo = receivingMessage.extraInfo
        )
      )
    )

  override def streamMessages(receivingMessageStream: Source[MessageRequestExample, NotUsed]): Source[MessageResponseExample, NotUsed] =
    receivingMessageStream.map(receivingMessage =>
      MessageResponseExample(
        id = UUID.randomUUID().toString,
        responseMessage = s"Stream responding to ${receivingMessage.message}",
        timestamp = Some(getTimestamp),
        extraInfo = receivingMessage.extraInfo
      )
    )

}