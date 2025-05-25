package in.diagraph.views

import in.diagraph.models.Path
import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, SVGPathElement}
import org.scalajs.dom.svg.Element

trait PathView {
  this: Path =>

  var svgPath: SVGPathElement = createPath()
  var label: Element          = createLabel()
  var labelPath: Element      = createLabelPath()

  private def createPath(): SVGPathElement = {
    svgPath = createElement[SVGPathElement](PATH)
    svgPath.setAttribute("marker-end", "url(#arrow)")
    if props.biDirectional then svgPath.setAttribute("marker-start", "url(#arrow)")
    svgPath.setAttribute("id", props.id)
    svgPath.setAttribute("stroke", "rgb(119, 119, 119)")
    svgPath.setAttribute("stroke-width", "1.2")
    svgPath.setAttribute("fill", "none")
    svgPath.setAttribute("overflow", "visible")
    svgPath.onmouseover = (e: dom.MouseEvent) => {
      svgPath.style.stroke = HIGHLIGHT_COLOR
      e.stopPropagation()
    }
    svgPath.onmouseout = (e: dom.MouseEvent) => {
      svgPath.style.stroke = "rgb(119, 119, 119)"
      e.stopPropagation()
    }
    updateCoordinates()
    svgPath
  }

  def createLabel(): Element = {
    val label = createElement[Element](TEXT)
    label.asInstanceOf[HTMLElement].style.fill = color()
    label.asInstanceOf[HTMLElement].style.fontSize = "12px"
    label.onmouseover = (e: dom.MouseEvent) => {
      svgPath.style.stroke = HIGHLIGHT_COLOR
      label.asInstanceOf[HTMLElement].style.fill = HIGHLIGHT_COLOR
      e.stopPropagation()
    }
    label.onmouseout = (e: dom.MouseEvent) => {
      svgPath.style.stroke = color()
      label.asInstanceOf[HTMLElement].style.fill = color()
      e.stopPropagation()
    }
    label
  }

  def createLabelPath(): Element = {
    labelPath = createElement[Element](TEXT_PATH)
    labelPath.setAttribute("href", "#" + props.id)
    labelPath.setAttribute("startOffset", "20%")
    props.text = if props.text == null then "" else props.text
    labelPath.innerHTML = if props.text.isEmpty then props.id else props.text
    label.appendChild(labelPath)
    labelPath
  }

  def render(parent: HTMLElement): Unit = {
    parent.appendChild(svgPath)
    parent.appendChild(label)
  }

  def updateCoordinates(): Unit = {
    svgPath.setAttribute("x1", props.x1.toString)
    svgPath.setAttribute("y1", props.y1.toString)
    svgPath.setAttribute("x2", props.x2.toString)
    svgPath.setAttribute("y2", props.y2.toString)
    svgPath.setAttribute("d", props.d)
  }
}
