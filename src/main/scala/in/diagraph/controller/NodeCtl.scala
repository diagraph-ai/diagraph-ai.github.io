package in.diagraph.controller

import in.diagraph.models.*
import in.diagraph.objects.Cache.{nodeMap, pathMap}
import in.diagraph.objects.Constants.*
import in.diagraph.objects.*
import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, MouseEvent}

import java.time.Instant
import scala.collection.mutable.ListBuffer
import scala.math.max

trait NodeCtl {
  this: Node =>

  def render(parent: HTMLElement): Unit = {
    parent.appendChild(contentFO)
    nodeMap += (props.id -> this)
    for (elem <- nodes) {
      elem._2.parent = this
      elem._2.render(bodySvg)
    }
    for (elem <- paths) {
      pathMap += (elem._2.props.id -> elem._2)
      elem._2.startNode = nodeMap(elem._2.props.sNode)
      elem._2.tailNode = nodeMap(elem._2.props.tNode)
      elem._2.parent = this
      elem._2.render(bodySvg)
    }
    updateSize()
  }

  def updateCoordinates(): Unit = {
    contentFO.setAttribute("x", props.x.toString)
    contentFO.setAttribute("y", props.y.toString)
  }

  def move(deltaXY: XY): Unit =
    if props.x + deltaXY.x > 0 && props.y + deltaXY.y > 0 then
      // update model
      props.x = props.x + deltaXY.x
      props.y = props.y + deltaXY.y
      // update view
      updateCoordinates()
      // update paths
      oPaths().foreach(path => path.updateStart(deltaXY))
      iPaths().foreach(path => path.updateEnd(deltaXY))
      subItoPaths().foreach(path => path.updateStart(deltaXY))
      subItiPaths().foreach(path => path.updateEnd(deltaXY))
      // update size recursively
      if parent != null then
        // update peers
//        for ((_, node) <- parent.nodes if node.props.id != props.id && isOverlap(this, node))
//          node.move(deltaXY)
        parent.updateSize()

  def getMouseDownEdge(e: MouseEvent): Int =
    if isBetween(e.pageX, west(), west() + 10) then WEST
    else if isBetween(e.pageX, east() - 10, east()) then EAST
    else if isBetween(e.pageY, north(), north() + 10) then NORTH
    else if isBetween(e.pageY, south() - 10, south()) then SOUTH
    else -1

  def addNode(nodeType: String = "node"): Unit = {
    val counter1 = Instant.now().toEpochMilli
    val newNode  = Node(Props(id = NODE + "-" + counter1, format = nodeType,
      x = 10, y = titleDiv.getBoundingClientRect().height + 10))
    nodes += (newNode.props.id   -> newNode)
    nodeMap += (newNode.props.id -> newNode)
    props.showChildren = true
    newNode.render(bodySvg)
    newNode.parent = this
    updateSize()
  }

  def deleteNode(): Unit =
    if parent != null then
      parent.bodySvg.removeChild(contentFO)
      parent.nodes.remove(props.id)
      parent.updateSize()
      nodeMap -= props.id

  def minimize(): Unit = {
    updateSize()
    oPaths().foreach(path => path.updateStart(this))
    iPaths().foreach(path => path.updateEnd(this))
    subItoPaths().foreach(path => path.updateStart(this))
    subItiPaths().foreach(path => path.updateEnd(this))
  }

  def maximize(): Unit = {
    // Get East and South peers
//    var eastPeers: List[Node] = List()
//    var southPeers: List[Node] = List()
//    if parent != null then
//      eastPeers = parent.nodes.values.filter(node => node.props.id != props.id && isEast(this, node)).toList
//      southPeers = parent.nodes.values.filter(node => node.props.id != props.id && isSouth(this, node)).toList
//    val initialWidth = width()
//    val initialHeight = height()
    props.showChildren = true
    updateSize()
    oPaths().foreach(path => path.updateStart(this))
    iPaths().foreach(path => path.updateEnd(this))
    subItoPaths().foreach(path => path.updateStart(getHalt(path.startNode)))
    subItiPaths().foreach(path => path.updateEnd(getHalt(path.tailNode)))
    if parent != null then
      for ((_, node) <- parent.nodes if node.props.id != props.id && isOverlap(this, node))
        node.updateSize()

    // Update East and South peers
//    val newWidth = width()
//    val newHeight = height()
//    val deltaWidth = newWidth - initialWidth
//    val deltaHeight = newHeight - initialHeight
//
//    for (node <- eastPeers)
//      if isOverlap(this, node) then node.move(XY(deltaWidth, 0))
//    for (node <- southPeers) {
//      if isOverlap(this, node) then node.move(XY(0, deltaHeight))
//    }
  }

  def updateSize(): Unit = {
    titleDiv.style.width = "max-content"
    val width1 = max(width(), titleDiv.getBoundingClientRect().width)
    titleDiv.style.width = width1.px
    bodySvg.setAttribute("width", width1.px)
    bodySvg.setAttribute("height", height().px)
    oPaths().foreach(path => path.updateStart(this))
    iPaths().foreach(path => path.updateEnd(this))
    if parent != null then parent.updateSize()
  }

  def updateImageSize(): Unit = {
    bodySvg.querySelector("img").asInstanceOf[HTMLElement].setAttribute("width", width().toString)
    bodySvg.querySelector("img").asInstanceOf[HTMLElement].setAttribute("height", height().toString)
  }

  def isSame(node: Node): Boolean =
    props.id == node.props.id

  def isDescendantOf(node: Node): Boolean =
    parent match {
      case null                                       => false
      case parent if parent.props.id == node.props.id => true
      case parent                                     => parent.isDescendantOf(node)
    }

  def oPaths(): ListBuffer[Path] =
    props.oPaths.flatMap(pathMap.get).to(ListBuffer)

  def iPaths(): ListBuffer[Path] =
    props.iPaths.flatMap(pathMap.get).to(ListBuffer)

  def subItoPaths(list: ListBuffer[Path] = ListBuffer()): ListBuffer[Path] = {
    nodes.values.foreach { node =>
      node.oPaths().foreach { path =>
        if !path.tailNode.isDescendantOf(this) then list.addOne(path)
      }
      node.subItoPaths().foreach(path => if !path.tailNode.isDescendantOf(this) then list.addOne(path))
    }
    list
  }

  def subItiPaths(list: ListBuffer[Path] = ListBuffer()): ListBuffer[Path] = {
    nodes.values.foreach { node =>
      node.iPaths().foreach { path =>
        if !path.startNode.isDescendantOf(this) then list.addOne(path)
      }
      node.subItiPaths().foreach(path => if !path.startNode.isDescendantOf(this) then list.addOne(path))
    }
    list
  }

  def getHaltNodes(node: Node, listBuffer: ListBuffer[Node] = ListBuffer()): ListBuffer[Node] = {
    if node.parent != this then getHaltNodes(node.parent, listBuffer)
    listBuffer.addOne(node)
    listBuffer
  }

  def getHalt(node: Node): Node = {
    val haltNodes: ListBuffer[Node] = getHaltNodes(node)
    haltNodes.find(node => !node.props.showChildren).getOrElse(node)
  }
}
