package github.dboroujerdi.sched.app.parse.html

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

object TestData {
  lazy val browser: JsoupBrowser = new JsoupBrowser()
  lazy val exampleSchedule = browser.parseString(
    """<html>
      <head></head>
      <body>
      <table class="tableData">
        <tr>
          <td>Time</td>
          <td>Name</td>
          <td>Sport</td>
        </tr>
        <tr>
          <td><span>Event Started!</span></td>
          <td><a href="http://schedule.com/match/1/2/3/123456/.html">Man City v West Ham</a></td>
          <td><a>Football</a></td>
        </tr>
        <tr>
          <td><span>Starts in: 16:35</span></td>
          <td><a href="http://schedule.com/match/1/2/3/123456/.html">Man City v West Ham</a></td>
          <td><a>Football</a></td>
        </tr>
        <tr>
          <td><span><a id="score" class="Score">Live At</a><span>23:30 UK</span> </span></td>
          <td><a href="http://schedule.com/match/1/2/3/10010467/.html">Arsenal v Man Utd</a></td>
          <td><a>Football</a></td>
        </tr>
        <tr>
          <td><span><a id="score" class="Score">Live At</a><span>23:30 UK</span></span></td>
          <td><a href="http://schedule.com/match/1/2/3/10010467/.html">Bulls v Eagles</a></td>
          <td><a>Basketball</a></td>
        </tr>
      </table>
      <table class="tableData">
        <tr>
          <td>Time</td>
          <td>Name</td>
          <td>Sport</td>
        </tr>
        <tr>
          <td><span><a id="score" class="Score">23 Oct</a><span>15:00 UK</span></span></td>
          <td><a href="http://schedule.com/match/1/2/3/10010467/.html">Watford v Chelsea</a></td>
          <td><a>Football</a></td>
        </tr>
        <tr>
          <td><a href="http://schedule.com/match/1/2/.html">Ilford v Barnet</a></td>
          <td><a>Football</a></td>
        </tr>
      </table>
      </body>
      </html>""")
}
