package in.diagraph.views

import in.diagraph.models.*
import in.diagraph.objects.*
import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.HTMLElement

trait NodeSvgView {
  this: Node =>
  def nodeSvgView(): HTMLElement = {
    val element           = createElement[HTMLElement](SVG)
    val defs: HTMLElement = createElement[HTMLElement](DEFS)

    val arrowM = createElement[HTMLElement](MARKER)
    arrowM.setAttribute("markerWidth", "5")
    arrowM.setAttribute("markerHeight", "5")
    arrowM.setAttribute("refX", "2.5")
    arrowM.setAttribute("refY", "2.5")
    arrowM.setAttribute("orient", "auto-start-reverse") // auto-start-reverse
    arrowM.id = "arrow"
    val path   = createElement[HTMLElement](PATH)
    path.setAttribute("d", "M 0 0 L 5 2.5 L 0 5 z")
    path.style.fill = "rgb(119, 119, 119)"
    arrowM.appendChild(path)

    defs.appendChild(arrowM)
    element.appendChild(defs)
    element.setAttribute("width", D_WIDTH.px)
    element.setAttribute("height", D_HEIGHT.px)
    element.id = props.id + "-svg"
    element
  }
}
