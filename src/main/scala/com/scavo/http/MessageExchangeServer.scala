import scalapb.zio_grpc.{ServerMain, ServiceList}
import zio.ZIO

object MessageExchangeServer extends ServerMain {
  override def port: Int = 8980

  val createMessageExchangeService = ZIO.succeed(new MessageExchangeServiceExampleImpl())

  def services: ServiceList[zio.ZEnv] =
    ServiceList.addM(createMessageExchangeService)
}