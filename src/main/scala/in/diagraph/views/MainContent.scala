package in.diagraph.views

import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.html.Div
import org.scalajs.dom.svg.Element

object MainContent {

  def containerGrid(): dom.html.Div = {
    val element = createElement[Div].withStyle(s"""
        display: grid;
        grid-template-columns: 1fr;
        height: 100vh;
        width: 100vw;
      """)
    element.id = "containerGrid"
    element.appendChild(leftSvg)
    element
  }

  val leftSvg: dom.html.Div = {
    val element = createElement[Div].withStyle(s"""
        overflow: auto;
      """)

    val svgElement = createElement[Element](SVG)
    svgElement.id = "mainSvg"
    svgElement.setAttribute("width", "100%")
    svgElement.setAttribute("height", "100vh")

    val defsElement    = createElement[Element](DEFS)
    val patternElement = createElement[Element](PATTERN)
    patternElement.setAttribute("id", "dotPattern")
    patternElement.setAttribute("width", "25")
    patternElement.setAttribute("height", "25")
    patternElement.setAttribute("patternUnits", "userSpaceOnUse")

    val circleElement = createElement[Element](CIRCLE)
    circleElement.setAttribute("cx", "1")
    circleElement.setAttribute("cy", "1")
    circleElement.setAttribute("r", ".25")
    circleElement.setAttribute("fill", svgBaseDotsColor())

    patternElement.appendChild(circleElement)
    defsElement.appendChild(patternElement)
    svgElement.appendChild(defsElement)

    val dotPatternRect = createElement[Element](RECTANGLE)
    dotPatternRect.id = "dotPatternRect"
    dotPatternRect.setAttribute("width", "100%")
    dotPatternRect.setAttribute("height", "2000px")
    dotPatternRect.setAttribute("fill", "url(#dotPattern)")

    svgElement.appendChild(dotPatternRect)

    element.appendChild(svgElement)
    element.id = "leftSvg"
    element
  }
}
