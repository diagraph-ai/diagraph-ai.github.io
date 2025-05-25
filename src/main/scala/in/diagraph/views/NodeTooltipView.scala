package in.diagraph.views

import in.diagraph.models.Node
import in.diagraph.objects.px
import org.scalajs.dom.html.Div
import org.scalajs.dom.{Event, HTMLElement, MouseEvent}

trait NodeTooltipView {
  this: Node =>
  def nodeTooltipView(e: MouseEvent): HTMLElement = {
    val contentDiv = createElement[Div].withStyle(s"""
         |width: max-content;
         |min-width: 100px;
         |left: ${e.pageX.px};
         |top: ${e.pageY.px};
         |position: absolute;
         |""".stripMargin)
    val content    = createElement[Div].withStyle(s"""
              overflow: auto;
              box-sizing: border-box;
              padding: 10px;
              background-color: #dadada;
              color: #333;
              border-radius: 5px;
              min-width: 100px;
              display: inline-block;
            """)
    content.innerHTML = props.toolTipText
    content.setAttribute("data-ph", "--text--")
    content.contentEditable = "true"
    content.oninput = (e: Event) => {
      props.toolTipText = content.innerHTML
      contentDiv.style.width = (content.getBoundingClientRect().width + 20).px
    }
    contentDiv.appendChild(content)
    contentDiv
  }
}
