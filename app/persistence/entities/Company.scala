package persistence.entities

import play.api.libs.json.{Format, Json}

case class Company(id: Option[Long], name: String)

object Company {
  def fromDTO(companyDTO: CompanyDTO): Company = Company(companyDTO.id, companyDTO.name)
}

case class CompanyDTO(id: Option[Long] = None, name: String)

object CompanyDTO {
  implicit val format: Format[CompanyDTO] = Json.format[CompanyDTO]

  def fromCompany(company: Company): CompanyDTO = CompanyDTO(company.id, company.name)
}
