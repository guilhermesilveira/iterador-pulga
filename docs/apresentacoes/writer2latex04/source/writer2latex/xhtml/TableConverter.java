/************************************************************************
 *
 *  TableConverter.java
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License version 2.1, as published by the Free Software Foundation.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *  MA  02111-1307  USA
 *
 *  Copyright: 2002-2004 by Henrik Just
 *
 *  All Rights Reserved.
 * 
 *  Version 0.3.3g (2004-10-25)
 *
 */

package writer2latex.xhtml;

import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import writer2latex.util.Misc;
import writer2latex.util.Config;
import writer2latex.office.XMLString;
import writer2latex.office.WriterStyleCollection;
import writer2latex.office.TableGridModel;

public class TableConverter extends ConverterHelper {

    // The collection of all table names
    // TODO: Create accessor method(s)
    protected Vector sheetNames = new Vector();
	
    public TableConverter(WriterStyleCollection wsc, Config config, DocumentSerializerImpl converter) {
        super(wsc,config,converter);
    }
	
    /** Converts an office node as a complete table (spreadsheet) document
     *
     *  @param onode the Office node containing the content to convert
     */
    public void convertTableContent(Element onode) {
        Node hnode = null;
        if (!onode.hasChildNodes()) { return; }
        if (!config.xhtmlCalcSplit()) { hnode = converter.nextOutFile(); }
        NodeList nList = onode.getChildNodes();
        int nLen = nList.getLength();
        for (int i=0; i<nLen; i++) {
            Node child = nList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sNodeName = child.getNodeName();
                if (sNodeName.equals(XMLString.TABLE_TABLE)) {
                    if (config.xhtmlCalcSplit()) { hnode = converter.nextOutFile(); }
                    // Collect name and add heading:
                    String sName = Misc.getAttribute(child,XMLString.TABLE_NAME);
                    sheetNames.add(sName);
                    Element heading = converter.createElement("h2");
                    hnode.appendChild(heading);
                    heading.setAttribute("id","tableheading"+(sheetNames.size()-1));
                    heading.appendChild(converter.createTextNode(sName));
                    handleTable(child,hnode);
                }
            }
        }
    }

    /** Process a table:table tag 
     * 
     *  @param onode the Office node containing the table element 
     *  @param hnode the XHTML node to which the table should be attached
     */
    public void handleTable(Node onode, Node hnode) {
        String sName = Misc.getAttribute(onode,XMLString.TABLE_NAME);

        TableGridModel tgm = new TableGridModel(onode);
        // Create table
        Element table = converter.createElement("table");
        converter.addTarget(table,sName+"%7Ctable");
        hnode.appendChild(table);

        // IE needs the cellspacing attribute, as it doesn't understand the css border-spacing attribute
        table.setAttribute("cellspacing","0");

        boolean bIsSubtable = XMLString.TABLE_SUB_TABLE.equals(onode.getNodeName());
        if (bIsSubtable) { // a subtable should have no margin!
            table.setAttribute("style","margin:0");
        }
        else {
            String sStyleName = Misc.getAttribute(onode,XMLString.TABLE_STYLE_NAME);
            getStyleCv().applyTableStyle(sStyleName,table);
        }
        // Create columns
        int nColCount = tgm.getMaxColCount();
        for (int nCol=0; nCol<nColCount; nCol++) {
            Element col = converter.createElement("col");
            table.appendChild(col);
            // Note: Always apply column style!
            getStyleCv().applyColumnStyle(tgm.getCol(nCol).getStyleName(),col);
        }
        // Create groups
        Element thead = converter.createElement("thead");
        Element tbody = converter.createElement("tbody");
        // Create rows
        int nRowCount = tgm.getRowCount();
        Element tgroup = thead;
        if (tgm.getRow(nRowCount-1).isHeader()) { // no body? xhtml will be unhappy
            tgroup = tbody;
        }
        for (int nRow=0; nRow<nRowCount; nRow++) {
            if (!tgm.getRow(nRow).isHeader()) {
                tgroup = tbody;
            }
            Element tr = converter.createElement("tr");
            tgroup.appendChild(tr);
            getStyleCv().applyRowStyle(tgm.getRow(nRow).getStyleName(),tr);
            for (int nCol=0; nCol<nColCount; nCol++) {
                Node cell = tgm.getCell(nRow,nCol);
                if (cell!=null && XMLString.TABLE_TABLE_CELL.equals(cell.getNodeName())) {
                    Element td = converter.createElement("td");
                    tr.appendChild(td);
                    String sRowSpan = Misc.getAttribute(cell,XMLString.TABLE_NUMBER_ROWS_SPANNED);
                    if (Misc.getPosInteger(sRowSpan,1)>1) {
                        td.setAttribute("rowspan",sRowSpan);
                    }
                    String sColSpan = Misc.getAttribute(cell,XMLString.TABLE_NUMBER_COLUMNS_SPANNED);
                    if (Misc.getPosInteger(sColSpan,1)>1) {
                        td.setAttribute("colspan",sColSpan);
                    }
                    // Is this a subtable?
                    Node subTable = Misc.getChildByTagName(cell,XMLString.TABLE_SUB_TABLE);
                    if (subTable!=null) { // if it is, there should be no padding in this cell
                        td.setAttribute("style","padding:0");
                    }
                    else {
                        //if (!config.xhtmlIgnoreStyles()) {
                        String sStyleName = Misc.getAttribute(cell,XMLString.TABLE_STYLE_NAME);
                        if (sStyleName==null) { // use column default
                            sStyleName = tgm.getCol(nCol).getDefaultCellStyleName();
                        }
                        String sValueType = Misc.getAttribute(cell,XMLString.TABLE_VALUE_TYPE);
                        getStyleCv().applyCellStyle(sStyleName,sValueType,td);
                        //}
                    }
                    if (cell.hasChildNodes()) {
                        getTextCv().traverseBlockText(cell,td);
                    }
                    if (!td.hasChildNodes()) {
                        td.appendChild( converter.createTextNode("\u00A0") );
                    }
                }
                //else if (XMLString.TABLE_COVERED_TABLE_CELL.equals(cell.getNodeName())) {
                    // covered table cells are not part of xhtml table model
                //}
            }
        }
        // Put the content into the table
        if (thead.hasChildNodes()) { table.appendChild(thead); }
        table.appendChild(tbody);
    }
	
}
