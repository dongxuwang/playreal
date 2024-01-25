package components

import com.softwaremill.macwire._
import managers.{CarManager, CarManagerLive, ComputerManager, ComputerManagerLive}

trait ManagerComponents extends RepositoryComponents {
  lazy val carManager: CarManager = wire[CarManagerLive]
  lazy val computerManager: ComputerManager = wire[ComputerManagerLive]
}
