<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:citeimg="http://chs.harvard.edu/xmlns/citeimg" exclude-result-prefixes="citeimg"
    version="1.0">
    
   
    <xsl:template match="/">
        
        
        
        <html>
            <head>
                <meta charset="UTF-8"/>
               <xsl:element name="meta">
                    <xsl:attribute name="http-equiv">refresh</xsl:attribute>
                    <xsl:attribute name="content">1;url=http://beta.hpcc.uh.edu/moo/remote_moo.html?path=<xsl:value-of select="//citeimg:imgPath/@val"/>&amp;label=<xsl:value-of select="//citeimg:label"/>&amp;roi=<xsl:value-of select="//citeimg:roi/@val"/>&amp;urn=<xsl:value-of select="//citeimg:urn"/>&amp;origurl=@images@</xsl:attribute>
                </xsl:element>
                <script type="text/javascript">
						window.location.href = "http://beta.hpcc.uh.edu/moo/remote_moo.html?path=<xsl:value-of select="//citeimg:imgPath/@val"/>&amp;label=<xsl:value-of select="//citeimg:label"/>&amp;roi=<xsl:value-of select="//citeimg:roi/@val"/>&amp;urn=<xsl:value-of select="//citeimg:urn"/>&amp;origurl=@images@"
                </script>
                <title>Remote Redirect: <xsl:apply-templates select="//citeimg:id"></xsl:apply-templates>: zoomable image</title>
              
                
             
                    
                
            </head>
            
            <body>
                <p>redirect</p>
                
            </body>
        </html>
    </xsl:template>
   
</xsl:stylesheet>
