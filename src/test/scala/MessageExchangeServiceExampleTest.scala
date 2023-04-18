import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.testkit.TestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AsyncWordSpecLike

class MessageExchangeServiceExampleTest
  extends TestKit(ActorSystem("Akka-gRPC"))
  with AsyncWordSpecLike
  with BeforeAndAfterAll {

    override def beforeAll(): Unit =
      new MessageExchangeServiceExampleImpl().startServer

    override def afterAll(): Unit =
      TestKit.shutdownActorSystem(system)

    "messageExchangeService" should {
      "send single request and receive single response" in {
        val messageExchangeClient = new MessageExchangeExampleClient()

        val msg = messageExchangeClient.sendSingleMessage("single message")
          msg.map(responseMessage => {
            assert(responseMessage.responseMessage == s"Responding to single message")
          })
      }
    }
}
