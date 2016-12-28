package github.dboroujerdi.sched.app.parse.html

import enumeratum._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

sealed abstract class Month(val shortName: String) extends EnumEntry

object Month extends Enum[Month] {

  // @formatter:off
  object January extends Month("Jan")
  object February extends Month("Feb")
  object March extends Month("Mar")
  object April extends Month("Apr")
  object May extends Month("May")
  object June extends Month("Jun")
  object July extends Month("Jul")
  object August extends Month("Aug")
  object September extends Month("Sep")
  object October extends Month("Oct")
  object November extends Month("Nov")
  object December extends Month("Dec")
  // @formatter:on

  val values = findValues

  private val map = values.foldLeft(Map[String, Int]()) {
    case (m, month) => m + (month.shortName -> indexOf(month))
  }

  def idxFromShortName(shortName: String): Option[Int] = map.get(shortName).map(_ + 1)
}

trait TimeParser {
  def parseTime(rawTime: String, currentDate: DateTime): Option[DateTime]
}

trait RawTimeParser extends TimeParser {

  private val datePattern = "[0-9]*\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)".r
  private val timePattern = "[0-9]*:[0-9]*".r

  private val fmt = DateTimeFormat.forPattern("HH:mm")

  def parseTime(rawTime: String, currentDate: DateTime): Option[DateTime] = rawTime.trim match {
    case "Event Started!" =>
      None

    case timeField if timeField.startsWith("Live At") =>
      for {
        timeStr <- timePattern.findFirstIn(timeField)
        matchTime <- updateWithMatchTime(timeStr)
      } yield updateWithTheCurrentYear(matchTime, currentDate)

    case timeField =>
      for {
        timeStr <- timePattern.findFirstIn(timeField)
        matchTime <- updateWithMatchTime(timeStr)
        (day, month) <- parseDayOfYear(timeField)
        matchDate <- Option(matchTime.withMonthOfYear(month).withDayOfMonth(day).withYear(currentDate.getYear))
      } yield checkAndHandleMatchesInNewYear(matchDate, currentDate)
  }

  private def updateWithTheCurrentYear(matchTime: DateTime, currentDate: DateTime) = {
    matchTime
      .withYear(currentDate.getYear)
      .withMonthOfYear(currentDate.getMonthOfYear)
      .withDayOfMonth(currentDate.getDayOfMonth)
      .withYear(currentDate.getYear)
  }

  private def updateWithMatchTime(timeStr: String) = {
    Option(fmt.parseDateTime(timeStr))
  }

  private def checkAndHandleMatchesInNewYear(matchDate: DateTime, currentDate: DateTime): DateTime = {
    if (matchDate.isBefore(currentDate))
      matchDate.withYear(currentDate.getYear + 1)
    else
      matchDate
  }

  private def parseDayOfYear(rawTimeStr: String): Option[(Int, Int)] = {
    datePattern.findFirstIn(rawTimeStr)
      .map(_.split(" "))
      .map(l => (l(0), l(1)))
      .map {
        case (dayStr, monthStr) => (dayStr.toInt, Month.idxFromShortName(monthStr))
      }
      .collect {
        case (day, Some(month)) => (day, month)
      }
  }
}

object RawTimeParser extends RawTimeParser
