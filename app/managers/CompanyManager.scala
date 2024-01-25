package managers

import managers.errors.ManagerError
import persistence.entities.{CarDTO, CompanyDTO}
import repositories.CompanyRepository

trait CompanyManager {

  def save(company: CompanyDTO): Unit
}


class CompanyManagerLive(repository: CompanyRepository) extends CompanyManager {

  override def save(company: CompanyDTO): Unit = ()
}
