import com.scavo.http.example.ZioExample.MessageExchangeServiceExampleClient
import com.scavo.http.example.{MessageRequestExample, MessageResponseExample}
import io.grpc.Status
import zio.stream.ZStream
import zio.{Has, ZIO}
import java.util.UUID

class MessageExchangeExampleClient{
  private val client = MessageExchangeServiceExampleClient

  def sendSingleMessage(message: String): ZIO[Has[MessageExchangeServiceExampleClient.ZService[Any, Any]] with Any, Status, MessageResponseExample] =
    client.sendMessage(
      MessageRequestExample(
        id = UUID.randomUUID().toString,
        message = message,
        timestamp = None,
        extraInfo = Seq.empty
      )
    )

  def sendSingleMessageStreamResponse(message: String): ZStream[Has[MessageExchangeServiceExampleClient.ZService[Any, Any]] with Any, Status, MessageResponseExample] =
    client.sendMessageStreamResponse(
      MessageRequestExample(
        id = UUID.randomUUID().toString,
        message = message,
        timestamp = None,
        extraInfo = Seq.empty
      )
    )

  def streamMessagesSingleResponse(
                                    messages: ZStream[Any, Status, String]
                                  ): ZIO[Has[MessageExchangeServiceExampleClient.ZService[Any, Any]] with Any, Status, MessageResponseExample] =
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
                      messages: ZStream[Any, Status, String]
                    ): ZStream[Has[MessageExchangeServiceExampleClient.ZService[Any, Any]] with Any, Status, MessageResponseExample] =
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
