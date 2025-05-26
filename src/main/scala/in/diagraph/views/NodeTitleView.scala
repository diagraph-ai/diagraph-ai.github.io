package in.diagraph.views

import in.diagraph.models.Node
import in.diagraph.objects.Cache.{clearTooltip, tooltip}
import in.diagraph.objects.Constants.{LIGHT, NODE, color}
import in.diagraph.views.Header.theme
import in.diagraph.objects.px
import org.scalajs.dom.{Event, MouseEvent, document}
import org.scalajs.dom.html.Div

import scala.scalajs.js.timers.setTimeout

trait NodeTitleView {
  this: Node =>
  def createTitleDiv(): Div = {
    val content = createElement[Div].withStyle(
      s"""
              overflow: auto;
              position: fixed;
              box-sizing: border-box;
              padding: 5px;
              color: rgb(119, 119, 119);
              border-radius: 5px;
              min-width: 100px;
            """)
    content.innerHTML = props.title
    content.setAttribute("data-ph", "--text--")
    content.contentEditable = "true"
    content.oninput = (e: Event) => {
      props.title = content.innerHTML
      updateSize()
    }
    content.ondblclick = (e: MouseEvent) => {
      if props.showChildren then {
        props.showChildren = false
        minimize()
      } else {
        props.showChildren = true
        maximize()
      }
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
