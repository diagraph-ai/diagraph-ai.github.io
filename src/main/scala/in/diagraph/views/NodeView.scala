package in.diagraph.views

import in.diagraph.models.*
import in.diagraph.objects.*
import in.diagraph.objects.Constants.*
import in.diagraph.views.Header.theme
import org.scalajs.dom
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.html.Div
import org.scalajs.dom.svg.Element

trait NodeView extends NodeTitleView, NodeSvgView, NodeTextView, NodeImageView, NodeTooltipView {
  this: Node =>

  // Properties
  var titleDiv: Div        = null
  var bodySvg: HTMLElement = null
  var contentFO: Element   = createForeignObject()

  // Behavior

  def createForeignObject(): Element = {
    val foreignObject   = createElement[Element](FOREIGN_OBJECT)
    foreignObject.setAttribute("x", props.x.px)
    foreignObject.setAttribute("y", props.y.px)
    foreignObject.asInstanceOf[HTMLElement].style.width = MAX_CONTENT
    foreignObject.asInstanceOf[HTMLElement].style.overflow = VISIBLE
    foreignObject.setAttribute("id", props.id)
    val contentDiv: Div = createElement[Div].withStyle(s"""
        width: max-content;
        border-radius: 5px;
        cursor: pointer;
        background-color: ${if theme == LIGHT then "#e8e8e8" else "#e8e8e8"};
        box-shadow: ${if props.format != TEXT then "0 .5rem 1rem rgba(0, 0, 0, .15)" else "none"};
        animation: animate-in 1s ease-out forwards;
      """)
    titleDiv            = createTitleDiv()
    bodySvg         = createBodySvg()
    contentDiv.appendChild(titleDiv)
    contentDiv.appendChild(bodySvg)
    foreignObject.appendChild(contentDiv)
    foreignObject
  }

  def createBodySvg(): HTMLElement = {
    val bodyDiv = props.format match {
      case NODE => nodeSvgView()
      case TEXT => nodeTextView()
      case IMAGE => nodeImageView()
      case _ => throw new IllegalArgumentException("Unsupported format")
    }
    bodyDiv
  }
}
