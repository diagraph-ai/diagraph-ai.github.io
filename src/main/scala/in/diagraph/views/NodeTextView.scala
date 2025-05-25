package in.diagraph.views

import in.diagraph.models.*
import in.diagraph.objects.Cache.{clearTooltip, tooltip}
import org.scalajs.dom
import org.scalajs.dom.html.Div
import org.scalajs.dom.{Event, MouseEvent, document}

import scala.scalajs.js.timers.setTimeout

trait NodeTextView {
  this: Node =>
  def nodeTextView(): Div = {
    val content = createElement[Div].withStyle(s"""
          overflow: auto;
          box-sizing: border-box;
          padding: 5px;
          color: #333;
          border-radius: 5px;
          min-width: 100px;
        """)
    content.innerHTML = props.text
    content.setAttribute("data-ph", "--text--")
    content.contentEditable = "true"
    content.oninput = (e: Event) => {
      props.text = content.innerHTML
      updateSize()
    }
//    content.onmouseover = (e: MouseEvent) => {
//      mouseOn = true
//      setTimeout(2000) {
//        if mouseOn then
//          clearTooltip()
//          tooltip = nodeTooltipView(e)
//          document.body.appendChild(tooltip)
//      }
//    }
//    content.onmouseout = (e: Event) => mouseOn = false
//    content.onclick = (e: Event) => clearTooltip()
    content
  }
}
