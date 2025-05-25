package in.diagraph.controller

import in.diagraph.objects.Cache.*
import in.diagraph.objects.Constants.*
import org.scalajs.dom
import org.scalajs.dom.{HTMLElement, document}

trait ContextMenuCtl {

  def contextMenuClickHandler = (e: dom.MouseEvent) => {
    e.target.asInstanceOf[HTMLElement].innerHTML match {
      case ADD_NODE if focusedNode != null            => focusedNode.addNode()
      //case ADD_TEXT if focusedNode != null            => focusedNode.addNode(TEXT)
      case ADD_IMAGE if focusedNode != null           => focusedNode.addNode(IMAGE)
      case REMOVE_NODE if focusedNode != null         => focusedNode.deleteNode()
      case REMOVE_PATH if focusedPath != null         => focusedPath.deletePath()
      case BI_DIRECTIONAL_PATH if focusedPath != null => focusedPath.makeBiDirectional()
      case _                                          =>
    }
    clearContextMenu()
    e.stopPropagation()
  }

  def clearContextMenu(): Unit = {
    if document.body.contains(contextMenu) then document.body.removeChild(contextMenu)
    focusedPath = null
    focusedNode = null
  }
}
