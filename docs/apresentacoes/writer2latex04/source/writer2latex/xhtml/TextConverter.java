/************************************************************************
 *
 *  TextConverter.java
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
 *  Copyright: 2002-2005 by Henrik Just
 *
 *  All Rights Reserved.
 * 
 *  Version 0.3.3j (2005-01-30)
 *
 */

package writer2latex.xhtml;

import java.util.Stack;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Locale;

import java.text.Collator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import writer2latex.util.Misc;
import writer2latex.util.Config;
import writer2latex.office.XMLString;
import writer2latex.office.IndexMark;
import writer2latex.office.ListCounter;
import writer2latex.office.PropertySet;
import writer2latex.office.StyleWithProperties;
import writer2latex.office.ParStyle;
import writer2latex.office.WriterStyleCollection;

/**
 * <p>Helper class (a struct) to contain information about an alphabetical index entry.</p>
 */
final class AlphabeticalEntry {
    String sWord; // the word for the index
    int nIndex; // the original index of this entry
}

public class TextConverter extends ConverterHelper {
    // At which outline level should we split files (0=no split)?
    int nSplit = 0;
    private int nTocIndex = -1;
    private int nAlphabeticalIndex = -1;
    private int nDontSplitLevel = 0; // if > 0 splitting is forbidden
    private ListCounter outlineNumbering;
    private int nHeadingCount=0; // used to create hypertargets in heading (used by toc)
    protected Stack sections = new Stack(); // stack of nested sections TODO: Accessor methods
    private boolean bInToc = false;
    // Alphabetical index
    Vector index = new Vector();
    int nIndexIndex = -1;
	
    private String sFntCitBodyStyle = null;
    private String sFntCitStyle = null;
    private String sEntCitBodyStyle = null;
    private String sEntCitStyle = null;

    // The collection of all headings (used for toc)
    NodeList headings = null;
    // Gather the footnotes and endnotes
    private LinkedList footnotes = new LinkedList();
    private LinkedList endnotes = new LinkedList();
    // Sometimes we have to create an inlinenode in a block context
    // We put it here and insert it in the first paragraph/heading to come:
    private Node asapNode = null;

    public TextConverter(WriterStyleCollection wsc, Config config, DocumentSerializerImpl converter) {
        super(wsc,config,converter);
        nSplit = config.getXhtmlSplitLevel();
        outlineNumbering = new ListCounter(wsc.getOutlineStyle());
        // Styles for footnotes and endnotes
        PropertySet notes = wsc.getFootnotesConfiguration();
        if (notes!=null) {
            sFntCitBodyStyle = notes.getProperty(XMLString.TEXT_CITATION_BODY_STYLE_NAME);
            sFntCitStyle = notes.getProperty(XMLString.TEXT_CITATION_STYLE_NAME);
        }
        notes = wsc.getEndnotesConfiguration();
        if (notes!=null) {
            sEntCitBodyStyle = notes.getProperty(XMLString.TEXT_CITATION_BODY_STYLE_NAME);
            sEntCitStyle = notes.getProperty(XMLString.TEXT_CITATION_STYLE_NAME);
        }
    }
	
    /** Converts an office node as a complete text document
     *
     *  @param onode the Office node containing the content to convert
     */
    public void convertTextContent(Element onode) {
        // Collect all headings (for toc)
        headings = onode.getElementsByTagName(XMLString.TEXT_H);
        traverseBlockText(onode,converter.nextOutFile());
    }

    protected int getTocIndex() { return nTocIndex; }
	
    protected int getAlphabeticalIndex() { return nAlphabeticalIndex; }
	
    ////////////////////////////////////////////////////////////////////////
    // BLOCK TEXT (returns current html node at end of block)

    public Node traverseBlockText(Node onode, Node hnode) {
        return traverseBlockText(onode,0,null,hnode);
    } 
	
    private Node traverseBlockText(Node onode, int nLevel, String styleName, Node hnode) {
        if (!onode.hasChildNodes()) { return hnode; }
        boolean bAfterHeading = false;
        NodeList nList = onode.getChildNodes();
        int nLen = nList.getLength();
        int i = 0;
        while (i < nLen) {
            Node child = nList.item(i);
            
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = child.getNodeName();
                // Anything but a section blocks splitting
                if (!nodeName.equals(XMLString.TEXT_SECTION)) { nDontSplitLevel++; }
                
                if (nodeName.startsWith("draw:")) {
                    getDrawCv().handleDrawElement(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_P)) {
                    // is there a block element, we should use?
                    String sStyleName = Misc.getAttribute(child,XMLString.TEXT_STYLE_NAME);
                    if (sStyleName!=null) {
                        StyleWithProperties style = wsc.getParStyle(sStyleName);
                        if (style!=null && style.isAutomatic()) {
                            sStyleName = style.getParentName();
                        }
                    }
                    XhtmlStyleMap xpar = config.getXParStyleMap();
                    if (sStyleName!=null && xpar.contains(sStyleName)) {
                        Node curHnode = hnode;
                        String sBlockElement = xpar.getBlockElement(sStyleName);
                        String sBlockCss = xpar.getBlockCss(sStyleName);
                        if (xpar.getBlockElement(sStyleName).length()>0) {
                            Element block = converter.createElement(xpar.getBlockElement(sStyleName));
                            if (!"(none)".equals(xpar.getBlockCss(sStyleName))) {
                                block.setAttribute("class",xpar.getBlockCss(sStyleName));
                            }
                            hnode.appendChild(block);
                            curHnode = block;
                        }
                        boolean bMoreParagraphs = true;
                        do {
                            handleParagraph(child,curHnode);
                            bMoreParagraphs = false;
                            if (++i<nLen) {
                                child = nList.item(i);
                                nodeName = child.getNodeName();
                                if (nodeName.equals(XMLString.TEXT_P)) {
                                    String sCurStyleName = Misc.getAttribute(child,XMLString.TEXT_STYLE_NAME);
                                    if (sCurStyleName!=null) {
                                        StyleWithProperties curStyle = wsc.getParStyle(sCurStyleName);
                                        if (curStyle!=null && curStyle.isAutomatic()) {
                                            sCurStyleName = curStyle.getParentName();
                                        }
                                    }
                                    if (sCurStyleName!=null && xpar.contains(sCurStyleName)) {
                                        if (sBlockElement.equals(xpar.getBlockElement(sCurStyleName)) &&
	                                        sBlockCss.equals(xpar.getBlockCss(sCurStyleName))) {
                                            bMoreParagraphs = true;
                                         }
                                    }
                                }
                            }
                        } while (bMoreParagraphs);
                        i--;
                    }
                    else {
                        handleParagraph(child,hnode);
                    }
                }
                else if(nodeName.equals(XMLString.TEXT_H)) {
                    int nOutlineLevel = Misc.getPosInteger(Misc.getAttribute(child, XMLString.TEXT_LEVEL),1);
                    hnode = maybeSplit(hnode,nOutlineLevel,bAfterHeading);
                    handleHeading(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_UNORDERED_LIST)) {
                    handleUL(child,nLevel+1,styleName,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_ORDERED_LIST)) {
                    handleOL(child,nLevel+1,styleName,hnode);
                }
                else if (nodeName.equals(XMLString.TABLE_TABLE)) {
                    getTableCv().handleTable(child,hnode);
                }
                else if (nodeName.equals(XMLString.TABLE_SUB_TABLE)) {
                    getTableCv().handleTable(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_SECTION)) {
                    hnode = handleSection(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_TABLE_OF_CONTENT)) {
                    hnode = maybeSplit(hnode,1,bAfterHeading);
                    handleTOC(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_ILLUSTRATION_INDEX)) {
                    handleLOF(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_TABLE_INDEX)) {
                    handleLOT(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_OBJECT_INDEX)) {
                    handleObjectIndex(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_USER_INDEX)) {
                    handleUserIndex(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_ALPHABETICAL_INDEX)) {
                    hnode = maybeSplit(hnode,1,bAfterHeading);
                    handleAlphabeticalIndex(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_BIBLIOGRAPHY)) {
                    hnode = maybeSplit(hnode,1,bAfterHeading);
                    handleBibliography(child,hnode);
                }
                else if (nodeName.equals(XMLString.TEXT_SEQUENCE_DECLS)) {
                    //handleSeqeuenceDecls(child);
                }
                // Reenable splitting
                if (!nodeName.equals(XMLString.TEXT_SECTION)) { nDontSplitLevel--; }
                // Remember if this was a heading
                bAfterHeading = nodeName.equals(XMLString.TEXT_H);
            }
            i++;
        }
        return hnode;
    }
    
    private Node maybeSplit(Node node, int nLevel, boolean bAfterHeading) {
        if (bAfterHeading || nDontSplitLevel>1) { return node; }
        if (nSplit>=nLevel) { return converter.nextOutFile(); }
        return node;
    }

    /* Process a text:section tag (returns current html node) */
    private Node handleSection(Node onode, Node hnode) {
        String sName = Misc.getAttribute(onode,XMLString.TEXT_NAME);
        String sStyleName = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
        Element div = converter.createElement("div");
        hnode.appendChild(div);
        converter.addTarget(div,sName+"%7Cregion");
        getStyleCv().applySectionStyle(sStyleName,div);
        sections.push(onode);
        Node newhnode = traverseBlockText(onode, div);
        sections.pop();
        return newhnode.getParentNode();
    }

    /*
     * Process a text:h tag
     */
    private void handleHeading(Node onode, Node hnode) {
        // Numbering: It is possible to define outline numbering in CSS2
        // using counters; but this is not supported by Mozilla 1.0.
        // TODO: Offer CSS2 solution as an alternative later.

        // Note: Conditional styles are not supported
        int nLevel = Misc.getPosInteger(Misc.getAttribute(onode, XMLString.TEXT_LEVEL),1);
        if (nLevel<=6) {
			// add Hx element
            Element heading = converter.createElement("h"+nLevel);
            hnode.appendChild(heading);
            // Apply writing direction
            String sStyleName = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
            StyleWithProperties style = wsc.getParStyle(sStyleName);
            if (style!=null) { getStyleCv().applyDirection(style,heading); }

            getStyleCv().setHeadingStyle(nLevel,sStyleName);
			
            // Prepend hyperlink target (from toc):
            if (!bInToc) {
                //heading.appendChild(converter.createTarget("heading"+(++nHeadingCount)));
                converter.addTarget(heading,"heading"+(++nHeadingCount));
            }

            // Prepend asapNode
            prependAsapNode(heading);
			
            // Prepend numbering
            heading.appendChild( converter.createTextNode(outlineNumbering.step(nLevel).getLabel()) );
			
            traverseInlineText(onode,heading);
        }
        else { // beyond h6 - export as ordinary paragraph
            handleParagraph(onode,hnode);
        }
    }

    /*
     * Process a text:p tag
     */
    private void handleParagraph(Node onode, Node hnode) {
        if (config.ignoreEmptyParagraphs() && !onode.hasChildNodes()) { return; }
        Element par;
        if (converter.isCalc()) { // attach inline text directly to parent (always a table cell)
            par = (Element) hnode;
        }
        else {
            String sStyleName = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
            par = createParagraph((Element) hnode, sStyleName);
        }
        prependAsapNode(par);
        if (onode.hasChildNodes()) {
            traverseInlineText(onode,par);
        }
        else {
            par.appendChild( converter.createTextNode("\u00A0") );
        }        
    }
    
    private void prependAsapNode(Node node) {
        if (asapNode!=null) {
            // May float past a split; check this first
            if (asapNode.getOwnerDocument()!=node.getOwnerDocument()) {
                asapNode = converter.importNode(asapNode,true);
            }
            node.appendChild(asapNode); asapNode = null;
        }
    }

    /*
     * Process a text:ordered-list tag.  
     */
    private void handleOL (Node onode, int nLevel, String sStyleName, Node hnode) {
	    // add an OL element
        String sStyleName1 = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
        if (sStyleName1!=null) { sStyleName = sStyleName1; }
        Element list = converter.createElement("ol");
        applyListStyle(nLevel,sStyleName,list);
        hnode.appendChild(list);
        traverseList(onode,nLevel,sStyleName,list);
    }

    /*
     * Process a text:unordered-list tag.  
     */
    private void handleUL (Node onode, int nLevel, String sStyleName, Node hnode) {
        // add an UL element
        String sStyleName1 = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
        if (sStyleName1!=null) { sStyleName = sStyleName1; }
        Element list = converter.createElement("ul");
        applyListStyle(nLevel,sStyleName,list);
        hnode.appendChild(list);
        traverseList(onode,nLevel,sStyleName,list);
    }

    /*
     * Process the contents of a list
     */
    private void traverseList (Node onode, int nLevel, String styleName, Node hnode) {
        if (onode.hasChildNodes()) {
            NodeList nList = onode.getChildNodes();
            int len = nList.getLength();
            
            for (int i = 0; i < len; i++) {
                Node child = nList.item(i);
                
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = child.getNodeName();
                    
                    if (nodeName.equals(XMLString.TEXT_LIST_ITEM)) {
					    // add an li element
                        Element item = converter.createElement("li");
                        hnode.appendChild(item);
                        boolean bRestart = "true".equals(Misc.getAttribute(child,
                            XMLString.TEXT_RESTART_NUMBERING));
                        int nStartValue = Misc.getPosInteger(Misc.getAttribute(child,
                            XMLString.TEXT_START_VALUE),1);
                        if (bRestart) {
                           item.setAttribute("value",Integer.toString(nStartValue));
                        }
                        traverseListItem(child,nLevel,styleName,item);
                    }
                    if (nodeName.equals(XMLString.TEXT_LIST_HEADER)) {
                        // add an li element
                        Element item = converter.createElement("li");
                        hnode.appendChild(item);
                        item.setAttribute("style","list-style-type:none");
                        traverseListItem(child,nLevel,styleName,item);
                    }
                }
            }
        }
    }
    
    
    /*
     * Process the contents of a list item
     * (a list header should only contain paragraphs, but we don't care)
     */
    private void traverseListItem (Node onode, int nLevel, String styleName, Node hnode) { 
        // First check if we have a single paragraph to be omitted
        // This should happen if we ignore styles and have no style-map
        // for the paragraph style used        
        if (config.xhtmlIgnoreStyles() && onode.hasChildNodes()) {
            NodeList list = onode.getChildNodes();
            int nLen = list.getLength();
            int nParCount = 0;
            boolean bNoPTag = true;
            for (int i=0; i<nLen; i++) {
                if (list.item(i).getNodeType()==Node.ELEMENT_NODE) {
                    if (list.item(i).getNodeName().equals(XMLString.TEXT_P)) {
                        nParCount++;
                        if (bNoPTag) {
                            String sStyleName = Misc.getAttribute(list.item(0),XMLString.TEXT_STYLE_NAME);
                            if (config.getXParStyleMap().contains(sStyleName)) {
                                bNoPTag = false;
                            }
                        }
                    }
                    else { // found non-text:p element
                        bNoPTag=false;
                    }
                }
            }
            if (bNoPTag && nParCount<=1) {
                // traverse the list
                for (int i = 0; i < nLen; i++) {
                    Node child = list.item(i);
          
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        String nodeName = child.getNodeName();
                    
                        if (nodeName.equals(XMLString.TEXT_P)) {
                            traverseInlineText(child,hnode);
                        }
                        if (nodeName.equals(XMLString.TEXT_ORDERED_LIST)) {
                            handleOL(child,nLevel+1,styleName,hnode);
                        }
                        if (nodeName.equals(XMLString.TEXT_UNORDERED_LIST)) {
                            handleUL(child,nLevel+1,styleName,hnode);
                        }
                    }
                }
                return;
            }
        }
        // Still here? - traverse block text as usual!
        traverseBlockText(onode,nLevel,styleName,hnode);
    }
		    
    /* Process table of contents
     */
    private void handleTOC (Node onode, Node hnode) {
        if (headings==null) { return; }
        nTocIndex = converter.getOutFileIndex();
        bInToc = true;
        Node source = Misc.getChildByTagName(onode,XMLString.TEXT_TABLE_OF_CONTENT_SOURCE);
        if (source!=null) {
            if ("chapter".equals(Misc.getAttribute(source,XMLString.TEXT_INDEX_SOURCE))) {
                // TODO
            }
            else { // generate toc
                Element div = converter.createElement("div");
                converter.addTarget(div,"tableofcontents");
                hnode.appendChild(div);
                // Generate title
                Node title = Misc.getChildByTagName(source,XMLString.TEXT_INDEX_TITLE_TEMPLATE);
                if (title!=null) {
                    String sStyleName = Misc.getAttribute(title,XMLString.TEXT_STYLE_NAME);
                    Element p = createParagraph(div,sStyleName);
                    traversePCDATA(title,p);
                }
                // Collect style names for entries
                // TODO: Should read the entire template
                String sEntryStyleName[] = new String[11];
                if (source.hasChildNodes()) {
                    NodeList nl = source.getChildNodes();
                    int nLen = nl.getLength();
                    for (int i = 0; i < nLen; i++) {
                        Node child = nl.item(i);
                        if (child.getNodeType() == Node.ELEMENT_NODE
                            && child.getNodeName().equals(XMLString.TEXT_TABLE_OF_CONTENT_ENTRY_TEMPLATE)) {
	                        int nLevel = Misc.getPosInteger(Misc.getAttribute(child,XMLString.TEXT_OUTLINE_LEVEL),1);
                            if (nLevel<=10) {
                                sEntryStyleName[nLevel] = Misc.getAttribute(child,XMLString.TEXT_STYLE_NAME);
	                        }
                        }
                    }	                        
                }
				
                // Generate entries
                int nMaxLevel = Misc.getPosInteger(Misc.getAttribute(source,XMLString.TEXT_OUTLINE_LEVEL),1);
                ListCounter counter = new ListCounter(wsc.getOutlineStyle());
                int nHeadingCount = 0;
                int nLen = headings.getLength();
                for (int i=0; i<nLen; i++) {
                    Node heading = headings.item(i);
                    int nLevel = Misc.getPosInteger(Misc.getAttribute(heading,XMLString.TEXT_LEVEL),1);
                    nHeadingCount++;
                    counter.step(nLevel);
                    if (nLevel<=nMaxLevel) {
                        Element p = createParagraph(div,sEntryStyleName[nLevel]);
                        p.appendChild(converter.createTextNode(counter.getLabel()));
                        Element a = converter.createLink("heading"+nHeadingCount);
                        p.appendChild(a);
                        traverseInlineText(heading,a);
                    }
                }
            }
        }
        bInToc = false;
    }

    /*
     * Process list of illustrations
     */
    private void handleLOF (Node onode, Node hnode) {
        // later
    }

    /*
     * Process list of tables
     */
    private void handleLOT (Node onode, Node hnode) {
        // later
    }

    /*
     * Process Object index
     */
    private void handleObjectIndex (Node onode, Node hnode) {
        // later
    }

    /*
     * Process User index
     */
    private void handleUserIndex (Node onode, Node hnode) {
        // later
    }

    /*
     * Process Alphabetical index
     */
    private void handleAlphabeticalIndex (Node onode, Node hnode) {
        nAlphabeticalIndex = converter.getOutFileIndex();
        Node source = Misc.getChildByTagName(onode,XMLString.TEXT_ALPHABETICAL_INDEX_SOURCE);
        if (source!=null) {
            Element div = converter.createElement("div");
            converter.addTarget(div,"alphabeticalindex");
            hnode.appendChild(div);
            // Generate title
            Node title = Misc.getChildByTagName(source,XMLString.TEXT_INDEX_TITLE_TEMPLATE);
            if (title!=null) {
                String sStyleName = Misc.getAttribute(title,XMLString.TEXT_STYLE_NAME);
                Element p = createParagraph(div,sStyleName);
                traversePCDATA(title,p);
            }
            // Collect style name for entries
            // TODO: Should read the entire template
            String sEntryStyleName = null;
            if (source.hasChildNodes()) {
                NodeList nl = source.getChildNodes();
                int nLen = nl.getLength();
                for (int i = 0; i < nLen; i++) {
                    Node child = nl.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE
                        && child.getNodeName().equals(XMLString.TEXT_ALPHABETICAL_INDEX_ENTRY_TEMPLATE)) {
		                // Note: There are actually three outline-levels: separator, 1, 2 and 3
                        int nLevel = Misc.getPosInteger(Misc.getAttribute(child,XMLString.TEXT_OUTLINE_LEVEL),1);
                        if (nLevel==1) {
                            sEntryStyleName = Misc.getAttribute(child,XMLString.TEXT_STYLE_NAME);
                        }
                    }	                        
                }
            }
            // Sort the index entries
            Collator collator;
            String sLanguage = Misc.getAttribute(source,XMLString.FO_LANGUAGE);
            if (sLanguage==null) { // use default locale
                collator = Collator.getInstance();
            }
            else {
                String sCountry = Misc.getAttribute(source,XMLString.FO_COUNTRY);
                if (sCountry==null) { sCountry=""; }
                collator = Collator.getInstance(new Locale(sLanguage,sCountry));
            }
            for (int i = 0; i<=nIndexIndex; i++) {
                for (int j = i+1; j<=nIndexIndex ; j++) {
                    AlphabeticalEntry entryi = (AlphabeticalEntry) index.get(i);
                    AlphabeticalEntry entryj = (AlphabeticalEntry) index.get(j);
                    if (collator.compare(entryi.sWord, entryj.sWord) > 0) {
                        index.set(i,entryj);
                        index.set(j,entryi);
                    }
                }
            }
            // Generate the index
            Element table = converter.createElement("table");
            table.setAttribute("style","width:100%");
            div.appendChild(table);
            Element tr = converter.createElement("tr");
            table.appendChild(tr);
            Element[] td = new Element[4];
            for (int i=0; i<4; i++) {
                td[i] = converter.createElement("td");
                td[i].setAttribute("style","vertical-align:top");
                tr.appendChild(td[i]);
            }
            int nColEntries = nIndexIndex/4+1;
            int nColIndex = -1;
            for (int i=0; i<=nIndexIndex; i++) {
                if (i%nColEntries==0) { nColIndex++; } 
                AlphabeticalEntry entry = (AlphabeticalEntry) index.get(i);
                Element p = createParagraph(td[nColIndex],sEntryStyleName);
                Element a = converter.createLink("idx"+entry.nIndex);
                p.appendChild(a);
                a.appendChild(converter.createTextNode(entry.sWord));
            }
        }
        
    }

    /*
     * Process Bibliography
     */
    private void handleBibliography (Node onode, Node hnode) {
        // Use the content, not the template
        // This is a temp. solution. Later we want to be able to create
        // hyperlinks from the bib-item to the actual entry in the bibliography,
        // so we have to recreate the bibliography from the template.
        Node body = Misc.getChildByTagName(onode,XMLString.TEXT_INDEX_BODY);
        if (body!=null) {
            Element div = converter.createElement("div");
            converter.addTarget(div,"bibliography");
            hnode.appendChild(div);
            //asapNode = converter.createTarget("bibliography");
            Node title = Misc.getChildByTagName(body,XMLString.TEXT_INDEX_TITLE);
            if (title!=null) { traverseBlockText(title,div); }
            traverseBlockText(body,div);
        }     
    }

    ////////////////////////////////////////////////////////////////////////
    // INLINE TEXT

    /*
     * Process inline text
     */
    private void traverseInlineText (Node onode,Node hnode) {        
        String styleName = Misc.getAttribute(onode, XMLString.TEXT_STYLE_NAME);
                              
        if (onode.hasChildNodes()) {
            NodeList nList = onode.getChildNodes();
            int nLen = nList.getLength();
                       
            for (int i = 0; i < nLen; i++) {
                
                Node child = nList.item(i);
                short nodeType = child.getNodeType();
               
                switch (nodeType) {
                    case Node.TEXT_NODE:
                        String s = child.getNodeValue();
                        if (s.length() > 0) {
                            hnode.appendChild( converter.createTextNode(s) );
                        }
                        break;
                        
                    case Node.ELEMENT_NODE:
                        String sName = child.getNodeName();
                        if (child.getNodeName().startsWith("draw:")) {
                            getDrawCv().handleDrawElement(child,hnode);
                        }
                        else if (child.getNodeName().equals(XMLString.TEXT_S)) {
                            if (config.ignoreDoubleSpaces()) {
                                hnode.appendChild( converter.createTextNode(" ") );
                            }
                            else {
                                int count= Misc.getPosInteger(Misc.getAttribute(child,XMLString.TEXT_C),1);
                                for ( ; count > 0; count--) {
                                    hnode.appendChild( converter.createTextNode("\u00A0") );
                                }
                            }
                        }
                        else if (sName.equals(XMLString.TEXT_TAB_STOP)) {
                            // xhtml does not have tab stops
                            hnode.appendChild( converter.createTextNode(" ") );
                        }
                        else if (sName.equals(XMLString.TEXT_LINE_BREAK)) {
                            if (!config.ignoreHardLineBreaks()) {
                                hnode.appendChild( converter.createElement("br") );
                            }
                        }
                        else if (sName.equals(XMLString.TEXT_SPAN)) {
                            handleSpan(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_A)) {
                            handleAnchor(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_FOOTNOTE)) {
                            handleFootnote(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_ENDNOTE)) {
                            handleEndnote(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_SEQUENCE)) {
	                        handleSequence(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_PAGE_NUMBER)) {
	                        handlePageNumber(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_PAGE_COUNT)) {
	                        handlePageCount(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_SEQUENCE_REF)) {
	                        handleSequenceRef(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_FOOTNOTE_REF)) {
	                        handleFootnoteRef(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_ENDNOTE_REF)) {
	                        handleEndnoteRef(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_REFERENCE_MARK)) {
	                        handleReferenceMark(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_REFERENCE_MARK_START)) {
	                        handleReferenceMark(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_REFERENCE_REF)) {
	                        handleReferenceRef(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_BOOKMARK)) {
	                        handleBookmark(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_BOOKMARK_START)) {
	                        handleBookmark(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_BOOKMARK_REF)) {
	                        handleBookmarkRef(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_ALPHABETICAL_INDEX_MARK)) {
	                        handleAlphabeticalIndexMark(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_ALPHABETICAL_INDEX_MARK_START)) {
	                        handleAlphabeticalIndexMarkStart(child,hnode);
                        }
                        else if (sName.equals(XMLString.TEXT_BIBLIOGRAPHY_MARK)) {
	                        handleBibliographyMark(child,hnode);
                        }
                        else if (sName.equals(XMLString.OFFICE_ANNOTATION)) {
                            converter.handleOfficeAnnotation(child,hnode);
                        }
						else if (sName.startsWith("text:")) {
							 traverseInlineText(child,hnode);
						}
                        // other tags are ignores;
                        break;
                    default:
                        // Do nothing
                }
            }
        }
    }
	
    private void handleSpan(Node onode, Node hnode) {
        String sStyleName = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
        Element span = createInline((Element) hnode,sStyleName);
        traverseInlineText (onode,span);
    }

    private void traversePCDATA(Node onode, Node hnode) {
        if (onode.hasChildNodes()) {
            NodeList nl = onode.getChildNodes();
            int nLen = nl.getLength();
            for (int i=0; i<nLen; i++) {
                if (nl.item(i).getNodeType()==Node.TEXT_NODE) {
                    hnode.appendChild( converter.createTextNode(nl.item(i).getNodeValue()) );
                }
            }
        }
    }
    
    protected void handleAnchor(Node onode, Node hnode) {
        // We use the same style to all anchors, even though they may have
        // individual styles assigned. Pass on the names to the style converter:
        // Note: OOo does not seem to use the style attributes?
        String sStyleName = Misc.getAttribute(onode,XMLString.TEXT_STYLE_NAME);
        String sVisitedStyleName = Misc.getAttribute(onode,XMLString.TEXT_VISITED_STYLE_NAME);
        getStyleCv().setAnchorStyle(sStyleName,sVisitedStyleName);
        // Now create the anchor:
        String sHref = Misc.getAttribute(onode,XMLString.XLINK_HREF);
        if (sHref!=null) {
            Element anchor;
            if (sHref.startsWith("#")) { // internal link
                anchor = converter.createLink(sHref.substring(1));
            }
            else { // external link
                anchor = converter.createElement("a");
                // Fix OOo problem:
                if (sHref.indexOf("?")==-1) { // No question mark
                    int n3F=sHref.indexOf("%3F");
                    if (n3F>0) { // encoded question mark
                        sHref = sHref.substring(0,n3F)+"?"+sHref.substring(n3F+3);
                    }
                }
                anchor.setAttribute("href",sHref);
            }
            hnode.appendChild(anchor);
            traverseInlineText(onode,anchor);
        }
        else {
            traverseInlineText(onode,hnode);
        }
    }

    /* Process a footnote */
    private void handleFootnote(Node onode, Node hnode) {
        String sId = Misc.getAttribute(onode,XMLString.TEXT_ID);
		Element span = createInline((Element) hnode,sFntCitBodyStyle);
        // Create target and back-link
        Element link = converter.createLink(sId);
        converter.addTarget(link,"body"+sId);
		span.appendChild(link);
        Node citation = Misc.getChildByTagName(onode,XMLString.TEXT_FOOTNOTE_CITATION);
        traversePCDATA(citation,link);
        footnotes.add(onode);
	} 
	
    public void insertFootnotes(Node hnode) {
        int n = footnotes.size();
        for (int i=0; i<n; i++) {
            Node footnote = (Node) footnotes.get(i);
            String sId = Misc.getAttribute(footnote,XMLString.TEXT_ID); 
            Node citation = Misc.getChildByTagName(footnote,XMLString.TEXT_FOOTNOTE_CITATION);
            Node body = Misc.getChildByTagName(footnote,XMLString.TEXT_FOOTNOTE_BODY);
            traverseNoteBody(sId,sFntCitStyle,citation,body,hnode);
        }
        footnotes.clear();
    }

    /* Process an endnote */
    private void handleEndnote(Node onode, Node hnode) {
        String sId = Misc.getAttribute(onode,XMLString.TEXT_ID);
		Element span = createInline((Element) hnode,sEntCitBodyStyle);
        // Create target and back-link
        Element link = converter.createLink(sId);
        converter.addTarget(link,"body"+sId);
		span.appendChild(link);
        Node citation = Misc.getChildByTagName(onode,XMLString.TEXT_ENDNOTE_CITATION);
        traversePCDATA(citation,link);
        endnotes.add(onode);
	} 

    public void insertEndnotes(Node hnode) {
        int n = endnotes.size();
        if (nSplit>0 && n>0) { hnode = converter.nextOutFile(); }
        for (int i=0; i<n; i++) {
            Node endnote = (Node) endnotes.get(i);
            String sId = Misc.getAttribute(endnote,XMLString.TEXT_ID); 
            Node citation = Misc.getChildByTagName(endnote,XMLString.TEXT_ENDNOTE_CITATION);
            Node body = Misc.getChildByTagName(endnote,XMLString.TEXT_ENDNOTE_BODY);
            traverseNoteBody(sId,sEntCitStyle,citation,body,hnode);
        }
    }

	/*
     * Process the contents of a footnote or endnote
     */
    private void traverseNoteBody (String sId, String sCitStyle, Node citation,Node onode, Node hnode) {
        // Create the anchor/footnote symbol:
        // Create target and link
        Element link = converter.createLink("body"+sId);
        converter.addTarget(link,sId);
        if (!config.xhtmlIgnoreStyles()) {
            getStyleCv().applyTextStyle(sCitStyle,link);
        }
        traversePCDATA(citation,link);
        // Add a space and save it for later insertion:
        Element span = converter.createElement("span");
        span.appendChild(link);
        span.appendChild(converter.createTextNode(" "));
        asapNode = span;
		
        traverseBlockText(onode,hnode);

        /*if (onode.hasChildNodes()) {
            NodeList nList = onode.getChildNodes();
            int len = nList.getLength();
            
            for (int i = 0; i < len; i++) {
                Node child = nList.item(i);
                
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = child.getNodeName();
                    
                    if (nodeName.equals(XMLString.TEXT_H)) {
                        handleHeading(child,hnode);
                    }

                    if (nodeName.equals(XMLString.TEXT_P)) {
                        handleParagraph(child,hnode);
                    }
                    
                    if (nodeName.equals(XMLString.TEXT_ORDERED_LIST)) {
                        handleOL(child,0,null,hnode);
                    }
                    
                    if (nodeName.equals(XMLString.TEXT_UNORDERED_LIST)) {
                        handleUL(child,0,null,hnode);
                    }
                }
            }
        }*/
    }

    private void handlePageNumber(Node onode, Node hnode) {
        // doesn't make any sense...
        hnode.appendChild( converter.createTextNode("(Page number)") );
    }
    
    private void handlePageCount(Node onode, Node hnode) {
       // also no sense
        hnode.appendChild( converter.createTextNode("(Page count)") );
    }

    private void handleSequence(Node onode, Node hnode) {
        // Use current value, but turn references into hyperlinks
        String sName = Misc.getAttribute(onode,XMLString.TEXT_REF_NAME);
        if (sName!=null && !bInToc) {
            Element anchor = converter.createTarget("seq"+sName);
            hnode.appendChild(anchor);
            traversePCDATA(onode,anchor);
        }
        else {
            traversePCDATA(onode,hnode);
        }        
    }
	
    private void createReference(Node onode, Node hnode, String sPrefix) {
        // Turn reference into hyperlink
        String sFormat = Misc.getAttribute(onode,XMLString.TEXT_REFERENCE_FORMAT);
        String sName = Misc.getAttribute(onode,XMLString.TEXT_REF_NAME);
        Element anchor = converter.createLink(sPrefix+sName);
        hnode.appendChild(anchor);
        if ("page".equals(sFormat)) { // all page numbers are 1 :-)
            anchor.appendChild( converter.createTextNode("1") );
        }
        else { // in other cases use current value
            traversePCDATA(onode,anchor);
        }
    }
		
    private void handleSequenceRef(Node onode, Node hnode) {
        createReference(onode,hnode,"seq");
    } 

    private void handleFootnoteRef(Node onode, Node hnode) {
        createReference(onode,hnode,"");
    } 
        
    private void handleEndnoteRef(Node onode, Node hnode) {
        createReference(onode,hnode,"");
    } 

    private void handleReferenceMark(Node onode, Node hnode) {
        String sName = Misc.getAttribute(onode,XMLString.TEXT_NAME);
        if (sName!=null && !bInToc) {
            hnode.appendChild(converter.createTarget("ref"+sName));
        }
    }
	
    private void handleReferenceRef(Node onode, Node hnode) {
        createReference(onode,hnode,"ref");
    } 

    private void handleBookmark(Node onode, Node hnode) {
        // Note: Two targets (may be the target of a hyperlink or a reference)
        String sName = Misc.getAttribute(onode,XMLString.TEXT_NAME);
        if (sName!=null && !bInToc) {
            hnode.appendChild(converter.createTarget(sName));
            hnode.appendChild(converter.createTarget("bkm"+sName));
        }
    }
	
    private void handleBookmarkRef(Node onode, Node hnode) {
        createReference(onode,hnode,"bkm");
    } 
	
    private void handleAlphabeticalIndexMark(Node onode, Node hnode) {
        if (bInToc) { return; }
        String sWord = Misc.getAttribute(onode,XMLString.TEXT_STRING_VALUE);
        if (sWord==null) { return; }
        AlphabeticalEntry entry = new AlphabeticalEntry();
        entry.sWord = sWord; entry.nIndex = ++nIndexIndex; 
        index.add(entry);
        hnode.appendChild(converter.createTarget("idx"+nIndexIndex));
    }

    private void handleAlphabeticalIndexMarkStart(Node onode, Node hnode) {
        if (bInToc) { return; }
        String sWord = IndexMark.getIndexValue(onode);
        if (sWord==null) { return; }
        AlphabeticalEntry entry = new AlphabeticalEntry();
        entry.sWord = sWord; entry.nIndex = ++nIndexIndex; 
        index.add(entry);
        hnode.appendChild(converter.createTarget("idx"+nIndexIndex));
    }
	
    private void handleBibliographyMark(Node onode, Node hnode) {
        if (bInToc) {
            traversePCDATA(onode,hnode);
        }
        else {
            Element anchor = converter.createLink("bibliography");
            hnode.appendChild(anchor);
            traversePCDATA(onode,anchor);
        }
    }
	
    /* apply hard formatting attribute style maps */
    private Element applyAttributes(Element node, StyleWithProperties style) {
        if (!config.xhtmlIgnoreStyles()) { return node; }
        if (style==null) { return node; }
        node = applyAttribute(node,"bold",getStyleCv().isBold(style));
        node = applyAttribute(node,"italics",getStyleCv().isItalics(style));
        node = applyAttribute(node,"fixed",getStyleCv().isFixed(style));
        node = applyAttribute(node,"superscript",getStyleCv().isSuperscript(style));
        node = applyAttribute(node,"subscript",getStyleCv().isSubscript(style));
        return node;
    }
	
    /* apply hard formatting attribute style maps */
    private Element applyAttribute(Element node, String sAttr, boolean bApply) {
        if (!bApply) { return node; }
        XhtmlStyleMap xattr = config.getXAttrStyleMap();
        if (!xattr.contains(sAttr)) { return node; }
        Element attr = converter.createElement(xattr.getElement(sAttr));
        if (!"(none)".equals(xattr.getCss(sAttr))) {
            attr.setAttribute("class",xattr.getCss(sAttr));
        }
        node.appendChild(attr);
        return attr;
    }
	
    /** Create a styled paragraph node */
    private Element createParagraph(Element node, String sStyleName) {
        XhtmlStyleMap xpar = config.getXParStyleMap();
        Element par;
        // First check to see if there is a map for this style:
        if (xpar.contains(sStyleName)) {
            par = converter.createElement(xpar.getElement(sStyleName));
            if (!"(none)".equals(xpar.getCss(sStyleName))) {
                par.setAttribute("class",xpar.getCss(sStyleName));
            }
            node.appendChild(par);
            return par;
        }
        // If this is an automatic style we have to check the parent style as well:
        ParStyle style = wsc.getParStyle(sStyleName);
        if (style!=null && style.isAutomatic() && style.getParentName()!=null) {
            String sParentName = style.getParentName();
            if (xpar.contains(sParentName)) {
                par = converter.createElement(xpar.getElement(sParentName));
                if (!"(none)".equals(xpar.getCss(sParentName))) {
                    par.setAttribute("class",xpar.getCss(sParentName));
                }
                node.appendChild(par);
                return applyAttributes(par,style);
            }
        }
        // Otherwise use the StyleConverter
        par = converter.createElement("p");
        getStyleCv().applyParStyle(sStyleName,par,!config.xhtmlIgnoreStyles());
        node.appendChild(par);
        return applyAttributes(par,style);
    }
	
    /** Create a styled inline node */
    private Element createInline(Element node, String sStyleName) {
        Element inline;
        // First check to see if there is a map for this style:
        XhtmlStyleMap xtext = config.getXTextStyleMap();
        if (xtext.contains(sStyleName)) {
            inline = converter.createElement(xtext.getElement(sStyleName));
            if (!"(none)".equals(xtext.getCss(sStyleName))) {
                inline.setAttribute("class",xtext.getCss(sStyleName));
            }
            node.appendChild(inline);
            return inline;
        }
        // Special care if we ignore formatting:
        if (config.xhtmlIgnoreStyles()) {
            StyleWithProperties style = wsc.getTextStyle(sStyleName);
            if (style!=null && style.isAutomatic()) {
                node = createInline(node,style.getParentName());
                return applyAttributes(node,style);
            }
            else { // ignore completely!
                return node;
            }
        }
        // Still here? Use styleconverter!
        inline = converter.createElement("span");
        getStyleCv().applyTextStyle(sStyleName,inline);
        node.appendChild(inline);
        return inline;
    }

    /* apply list style */
    private void applyListStyle(int nLevel, String sStyleName, Element list) {
        XhtmlStyleMap xlist = config.getXListStyleMap();
        if (xlist.contains(sStyleName)) {
            if (!"(none)".equals(xlist.getCss(sStyleName))) {
                list.setAttribute("class",xlist.getCss(sStyleName));
            }
        }
        else {
            getStyleCv().applyListStyle(nLevel,sStyleName,list,!config.xhtmlIgnoreStyles());
        }
    }



	
}


