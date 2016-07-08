<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:cts="http://chs.harvard.edu/xmlns/cts" xmlns:ti="http://chs.harvard.edu/xmlns/cts" xmlns:dc="http://purl.org/dc/elements/1.1" xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output omit-xml-declaration="yes" method="html" encoding="UTF-8"/>
    <xsl:include href="cts_header.xsl"/>
    
    <xsl:variable name="homeUrl">home</xsl:variable>
    <xsl:variable name="formsUrl">home</xsl:variable>
    
    
    <xsl:template match="/">
        
        <html>
            <head>
                
                <meta name="viewport" content="width=device-width; initial-scale=1.0"/>
                <link href="cts-ui/css/cts-core.css" rel="stylesheet" title="CSS for CTS" type="text/css"/>
                <link href="cts-ui/css/cts.css" rel="stylesheet"/>
                <style>
                    p { margin: 0em; }
                </style>
                <xsl:choose>
                    <xsl:when
                        test="/cts:CTSError">
                        <title>Error</title>
                    </xsl:when>
                    <xsl:otherwise>
                        <title>Contents of CTS Library
                        </title>
                    </xsl:otherwise>
                </xsl:choose>
            </head>
            <body>
                
                
                <header>
                    <xsl:call-template name="header"/>
                </header>
                
                <nav>
                    <p> 
                        <xsl:element name="a">
                            <xsl:attribute name="href"><xsl:value-of select="$homeUrl"/></xsl:attribute>
                            home
                        </xsl:element>
                        
                        <!--<xsl:element name="a">
							<xsl:attribute name="href"><xsl:value-of select="$formsUrl"/></xsl:attribute>
							Look up material by URN
						</xsl:element>-->
                        
                        
                    </p>
                    
                </nav>
                
                <article>
                    <h1>Text Inventory</h1>
                    
                    <ul class="mH" id="invMenu" >
                            <xsl:apply-templates
                                select="//ti:textgroup"/>
                    </ul>
                    
                    
                </article>
                
                <footer>
                    <xsl:call-template name="footer"/>
                </footer>
                
            </body>
        </html>
        
        
        
    </xsl:template>
    
    <xsl:template match="ti:textgroup">
        <div class="textgroup" >
            <h2>Text Group: <strong><xsl:apply-templates select="ti:groupname"/></strong></h2>
            <p><code><xsl:value-of select="@urn"/></code></p>
            <ul>
                <xsl:apply-templates select="ti:work"/>
            </ul>
        </div>
    </xsl:template>
    
    <xsl:template match="ti:work">
        <li class="cts-work" >
            <p><span class="highlight">Work: </span><xsl:apply-templates select="../ti:groupname"/>, <strong><em><xsl:apply-templates select="ti:title"/></em></strong></p>
            
            <p><code><xsl:value-of select="@urn"/></code></p>
            <ul>
                <xsl:apply-templates select="ti:edition"/>
                <xsl:apply-templates select="ti:translation"/>
            </ul>
        </li>
    </xsl:template>
    
    <xsl:template match="ti:edition">
        <li class="cts-edition cts-version">
            
            <p><span class="highlight">Edition: </span><xsl:apply-templates select="../../ti:groupname"/>, <em><xsl:apply-templates select="../ti:title"/></em>: <strong><xsl:apply-templates select="ti:label"/></strong></p>
            <p><xsl:apply-templates select="ti:description"/></p>
            <xsl:call-template name="startReading">
                <xsl:with-param name="urn"><xsl:value-of select="@urn"/></xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="queryForm">
                <xsl:with-param name="urn"><xsl:value-of select="@urn"/></xsl:with-param>
                <xsl:with-param name="textType">Edition</xsl:with-param>
            </xsl:call-template>
            <ul>
                <xsl:apply-templates select="ti:exemplar">
                    <xsl:with-param name="textType">Edition</xsl:with-param>
                </xsl:apply-templates>
            </ul>
        </li>
    </xsl:template>
    
    <xsl:template match="ti:exemplar">
        <xsl:param name="textType"/>
        <li class="cts-exemplar cts-version">
            
            <p><span class="highlight">Exemplar of <xsl:value-of select="$textType"/>: </span><xsl:apply-templates select="../../../ti:groupname"/> <em><xsl:apply-templates select="../../ti:title"/></em>: <xsl:apply-templates select="../ti:label"/>  <strong><xsl:apply-templates select="ti:label"/></strong></p>
            <p><xsl:apply-templates select="ti:description"/></p>
            <xsl:call-template name="startReading">
                <xsl:with-param name="urn"><xsl:value-of select="@urn"/></xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="queryForm">
                <xsl:with-param name="urn"><xsl:value-of select="@urn"/></xsl:with-param>
                <xsl:with-param name="textType">Exemplar</xsl:with-param>
            </xsl:call-template>
            
        </li>
    </xsl:template>
    
    <xsl:template match="ti:translation">
        <li class="cts-translation cts-version">
            
            <p><span class="highlight">Translation: </span><xsl:apply-templates select="../../ti:groupname"/> <em><xsl:apply-templates select="../ti:title"/></em>: <strong><xsl:apply-templates select="ti:label"/></strong></p>
            
            <p><xsl:apply-templates select="ti:description"/></p>
           <xsl:call-template name="startReading">
               <xsl:with-param name="urn"><xsl:value-of select="@urn"/></xsl:with-param>
           </xsl:call-template>
            <xsl:call-template name="queryForm">
                <xsl:with-param name="urn"><xsl:value-of select="@urn"/></xsl:with-param>
                <xsl:with-param name="textType">Translation</xsl:with-param>
            </xsl:call-template>
            <ul>
            <xsl:apply-templates select="ti:exemplar">
                <xsl:with-param name="textType">Translation</xsl:with-param>
            </xsl:apply-templates>
            </ul>
        </li>
    </xsl:template>
    
    <xsl:template match="ti:groupname">
        <xsl:value-of select="."/><xsl:text> </xsl:text>
    </xsl:template>

    <xsl:template match="ti:title">
        <xsl:value-of select="."/><xsl:text> </xsl:text>
    </xsl:template>
    
    <xsl:template name="startReading">
        <xsl:param name="urn"></xsl:param>
        <p>
            <code><xsl:value-of select="$urn"/></code>
           <xsl:element name="a">
            <xsl:attribute name="href">api?request=GetFirstUrn&amp;stylesheet=cts_firsturn&amp;urn=<xsl:value-of select="$urn"/></xsl:attribute>
            (Go to first citation-unit.)
        </xsl:element>
        </p>
    </xsl:template>
    
    <xsl:template name="queryForm">
        <xsl:param name="urn"></xsl:param>
        <xsl:param name="textType"></xsl:param>
        <xsl:element name="form">
            <xsl:attribute name="action">api</xsl:attribute>
            <xsl:attribute name="method">get</xsl:attribute>
            <input type="hidden" name="request" value="GetPassagePlus"/>
            <input type="hidden" name="stylesheet" value="cts_passage"/>
            <xsl:element name="input">
                <xsl:attribute name="type">text</xsl:attribute>
                <xsl:attribute name="name">urn</xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="$urn"/>REPLACE_WITH_VALID_CITATION</xsl:attribute>
                <xsl:attribute name="size">70</xsl:attribute>
            </xsl:element>
            Units of context: 
            <xsl:element name="input">
                <xsl:attribute name="type">text</xsl:attribute>
                <xsl:attribute name="name">context</xsl:attribute>
                <xsl:attribute name="value">0</xsl:attribute>
                <xsl:attribute name="size">3</xsl:attribute>
            </xsl:element>
            <xsl:element name="input">
            <xsl:attribute name="type">submit</xsl:attribute>
            
            <xsl:attribute name="value">Browse <xsl:value-of select="$textType"/></xsl:attribute>
            </xsl:element>
        </xsl:element>
        <xsl:element name="form">
            <xsl:attribute name="action">api</xsl:attribute>
            <xsl:attribute name="method">get</xsl:attribute>
            <input type="hidden" name="request" value="GetValidReff"/>
            <input type="hidden" name="stylesheet" value="cts_validreff"/>
            <input type="hidden" name="level" value="1"/>
            <xsl:element name="input">
                <xsl:attribute name="type">text</xsl:attribute>
                <xsl:attribute name="name">urn</xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="$urn"/></xsl:attribute>
                <xsl:attribute name="size">40</xsl:attribute>
            </xsl:element>
            
            <xsl:element name="input">
                <xsl:attribute name="type">submit</xsl:attribute>
                
                <xsl:attribute name="value">See Valid Citations</xsl:attribute>
            </xsl:element>
        </xsl:element>
        <!-- 
            
            -->
       
    </xsl:template>
    
    <xsl:template match="@*|node()" priority="-1">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
