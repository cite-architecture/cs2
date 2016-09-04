<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns="http://chs.harvard.edu/xmlns/cite" 
    xmlns:cite="http://chs.harvard.edu/xmlns/cite" 
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:tei="http://www.tei-c.org/ns/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output omit-xml-declaration="yes" method="html" encoding="UTF-8"/>
    <xsl:include href="cite_header.xsl"/>
    
   <xsl:include href="cite_variables.xsl"/>
    <xsl:include href="cite_templates.xsl"/>
    
    
    <xsl:template match="/">
        
        <html>
            <head>
                
                <meta name="viewport" content="width=device-width; initial-scale=1.0"/>
                <link href="cite-ui/css/cite-core.css" rel="stylesheet" title="CSS for CITE" type="text/css"/>
                
                <style>
                    p { margin: 0em; }
                </style>
                <xsl:choose>
                    <xsl:when
                        test="/cite:CITEError">
                        <title>Error</title>
                    </xsl:when>
                    <xsl:otherwise>
                        <title>Contents of CITE Library
                        </title>
                    </xsl:otherwise>
                </xsl:choose>
            </head>
            <body>
                
                
                <header>
                    <xsl:call-template name="cite_header"/>
                </header>
                
                <xsl:call-template name="cite_nav"/>
                
                <article>
                    <h1>CITE Collection Inventory</h1>
                   
                    <ul class="mH" id="invMenu" >
                            <xsl:apply-templates
                                select="//cite:citeCollection"/>
                    </ul>
                    
                    
                </article>
                
                <footer>
                    <xsl:call-template name="cite_footer"/>
                </footer>
                
            </body>
        </html>
        
        
        
    </xsl:template>
    
    <xsl:template match="cite:citeCollection">
        <div class="collection" >
            <h2>Collection: <strong><xsl:value-of select="dc:title"/></strong></h2>
            <p><code><xsl:value-of select="@urn"/></code></p>
            <div class="cite_serviceLinks">
                <p>
                    <xsl:element name="a">
                        <xsl:attribute name="href"><xsl:value-of select="$serviceUrl"/><xsl:value-of select="$getValidReffUrl"/><xsl:value-of select="@urn"/></xsl:attribute>
                        See all valid citations for collection <code><xsl:value-of select="@urn"/></code>.
                    </xsl:element>
                </p>
            </div>
            <div class="cite_requestData">
            
           
            <h3>Properties</h3>
               
            <ul>
                <xsl:if test="cite:extendedBy"><xsl:apply-templates select="cite:extendedBy"/></xsl:if>
                <xsl:apply-templates select="cite:citeProperty"/>
            </ul>
            </div>
            
        </div>
    </xsl:template>
    
  <xsl:template match="cite:citeProperty">
      <li><xsl:value-of select="@name"/> (type=<xsl:value-of select="@type"/>)</li>
  </xsl:template>
    
    <xsl:template match="cite:extendedBy">
        <li>Extended by capabilities defined by: <xsl:value-of select="@extension"/>.</li>
    </xsl:template>
    
  
    
    <xsl:template match="@*|node()" priority="-1">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
