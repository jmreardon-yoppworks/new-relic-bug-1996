import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

object ConcurrentQueries {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  // A simple dictionary table with keys and values
  class Dict(tag: Tag) extends Table[(Long, String)](tag, "INT_DICT") {
    def key = column[Long]("KEY", O.PrimaryKey)

    def value = column[String]("VALUE")

    def * = (key, value)
  }

  def main(args: Array[String]): Unit = {

    val dict = TableQuery[Dict]
    val db = Database.forConfig("h2mem1")

    try {
      def testQuery(from: Long, limit: Long) =
        dict.filter(_.key > from).sortBy(_.key).map(_.value).take(limit)

      Await.result(
        db.run(
          DBIO.seq(
            dict.schema.create,
            dict ++= (1L to 100L).zip(Iterator.continually('a' to 'e').flatten.map(_.toString)),
          ),
        )
        , Duration.Inf,
      )

      println("Database populated")

      val runResults = Future.traverse(1 to 100) { x =>
        db.run(testQuery(x, 2).result).map { v => println(s"Query Result $x: " + v) }
      }

      Await.result(runResults, 10.seconds)

    } finally db.close
  }
}
