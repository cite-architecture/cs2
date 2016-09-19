<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns="http://chs.harvard.edu/xmlns/citeimg" 
    xmlns:citeimg="http://chs.harvard.edu/xmlns/citeimg" 
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
                        test="/citeimg:CITEError">
                        <title>Error</title>
                    </xsl:when>
                    <xsl:otherwise>
                        <title>CITE: Get Image Plus for <xsl:value-of select="//citeimg:resolvedUrn"/>
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
                    
                    <h1>Image</h1>
                    <h2><xsl:value-of select="//citeimg:request/citeimg:resolvedUrn"/></h2> 
                    <p><xsl:value-of select="//citeimg:reply/citeimg:caption"/></p>
                    
                    <p>
                        <xsl:element name="a">
                            <xsl:attribute name="href"><xsl:value-of select="$ictUrl"/><xsl:value-of select="//citeimg:request/citeimg:resolvedUrn"/></xsl:attribute>
                            Cite and quote this image.
                        </xsl:element>
                        
                        
                        
                    </p>
                    
                    <p>The image is linked to a view you can zoom/pan.</p>
                    <p>
                        <xsl:element name="a">
                            <xsl:attribute name="href"><xsl:value-of select="translate(//citeimg:reply/citeimg:zoomableUrl,' ','')"/></xsl:attribute>
                            <xsl:element name="img">
                                <xsl:attribute name="src"><xsl:value-of select="translate(//citeimg:reply/citeimg:binaryUrl,' ','')"/></xsl:attribute>
                            </xsl:element>
                        </xsl:element>
                    </p>
                    
                    <h2>Rights</h2>
                    
                    <p><xsl:value-of select="//citeimg:reply/citeimg:rights"/></p>
                    
                </article>
                
                <footer>
                    <xsl:call-template name="cite_header"/>
                </footer>
                
            </body>
        </html>
        
        
        
    </xsl:template>
    
</xsl:stylesheet>
