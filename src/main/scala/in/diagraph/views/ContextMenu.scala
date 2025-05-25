package in.diagraph.views

import in.diagraph.controller.ContextMenuCtl
import in.diagraph.objects.Cache.*
import in.diagraph.objects.Constants.*
import in.diagraph.objects.px
import org.scalajs.dom
import org.scalajs.dom.html.Div
import org.scalajs.dom.{HTMLElement, SVGPathElement, SVGTextPathElement, document}

object ContextMenu extends ContextMenuCtl {

  def contextMenuEvent = (e: dom.MouseEvent) => {
    e.preventDefault()
    clearContextMenu()
//    clearTooltip()
    if e.target.isInstanceOf[SVGPathElement] || e.target.isInstanceOf[SVGTextPathElement] then
      focusedPath =
        if e.target.isInstanceOf[SVGPathElement] then pathMap(e.target.asInstanceOf[HTMLElement].id)
        else pathMap(e.target.asInstanceOf[SVGTextPathElement].getAttribute("href").substring(1))
    else
      val foreignObject = e.target.asInstanceOf[HTMLElement].closest("foreignObject")
      if foreignObject != null then
        focusedNode = nodeMap.get(e.target.asInstanceOf[HTMLElement].closest("foreignObject").id).get

    contextMenu = createElement[Div].withStyle(s"""position: absolute;
         |background-color: #f9f9f9;
         |border: 1px solid #f1f1f1;
         |border-radius: 5px;
         |box-shadow: 2px 2px 5px #888888;
         |z-index: 1000;
         |left: ${e.pageX.px};
         |top: ${e.pageY.px};
         |""".stripMargin)
    contextMenu.appendChild(contextMenuDiv())
    document.body.appendChild(contextMenu)
  }

  def contextMenuDiv() = {
    val element = createElement[dom.html.Div].withStyle("""padding: 5px;
        |cursor: pointer;
        |""".stripMargin)
    if focusedNode != null then
      element.appendChild(contextMenuDivItem(ADD_NODE))
      //element.appendChild(contextMenuDivItem(ADD_TEXT))
      element.appendChild(contextMenuDivItem(ADD_IMAGE))
      element.appendChild(contextMenuDivItem(REMOVE_NODE))
    else if focusedPath != null then
      element.appendChild(contextMenuDivItem(REMOVE_PATH))
      element.appendChild(contextMenuDivItem(BI_DIRECTIONAL_PATH))
    element
  }

  def contextMenuDivItem(text: String) = {
    val element = createElement[Div].withStyle("""padding: 5px;
        |border-bottom: 1px solid #f1f1f1;
        |font-size: 14px;
        |color: #333;
        |""".stripMargin)
    element.innerHTML = text
    element.onclick = contextMenuClickHandler
    element
  }
}
