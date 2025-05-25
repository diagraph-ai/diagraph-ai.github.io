package in.diagraph.models

import in.diagraph.controller.PathCtl
import in.diagraph.objects.Cache.{mNode, pathMap}
import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import upickle.legacy.ReadWriter

import java.time.Instant

case class Path(props: Props) extends PathCtl derives ReadWriter {

  var parent: Node    = null
  var startNode: Node = null
  var tailNode: Node  = null

}

object Path {
  def addPath(e: MouseEvent, node: Node): Path = {
    var x: Double   = 0.0;
    var y: Double   = 0.0;
    var rad: Double = 0.0
    val edge        = node.getMouseDownEdge(e)
    edge match {
      case EAST  =>
        x = node.east()
        rad = ((e.pageY - node.north()) / node.height()) * 100
        y = ((node.height() / 100) * rad) + node.north()
      case SOUTH =>
        y = node.south()
        rad = ((e.pageX - node.west()) / node.width()) * 100
        x = ((node.width() / 100) * rad) + node.west()
      case WEST  =>
        x = node.west()
        rad = ((e.pageY - node.north()) / node.height()) * 100
        y = ((node.height() / 100) * rad) + node.north()
      case NORTH =>
        y = node.north()
        rad = ((e.pageX - node.west()) / node.width()) * 100
        x = ((node.width() / 100) * rad) + node.west()
      case _     =>
    }
    val time        = Instant.now().getEpochSecond()
    val path        = new Path(
      Props(
        id = PATH + "-" + time,
        x1 = x - mNode.west(),
        y1 = y - mNode.north(),
        x2 = x - mNode.west(),
        y2 = y - mNode.north()
      )
    )
    path.startNode = node
    path.props.sEdge = edge
    path.props.sRad = rad
    path.props.sNode = node.props.id
    pathMap += (path.props.id -> path)
    node.props.oPaths.addOne(path.props.id)
    path
  }

}
