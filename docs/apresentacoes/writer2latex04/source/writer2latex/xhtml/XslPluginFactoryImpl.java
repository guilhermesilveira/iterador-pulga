/************************************************************************
 *
 *  Xhtml10PluginFactoryImpl.java
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
 *  Copyright: 2001-2002 by Henrik Just
 *
 *  All Rights Reserved.
 * 
 * version 0.3.3f (2004-08-11)
 *
 */


package writer2latex.xhtml;

import java.io.InputStream;
import java.io.IOException;

import org.openoffice.xmerge.Document;
import org.openoffice.xmerge.ConvertData;
import org.openoffice.xmerge.DocumentSerializer;
import org.openoffice.xmerge.DocumentSerializerFactory;
import org.openoffice.xmerge.converter.dom.DOMDocument;
import org.openoffice.xmerge.converter.xml.sxw.SxwDocument;
import org.openoffice.xmerge.converter.xml.sxw.SxwPluginFactory;

import org.openoffice.xmerge.util.registry.ConverterInfo;

/**
 * Factory class used to create converters from Writer XML to XHTML+MathML format.
 */
public final class XslPluginFactoryImpl extends SxwPluginFactory 
    implements  DocumentSerializerFactory {    

   /**
    *  <p>Constructor that caches the <code>ConvertInfo</code> that
    *     corresponds to the registry information for this plug-in.</p>
    *
    *  @param  ci  <code>ConvertInfo</code> object.
    */
    public XslPluginFactoryImpl (ConverterInfo ci) {
        super(ci);
    }
   
    /**
     *  <p>The <code>DocumentSerializer</code> is used to convert 
     *  from the OpenOffice Writer <code>Document</code> format 
     *  to XHTML+MathML <code>Document</code> format.</p>
     *
     *  <p>The <code>ConvertData</code> object is passed along to the
     *     created <code>DocumentSerializer</code> via its constructor.
     *     The <code>ConvertData</code> is read and converted when the
     *     the <code>DocumentSerializer</code> object's
     *     <code>serialize</code> method is called.</p>
     *
     *  @param  doc  <code>Document</code> object that the created
     *               <code>DocumentSerializer</code> object uses
     *               as input.
     *
     *  @return  A <code>DocumentSerializer</code> object.
     */   
    public DocumentSerializer createDocumentSerializer(Document doc) {
        return new DocumentSerializerImpl(doc,XhtmlDocument.XHTML_MATHML_XSL,null);
    }
            
    
    /**
     *  <p>Create a <code>Document</code> object that corresponds to
     *  the XHTML+MathML data passed in via the <code>InputStream</code>
     *  object.  
     *
     *  <p>This method will read from the given <code>InputStream</code>
     *  object.  The returned <code>Document</code> object will contain
     *  the necessary data for the other objects created by the
     *  <code>PluginFactoryImpl</code> to process, like the
     *  <code>DocumentSerializerImpl</code> object and a
     *  <code>DocumentMerger</code> object.</p>
     *
     *  @param  name  The <code>Document</code> name.
     *  @param  is    <code>InputStream</code> object corresponding 
     *                to the <code>Document</code>.
     *
     *  @return  A <code>Document</code> object representing the
     *           XHTML+MathML format (as a DOM tree).
     *
     *  @throws   IOException   If any I/O error occurs.
     */

    public Document createDeviceDocument(String name, InputStream is) 
            throws IOException {
        XhtmlDocument htmlDoc = new XhtmlDocument(name,XhtmlDocument.XHTML_MATHML_XSL);
        htmlDoc.read(is);
        return htmlDoc;
    }    

}