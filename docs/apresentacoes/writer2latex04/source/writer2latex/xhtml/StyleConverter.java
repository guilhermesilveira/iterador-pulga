/************************************************************************
 *
 *  StyleConverter.java
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
 *  Version 0.3.3j (2005-03-06)
 *
 */

package writer2latex.xhtml;

import java.util.Enumeration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import writer2latex.office.*;
import writer2latex.util.*;

/**
 * <p>This class converts OOo Writer XML styles to CSS2 styles, which
 * are the applied to xhtml elements.</p>
 * <p>Note that some elements in OOo has attributes that also maps
 * to CSS2 properties. Example: the width of a text box.</p>
 * <p>Also note, that some OOo style properties cannot be mapped to
 * CSS2 without creating an additional inline element.</p> 
 *
 */
class StyleConverter extends ConverterHelper {

    private int nType; // the doctype

    private String sScale;
    private String sColScale;
    private boolean bConvertToPx;

    // Translation of OOo style names to CSS style names:
    private ExportNameCollection parStyleNames=new ExportNameCollection(true);
    private ExportNameCollection textStyleNames=new ExportNameCollection(true);
    private ExportNameCollection frameStyleNames=new ExportNameCollection(true);
    private ExportNameCollection listStyleNames=new ExportNameCollection(true);
    // Some bookkeeping for headings and anchors
    private String[] sHeadingStyles = new String[7];
    private String sAnchorStyle = null;
    private String sVisitedAnchorStyle = null;

    StyleConverter(WriterStyleCollection wsc, Config config, DocumentSerializerImpl converter, int nType) {
        super(wsc,config,converter);
        this.nType = nType;
        sScale = config.getXhtmlScaling();
        sColScale = config.getXhtmlColumnScaling();
        bConvertToPx = config.xhtmlConvertToPx();
    }
                        
    // Methods to apply a style to an xhtml element
	
    // Apply direction (which is a style attribute in OOo)
    public void applyDirection(StyleWithProperties style, Element hnode) {
        String sDir = style.getProperty(XMLString.STYLE_WRITING_MODE);
        if ("lr-tb".equals(sDir)) {
            hnode.setAttribute("dir","ltr");
        }
        else if ("rl-tb".equals(sDir)) {
            hnode.setAttribute("dir","rtl");
        }
    }
	
    // Apply language+country (which is a style attribute in OOo)
    public void applyLang(StyleWithProperties style, Element hnode) {
        String sLang = style.getProperty(XMLString.FO_LANGUAGE);
        String sCountry = style.getProperty(XMLString.FO_COUNTRY);
        if (sLang!=null) {
            if (sCountry==null) { hnode.setAttribute("xml:lang",sLang); }
            else { hnode.setAttribute("xml:lang",sLang+"-"+sCountry); }
            // in case of xhtml 1.0 strict we need the lang attribute too:
            if (nType==XhtmlDocument.XHTML10) {
                if (sCountry==null) { hnode.setAttribute("lang",sLang); }
                else { hnode.setAttribute("lang",sLang+"-"+sCountry); }
            }
        }
    }

    // Apply the default language
    public void applyDefaultLanguage(Element hnode) {
        StyleWithProperties style = converter.isCalc() ? wsc.getDefaultCellStyle() : wsc.getDefaultParStyle();       
        if (style!=null) {
            applyLang(style,hnode);
        }
    }

    public void applyTextStyle(String sStyleName, Element hnode) {
        // sStyleName should be the name of a text style
        // hnode is the node to attach the style to
        StyleWithProperties style = wsc.getTextStyle(sStyleName);
        if (style!=null) {
            applyLang(style,hnode);
            if (style.isAutomatic()) { // hard formatting
                CSVList props = new CSVList(";");
                cssText(style,props);
                hnode.setAttribute("style",props.toString());
            }
            else { // soft formatting
                hnode.setAttribute("class",textStyleNames.getExportName(sStyleName));
            }            
        }
    }

    /*
    TODO: drop caps (contained in a child of the style:properties element)
    The CSS attributes should be applied to the :first-letter
    pseudo-element or to an additional inline element.
    */
	
    public void applyParStyle(String sStyleName, Element hnode, boolean bUseHard) {
        // sStyleName should be the name of a paragraph style
        // hnode is the node to attach the style to
        StyleWithProperties style = wsc.getParStyle(sStyleName);
        if (style!=null) {
            if (style.isAutomatic()) { // hard formatting
                // First apply class for parent style:
                String sParentName = style.getParentName();
                if (sParentName!=null) {
                    hnode.setAttribute("class",parStyleNames.getExportName(sParentName));
                }
                if (bUseHard) {
                    CSVList props = new CSVList(";");
                    cssBox(style,props,false);
                    cssPar(style,props,false);
                    cssText(style,props,false);
                    if (!props.isEmpty()) {
                        hnode.setAttribute("style",props.toString());
                    }
                }
            }
            else { // soft formatting
                hnode.setAttribute("class",parStyleNames.getExportName(sStyleName));
            }            
            applyLang(style,hnode);
            applyDirection(style,hnode);
        }
    }
	
    public void applyFrameStyle(String sStyleName, Element hnode, CSVList props, boolean bFloat, boolean bUseHard) {
        // hnode is the node to attach the style to
        // props contain additional CSS attribute (height, width)
        // returns true if this frame is not floating
        StyleWithProperties style = wsc.getFrameStyle(sStyleName);
        if (style!=null) {
            // don't float if we're not supposed to wrap text
            if ("none".equals(style.getProperty(XMLString.STYLE_WRAP))) { bFloat=false; } 
            if (bFloat) { cssFloat(style,props); }
            if (style.isAutomatic()) { // hard formatting
                if (bUseHard) { cssBox(style,props); }
                hnode.setAttribute("style",props.toString());
            }
            else { // soft formatting
                hnode.setAttribute("class","frame"+frameStyleNames.getExportName(sStyleName));
                hnode.setAttribute("style",props.toString());
            }
        }
    }
	
/*    public void applyExternalFrameStyle(String sStyleName, Element hnode, CSVList props) {
        // hnode is the node to attach the style to
        // props contain additional CSS attribute (height, width)
        StyleWithProperties style = wsc.getFrameStyle(sStyleName);
        if (style!=null) {
            String s = style.getProperty(XMLString.STYLE_HORIZONTAL_POS);
            if ("center".equals(s)) { props.addValue("align","center"); }
            else if ("right".equals(s)) { props.addValue("align","right"); }
            else if ("outside".equals(s)) { props.addValue("align","right"); }
            else { props.addValue("align","left"); }
        }
    }*/
	
    public void applySectionStyle(String sStyleName, Element hnode) {
        // sStyleName should be the name of a section style
        // hnode is the node to attach the style to
        SectionStyle style = wsc.getSectionStyle(sStyleName);
        if (style!=null) {
            applyLang(style,hnode);
            applyDirection(style,hnode);
            if (config.xhtmlIgnoreStyles()==false) {
                CSVList props = new CSVList(";");
                //With css3 columns can be supported, eg.:
                //props.addValue("column-count",Integer.toString(style.getColCount()));
                cssBox(style,props);
                if (props.toString().length()>0) {
                    hnode.setAttribute("style",props.toString());
                }
            }
        }
    }
	
    public void applyPageStyle(String sMasterPage, Element hnode) {
        // sMasterPage should be the name of a master page
        // Apply page master to hnode (usually the body)
        if (sMasterPage==null) { return; }
        MasterPage masterPage = wsc.getMasterPage(sMasterPage);
        if (masterPage==null) { return; }
        String sPageMaster = masterPage.getProperty(XMLString.STYLE_PAGE_MASTER_NAME);
        PageMaster pageMaster = wsc.getPageMaster(sPageMaster);
        if (pageMaster==null) { return; }
        applyDirection(pageMaster,hnode);
        if (config.xhtmlIgnoreStyles()==false) {
            CSVList props = new CSVList(";");
            cssBackground(pageMaster,props,true);
            if (props.toString().length()>0) {
                hnode.setAttribute("style",props.toString());
            }
        }
    }
	
    public void applyTableStyle(String sStyleName, Element hnode) {
        // hnode is the node to attach the style to
        StyleWithProperties style = wsc.getTableStyle(sStyleName);
        if (style!=null) {
            // Table styles are allways automatic in OOo 
            if (style.isAutomatic()) { // hard formatting
                applyDirection(style,hnode);
                //if (config.xhtmlIgnoreStyles()==false) {
                    CSVList props = new CSVList(";");
                    cssBox(style,props);
                    // Writer uses a separating border model, Calc a collapsing:
                    //props.addValue("border-collapse", bCalc ? "collapse" : "separate");
                    // For now always use separating model:
                    props.addValue("border-collapse", "separate");
                    props.addValue("border-spacing", "0");
                    if (converter.isCalc()) { props.addValue("white-space:nowrap"); }
                    hnode.setAttribute("style",props.toString());
                //}
            }
        }
    }

    public void applyColumnStyle(String sStyleName, Element hnode) {
        // hnode is the node to attach the style to
        StyleWithProperties style = wsc.getColumnStyle(sStyleName);
        if (style!=null) {
            // Column styles are allways automatic in OOo 
            if (style.isAutomatic()) { // hard formatting
                CSVList props = new CSVList(";");
                cssColumn(style,props);
                hnode.setAttribute("style",props.toString());
            }
        }
    }

    public void applyRowStyle(String sStyleName, Element hnode) {
        // hnode is the node to attach the style to
        StyleWithProperties style = wsc.getRowStyle(sStyleName);
        if (style!=null) {
            // Row styles are allways automatic in OOo 
            if (style.isAutomatic()) { // hard formatting
                CSVList props = new CSVList(";");
                cssRow(style,props);
                cssBackground(style,props,false);
                hnode.setAttribute("style",props.toString());
            }
        }
    }

    public void applyCellStyle(String sStyleName, String sValueType, Element hnode) {
        // hnode is the node to attach the style to
        StyleWithProperties style = wsc.getCellStyle(sStyleName);
        if (style!=null) {
            // Always treat as automatic (todo: change this for spreadsheets) 
            //if (style.isAutomatic()) { // hard formatting
                CSVList props = new CSVList(";");
                cssText(style,props); // only relevant for spreadsheets
                cssPar(style,props); // only relevant for spreadsheets
                cssBox(style,props);
                cssCell(style,props,sValueType);
                hnode.setAttribute("style",props.toString());
            //}
        }
    }
	
    public void applyListStyle(int nLevel, String sStyleName, Element hnode, boolean bUseHard) {
        // hnode is the node to attach the style to
        ListStyle style = wsc.getListStyle(sStyleName);
        if (style!=null) { 
            if (style.isAutomatic()) { // hard formatting
                if (bUseHard) {
                    CSVList props = new CSVList(";");
                    cssList(style,nLevel,props);
                    hnode.setAttribute("style",props.toString());
                }
            }
            else { // soft formatting
                hnode.setAttribute("class","listlevel"+Integer.toString(nLevel)
                                   +listStyleNames.getExportName(sStyleName));
            }            
        }
    }

    public void setHeadingStyle(int nLevel, String sStyleName) {
        if (sHeadingStyles[nLevel]==null) {
		    sHeadingStyles[nLevel] = sStyleName;
        }
    }

    public void setAnchorStyle(String sStyleName, String sVisitedStyleName) {
        if (sAnchorStyle==null) {
		    sAnchorStyle = sStyleName;
            sVisitedAnchorStyle = sVisitedStyleName;
        }
    }

    // Export styles to CSS
    public Node exportStyles(Document htmlDOM) {
        Element htmlStyle = htmlDOM.createElement("style");
        htmlStyle.setAttribute("media","all");
        htmlStyle.setAttribute("type","text/css");
        htmlStyle.appendChild(htmlDOM.createTextNode("\n"));
		
        // Default paragraph/cell style is applied to the body element:
        StyleWithProperties defaultStyle = converter.isCalc() ? wsc.getDefaultCellStyle()
                                                 : wsc.getDefaultParStyle();
        if (defaultStyle!=null) {
            CSVList props = new CSVList(";");
            cssText(defaultStyle,props); // text properties only!
            Node css = htmlDOM.createTextNode("      body {"+props.toString()+"}\n");
            htmlStyle.appendChild(css);
        }

        // Styles for headings
        for (int i=1; i<=6; i++) {
            if (sHeadingStyles[i]!=null) {
                StyleWithProperties style = wsc.getParStyle(sHeadingStyles[i]);
                if (style!=null) {
                    CSVList props = new CSVList(";");
                    cssBox(style,props);
                    cssPar(style,props);
                    cssText(style,props);
                    props.addValue("clear","left");
                    Node css = htmlDOM.createTextNode("      h"+i+" {"+props.toString()+"}\n");
                    htmlStyle.appendChild(css);
                }
            }
        }
		
        // List styles
        Enumeration enumeration = wsc.getListStyles().getStylesEnumeration();
        while (enumeration.hasMoreElements()) {
            ListStyle style = (ListStyle) enumeration.nextElement();
            String sName = style.getName();
            if (!style.isAutomatic() && listStyleNames.containsName(sName)) {
                for (int nLevel=1; nLevel<10; nLevel++) {
                    CSVList props = new CSVList(";");
                    cssList(style,nLevel,props);
                    String sCssName = ".listlevel"+Integer.toString(nLevel)
                                      +listStyleNames.getExportName(sName);
                    Node css = htmlDOM.createTextNode("      "+sCssName+" {"+props.toString()+"}\n");
                    htmlStyle.appendChild(css);
                }
            }
        } 
		
        // Frame styles
        enumeration = wsc.getFrameStyles().getStylesEnumeration();
        while (enumeration.hasMoreElements()) {
            StyleWithProperties style = (StyleWithProperties) enumeration.nextElement();
            String sName = style.getName();
            if (!style.isAutomatic() && frameStyleNames.containsName(sName)) {
                CSVList props = new CSVList(";");
                cssBox(style,props);
                Node css = htmlDOM.createTextNode(
                    "      .frame"+frameStyleNames.getExportName(sName)+" {"+props.toString()+"}\n");
                htmlStyle.appendChild(css);
            }
        } 

        // Paragraph styles
        enumeration = wsc.getParStyles().getStylesEnumeration();
        while (enumeration.hasMoreElements()) {
            StyleWithProperties style = (StyleWithProperties) enumeration.nextElement();
            String sName = style.getName();
            if (!style.isAutomatic() && parStyleNames.containsName(sName)) {
                CSVList props = new CSVList(";");
                cssBox(style,props);
                cssPar(style,props);
                cssText(style,props);
                Node css = htmlDOM.createTextNode(
                    "      p."+parStyleNames.getExportName(sName)+" {"+props.toString()+"}\n");
                htmlStyle.appendChild(css);
            }
        } 

        // Anchor styles
        if (sAnchorStyle!=null) {
            StyleWithProperties style = wsc.getTextStyle(sAnchorStyle);
            CSVList props = new CSVList(";");
            cssText(style,props);
            Node css = htmlDOM.createTextNode(
                "      a:link {"+props.toString()+"}\n");
            htmlStyle.appendChild(css);
        }
        if (sVisitedAnchorStyle!=null) {
            StyleWithProperties style = wsc.getTextStyle(sAnchorStyle);
            CSVList props = new CSVList(";");
            cssText(style,props);
            Node css = htmlDOM.createTextNode(
                "      a:visited {"+props.toString()+"}\n");
            htmlStyle.appendChild(css);
        }

        // Text styles
        enumeration = wsc.getTextStyles().getStylesEnumeration();
        while (enumeration.hasMoreElements()) {
            StyleWithProperties style = (StyleWithProperties) enumeration.nextElement();
            String sName = style.getName();
            if (!style.isAutomatic() && textStyleNames.containsName(sName)) {
                CSVList props = new CSVList(";");
                cssText(style,props);
                Node css = htmlDOM.createTextNode(
				    "      span."+textStyleNames.getExportName(sName)+" {"+props.toString()+"}\n");
                htmlStyle.appendChild(css);
            }
        }
		
        htmlStyle.appendChild(htmlDOM.createTextNode("    "));

        return htmlStyle;
    }
	
    // Methods to query individual formatting properties (no inheritance)
	
    // Does this style contain the bold attribute?
    public boolean isBold(StyleWithProperties style) {
        String s = style.getProperty(XMLString.FO_FONT_WEIGHT,false);
        return s!=null && "bold".equals(s);
    }

    // Does this style contain the italics/oblique attribute?
    public boolean isItalics(StyleWithProperties style) {
        String s = style.getProperty(XMLString.FO_FONT_STYLE,false);
        return s!=null && !"normal".equals(s);
    }
	
    // Does this style contain a fixed pitch font?
    public boolean isFixed(StyleWithProperties style) {
        String s = style.getProperty(XMLString.STYLE_FONT_NAME,false);
        String s2 = null;
        String s3 = null;
        if (s!=null) {
            FontDeclaration fd = (FontDeclaration) wsc.getFontDeclarations().getStyle(s);
            s2 = fd.getProperty(XMLString.STYLE_FONT_FAMILY_GENERIC);
            s3 = fd.getProperty(XMLString.STYLE_FONT_PITCH);
        }
        else {            
            s = style.getProperty(XMLString.FO_FONT_FAMILY,false);
            s2 = style.getProperty(XMLString.STYLE_FONT_FAMILY_GENERIC,false);
            s3 = style.getProperty(XMLString.STYLE_FONT_PITCH,false);
        }
        if ("fixed".equals(s3)) { return true; }
        if ("modern".equals(s2)) { return true; }
        return false;
    }

    // Does this style specify superscript?
    public boolean isSuperscript(StyleWithProperties style) {
        String sPos = style.getProperty(XMLString.STYLE_TEXT_POSITION,false);
        if (sPos==null) return false;
        if (sPos.startsWith("sub")) return false;
        if (sPos.startsWith("-")) return false;
        return true;
    }

    // Does this style specify subscript?
    public boolean isSubscript(StyleWithProperties style) {
        String sPos = style.getProperty(XMLString.STYLE_TEXT_POSITION,false);
        if (sPos==null) return false;
        if (sPos.startsWith("sub")) return true;
        if (sPos.startsWith("-")) return true;
        return false;
    }
	
    // Methods to translate styles to css

    private String scale(String s) {
        if (bConvertToPx) {
            return Misc.length2px(Misc.multiply(sScale,s));
        }
        else {
            return Misc.multiply(sScale,s);
        }
    }
	
    private String borderScale(String sBorder) {
        SimpleInputBuffer in = new SimpleInputBuffer(sBorder);
        StringBuffer out = new StringBuffer();
        while (in.peekChar()!='\0') {
            // Skip spaces
            while(in.peekChar()==' ') { out.append(" "); in.getChar(); }
            // If it's a number it must be a unit -> convert it
            if ('0'<=in.peekChar() && in.peekChar()<='9') {
                out.append(scale(in.getNumber()+in.getIdentifier()));
            }
            // skip other characters
            while (in.peekChar()!=' ' && in.peekChar()!='\0') {
                out.append(in.getChar());
            } 
        }
        return out.toString();
    }

    private void cssText(StyleWithProperties style, CSVList props) {
        cssText(style,props,true);
    }
	
    private void cssText(StyleWithProperties style, CSVList props, boolean bInherit) {
        // translates text style properties.
        // these can be applied to text styles and paragraph styles.
        // The following properties are not supported by CSS2:
        // style:text-outline (could maybe be simulated with css text-shadow)
        // style:font-charset (non unicode encoding...)
        // fo:score-spaces (don't underline/overstrike spaces if false)
        // style:letter-kerning (disable/enable kerning)
        
        String s,s2,s3,s4;
        CSVList val;
		
        // Font family
        if (bInherit || style.getProperty(XMLString.STYLE_FONT_NAME,false)!=null) {
            val = new CSVList(","); // multivalue property!
            // Get font family information from font declaration or from style
            s = style.getProperty(XMLString.STYLE_FONT_NAME);
            if (s!=null) {
                FontDeclaration fd = (FontDeclaration) wsc.getFontDeclarations().getStyle(s);
                s = fd.getProperty(XMLString.FO_FONT_FAMILY);
                s2 = fd.getProperty(XMLString.STYLE_FONT_FAMILY_GENERIC);
                s3 = fd.getProperty(XMLString.STYLE_FONT_PITCH);
            }
            else {            
                s = style.getProperty(XMLString.FO_FONT_FAMILY);
                s2 = style.getProperty(XMLString.STYLE_FONT_FAMILY_GENERIC);
                s3 = style.getProperty(XMLString.STYLE_FONT_PITCH);
            }
   		
            // Add the western font family (CJK and CTL is more complicated)
            if (s!=null) { val.addValue(s); }
            // Add generic font family
            if ("fixed".equals(s3)) { val.addValue("monospace"); }
            else if ("roman".equals(s2)) { val.addValue("serif"); }
            else if ("swiss".equals(s2)) { val.addValue("sans-serif"); }
            else if ("modern".equals(s2)) { val.addValue("monospace"); }
            else if ("decorative".equals(s2)) { val.addValue("fantasy"); }
            else if ("script".equals(s2)) { val.addValue("cursive"); }
            else if ("system".equals(s2)) { val.addValue("monospace"); } // Is this right?
            if (!val.isEmpty()) { props.addValue("font-family",val.toString()); }
        }
		
        // Font style (italics): This property fit with css
        s = style.getProperty(XMLString.FO_FONT_STYLE,bInherit);
	    if (s!=null) { props.addValue("font-style",s); }
	  
        // Font variant (small caps): This property fit with css
        s = style.getProperty(XMLString.FO_FONT_VARIANT,bInherit);
        if (s!=null) { props.addValue("font-variant",s); }
	    
        // Font weight (bold): This property fit with css
        s = style.getProperty(XMLString.FO_FONT_WEIGHT,bInherit);
        if (s!=null) { props.addValue("font-weight",s); }
 
        // Font size: Absolute values of this property fit with css
        // this is handled together with sub- and superscripts (style:text-position)
        // First value: sub, super or percentage (raise/lower relative to font height)
        // Second value (optional): percentage (relative size);
        if (bInherit || style.getProperty(XMLString.FO_FONT_SIZE,false)!=null
                     || style.getProperty(XMLString.STYLE_TEXT_POSITION,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_FONT_SIZE);
            s2 = style.getProperty(XMLString.STYLE_TEXT_POSITION);
	        if (s2!=null) {
                s2 = s2.trim();
                int i = s2.indexOf(" ");
                if (i>0) { // two values
                    s3 = s2.substring(0,i);
                    s4 = s2.substring(i+1);
                } 		
                else { // one value
                    s3 = s2; s4="100%";
                }
                if (s!=null) { props.addValue("font-size",Misc.multiply(s4,scale(s))); }
                else { props.addValue("font-size",s4); }
                props.addValue("vertical-align",s3);
            }
            else if (s!=null) {
                props.addValue("font-size",scale(s));
            }
        }

        // Color: This attribute fit with css
        s = style.getProperty(XMLString.FO_COLOR,bInherit);
	    if (s!=null) { props.addValue("color",s); }
	  
        // Background color: This attribute fit with css when applied to inline text
        s = style.getProperty(XMLString.STYLE_TEXT_BACKGROUND_COLOR,bInherit);
	    if (s!=null) { props.addValue("background-color",s); }
	  
        // Shadow: This attribute fit with css
        // (Currently OOo has only one shadow style, which is saved as 1pt 1pt)
        s = style.getProperty(XMLString.FO_TEXT_SHADOW,bInherit);
        if (s!=null) { props.addValue("text-shadow",s); }
	  
        // Text decoration. Here OOo is more flexible that CSS2.
        s = style.getProperty(XMLString.STYLE_TEXT_CROSSING_OUT,bInherit);
        s2 = style.getProperty(XMLString.STYLE_TEXT_UNDERLINE,bInherit);
        s3 = style.getProperty(XMLString.STYLE_TEXT_BLINKING,bInherit);
        // Issue: Since these three properties all maps to the single CSS property
        // text-decoration, there is no way to turn on one kind of decoration and 
        // turn another one off (without creating another inline element).
        // If one decoration is turned of, we turn them all off:
        if ("none".equals(s) || "none".equals(s2) || "false".equals(s3)) {
            props.addValue("text-decoration","none");
        }
        else { // set the required properties
            val = new CSVList(" "); // multivalue property!
            if (s!=null && !"none".equals(s)) { val.addValue("line-through"); }
            if (s2!=null && !"none".equals(s2)) { val.addValue("underline"); }
            if (s3!=null && "true".equals(s3)) { val.addValue("blink"); }
            if (!val.isEmpty()) { props.addValue("text-decoration",val.toString()); }  
        }
  
        // Letter spacing: This property fit with css
        s = style.getProperty(XMLString.FO_LETTER_SPACING,bInherit);
	    if (s!=null) { props.addValue("letter-spacing",scale(s)); }
  
        // Capitalization: This property fit with css
        s = style.getProperty(XMLString.FO_TEXT_TRANSFORM,bInherit);
	    if (s!=null) { props.addValue("text-transform",s); }
    }

    private void cssPar(StyleWithProperties style, CSVList props){
        cssPar(style,props,true);
    }
	
    private void cssPar(StyleWithProperties style, CSVList props, boolean bInherit){
        // translates paragraph style properties.
        // The following properties are not supported by CSS2:
        // style:justify-single-word and style:text-align-last

        String s;
/* problem: 120% times normal makes no sense...
        s = style.getProperty(XMLString.FO_LINE_HEIGHT);
	    if (s!=null && s.equals("normal")) {
            props.addValue("line-height:normal;"; 
        }
        else { // length or percentage
            s = style.getAbsoluteProperty(XMLString.FO_LINE_HEIGHT);
            if (s!=null) { props.addValue("line-height",s); }
        }
		*/
        // TODO: style:line-height-at-least and stype:line-spacing
		
        // Indentation: Absolute values of this property fit with css...
        if (bInherit || style.getProperty(XMLString.FO_TEXT_INDENT,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_TEXT_INDENT);
	        if (s!=null) { 
                props.addValue("text-indent",scale(s));
            }
            else { // ... but css doesn't have this one
                s = style.getProperty(XMLString.STYLE_AUTO_TEXT_INDENT);
                if ("true".equals(s)) { props.addValue("text-indent","2em"); }
            }
        }
		
        // Alignment: This property fit with css, but two values have different names		
        s = style.getProperty(XMLString.FO_TEXT_ALIGN,bInherit);
        if (s!=null) { // rename two property values:
            if (s.equals("start")) { s="left"; }
            else if (s.equals("end")) { s="right"; }
            props.addValue("text-align",s);
        }
		
        // Wrap (only in table cells, only in Calc):
        if (converter.isCalc()) {
            s = style.getProperty(XMLString.FO_WRAP_OPTION,bInherit);
            if ("no-wrap".equals(s)) props.addValue("white-space","nowrap");
            else if ("wrap".equals(s)) props.addValue("white-space","normal");
        }
    }

    private void cssColumn(StyleWithProperties style, CSVList props){
        // Translates column style properties
        // Width: Fit with css
        String s = style.getAbsoluteProperty(XMLString.STYLE_COLUMN_WIDTH);
        if (s!=null) { props.addValue("width",Misc.multiply(sColScale,scale(s))); }
    }
	
    private void cssRow(StyleWithProperties style, CSVList props){
        // Translates row style properties
        // OOo offers style:row-height and style:min-row-height
        // In css row heights are always minimal, so both are exported as height
        // If neither is specified, the tallest cell rules; this fits with css.
        String s = style.getAbsoluteProperty(XMLString.STYLE_ROW_HEIGHT);
        if (s==null) { s = style.getAbsoluteProperty(XMLString.STYLE_MIN_ROW_HEIGHT); }
        if (s!=null) { props.addValue("height",scale(s)); }
    }

    private void cssCell(StyleWithProperties style, CSVList props, String sValueType){
        // Translates cell style properties
        // Vertical align: Some values fit with css
        // The default is always "top" (also the last value "automatic") 
        String s = style.getProperty(XMLString.FO_VERTICAL_ALIGN);
        if ("middle".equals(s)) { props.addValue("vertical-align","middle"); }
        else if ("bottom".equals(s)) { props.addValue("vertical-align","bottom"); }
        else { props.addValue("vertical-align","top"); }
        // Automatic horizontal alignment
        if (!"fix".equals(style.getProperty(XMLString.STYLE_TEXT_ALIGN_SOURCE)) &&
            sValueType!=null) {
            // strings go left, other types (float, time, date, percentage, currency, boolean) go right
            props.addValue("text-align","string".equals(sValueType) ? "left" : "right");
        }
    }
	
    private void cssBackground(StyleWithProperties style, CSVList props, boolean bInherit){
        // Background color: Same as in css
        String s = style.getProperty(XMLString.FO_BACKGROUND_COLOR,bInherit);
        if (s!=null) { props.addValue("background-color",s); }
		
        // Background image: TODO (In OOo this is contained in a subelement of the prop's)
    }
	
    private void cssBox(StyleWithProperties style, CSVList props){
        cssBox(style,props,true);
    }
	
    private void cssBox(StyleWithProperties style, CSVList props, boolean bInherit){
        // translates "box" style properties.
        // these can be applied to paragraph styles, frame styles and page styles.
        // The following properties are not supported by CSS2:
        // style:border-line-width and style:border-line-width-*
        // TODO: What about shadow?
        String s;
		
        // Margins: *Absolute* values fit with css
        if (bInherit || style.getProperty(XMLString.FO_MARGIN_LEFT,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_MARGIN_LEFT);
            if (s!=null) { props.addValue("margin-left",scale(s)); }
            else { props.addValue("margin-left","0"); }
        }
        if (bInherit || style.getProperty(XMLString.FO_MARGIN_RIGHT,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_MARGIN_RIGHT);
	        if (s!=null) { props.addValue("margin-right",scale(s)); }
            else { props.addValue("margin-right","0"); }
        }
        if (bInherit || style.getProperty(XMLString.FO_MARGIN_TOP,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_MARGIN_TOP);
	        if (s!=null) { props.addValue("margin-top",scale(s)); }
            else { props.addValue("margin-top","0"); }
        }
        if (bInherit || style.getProperty(XMLString.FO_MARGIN_BOTTOM,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_MARGIN_BOTTOM);
	        if (s!=null) { props.addValue("margin-bottom",scale(s)); }
            else { props.addValue("margin-bottom","0"); }
        }
        
        // Padding: *Absolute* values fit with css
        s=null;
        if (bInherit || style.getProperty(XMLString.FO_PADDING,false)!=null) {
            s = style.getAbsoluteProperty(XMLString.FO_PADDING);
        }
        if (s!=null) {
            props.addValue("padding",scale(s));
        }
        else { // apply individual paddings
            if (bInherit || style.getProperty(XMLString.FO_PADDING_TOP,false)!=null) {
                s = style.getAbsoluteProperty(XMLString.FO_PADDING_TOP);
                if (s!=null) { props.addValue("padding-top",scale(s)); }
            }
            if (bInherit || style.getProperty(XMLString.FO_PADDING_BOTTOM,false)!=null) {
                s = style.getAbsoluteProperty(XMLString.FO_PADDING_BOTTOM);
                if (s!=null) { props.addValue("padding-bottom",scale(s)); }
            }
            if (bInherit || style.getProperty(XMLString.FO_PADDING_LEFT,false)!=null) {
                s = style.getAbsoluteProperty(XMLString.FO_PADDING_LEFT);
                if (s!=null) { props.addValue("padding-left",scale(s)); }
            }
            if (bInherit || style.getProperty(XMLString.FO_PADDING_RIGHT,false)!=null) {
                s = style.getAbsoluteProperty(XMLString.FO_PADDING_RIGHT);
                if (s!=null) { props.addValue("padding-right",scale(s)); }
            }
        }
		 
        // Border: Same as in css
        boolean bHasBorder = false;
        s=null;
        if (bInherit || style.getProperty(XMLString.FO_BORDER,false)!=null) {
            s = style.getProperty(XMLString.FO_BORDER);
        }
        if (s!=null) {
            props.addValue("border",borderScale(s)); bHasBorder = true;
        }
        else { // apply individual borders
            if (bInherit || style.getProperty(XMLString.FO_BORDER_TOP,false)!=null) {
                s = style.getProperty(XMLString.FO_BORDER_TOP);
                if (s!=null) { props.addValue("border-top",borderScale(s)); bHasBorder=true; }
            }
            if (bInherit || style.getProperty(XMLString.FO_BORDER_BOTTOM,false)!=null) {
                s = style.getProperty(XMLString.FO_BORDER_BOTTOM);
                if (s!=null) { props.addValue("border-bottom",borderScale(s)); bHasBorder=true; }
            }
            if (bInherit || style.getProperty(XMLString.FO_BORDER_LEFT,false)!=null) {
                s = style.getProperty(XMLString.FO_BORDER_LEFT);
                if (s!=null) { props.addValue("border-left",borderScale(s)); bHasBorder=true; }
            }
            if (bInherit || style.getProperty(XMLString.FO_BORDER_RIGHT,false)!=null) {
                s = style.getProperty(XMLString.FO_BORDER_RIGHT);
                if (s!=null) { props.addValue("border-right",borderScale(s)); bHasBorder=true; }
            }
        }
        // Default to no border:
        if (bInherit && !bHasBorder) { props.addValue("border","none"); }

        cssBackground(style,props,bInherit);
    } 

    private void cssFloat(StyleWithProperties style, CSVList props){
	    String s = style.getProperty(XMLString.STYLE_HORIZONTAL_POS);
        if ("from-left".equals(s) || "left".equals(s)) {
            props.addValue("float","left");
        }
        else {
            props.addValue("float","right");
        }
    }

    private void cssList(ListStyle style, int nLevel, CSVList props){
        // translates "list" style properties for a particular level
        // Mozilla does not seem to support the "marker" mechanism of CSS2
        // so we will stick with the simpler CSS1-like list style properties
        String sLevelType = style.getLevelType(nLevel);
        if (XMLString.TEXT_LIST_LEVEL_STYLE_NUMBER.equals(sLevelType)) {
            // Numbering style, get number format
            String sNumFormat = style.getLevelProperty(nLevel,XMLString.STYLE_NUM_FORMAT);
            if ("1".equals(sNumFormat)) { props.addValue("list-style-type","decimal"); }
            else if ("i".equals(sNumFormat)) { props.addValue("list-style-type","lower-roman"); }
            else if ("I".equals(sNumFormat)) { props.addValue("list-style-type","upper-roman"); }
            else if ("a".equals(sNumFormat)) { props.addValue("list-style-type","lower-alpha"); }
            else if ("A".equals(sNumFormat)) { props.addValue("list-style-type","upper-alpha"); }
        }
        else if (XMLString.TEXT_LIST_LEVEL_STYLE_BULLET.equals(sLevelType)) {
            // Bullet. We can only choose from disc, bullet and square
            switch (nLevel % 3) {
                case 1: props.addValue("list-style-type","disc"); break;
                case 2: props.addValue("list-style-type","bullet"); break;
                case 0: props.addValue("list-style-type","square"); break;
            }
        }
        else if (XMLString.TEXT_LIST_LEVEL_STYLE_IMAGE.equals(sLevelType)) {
            // Image. TODO: Handle embedded images
            String sHref = style.getLevelProperty(nLevel,XMLString.XLINK_HREF);
            if (sHref!=null) { props.addValue("list-style-image","url('"+sHref+"')"); }
        }
    }
		
}