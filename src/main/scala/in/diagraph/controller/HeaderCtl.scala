package in.diagraph.controller

import in.diagraph.models.{Graph, Node, Props}
import in.diagraph.objects.Cache.{mNode, mainSVG, nodeMap, pathMap}
import in.diagraph.objects.Constants.NODE
import in.diagraph.views.Header.graphSelectElement
import org.scalajs.dom
import org.scalajs.dom.{Blob, BlobPropertyBag, FileReader, HTMLElement, URL, document, html, window}
import sttp.client4.fetch.FetchBackend
import sttp.client4.{UriContext, quickRequest}
import upickle.legacy.{read, write}

import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

trait HeaderCtl {

  def loadJsonFromLocal(): Unit = {
    val input = document.createElement("input").asInstanceOf[html.Input]
    input.`type` = "file"
    input.accept = ".json"
    input.onchange = { (_: dom.Event) =>
      val file = input.files(0)
      val reader = new FileReader()
      reader.onload = { (_: dom.Event) =>
        val jsonString = reader.result.asInstanceOf[String]
        mNode = read[Node](jsonString)
        nodeMap = Map()
        pathMap = Map()
        mNode.render(mainSVG)
      }
      reader.readAsText(file)
    }
    input.click() // Open file dialog
  }

  def loadGraph(graphName: String): Unit = {
    removeOldGraph()
    if graphName == "default" then
      mNode = Node(Props(id = NODE + "-" + Instant.now().toEpochMilli, format = NODE, x = 100, y = 100))
      nodeMap = Map()
      mNode.render(mainSVG)
      return
//    val backend  = FetchBackend()
//    val response = quickRequest
//      .get(uri"http://localhost:9000/getGraph/$graphName")
//      // .get(uri"https://www.diagraph.in/maps-get/$graphName")
//      .send(backend)
//    response.onComplete { resp =>
//      if resp.get.body.nonEmpty then mNode = read[Node](resp.get.body)
//      else mNode = Node(Props(id = NODE + "-" + Instant.now().toEpochMilli, format = NODE, x = 100, y = 100))
//      nodeMap = Map()
//      mNode.render(mainSVG)
//    }
  }

  def removeOldGraph(): Unit = {
    val children      = mainSVG.childNodes
    val nodesToRemove = for {
      i    <- 0 until children.length
      child = children(i)
      if !child.nodeName.equalsIgnoreCase("defs") &&
        !child.asInstanceOf[dom.Element].id.equalsIgnoreCase("dotPatternRect")
    } yield child
    nodesToRemove.foreach(mainSVG.removeChild)
  }

  def saveGraph(): Unit = {
    val graphName = window.prompt("Graph Name : ", graphSelectElement.value)
    val jGraph    = Graph(graphName + ".json", mNode)
    val backend   = FetchBackend()
    val response  = quickRequest
      .post(uri"http://localhost:9000/saveGraph")
      // .post(uri"https://www.diagraph.in/saveGraph")
      .header("Content-Type", "application/json")
      .body(write(jGraph))
      .send(backend)
    response.onComplete { resp =>
      if resp.get.body.nonEmpty then
        val msg = resp.get.body
        window.alert(msg)
      else window.alert("Graph saved successfully")
    }
  }

  def saveGraphToLocal(): Unit = {
    val graphName = window.prompt("Graph Name: ", "default")
    if (graphName == null || graphName.trim.isEmpty) return
    //val jGraph = Graph(graphName + ".json", mNode)
    val json = write(mNode)
    val blob = new Blob(js.Array(json), BlobPropertyBag("application/json"))
    val url = URL.createObjectURL(blob)
    val a = dom.document.createElement("a").asInstanceOf[dom.html.Anchor]
    a.href = url
    a.download = graphName + ".json"
    dom.document.body.appendChild(a)
    a.click()
    dom.document.body.removeChild(a)
    URL.revokeObjectURL(url)
  }
}
