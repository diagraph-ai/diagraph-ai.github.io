package in.diagraph.controller

import in.diagraph.models.Node.lca
import in.diagraph.models.{Node, Path, XY}
import in.diagraph.objects.*
import in.diagraph.objects.Cache.{clearTooltip, mNode, nodeMap, pathMap, tooltip}
import in.diagraph.objects.Constants.*
import in.diagraph.views.{ContextMenu, Header, MainContent}
import org.scalajs.dom
import org.scalajs.dom.*

object DocumentCtl {
  // Properties
  var isDragging         = false
  var isResizing         = false
  var isJoining          = false
  var startX             = 0.0
  var startY             = 0.0
  var selectedNode: Node = null
  var movingNode: Node   = null
  var resizingNode: Node = null
  var joiningSNode: Node = null
  var cPath: Path        = null

  // Behavior
  def init(): Unit = {
    document.addEventListener("DOMContentLoaded", contentLoadHandler)
    document.oncontextmenu = ContextMenu.contextMenuEvent
    document.onclick = mouseClickHandler
    document.onmousedown = mouseDownHandler
    document.onmousemove = mouseMoveHandler
    document.onmouseover = mouseOverHandler
    document.onmouseup = mouseUpHandler
  }

  private def contentLoadHandler = { (event: Event) =>
    document.body.appendChild(Header.header())
    // document.body.appendChild(Toolbar.toolbar())
    document.body.appendChild(MainContent.content())
    Header.loadGraph("default")
  }

  private def mouseClickHandler = (e: MouseEvent) => {
    ContextMenu.clearContextMenu()
//    clearTooltip()
    if e.target.isInstanceOf[SVGTextPathElement] then
      val path = pathMap(e.target.asInstanceOf[SVGTextPathElement].getAttribute("href").substring(1))
      path.props.text = window.prompt("Enter text", path.props.text)
      path.labelPath.innerHTML = path.props.text
    val node = getTargetNode(e)
    if node != null then selectedNode = node
    if e.target.isInstanceOf[SVGElement] then clearTooltip()
  }

  def getTargetNode(e: MouseEvent): Node = {
    val foreignObject = e.target.asInstanceOf[HTMLElement].closest("foreignObject")
    if foreignObject != null && nodeMap.contains(foreignObject.id) then nodeMap.get(foreignObject.id).get
    else null
  }

  private def mouseDownHandler = (e: MouseEvent) => {
    clearTooltip()
    val node = getTargetNode(e)
    if node != null then
      val mouseDownEdge = node.getMouseDownEdge(e)
      if mouseDownEdge == -1 then
        isDragging = true
        movingNode = node
        startX = e.pageX
        startY = e.pageY
      else if node.props.format.equals(TEXT) || node.props.format.equals(IMAGE) then
        isResizing = true
        resizingNode = node
      else
        isJoining = true
        val path: Path = Path.addPath(e, node)
        cPath = path
        mNode.paths.addOne(path.props.id, path)
        mNode.bodySvg.appendChild(path.svgPath)
        mNode.bodySvg.appendChild(path.label)
    e.stopPropagation()
  }

  private def mouseMoveHandler = (e: MouseEvent) => {
    if (isDragging) {
      val deltaXY = XY(e.pageX - startX, e.pageY - startY)
      movingNode.move(deltaXY)
      startX = e.pageX
      startY = e.pageY
    }
    if (isJoining && cPath != null) {
      cPath.props.x2 = e.pageX - mNode.west()
      cPath.props.y2 = e.pageY - mNode.north()
      cPath.updatePath()
    }
    if (isResizing) {
      resizingNode.props.width = resizingNode.bodySvg.getBoundingClientRect().width
      resizingNode.props.height = resizingNode.bodySvg.getBoundingClientRect().height
      if resizingNode.props.format.equals(IMAGE) then resizingNode.updateImageSize()
      if resizingNode.parent != null then resizingNode.parent.updateSize()
    }
  }

  private def mouseOverHandler = (e: MouseEvent) => {}

  private def mouseUpHandler: MouseEvent => Unit = (e: MouseEvent) => {
    if (isJoining && cPath != null) {
      val endNode = getTargetNode(e)
      if endNode != null && !endNode.isSame(cPath.startNode)
        && !cPath.startNode.isDescendantOf(endNode)
      then
        cPath.tailNode = endNode
        cPath.props.tNode = endNode.props.id

        val lcaNode: Node = lca(cPath.startNode, cPath.tailNode)
        cPath.parent = lcaNode
        mNode.paths.remove(cPath.props.id)
        mNode.bodySvg.removeChild(cPath.svgPath)
        mNode.bodySvg.removeChild(cPath.label)

        cPath.endPath(e, endNode)
        cPath.updatePath()
        lcaNode.paths.addOne(cPath.props.id, cPath)
        lcaNode.bodySvg.appendChild(cPath.svgPath)
        lcaNode.bodySvg.appendChild(cPath.label)
      else
        cPath.startNode.props.oPaths -= cPath.props.id
        pathMap -= cPath.props.id
        mNode.bodySvg.removeChild(cPath.svgPath)
        mNode.paths.remove(cPath.props.id)
    }
    isDragging = false
    isResizing = false
    isJoining = false
  }
}
