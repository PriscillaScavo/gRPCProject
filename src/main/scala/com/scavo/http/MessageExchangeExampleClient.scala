import akka.NotUsed
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source
import com.scavo.http.{MessageExchangeServiceExample, MessageExchangeServiceExampleClient, MessageRequestExample, MessageResponseExample}

import java.util.UUID
import scala.concurrent.{ExecutionContextExecutor, Future}

class MessageExchangeExampleClient(implicit actorSystem: ActorSystem){
  implicit val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher

  private val clientSettings: GrpcClientSettings =
    GrpcClientSettings.fromConfig(MessageExchangeServiceExample.name)

  private val client = MessageExchangeServiceExampleClient(clientSettings)

  def sendSingleMessage(message: String): Future[MessageResponseExample] =
    client.sendMessage(
      MessageRequestExample(
        id = UUID.randomUUID().toString,
        message = message,
        timestamp = None,
        extraInfo = Seq.empty
      )
    )

  def sendSingleMessageStreamResponse(message: String): Source[MessageResponseExample, NotUsed] =
    client.sendMessageStreamResponse(
      MessageRequestExample(
        id = UUID.randomUUID().toString,
        message = message,
        timestamp = None,
        extraInfo = Seq.empty
      )
    )

  def streamMessagesSingleResponse(
                                    messages: Source[String, NotUsed]
                                  ): Future[MessageResponseExample] =
    client.streamMessagesSingleResponse(
      messages.map(m =>
        MessageRequestExample(
          id = UUID.randomUUID().toString,
          message = m,
          timestamp = None,
          extraInfo = Seq.empty
        )
      )
    )

  def streamMessages(
                      messages: Source[String, NotUsed]
                    ): Source[MessageResponseExample, NotUsed] =
    client.streamMessages(
      messages.map(m =>
        MessageRequestExample(
          id = UUID.randomUUID().toString,
          message = m,
          timestamp = None,
          extraInfo = Seq.empty
        )
      )
    )
}
