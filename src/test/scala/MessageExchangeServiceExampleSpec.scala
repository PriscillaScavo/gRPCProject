import com.scavo.http.example.ZioExample.MessageExchangeServiceExampleClient
import io.grpc.Status
import scalapb.zio_grpc.{Server, ZManagedChannel}
import zio.{Has, ZIO, ZLayer, ZManaged}
import zio.test.Assertion.{equalTo, isNonEmptyString}
import zio.test.{DefaultRunnableSpec, TestFailure, ZSpec, assertM, assertTrue}
import io.grpc.ManagedChannelBuilder
import zio.stream.ZStream

class MessageExchangeServiceExampleSpec extends DefaultRunnableSpec {

  val clientLayer: ZLayer[Any, TestFailure[Throwable], Has[MessageExchangeServiceExampleClient.ZService[Any, Any]]] = {
    val layer = MessageExchangeServiceExampleClient.live(
      ZManagedChannel(
        ManagedChannelBuilder.forAddress("localhost", 8980)
      )
    )
   layer.mapError(TestFailure.fail(_))

  }

  def spec: ZSpec[_root_.zio.test.environment.TestEnvironment, Any] =
    suite("messageExchangeServiceExample")(
      testM("send single request and receive single response" ) {
        val messageExchangeExampleClient = new MessageExchangeExampleClient()
        val responseMessage: ZIO[Has[MessageExchangeServiceExampleClient.ZService[Any, Any]] with Any, Status, String] =
          messageExchangeExampleClient.sendSingleMessage("single message")
            .map(responseMessage => responseMessage.responseMessage)

        assertM(responseMessage)(equalTo("Responding to single message"))

      },
      testM("send single request and receive stream response") {
        val messageExchangeClient = new MessageExchangeExampleClient()
        val responseStream = messageExchangeClient.sendSingleMessageStreamResponse("single message")
        val size = responseStream.runCollect.map(_.length)
        val head = responseStream.take(1).runHead
        val firstElementTimestamp = head.map(_.get.timestamp.toString)
        head.map(msgResponse => assertTrue(msgResponse.get.responseMessage == "Stream responding to single message"))

        assertM(size)(equalTo(1))
        assertM(firstElementTimestamp)(isNonEmptyString)
      },
      testM("stream requests and receive single response"){
        val messageExchangeClient = new MessageExchangeExampleClient()
        val stream: ZStream[Any, Status, String] = ZStream("message 1", "message 2", "message 3")

        val response = messageExchangeClient.streamMessagesSingleResponse(stream)
        val timestampResponse = response.map(_.timestamp.toString)
        val responseMsg = response.map(_.responseMessage)
        assertM(timestampResponse)(isNonEmptyString)
        assertM(responseMsg)(equalTo("Responding to stream message 1, message 2, message 3"))
      },
      testM("stream requests and stream responses"){
        val messageExchangeClient = new MessageExchangeExampleClient()
        val stream: ZStream[Any, Status, String] = ZStream("message 1", "message 2", "message 3")
        val responseStream = messageExchangeClient.streamMessages(stream)
        val size = responseStream.runCollect.map(_.length)
        val msg1 = responseStream.take(1).runHead.map(_.get.responseMessage)
        val msg2 = responseStream.take(2).runHead.map(_.get.responseMessage)
        val msg3 = responseStream.take(3).runHead.map(_.get.responseMessage)

        assertM(msg1)(equalTo("Stream responding to message 1"))
        assertM(msg2)(equalTo("Stream responding to message 2"))
        assertM(msg3)(equalTo("Stream responding to message 3"))
        assertM(size)(equalTo(3))
      }
    ).provideLayer(clientLayer)
}
