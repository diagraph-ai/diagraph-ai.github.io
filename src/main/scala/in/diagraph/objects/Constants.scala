package in.diagraph.objects

import in.diagraph.views.Header.theme

object Constants {
  val LIGHT          = "light"
  val DARK           = "dark"
  val SVG_NS         = "http://www.w3.org/2000/svg"
  val HEADER         = "header"
  val ANCHOR         = "a"
  val NAV            = "nav"
  val FOREIGN_OBJECT = "foreignObject"
  val SVG            = "svg"
  val DEFS           = "defs"
  val PATTERN        = "pattern"
  val DIV            = "div"
  val SPAN           = "span"
  val IMG            = "img"
  val BUTTON         = "button"
  val INPUT          = "input"
  val TEXTAREA       = "textarea"
  val CANVAS         = "canvas"
  val SELECT         = "select"
  val OPTION         = "option"
  val TABLE          = "table"
  val TABLE_ROW      = "tr"
  val TABLE_CELL     = "td"
  val UL             = "ul"
  val OL             = "li"

  val TEXT_PATH        = "textPath"
  val CIRCLE           = "circle"
  val RECTANGLE        = "rect"
  val UNSUPPORTED_TYPE = "Unsupported type"

  val MARKER = "marker"
  val NODE   = "node"
  val PATH   = "path"

  val POINTER     = "pointer"
  val MAX_CONTENT = "max-content"
  val VISIBLE     = "visible"

  val ADD_NODE            = "Add Node"
  val ADD_TEXT            = "Add Text"
  val ADD_IMAGE           = "Add Image"
  val TEXT                = "text"
  val IMAGE               = "image"
  val REMOVE_NODE         = "Remove"
  val REMOVE_PATH         = "Remove Path"
  val BI_DIRECTIONAL_PATH = "Bi-Directional"

  val GRAPH_SELECT    = "graphSelect"
  val TRANSPARENT     = "transparent"
  val GREEN           = "#008476"
  val HIGHLIGHT_COLOR = "#FF8C00"

  val EAST: Int    = 0
  val SOUTH: Int   = 1
  val WEST: Int    = 2
  val NORTH: Int   = 3
  val INVALID: Int = -1

  val D_WIDTH: Double  = 100
  val D_HEIGHT: Double = 30

  def color(): String = if theme == "light" then "#333" else "#eee"

  def bgColor(): String = if theme == "light" then "#eee" else "#333"

  def svgBaseDotsColor(): String = if theme == "light" then "#333" else "#eee"

  def bodyCss(): String = {
    val bgColor = if theme == "light" then "#eee" else "#333"
    val color   = if theme == "light" then "#333" else "#eee"
    s"""
      margin: 0;
      padding: 0;
      background-color: $bgColor;
      color: $color;
    """
  }

  val headerCss       =
    """
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      z-index: 1000;
    """
  val headerDivCss    =
    """
      display: flex;
      justify-content: space-between;
      padding: 5px;
      margin: 5px;
      border-radius: 10px;
    """
  val headerDivNavCss =
    """
      display: flex;
      align-items: center;
    """

  val headerDivNavLinkCss  =
    """
      padding: 10px;
      text-decoration: none;
      cursor: pointer;
    """
  val headerDivNavTitleCss =
    """
      font-size: 24px;
      font-weight: bold;
      text-decoration: none;
      background-color: #008476;
      color: #eee;
      padding: 5px;
      cursor: pointer;
      border-radius: 5px;
    """
  val headerGraphSelectCss =
    """
       margin-left: 10px;
       padding: 5px;
       font-size: 14px;
       border-radius: 4px;
       border: 1px solid #ccc;
    """

  // for Navigation bar
  val toolbarCss     =
    """
      position: fixed;
      top: 50%;
      left: 5px;
      transform: translateY(-50%);
      height: 50%;
      background-color: #777;
      z-index: 1000;
      display: flex;
      flex-direction: column;
      padding: 10px;
      align-items: center;
      border-radius: 10px;
    """
  val toolbarIconCss =
    """
      width: 30px;
      height: 30px;
      margin-bottom: 10px;
      cursor: pointer;
    """

}
