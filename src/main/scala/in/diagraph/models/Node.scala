package in.diagraph.models

import in.diagraph.controller.NodeCtl
import in.diagraph.views.NodeView
import in.diagraph.objects.Cache.{mNode, nodeMap}
import in.diagraph.objects.Constants.*
import upickle.legacy.ReadWriter

import scala.collection.mutable.{ListBuffer, Map}

case class Node(props: Props = Props(), var nodes: Map[String, Node] = Map(), var paths: Map[String, Path] = Map())
    extends NodeCtl,
      NodeView derives ReadWriter {

  // Properties
  var parent: Node     = null
  var mouseOn: Boolean = false

  override def toString: String =
    "NodeId : " + props.id

  // Behavior

  def width(): Double = {
    var maxWidth =
      if props.format.equals(TEXT) || props.format.equals(IMAGE) then bodySvg.getBoundingClientRect().width
      else titleDiv.getBoundingClientRect().width
    if props.showChildren then
      for (node <- nodes)
        if node._2.props.x + node._2.width() > maxWidth then maxWidth = node._2.props.x + node._2.width()
      maxWidth = maxWidth + 20
    maxWidth
  }

  def height(): Double = {
    var maxHeight =
      if props.format.equals(TEXT) || props.format.equals(IMAGE) then bodySvg.getBoundingClientRect().height
      else titleDiv.getBoundingClientRect().height
    if props.showChildren then
      for (node <- nodes)
        if node._2.props.y + node._2.height() > maxHeight then maxHeight = node._2.props.y + node._2.height()
      maxHeight = maxHeight + 20
    maxHeight
  }

  def west(): Double =
    props.x + (if parent != null then parent.west() else 0)

  def east(): Double =
    west() + width()

  def north(): Double =
    props.y + (if parent != null then parent.north() else 0)

  def south(): Double =
    north() + height()
}

object Node {
  def lca(sNode: Node, tNode: Node): Node = {
    val sParents = getParents(sNode)
    val tParents = getParents(tNode)
    sParents
      .find(tParents.contains)
      .flatMap(nodeMap.get)
      .getOrElse(mNode)
  }

  @scala.annotation.tailrec
  def getParents(sNode: Node, listBuffer: ListBuffer[String] = ListBuffer()): ListBuffer[String] =
    sNode.parent match {
      case null   => listBuffer
      case parent =>
        listBuffer.addOne(parent.props.id)
        getParents(parent, listBuffer)
    }
}
