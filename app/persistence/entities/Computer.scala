package persistence.entities

import anorm.{Macro, ToParameterList}
import play.api.libs.json.{Format, Json}

import java.util.Date


case class Computer(id: Option[Long] = None,
                    name: String,
                    introduced: Option[Date],
                    discontinued: Option[Date],
                    companyId: Option[Long])
object Computer {
  implicit def toParameters: ToParameterList[Computer] =
    Macro.toParameters[Computer]
  def unapply(c: Computer): Option[(Option[Long], String, Option[Date], Option[Date], Option[Long])] = Some((c.id, c.name, c.introduced, c.discontinued, c.companyId))
}

case class ComputerDTO(id: Option[Long] = None,
                       name: String,
                       introduced: Option[Date],
                       discontinued: Option[Date],
                       companyId: Option[Long]) {

  def toComputer: Computer = Computer(id, name, introduced, discontinued, companyId)
}

object ComputerDTO {
  implicit val format: Format[ComputerDTO] = Json.format[ComputerDTO]
}
