package github.dboroujerdi.sched.app

import github.dboroujerdi.sched.api.model.ScheduledEvent
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import net.ruippeixotog.scalascraper.model.Element

package object parse {

  type EventElement = Element
  type TeamsNames = (String, String)

  type RowParser = Element => ErrorOrEvent

  type FieldExtractor = Element => Option[Fields]
  type Fields = (String, String, String, String)
  type FieldParser = Fields => Option[ScheduledEvent]
}
