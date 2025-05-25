package in.diagraph.controller

import in.diagraph.models.{Node, Path, XY}
import in.diagraph.objects.Cache.*
import in.diagraph.objects.Constants.*
import in.diagraph.views.PathView
import org.scalajs.dom.MouseEvent

trait PathCtl extends PathView {
  this: Path =>

  def updateStart(deltaXY: XY) = {
    // update model
    props.x1 += deltaXY.x
    props.y1 += deltaXY.y
    // update view
    updatePath()
  }

  def updateEnd(deltaXY: XY) = {
    // update model
    props.x2 += deltaXY.x
    props.y2 += deltaXY.y
    // update view
    updatePath()
  }

  def updateStart(node: Node) = {
    props.sEdge match
      case EAST  =>
        props.x1 = node.east() - parent.west()
        props.y1 = node.north() + (node.height() / 100 * props.sRad) - parent.north()
      case SOUTH =>
        props.x1 = node.west() + (node.width() / 100 * props.sRad) - parent.west()
        props.y1 = node.south() - parent.north()
      case WEST  =>
        props.x1 = node.west() - parent.west()
        props.y1 = node.north() + (node.height() / 100 * props.sRad) - parent.north()
      case NORTH =>
        props.x1 = node.west() + (node.width() / 100 * props.sRad) - parent.west()
        props.y1 = node.north() - parent.north()
      case _     =>
    updatePath()
  }

  def updateEnd(node: Node) = {
    props.tEdge match
      case EAST  =>
        props.x2 = node.east() - parent.west()
        props.y2 = node.north() + (node.height() / 100 * props.sRad) - parent.north()
      case SOUTH =>
        props.x2 = node.west() + (node.width() / 100 * props.sRad) - parent.west()
        props.y2 = node.south() - parent.north()
      case WEST  =>
        props.x2 = node.west() - parent.west()
        props.y2 = node.north() + (node.height() / 100 * props.sRad) - parent.north()
      case NORTH =>
        props.x2 = node.west() + (node.width() / 100 * props.sRad) - parent.west()
        props.y2 = node.north() - parent.north()
      case _     =>
    updatePath()
  }

  def updateEndWest(node: Node) = {
    props.x2 = node.props.x
    props.y2 = node.props.y + (node.height() / 100 * props.tRad)
    updatePath()
  }

  def updateStartToNodeEast(node: Node, parent: Node) = {
    props.x1 = node.east() - parent.west()
    props.y1 = node.north() - parent.north()
    updatePath()
  }

  def updatePath(): Unit = {
    updateCoordinates()
    if props.sEdge == NORTH || props.sEdge == SOUTH then
      val cx1 = props.x1
      val cy1 = props.y1 + (props.y2 - props.y1) / 2
      val cx2 = props.x2
      val cy2 = props.y1 + (props.y2 - props.y1) / 2
      props.d = "M " + props.x1 + " " + props.y1
        + " C " + cx1 + " " + cy1 + " " + cx2 + " " + cy2 + " " + props.x2 + " " + props.y2
      svgPath.setAttribute("d", props.d)
    else
      val cx1 = props.x1 + (props.x2 - props.x1) / 2
      val cy1 = props.y1
      val cx2 = props.x1 + (props.x2 - props.x1) / 2
      val cy2 = props.y2
      props.d = "M " + props.x1 + " " + props.y1
        + " C " + cx1 + " " + cy1 + " " + cx2 + " " + cy2 + " " + props.x2 + " " + props.y2
      svgPath.setAttribute("d", props.d)
  }

  def endPath(e: MouseEvent, node: Node): Unit = {
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
    props.x1 = props.x1 + mNode.west() - parent.west()
    props.y1 = props.y1 + mNode.north() - parent.north()
    props.x2 = x - parent.west()
    props.y2 = y - parent.north()
    tailNode = node
    tailNode.props.iPaths.addOne(props.id)
    props.tEdge = edge
    props.tNode = node.props.id
    props.tRad = rad
  }

  def makeBiDirectional(): Unit = {
    props.biDirectional = true
    svgPath.setAttribute("marker-start", "url(#arrow)")
  }

  def deletePath(): Unit = {
    if startNode != null then startNode.props.oPaths -= props.id
    if tailNode != null then tailNode.props.iPaths -= props.id
    pathMap -= props.id
    if parent != null then
      parent.paths -= props.id
      parent.bodySvg.removeChild(svgPath)
  }
}
