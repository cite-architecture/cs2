<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite" version="1.0">

    <xsl:output method="html" omit-xml-declaration="yes"/>


  

    <xsl:template name="cite_getValidReffLink">
        <div class="cite_serviceLinks">
            <p>
                <xsl:element name="a">
                    <xsl:attribute name="href"><xsl:value-of select="$serviceUrl"/><xsl:value-of
                            select="$getValidReffUrl"/><xsl:value-of select="//cite:resolvedUrn"
                        /></xsl:attribute> See all valid citations for collection
                            <code><xsl:value-of select="//cite:resolvedUrn"/></code>. </xsl:element>
            </p>
        </div>
    </xsl:template>

    <xsl:template name="cite_error">
        <h1>CITE Error</h1>
        <xsl:apply-templates select="//cite:message"/>
        <xsl:apply-templates select="//cite:code"/>
    </xsl:template>

    <xsl:template name="cite_nav">
        <nav>
            <p>
                <xsl:element name="a">
                    <xsl:attribute name="href"><xsl:value-of select="$homeUrl"/></xsl:attribute>
                    Image Home </xsl:element>

            </p>

        </nav>
    </xsl:template>

  

    


</xsl:stylesheet>
