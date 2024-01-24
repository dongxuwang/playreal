package components

import com.softwaremill.macwire._
import managers.{CarManager, CarManagerLive}

trait ManagerComponents extends RepositoryComponents {
  lazy val carManager: CarManager = wire[CarManagerLive]
}
