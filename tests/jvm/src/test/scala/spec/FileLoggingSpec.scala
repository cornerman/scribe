package spec

import java.nio.file.{Files, Path, Paths}
import scribe.formatter.Formatter
import scribe.writer.FileWriter
import scribe.{LogHandler, Logger}
import org.scalatest.{Matchers, WordSpec}

import scala.io.Source

class FileLoggingSpec extends WordSpec with Matchers {
  lazy val fileLogger: Logger = Logger(parentName = None)
  lazy val logFile: Path = Paths.get("logs/test.log")
  lazy val writer: FileWriter = FileWriter.flat("test")

  "File Logging" should {
    "configure logging to a temporary file" in {
      if (Files.exists(logFile)) {
        Files.delete(logFile)
      }
      fileLogger.addHandler(LogHandler(formatter = Formatter.simple, writer = writer))
    }
    "log to the file" in {
      fileLogger.info("Testing File Logger")
    }
    "verify the file was logged to" in {
      Files.exists(logFile) should be(true)
      val source = Source.fromFile(logFile.toFile)
      try {
        source.mkString.trim should equal("Testing File Logger")
      } finally {
        source.close()
        Files.delete(logFile)
      }
    }
    "close and release the file handle" in {
      writer.close()
    }
  }
}