/************************************************************************
 *
 *  DrawConverter.java
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
 *  Version 0.3.3g (2004-10-30)
 *
 */

package writer2latex.xhtml;

import java.io.IOException;

import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import org.openoffice.xmerge.converter.xml.EmbeddedObject;
import org.openoffice.xmerge.converter.xml.EmbeddedBinaryObject;
import org.openoffice.xmerge.converter.xml.EmbeddedXMLObject;

import writer2latex.util.Misc;
import writer2latex.util.Config;
import writer2latex.util.CSVList;
import writer2latex.xmerge.BinaryGraphicsDocument;
import writer2latex.office.XMLString;
import writer2latex.office.MIMETypes;
import writer2latex.office.WriterStyleCollection;
import writer2latex.xhtml.XhtmlStyleMap;

public class DrawConverter extends ConverterHelper {

    private String sScale;
    private boolean bConvertToPx;

    public DrawConverter(WriterStyleCollection wsc, Config config, DocumentSerializerImpl converter) {
        super(wsc,config,converter);
        sScale = config.getXhtmlScaling();
        bConvertToPx = config.xhtmlConvertToPx();
    }
	
    // Draw documents (presentations)

    public void convertDrawContent(Element onode) {
        if (!onode.hasChildNodes()) { return; }
        NodeList nList = onode.getChildNodes();
        int nLen = nList.getLength();
        for (int i=0; i<nLen; i++) {
            Node child = nList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String sNodeName = child.getNodeName();
                if (sNodeName.equals(XMLString.DRAW_PAGE)) {
                    handleDrawPage((Element)child,converter.nextOutFile());
                }
            }
        }
    }

    private void handleDrawPage(Element onode, Node hnode) {
        Element div = converter.createElement("div");
        hnode.appendChild(div);
        CSVList props = new CSVList(";");
        //applySize(onode,props,false);
        div.setAttribute("style",props.toString());

        // Traverse the draw:page
        if (!onode.hasChildNodes()) { return; }
        NodeList nList = onode.getChildNodes();
        int nLen = nList.getLength();
        for (int i=0; i<nLen; i++) {
            Node child = nList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                handleDrawElement(child,div);
            }
        }
    }
	
    /////////////////////////////////////////////////////////////////////////
    // DRAW ELEMENTS
	
    public void handleDrawElement(Node onode, Node hnode) {
        // onode must be an elment in the draw namespace
        String sName = onode.getNodeName();
        if (sName.equals(XMLString.DRAW_OBJECT)) {
            handleDrawObject(onode,hnode);
        }		
        else if (sName.equals(XMLString.DRAW_IMAGE)) {
            handleDrawImage(onode,hnode);
        }		
        else if (sName.equals(XMLString.DRAW_TEXT_BOX)) {
            handleDrawTextBox(onode,hnode);
        }		
        else if (sName.equals(XMLString.DRAW_A)) {
            // we handle this like text:a
            getTextCv().handleAnchor(onode,hnode);
        }		
    }

    private void handleDrawObject(Node onode, Node hnode) {
        String sHref = Misc.getAttribute(onode, XMLString.XLINK_HREF);
        if (sHref!=null) { // Embedded object in package or linked object
            if (sHref.startsWith("#")) { // Embedded object in package
                sHref = sHref.substring(3); // Strip leading path chars ("#/.")
                EmbeddedObject object = converter.getEmbeddedObject(sHref); 
                if (MIMETypes.MATH.equals(object.getType())) { // Formula!
                    EmbeddedXMLObject xmlObject = (EmbeddedXMLObject) object;
                    // Document settings = object.getSettingsDOM();
                    try {
                        hnode.appendChild(converter.createTextNode(" "));
                        getMathCv().convert(xmlObject.getContentDOM().getDocumentElement(),hnode);
                        //hnode.appendChild(converter.importNode(
                        //          xmlObject.getContentDOM().getDocumentElement(),true) );
                        hnode.appendChild(converter.createTextNode(" "));
                    }
                    catch (SAXException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    //killAnnotation(hnode);
                }
                else { // unsupported object
                    hnode.appendChild( converter.createTextNode("[Warning: object ignored]"));
                }
            }
            else { // TODO: Linked object
                hnode.appendChild( converter.createTextNode("[Warning: Linked object ignored]"));
            }
        }
        else { // flat xml format
            Node formula = Misc.getChildByTagName(onode,XMLString.MATH_MATH);
            if (formula != null) {
                hnode.appendChild(converter.createTextNode(" "));
                getMathCv().convert(formula,hnode);
                //hnode.appendChild(converter.importNode(formula,true));
                hnode.appendChild(converter.createTextNode(" "));
                //killAnnotation(hnode);
            }
        }
    }
	
    private void handleDrawImage(Node onode, Node hnode) {
        // TODO: sometimes inlinetext...
        String sName = Misc.getAttribute(onode,XMLString.DRAW_NAME);

        // get the image from the ImageLoader
        String sFileName = null;
        String sHref = Misc.getAttribute(onode,XMLString.XLINK_HREF);
        if (sHref!=null && !sHref.startsWith("#")) {
            // Linked image is not yet handled by ImageLoader. This is a temp.
            // solution (will go away when ImageLoader is finished)
            sFileName = sHref;
            String sExt = sHref.substring(sHref.lastIndexOf(".")).toLowerCase();
        }
        else { // embedded or base64 encoded image
            BinaryGraphicsDocument bgd = converter.getImageLoader().getImage(onode);
            if (bgd!=null) {
                converter.addDocument(bgd);
                sFileName = bgd.getFileName();
            }
        }
		
        if (sFileName==null) { return; }
		
        // Now for the actual inclusion (sFileName contains the file name)
        String sWidth=Misc.getAttribute(onode,XMLString.SVG_WIDTH);
        String sHeight=Misc.getAttribute(onode,XMLString.SVG_HEIGHT);
        String sAnchor = Misc.getAttribute(onode,XMLString.TEXT_ANCHOR_TYPE);
        Element image = converter.createElement("img");
        converter.addTarget(image,sName+"%7Cgraphics");
        image.setAttribute("src",sFileName);
        if (sName == null) { sName = sFileName; }
        image.setAttribute("alt",sName);
        CSVList props = new CSVList(";");
        if (sWidth!=null) { props.addValue("width",scale(sWidth)); }
        if (sHeight!=null) { props.addValue("height",scale(sHeight)); }
        String sStyleName = Misc.getAttribute(onode, XMLString.DRAW_STYLE_NAME);
        applyFrameStyle(sStyleName,image,props,!"as-char".equals(sAnchor));
        hnode.appendChild(image);
    }

    private void handleDrawTextBox(Node onode, Node hnode) {
        String sName = Misc.getAttribute(onode,XMLString.DRAW_NAME);
        // The div must be included in something else; here an object
        Element container = converter.createElement("object");
        hnode.appendChild(container);
        Element textbox = converter.createElement("div");
        converter.addTarget(textbox,sName+"%7Cframe");
        container.appendChild(textbox);
        // Now style it
        String sWidth = Misc.getAttribute(onode,XMLString.SVG_WIDTH);
        String sAnchor = Misc.getAttribute(onode,XMLString.TEXT_ANCHOR_TYPE);
        CSVList props = new CSVList(";");
        if (sWidth!=null) { props.addValue("width",scale(sWidth)); }
        String sStyleName = Misc.getAttribute(onode, XMLString.DRAW_STYLE_NAME);
        applyFrameStyle(sStyleName,textbox,props,!"as-char".equals(sAnchor));
        getTextCv().traverseBlockText(onode,textbox);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utility methods
	
    /* apply frame style */
    private void applyFrameStyle(String sStyleName,Element element,CSVList props,boolean bFloating) {
        XhtmlStyleMap xframe = config.getXFrameStyleMap();
        if (xframe.contains(sStyleName)) {
            if (!"(none)".equals(xframe.getCss(sStyleName))) {
                element.setAttribute("class",xframe.getCss(sStyleName));
            }
            element.setAttribute("style",props.toString());
        }
        else {
            getStyleCv().applyFrameStyle(sStyleName,element,props,bFloating,!config.xhtmlIgnoreStyles());
        }
    }
	
    private void applySize(Element node, CSVList props, boolean bOnlyWidth) {
        String sWidth = node.getAttribute(XMLString.SVG_WIDTH);
        if (sWidth!=null) { props.addValue("width",scale(sWidth)); }
        if (bOnlyWidth) { return; }
        String sHeight = node.getAttribute(XMLString.SVG_HEIGHT);
        if (sHeight!=null) { props.addValue("height",scale(sHeight)); }
    }
	
    private void applyPosition(Element node, CSVList props) {
        props.addValue("position","absolute");
        String sX = node.getAttribute(XMLString.SVG_X);
        if (sX!=null) { props.addValue("left",scale(sX)); }
        String sY = node.getAttribute(XMLString.SVG_Y);
        if (sY!=null) { props.addValue("top",scale(sY)); }
    }

    private String scale(String s) {
        if (bConvertToPx) {
            return Misc.length2px(Misc.multiply(sScale,Misc.truncateLength(s)));
        }
        else {
            return Misc.multiply(sScale,Misc.truncateLength(s));
        }
    }
	
}


