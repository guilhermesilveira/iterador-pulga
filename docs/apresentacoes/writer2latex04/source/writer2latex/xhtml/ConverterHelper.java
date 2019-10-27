/************************************************************************
 *
 *  ConverterHelper.java
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
 *  Version 0.3.3g (2004-10-19)
 *
 */

package writer2latex.xhtml;

import writer2latex.util.Config;
import writer2latex.office.WriterStyleCollection;

public class ConverterHelper {
    protected WriterStyleCollection wsc;
    protected Config config;
    protected DocumentSerializerImpl converter;
	
    protected StyleConverter getStyleCv() { return converter.getStyleCv(); }

    protected TextConverter getTextCv() { return converter.getTextCv(); }
	
    protected TableConverter getTableCv() { return converter.getTableCv(); }

    protected DrawConverter getDrawCv() { return converter.getDrawCv(); }

    protected MathConverter getMathCv() { return converter.getMathCv(); }

    public ConverterHelper(WriterStyleCollection wsc, Config config, DocumentSerializerImpl converter) {
        this.wsc = wsc;
        this.config = config;
        this.converter = converter;
    }
}
