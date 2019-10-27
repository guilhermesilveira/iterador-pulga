/************************************************************************
 *
 *  XhtmlDocument.java
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
 *  Version 0.3.3i (2004-12-28)
 *
 */

package writer2latex.xhtml;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMImplementation;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.openoffice.xmerge.converter.dom.DOMDocument;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 *  An implementation of <code>Document</code> for
 *  XHTML documents.
 */
public class XhtmlDocument extends DOMDocument {

    /** Constant to identify XHTML 1.0 strict documents */
    public static final int XHTML10 = 0;
	
    /** Constant to identify XHTML 1.1 documents (not used currently) */
    public static final int XHTML11 = 1;
    
    /** Constant to identify XHTML + MathML documents */
    public static final int XHTML_MATHML = 2;

    /** Constant to identify XHTML + MathML documents using the xsl transformations
     *  from w3c's math working group (http://www.w3.org/Math/XSL/)
     */
    public static final int XHTML_MATHML_XSL = 3;
	
    private static final String[] sExtension = { ".html", ".html", ".xhtml", ".xml" };

    private int nType;
	
    private boolean bNoDoctype = false;
	
    private static final String[] sEmpty = { "base", "meta", "link", "hr", "br", "param", "img", "area", "input", "col" };
	
    public static final String getExtension(int nType) {
        return sExtension[nType];
    } 

    /**
     *  Constructor. This constructor also creates the DOM (with root element only),
     *  unlike the constructors in org.openoffice.xmerge.converter.dom.DOMDocument.
     *  @param  name  <code>Document</code> name.
     *  @param  nType the type of document
     */
    public XhtmlDocument(String name, int nType) {
        super(name,sExtension[nType]);
        this.nType = nType;
        // Define publicId and systemId
        String sPublicId = null;
        String sSystemId = null;		
        switch (nType) {
            case XHTML10 :
                sPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
                sSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
                break; 
            case XHTML11 :
                sPublicId = "-//W3C//DTD XHTML 1.1//EN";
                sSystemId = "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";
                break; 
            case XHTML_MATHML :
            case XHTML_MATHML_XSL : 
                sPublicId = "-//W3C//DTD XHTML 1.1 plus MathML 2.0//EN";
                sSystemId = "http://www.w3.org/Math/DTD/mathml2/xhtml-math11-f.dtd";
                //sSystemId = "http://www.w3.org/TR/MathML2/dtd/xhtml-math11-f.dtd"; (old version)
                /* An alternative is to use XHTML + MathML + SVG:
                sPublicId = "-//W3C//DTD XHTML 1.1 plus MathML 2.0 plus SVG 1.1//EN",
                sSystemId = "http://www.w3.org/2002/04/xhtml-math-svg/xhtml-math-svg.dtd"); */
        }

        // create DOM with root element only
        Document contentDOM = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            DOMImplementation domImpl = builder.getDOMImplementation();
            DocumentType doctype = domImpl.createDocumentType("html", sPublicId, sSystemId); 
            contentDOM = domImpl.createDocument("http://www.w3.org/1999/xhtml","html",doctype);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        contentDOM.getDocumentElement().setAttribute("xmlns","http://www.w3.org/1999/xhtml");
        setContentDOM(contentDOM);
    }
	
    public void setNoDoctype(boolean b) { bNoDoctype = b; }
	
    public String getFileExtension() { return super.getFileExtension(); }

    /**
     *  Write out content to the supplied <code>OutputStream</code>.
     *  (with pretty printing)
     *  @param  os  XML <code>OutputStream</code>.
     *  @throws  IOException  If any I/O error occurs.
     */
    public void write(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os,"UTF-8");
        // Omit xml prolog for pure xhtml documents (to be browser safe)
        if (nType==XHTML_MATHML || nType==XHTML_MATHML_XSL) {
            osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        }
        // Either specify doctype or xsl transformation (the user may require
        // that no doctype is used; this may be desirable for further transformations)
        if (nType==XHTML_MATHML_XSL) {
            // Original url: http://www.w3.org/Math/XSL/pmathml.xsl
            osw.write("<?xml-stylesheet type=\"text/xsl\" href=\"pmathml.xsl\"?>\n");
        }
        else if (!bNoDoctype) {
            osw.write("<!DOCTYPE html PUBLIC \"");
            osw.write(getContentDOM().getDoctype().getPublicId());
            osw.write("\" \"");
            osw.write(getContentDOM().getDoctype().getSystemId());
            osw.write("\">\n");
        }
        write(getContentDOM().getDocumentElement(),0,osw);
        osw.flush();
        osw.close();
    }

    private boolean isEmpty(String sTagName) {
        for (int i=0; i<sEmpty.length; i++) {
            if (sEmpty[i].equals(sTagName)) { return true; }
        }
        return false;
    }
	
    // Write nodes; we only need element, text and comment nodes
    private void write(Node node, int nLevel, OutputStreamWriter osw) throws IOException {
        short nType = node.getNodeType();
        switch (nType) {
            case Node.ELEMENT_NODE:
                if (isEmpty(node.getNodeName())) {
                    // This node must be empty, we ignore childnodes
                    if (nLevel>=0) { writeSpaces(nLevel,osw); }
                    osw.write("<"+node.getNodeName());
                    writeAttributes(node,osw);
                    osw.write(" />");
                    if (nLevel>=0) { osw.write("\n"); }
                }
                else if (node.hasChildNodes()) {
                    // Block pretty print from this node?
                    NodeList list = node.getChildNodes();
                    int nLen = list.getLength();
                    boolean bBlockPrettyPrint = false;
                    if (nLevel>=0) {
                        for (int i = 0; i < nLen; i++) {
                            bBlockPrettyPrint |= list.item(i).getNodeType()==Node.TEXT_NODE;
                        }
                    }
                    // Print start tag
                    if (nLevel>=0) { writeSpaces(nLevel,osw); }
                    osw.write("<"+node.getNodeName());
                    writeAttributes(node,osw);
                    osw.write(">");
                    if (nLevel>=0 && !bBlockPrettyPrint) { osw.write("\n"); }
                    // Print children
                    for (int i = 0; i < nLen; i++) {
                        int nNextLevel;
                        if (bBlockPrettyPrint || nLevel<0) { nNextLevel=-1; }
                        else { nNextLevel=nLevel+1; }
                        write(list.item(i),nNextLevel,osw);
                    }
                    // Print end tag
                    if (nLevel>=0 && !bBlockPrettyPrint) { writeSpaces(nLevel,osw); }
                    osw.write("</"+node.getNodeName()+">");
                    if (nLevel>=0) { osw.write("\n"); }
                }
                else { // empty element
                    if (nLevel>=0) { writeSpaces(nLevel,osw); }
                    osw.write("<"+node.getNodeName());
                    writeAttributes(node,osw);
                    // HTML compatibility: use end-tag even if empty
                    if (nType<=XHTML11) {
                        osw.write("></"+node.getNodeName()+">");
                    }
                    else {
                        osw.write(" />");
                    }
                    if (nLevel>=0) { osw.write("\n"); }
                }
                break;
            case Node.TEXT_NODE:
                write(node.getNodeValue(),osw);
                break;
            case Node.COMMENT_NODE:
                if (nLevel>=0) { writeSpaces(nLevel,osw); }
                osw.write("<!-- ");
                write(node.getNodeValue(),osw);
                osw.write(" -->");
                if (nLevel>=0) { osw.write("\n"); }
        }
    }
	
    private void writeAttributes(Node node, OutputStreamWriter osw) throws IOException {
        NamedNodeMap attr = node.getAttributes();
        int nLen = attr.getLength();
        for (int i=0; i<nLen; i++) {
            Node item = attr.item(i);
            osw.write(" ");
            write(item.getNodeName(),osw);
            osw.write("=\"");
            write(item.getNodeValue(),osw);
            osw.write("\"");
        }
    }

    private void writeSpaces(int nCount, OutputStreamWriter osw) throws IOException {
        for (int i=0; i<nCount; i++) { osw.write("  "); }
    }
	
    private void write(String s, OutputStreamWriter osw) throws IOException {
        int nLen = s.length();
        char c;
        for (int i=0; i<nLen; i++) {
            c = s.charAt(i);
            switch (c) {
                case ('<'): osw.write("&lt;"); break;
                case ('>'): osw.write("&gt;"); break;
                case ('&'): osw.write("&amp;"); break;
                case ('"'): osw.write("&quot;"); break;
                case ('\''): osw.write( nType == XHTML10 ? "&#39;" : "&apos;"); break;
                default: osw.write(c);
            }
        }
    }

}








