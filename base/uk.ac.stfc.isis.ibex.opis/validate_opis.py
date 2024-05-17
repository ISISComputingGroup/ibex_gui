from check_opi_format import file_iterator, DEFAULT_ROOT_DIR
from lxml import etree
from lxml.etree import LxmlError
import os.path as path

xml_parser = etree.XMLParser(remove_blank_text=True)

dummy_widget_name = "Dummy"

dummy_widget_xml = """
  <widget typeId="org.csstudio.opibuilder.widgets.ActionButton" version="2.0.0">
    <actions hook="false" hook_all="false" />
    <border_alarm_sensitive>false</border_alarm_sensitive>
    <border_color>
      <color name="ISIS_Border" red="0" green="0" blue="0" />
    </border_color>
    <border_style>0</border_style>
    <border_width>1</border_width>
    <enabled>true</enabled>
    <font>
      <opifont.name fontName="Segoe UI" height="9" style="0" pixels="false">ISIS_Button_NEW</opifont.name>
    </font>
    <forecolor_alarm_sensitive>false</forecolor_alarm_sensitive>
    <foreground_color>
      <color name="ISIS_Standard_Text" red="0" green="0" blue="0" />
    </foreground_color>
    <height>1</height>
    <image></image>
    <name>{name}</name>
    <push_action_index>0</push_action_index>
    <pv_name></pv_name>
    <pv_value />
    <rules />
    <scale_options>
      <width_scalable>true</width_scalable>
      <height_scalable>true</height_scalable>
      <keep_wh_ratio>false</keep_wh_ratio>
    </scale_options>
    <scripts />
    <style>1</style>
    <text></text>
    <toggle_button>false</toggle_button>
    <tooltip></tooltip>
    <visible>true</visible>
    <widget_type>Action Button</widget_type>
    <width>1</width>
    <wuid>-648922a4:1624e4fa0bd:-7f69</wuid>
    <x>{x}</x>
    <y>{y}</y>
  </widget>"""


def check_widget_correct(widget_node):
    width = str(widget_node.find("width").text)
    height = str(widget_node.find("height").text)
    name = widget_node.find("name").text
    return width == "1" and height == "1" and name.lower() == dummy_widget_name.lower()


files_to_change = {}  # A dictionary of the file paths to change and their new content

OPI_folder = path.join(path.dirname(path.abspath(__file__)), DEFAULT_ROOT_DIR)
for file_path in file_iterator(OPI_folder):
    filename = path.basename(file_path)

    try:
        root = etree.parse(file_path, xml_parser)
    except LxmlError as e:
        print("{}: XML failed to parse {}".format(filename, e))
        continue

    try:
        display_element = root.xpath("/display")[0]
    except IndexError as e:
        print("No display element found {}".format(filename))
        continue

    try:
        last_widget = root.xpath("/display/widget[last()]")[0]
    except IndexError as e:
        print("No widgets found for {}".format(filename))
        continue

    if check_widget_correct(last_widget):
        continue

    # Button must be contained in the bounding box of the OPI so let's put it in the top left
    top_left_pixel = None

    for widget in root.xpath("/display/widget"):
        widget_x_y = widget.find("x").text, widget.find("y").text

        if top_left_pixel is None:
            top_left_pixel = widget_x_y
        else:
            top_left_pixel = min(top_left_pixel[0], widget_x_y[0]), min(top_left_pixel[1], widget_x_y[1])

    print("Dummy widget not found on {} adding to position {}".format(filename, top_left_pixel))
    xml_to_insert = dummy_widget_xml.format(name=dummy_widget_name, x=top_left_pixel[0], y=top_left_pixel[1])

    dummy_widget = etree.fromstring(xml_to_insert)

    display_element.append(dummy_widget)

    files_to_change[file_path] = root

if files_to_change:
    for file_path, root in files_to_change.items():
        with open(file_path, "wb") as xml_file:
            xml_file.write(etree.tostring(root, pretty_print=True, xml_declaration=True, encoding='UTF-8'))
    print("Confirm dummy button has been added correctly and re-commit")
    exit(1)
else:
    exit(0)

