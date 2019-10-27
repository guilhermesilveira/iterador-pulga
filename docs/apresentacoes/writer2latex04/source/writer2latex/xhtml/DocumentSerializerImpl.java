/************************************************************************
 *
 *  DocumentSerializerImpl.java
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

import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import org.openoffice.xmerge.converter.dom.DOMDocument;
import org.openoffice.xmerge.converter.xml.OfficeDocument;
import org.openoffice.xmerge.converter.xml.sxc.SxcDocument;
import org.openoffice.xmerge.converter.xml.EmbeddedObject;
import org.openoffice.xmerge.ConvertData;
import org.openoffice.xmerge.ConvertException;
import org.openoffice.xmerge.DocumentSerializer;

import writer2latex.Application;
import writer2latex.util.*;
import writer2latex.xmerge.SxiDocument;
import writer2latex.office.*;

/**
 * <p>Helper class (a struct) to contain information about a Link.</p>
 */
final class LinkDescriptor {
    Element element; // the a-element
    String sId; // the id to link to
    int nIndex; // the index of *this* file
}

/**
 * <p>XHTML implementation of <code>DocumentDeserializer</code> 
 * for use by {@link
 * writer2latex.xhtml.PluginFactoryImpl
 * PluginFactoryImpl}.</p>
 *
 * <p>This converts an OpenOffice.org XML file to an XHTML(+MathML) document<.</p>
 *
 */
public class DocumentSerializerImpl implements DocumentSerializer {

    // The configuration
    private Config config;
	
    // The locale
    private L10n l10n;
                        
    // The source document
    private OfficeDocument oooDoc;
    private Document styleDOM;
    private Document contentDOM;
    private WriterStyleCollection wsc;
    private MetaData metaData;
    private ImageLoader imageLoader;
    private boolean bWriter;
    private boolean bCalc;
    private boolean bImpress;
	
    // The helpers
    private StyleConverter styleCv;
    private TextConverter textCv;
    private TableConverter tableCv;
    private DrawConverter drawCv;
    private MathConverter mathCv;

    // The xhtml output file(s)
    private String sOutFileName;
    private ConvertData cd;
    Vector outFiles = new Vector();
    protected int nType = XhtmlDocument.XHTML10; // the doctype
    private int nOutFileIndex = -1;
    private XhtmlDocument htmlDoc;
    private Document htmlDOM;
    private Element htmlHead;
    private Element htmlBody;
    private Element htmlBodyContent;

    // Hyperlinks
    Hashtable targets = new Hashtable();
    LinkedList links = new LinkedList();
    // Strip illegal characters from internal hyperlink targets
    private ExportNameCollection targetNames = new ExportNameCollection(true);
		
    /**
     *  <p>Initialises a new <code>DocumentSerializerImpl</code> using the.<br>
     *     supplied <code>Document</code></p>
     *
     * <p>The supplied document should be an {@link
     *    org.openoffice.xmerge.converter.xml.OfficeDocument OfficeDocument}
     *    object.</p>
     *
     *  @param  document  The <code>Document</code> to convert.    
     */
    public DocumentSerializerImpl(org.openoffice.xmerge.Document doc, int nType,
        String sConfigFileName) {
        // Get configuration
        config = new Config();
        if (sConfigFileName!=null) {
            File f = new File(sConfigFileName);
            if (f.exists()) {
                config.read(sConfigFileName);
            }
        }
        oooDoc = (OfficeDocument) doc;
        sOutFileName = oooDoc.getName();
        bCalc = oooDoc instanceof SxcDocument;
        bImpress = oooDoc instanceof SxiDocument;
        bWriter = !(bCalc || bImpress);
        this.nType = nType;
        // Load style info
        styleDOM = oooDoc.getStyleDOM();
        contentDOM = oooDoc.getContentDOM();
        wsc = new WriterStyleCollection();
        wsc.loadStylesFromDOM(styleDOM,contentDOM);
    }
	
    public void setOutFileName(String s) {
        sOutFileName = Misc.trimDocumentName(s,XhtmlDocument.getExtension(nType));
    }
	
    protected ImageLoader getImageLoader() { return imageLoader; }
	
    protected StyleConverter getStyleCv() { return styleCv; }

    protected TextConverter getTextCv() { return textCv; }
	
    protected TableConverter getTableCv() { return tableCv; }

    protected DrawConverter getDrawCv() { return drawCv; }

    protected MathConverter getMathCv() { return mathCv; }
	
    protected EmbeddedObject getEmbeddedObject(String sHref) { return oooDoc.getEmbeddedObject(sHref); }
	
    protected boolean isWriter() { return bWriter; }

    protected boolean isCalc() { return bCalc; }

    protected boolean isImpress() { return bImpress; }
	
    protected int getOutFileIndex() { return nOutFileIndex; }
	
    protected void addDocument(org.openoffice.xmerge.Document doc) { cd.addDocument(doc); }
	
    protected Element createElement(String s) { return htmlDOM.createElement(s); }
	
    protected Text createTextNode(String s) { return htmlDOM.createTextNode(s); }
	
    protected Node importNode(Node node, boolean bDeep) { return htmlDOM.importNode(node,bDeep); }
	
    /**
     *  <p>Convert the data passed into the <code>DocumentSerializerImpl</code>
     *  constructor into XHTML format.</p>
     *
     *  <p>This method may or may not be thread-safe.  It is expected
     *  that the user code does not call this method in more than one
     *  thread.  And for most cases, this method is only done once.</p>
     *
     *  @return  <code>ConvertData</code> object to pass back the
     *           converted data.
     *
     *  @throws  ConvertException  If any conversion error occurs.
     *  @throws  IOException       If any I/O error occurs.
     */
    public ConvertData serialize() throws IOException, ConvertException {      

        l10n = new L10n();
		
        metaData = new MetaData(oooDoc);
        imageLoader = new ImageLoader(oooDoc,sOutFileName,true);
        styleCv = new StyleConverter(wsc,config,this,nType);
        textCv = new TextConverter(wsc,config,this);
        tableCv = new TableConverter(wsc,config,this);
        drawCv = new DrawConverter(wsc,config,this);
        mathCv = new MathConverter(wsc,config,this,nType!=XhtmlDocument.XHTML10);

        cd = new ConvertData();
		
        // Set locale to document language
        StyleWithProperties style = bCalc ? wsc.getDefaultCellStyle() : wsc.getDefaultParStyle();
        if (style!=null) {
            String sLang = style.getProperty(XMLString.FO_LANGUAGE);
            String sCountry = style.getProperty(XMLString.FO_COUNTRY);
            if (sLang!=null) {
                if (sCountry==null) { l10n.setLocale(sLang); }
                else { l10n.setLocale(sLang+"-"+sCountry); }
            }
        }

		NodeList list;
        // Traverse the body
        list = contentDOM.getElementsByTagName(XMLString.OFFICE_BODY);
        int nLen = list.getLength();
        if (nLen > 0) {
            Element body = (Element) list.item(0);
            if (bCalc) { tableCv.convertTableContent(body); }
            else if (bImpress) { drawCv.convertDrawContent(body); }
            else { textCv.convertTextContent(body); }
        }
		
        // Add footnotes and endnotes
        textCv.insertFootnotes(htmlBody);
        textCv.insertEndnotes(htmlBody);

        // Resolve links
        ListIterator iter = links.listIterator();
        while (iter.hasNext()) {
            LinkDescriptor ld = (LinkDescriptor) iter.next();
            Integer targetIndex = (Integer) targets.get(ld.sId);
            if (targetIndex!=null) {
                int nTargetIndex = targetIndex.intValue();
                if (nTargetIndex == ld.nIndex) { // same file
                    ld.element.setAttribute("href","#"+targetNames.getExportName(ld.sId));
                }
                else {
                    ld.element.setAttribute("href",getOutFileName(nTargetIndex,true)
                                            +"#"+targetNames.getExportName(ld.sId));
                }
            }
        }

        // Export styles (temp.)
        if (!config.xhtmlIgnoreStyles()) {
            for (int i=0; i<=nOutFileIndex; i++) {
                Document dom = ((XhtmlDocument) outFiles.get(i)).getContentDOM();
                NodeList hlist = dom.getElementsByTagName("head");
                hlist.item(0).appendChild(styleCv.exportStyles(dom));
            }
        }
        
        // Create headers & footers
        if (bCalc) {
            for (int i=0; i<=nOutFileIndex; i++) {
                Document dom = ((XhtmlDocument) outFiles.get(i)).getContentDOM();
                NodeList hlist = dom.getElementsByTagName("div");
                Node header = hlist.item(0);
                Node footer = hlist.item(hlist.getLength()-1);
                // Add title in header:
                String sTitle = metaData.getTitle();
                if (sTitle!=null) {
                    Element title = dom.createElement("h1");
                    header.appendChild(title);
                    title.appendChild(dom.createTextNode(sTitle));
                }
                // Add links to all sheets: 
                Element headerPar = dom.createElement("p");
                header.appendChild(headerPar);
                header.appendChild(dom.createElement("hr"));
                footer.appendChild(dom.createElement("hr"));
                Element footerPar = dom.createElement("p");
                footer.appendChild(footerPar);
                int nSheets = tableCv.sheetNames.size();
                for (int j=0; j<nSheets; j++) {
                    if (config.xhtmlCalcSplit()) {
                        addNavigationLink(dom,headerPar,(String) tableCv.sheetNames.get(j),j);
                        addNavigationLink(dom,footerPar,(String) tableCv.sheetNames.get(j),j);
                    }
                    else {
                        addInternalNavigationLink(dom,headerPar,(String) tableCv.sheetNames.get(j),"tableheading"+j);
                        addInternalNavigationLink(dom,footerPar,(String) tableCv.sheetNames.get(j),"tableheading"+j);
	                }
                }
            }
        }
        else if (bImpress || config.getXhtmlSplitLevel()>0) {
            for (int i=0; i<=nOutFileIndex; i++) {
                Document dom = ((XhtmlDocument) outFiles.get(i)).getContentDOM();
                NodeList hlist = dom.getElementsByTagName("div");
                Node header = hlist.item(0);
                Node footer = hlist.item(hlist.getLength()-1);
                addNavigationLink(dom,header,l10n.get(L10n.FIRST),0);
                addNavigationLink(dom,header,l10n.get(L10n.PREVIOUS),i-1);
                addNavigationLink(dom,header,l10n.get(L10n.NEXT),i+1);
                addNavigationLink(dom,header,l10n.get(L10n.LAST),nOutFileIndex);
                if (textCv.getTocIndex()>=0) {
                    addNavigationLink(dom,header,l10n.get(L10n.CONTENTS),textCv.getTocIndex());
                }
                if (textCv.getAlphabeticalIndex()>=0) {
                    addNavigationLink(dom,header,l10n.get(L10n.INDEX),textCv.getAlphabeticalIndex());
                }
                header.appendChild(dom.createElement("hr"));
                footer.appendChild(dom.createElement("hr"));
                addNavigationLink(dom,footer,l10n.get(L10n.FIRST),0);
                addNavigationLink(dom,footer,l10n.get(L10n.PREVIOUS),i-1);
                addNavigationLink(dom,footer,l10n.get(L10n.NEXT),i+1);
                addNavigationLink(dom,footer,l10n.get(L10n.LAST),nOutFileIndex);
                if (textCv.getTocIndex()>=0) {
                    addNavigationLink(dom,footer,l10n.get(L10n.CONTENTS),textCv.getTocIndex());
                }
                if (textCv.getAlphabeticalIndex()>=0) {
                    addNavigationLink(dom,footer,l10n.get(L10n.INDEX),textCv.getAlphabeticalIndex());
                }
            }
        }

        return cd;
    }
	
    private void addNavigationLink(Document dom, Node node, String s, int nIndex) {
        if (nIndex>=0 && nIndex<=nOutFileIndex) {
            Element a = dom.createElement("a");
            a.setAttribute("href",getOutFileName(nIndex,true));
            a.appendChild(dom.createTextNode(s));
            node.appendChild(dom.createTextNode("["));
            node.appendChild(a);
            node.appendChild(dom.createTextNode("] "));
        }
        else {
            node.appendChild(dom.createTextNode("["+s+"] "));
        }
    }
	
    private void addInternalNavigationLink(Document dom, Node node, String s, String sLink) {
        Element a = dom.createElement("a");
        a.setAttribute("href","#"+sLink);
        a.appendChild(dom.createTextNode(s));
        node.appendChild(dom.createTextNode("["));
        node.appendChild(a);
        node.appendChild(dom.createTextNode("] "));
    }

 
    public void handleOfficeAnnotation(Node onode, Node hnode) {
        // Get the paragraph, and grab the contents
        Node paragraph = Misc.getChildByTagName(onode,XMLString.TEXT_P);
        if (paragraph!=null) {
            if (paragraph.hasChildNodes()) {
                StringBuffer buf = new StringBuffer();
                NodeList nl = paragraph.getChildNodes();
                int nLen = nl.getLength();
                for (int i=0; i<nLen; i++) {
                    if (nl.item(i).getNodeType()==Node.TEXT_NODE) {
                        buf.append(nl.item(i).getNodeValue());
                    }
                }
                Node commentNode = htmlDOM.createComment(buf.toString());
                hnode.appendChild(commentNode);
            }
        }
    }
	
    /////////////////////////////////////////////////////////////////////////
    // UTILITY METHODS
	
    // Create output file name (docname.html, docname1.html, docname2.html etc.)
    private String getOutFileName(int nIndex, boolean bWithExt) {
        return sOutFileName + (nIndex>0 ? Integer.toString(nIndex) : "") 
                            + (bWithExt ? htmlDoc.getFileExtension() : "");
    }
	
    // Prepare next output file
    public Element nextOutFile() {
        if (nOutFileIndex>=0) { textCv.insertFootnotes(htmlBodyContent); }
        htmlDoc = new XhtmlDocument(getOutFileName(++nOutFileIndex,false),nType);
        htmlDoc.setNoDoctype(config.xhtmlNoDoctype());
        outFiles.add(nOutFileIndex,htmlDoc);
        cd.addDocument(htmlDoc);
        
        // Create head + body
        htmlDOM = htmlDoc.getContentDOM();
        Element rootElement = htmlDOM.getDocumentElement();
        styleCv.applyDefaultLanguage(rootElement);
        rootElement.appendChild(htmlDOM.createComment(
             "This file was converted to xhtml by "
             + (bWriter ? "Writer" : (bCalc ? "Calc" : "Impress"))
             + "2xhtml ver. " + Application.getVersion() + 
             ". See http://www.hj-gym.dk/~hj/writer2latex for more info."));
        htmlHead = htmlDOM.createElement("head");
        rootElement.appendChild(htmlHead);
        htmlBody = htmlDOM.createElement("body");
        rootElement.appendChild(htmlBody);
		
        // Apply page formatting (using first master page)
        styleCv.applyPageStyle(wsc.getFirstMasterPageName(),htmlBody);

        // Add title (required by xhtml)
        Element title = htmlDOM.createElement("title");
        htmlHead.appendChild(title);
        String sTitle = metaData.getTitle();
        if (sTitle==null) { // use filename as fallback
            sTitle = htmlDoc.getFileName();
        }
        title.appendChild( htmlDOM.createTextNode(sTitle) );

        // Declare charset (we need this for xhtml because we have no <?xml ... ?>)
        if (nType==XhtmlDocument.XHTML10) {
            Element meta = htmlDOM.createElement("meta");
            meta.setAttribute("http-equiv","Content-Type");
            meta.setAttribute("content","text/html; charset=utf-8");
            htmlHead.appendChild(meta);
        }
		
        // "Traditional" meta data
        //createMeta("generator","Writer2LaTeX "+Misc.VERSION);
        createMeta("description",metaData.getDescription());
        createMeta("keywords",metaData.getKeywords());

        // Dublin core meta data (optional)
        // Format as recommended on dublincore.org
        // Declare meta data profile
        if (config.xhtmlUseDublinCore()) {
            htmlHead.setAttribute("profile","http://dublincore.org/documents/dcq-html/");
            // Add link to declare namespace
            Element dclink = htmlDOM.createElement("link");
            dclink.setAttribute("rel","schema.DC");
            dclink.setAttribute("href","http://purl.org/dc/elements/1.1/");
            htmlHead.appendChild(dclink);
            // Insert the actual meta data
            createMeta("DC.title",metaData.getTitle());
            createMeta("DC.subject",metaData.getSubject());
            createMeta("DC.description",metaData.getDescription());
            createMeta("DC.creator",metaData.getCreator());
            createMeta("DC.date",metaData.getDate());
            createMeta("DC.language",metaData.getLanguage());
        }
        
        // Add link to stylesheet
        if (config.xhtmlCustomStylesheet().length()>0) {
            Element htmlStyle = htmlDOM.createElement("link");
            htmlStyle.setAttribute("rel","stylesheet");
            htmlStyle.setAttribute("type","text/css");
            htmlStyle.setAttribute("media","all");
            htmlStyle.setAttribute("href",config.xhtmlCustomStylesheet());
            htmlHead.appendChild(htmlStyle);
        }
        /* later....
        if (nSplit>0 && !config.xhtmlIgnoreStyles()) {
            Element htmlStyle = htmlDOM.createElement("link");
            htmlStyle.setAttribute("rel","stylesheet");
            htmlStyle.setAttribute("type","text/css");
            htmlStyle.setAttribute("media","all");
            htmlStyle.setAttribute("href",oooDoc.getName()+"-styles.css");
            htmlHead.appendChild(htmlStyle);
        }*/
        // Note: For single output file, styles are exported to the doc at the end.

        // Create header+footer in case of calc, impress or multiple output files:
        if (bCalc || bImpress || config.getXhtmlSplitLevel()>=1) {
            Element header = htmlDOM.createElement("div");
            header.setAttribute("id","header");
            htmlBody.appendChild(header);

            htmlBodyContent = htmlDOM.createElement("div");
            htmlBodyContent.setAttribute("id","content");
            htmlBody.appendChild(htmlBodyContent);

            Element footer = htmlDOM.createElement("div");
            footer.setAttribute("id","footer");
            htmlBody.appendChild(footer);
        }
        else {
            htmlBodyContent = htmlBody;
        }
        // Recreate nested sections, if any
        if (!textCv.sections.isEmpty()) {
            Iterator iter = textCv.sections.iterator();
            while (iter.hasNext()) {
                Element section = (Element) iter.next();
                String sStyleName = Misc.getAttribute(section,XMLString.TEXT_STYLE_NAME);
                Element div = htmlDOM.createElement("div");
                htmlBodyContent.appendChild(div);
                htmlBodyContent = div;
                styleCv.applySectionStyle(sStyleName,div);
            }
        }
        
        return htmlBodyContent;
    }
	
	
    // create a target
    public Element createTarget(String sId) {
        Element a = htmlDOM.createElement("a");
        a.setAttribute("id",targetNames.getExportName(sId));
        targets.put(sId, new Integer(nOutFileIndex));
        return a;
    }
	
    // put a target id on an existing element
    public void addTarget(Element node,String sId) {
        node.setAttribute("id",targetNames.getExportName(sId));
        targets.put(sId, new Integer(nOutFileIndex));
    }

    // create a link
    public Element createLink(String sId) {
        Element a = htmlDOM.createElement("a");
        LinkDescriptor ld = new LinkDescriptor();
        ld.element = a; ld.sId = sId; ld.nIndex = nOutFileIndex;
        links.add(ld);
        return a;
    }
	
    private void createMeta(String sName, String sValue) {
        if (sValue==null) { return; }
        Element meta = htmlDOM.createElement("meta");
        meta.setAttribute("name",sName);
        meta.setAttribute("content",sValue);
        htmlHead.appendChild(meta);
    }
	
		
}