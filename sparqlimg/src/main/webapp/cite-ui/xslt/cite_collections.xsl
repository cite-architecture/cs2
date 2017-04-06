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
                        <title>Collections Extended by CITE-Image
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
                    <h1>Collections Extended by CITE-Image </h1>

                    <ul>
                        <xsl:for-each select="//citeimg:collection">

                            <xsl:apply-templates select="current()"/>
                        </xsl:for-each>

                    </ul>


                </article>

                <footer>
                    <xsl:call-template name="cite_footer"/>
                </footer>

            </body>
        </html>



    </xsl:template>



    <xsl:template match="citeimg:collection">
        <li><xsl:element name="a">
            <xsl:attribute name="href"><xsl:value-of select="$getPagedUrl"/><xsl:value-of select="@urn"/></xsl:attribute>
            <xsl:value-of select="@urn"/>
        </xsl:element>
        </li>


    </xsl:template>



    <xsl:template match="@*|node()" priority="-1">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
