<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:cts="http://chs.harvard.edu/xmlns/cts"
    xmlns:dc="http://purl.org/dc/elements/1.1" xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes"/>
    <xsl:include href="cts_header.xsl"/>
    <xsl:include href="chs_tei_to_html5.xsl"/>
    <xsl:variable name="homeUrl">home</xsl:variable>
    <xsl:variable name="formsUrl">home</xsl:variable>
    
    <!-- Framework for main body of document -->
    
    <xsl:template match="/">
        <!-- can some of the reply contents in xsl variables
			for convenient use in different parts of the output -->
        <xsl:variable name="urnString">
            <xsl:value-of select="//cts:request/cts:requestUrn"/>
        </xsl:variable>
        
        <html>
            <head>
                <meta name="viewport" content="width=device-width; initial-scale=1.0"/>
                <link href="cts-ui/css/cts-core.css" rel="stylesheet" title="CSS for CTS" type="text/css"/>
                <link href="cts-ui/css/cts.css" rel="stylesheet"/>
                <link href="cts-ui/css/chs-tei.css" rel="stylesheet"/>
                <xsl:choose>
                    <xsl:when test="/cts:CTSError">
                        <title>Error</title>
                    </xsl:when>
                    <xsl:otherwise>
                        <title>Valid References: <xsl:value-of select="//cts:request/cts:requestUrn"/>
                        </title>
                    </xsl:otherwise>
                </xsl:choose>
            </head>
            <body>
                <header>
                    <xsl:call-template name="header"/>
                </header>
                <nav>
                    <p>navigation: <xsl:element name="a">
                        <xsl:attribute name="href"><xsl:value-of select="$homeUrl"
                        /></xsl:attribute> home </xsl:element>
                    </p>
                </nav>
                <article>
                    <xsl:choose>
                        <xsl:when test="/cts:CTSError">
                            <xsl:apply-templates select="cts:CTSError"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <h1>Valid References: <xsl:value-of select="//cts:request/cts:requestUrn"/>
                            </h1>
                            <p>
                                <xsl:value-of select="//cts:reply/cts:urn"/>
                            </p>
                            
                            <div id="textDisplay">
                                <xsl:apply-templates select="//cts:reply"/>
                                
                                <div style="clear:both"></div>
                            </div>
                            
                            
                        </xsl:otherwise>
                    </xsl:choose>
                </article>
                <footer>
                    <xsl:call-template name="footer"/>
                </footer>
            </body>
        </html>
    </xsl:template>
    <!-- End Framework for main body document -->
    
    
    <!-- Match elements of the CTS reply -->
    <xsl:template match="cts:reply">
        <xsl:variable name="level">
            <xsl:value-of select="//cts:request/cts:level"/>
        </xsl:variable>
        <xsl:element name="div">
            <xsl:attribute name="lang">
                <xsl:value-of select="//passage/@xml:lang"/>
            </xsl:attribute>
            <xsl:attribute name="id">cts-textReply</xsl:attribute>
            <!-- This is where we will catch TEI markup -->
            <ul>
            <xsl:for-each select="cts:reff/cts:urn">
                <li>
                    <xsl:value-of select="current()"/> [
                    <xsl:element name="a">
                        <xsl:attribute name="href">api?request=GetPassagePlus&amp;stylesheet=cts_passage&amp;urn=<xsl:value-of
                            select="normalize-space(current())"/></xsl:attribute>
                        see text
                    </xsl:element>
                    
                    &#160; | &#160; <xsl:element name="a">
                        <xsl:attribute name="href">api?request=GetValidReff&amp;stylesheet=cts_validreff&amp;urn=<xsl:value-of
                            select="normalize-space(current())"/>&amp;level=<xsl:value-of select="$level + 1"/></xsl:attribute>
                        list contained citations
                    </xsl:element> ]
                
                </li>
            </xsl:for-each>
            </ul>
            
        </xsl:element>
    </xsl:template>
    
    
    
    

    <xsl:template match="cts:CTSError">
        <h1>CTS Error</h1>
        <p class="cts:error">
            <xsl:apply-templates select="cts:message"/>
        </p>
        <p>Error code: <xsl:apply-templates select="cts:code"/></p>
        <p>Error code: <xsl:apply-templates select="cts:code"/></p>
        <p>CTS library version: <xsl:apply-templates select="cts:libraryVersion"/>
        </p>
        <p>CTS library date: <xsl:apply-templates select="cts:libraryDate"/>
        </p>
    </xsl:template>
    
    
</xsl:stylesheet>
