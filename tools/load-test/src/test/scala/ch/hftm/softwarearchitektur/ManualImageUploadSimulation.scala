package ch.hftm.softwarearchitektur

import java.util.Optional

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ManualImageUploadSimulation extends Simulation {

  val targetHost = Optional.ofNullable(System.getProperty("target.host")).orElse("localhost")
  val targetPort = Optional.ofNullable(System.getProperty("target.port")).orElse("8080")

  val userCount = Optional.ofNullable(System.getProperty("test.total-users")).orElse("100").toInt
  val timeFrame = Optional.ofNullable(System.getProperty("test.time-frame")).orElse("1").toInt

  val httpProtocol = http
    .baseUrl(s"http://${targetHost}:8080")
    .inferHtmlResources()
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("de,en-US;q=0.7,en;q=0.3")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:78.0) Gecko/20100101 Firefox/78.0")


  val headersImageIncorrectMaskedNose = Map(
    "Content-Type" -> "multipart/form-data; boundary=---------------------------29391054502195736439763004546",
    "Origin" -> "http://localhost:8080",
    "Upgrade-Insecure-Requests" -> "1")

  val headersImageCorrectMasked = Map(
    "Content-Type" -> "multipart/form-data; boundary=---------------------------365817642414213404661072166355",
    "Origin" -> "http://localhost:8080",
    "Upgrade-Insecure-Requests" -> "1")

  val headersImageIncorrectMaskedMouthAndNose = Map(
    "Content-Type" -> "multipart/form-data; boundary=---------------------------4890360502506214611571930368",
    "Origin" -> "http://localhost:8080",
    "Upgrade-Insecure-Requests" -> "1")

  val scn = scenario("RecordedSimulation")
    .exec(
      http("initial request to /")
        .get("/")
        .resources(http("fetch materialize.min.css")
          .get("/css/materialize.min.css"),
          http("fetch materialize.min.js")
            .get("/js/materialize.min.js"),
          http("redirect to /manual-upload.html")
            .get("/manual-upload.html")
        )
    )
    .pause(3)
    .repeat(10) {
      exec(
        http("Upload incorrect masked person (nose)")
          .post("/api/image-upload")
          .headers(headersImageIncorrectMaskedNose)
          .body(RawFileBody("ch/hftm/softwarearchitektur/manualimageuploadsimulation/imageIncorrectMaskedNose.dat")))
        .pause(3)
        .exec(http("Upload correct masked picture")
          .post("/api/image-upload")
          .headers(headersImageCorrectMasked)
          .body(RawFileBody("ch/hftm/softwarearchitektur/manualimageuploadsimulation/imageCorrectMasked.dat")))
        .pause(3)
        .exec(http("Upload incorrect masked person (mouth and nose)")
          .post("/api/image-upload")
          .headers(headersImageIncorrectMaskedMouthAndNose)
          .body(RawFileBody("ch/hftm/softwarearchitektur/manualimageuploadsimulation/imageIncorrectMaskedMouthAndNose.dat")))

    }

  setUp(scn.inject(
    // atOnceUsers(1)
    rampUsers(userCount) during (timeFrame minutes)
  ))
    .assertions(
      global.responseTime.max.lt(500 ),
      global.successfulRequests.percent.gt(99)
    )
    .protocols(httpProtocol)
}