package in.diagraph.objects

import in.diagraph.models.{Node, Path}
import org.scalajs.dom.{Element, HTMLElement, document}

object Cache {
  def mainSVG: HTMLElement       = document.getElementById("mainSvg").asInstanceOf[HTMLElement]
  var mNode: Node                = null
  var contextMenu: Element       = null
  var focusedNode: Node          = null
  var focusedPath: Path          = null
  var nodeMap: Map[String, Node] = Map()
  var pathMap: Map[String, Path] = Map()
  val handleSize                 = 10 // Size of the resize handles
  var tooltip: Element           = null

  def clearTooltip(): Unit =
    if tooltip != null then
      if document.body.contains(tooltip) then document.body.removeChild(tooltip)
      tooltip = null
}
