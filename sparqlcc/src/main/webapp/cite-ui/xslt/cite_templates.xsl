<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cite="http://chs.harvard.edu/xmlns/cite" version="1.0">

    <xsl:output method="html" omit-xml-declaration="yes"/>


    <xsl:template name="cite_requestData">
        <div class="cite_requestData">
            <p>Request parameters:</p>
            <ul>
                <xsl:for-each select="//cite:request/*">
                    <li>Parameter: <xsl:value-of select="local-name()"/> = <xsl:value-of
                            select="current()"/></li>
                </xsl:for-each>
            </ul>
        </div>
    </xsl:template>

    <xsl:template name="cite_getObjectLink">
        <xsl:param name="urn"/>
        <xsl:element name="a">
            <xsl:attribute name="href">
                <xsl:value-of select="$getObjectPlusUrl"/>
                <xsl:value-of select="$urn"/>
            </xsl:attribute>
            <code>
                <xsl:value-of select="$urn"/>
            </code>
        </xsl:element>
    </xsl:template>

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
                    home </xsl:element>

            </p>

        </nav>
    </xsl:template>

    <xsl:template name="cite_pagedVR">
        <xsl:if test="//cite:request/cite:request = 'GetPagedValidReff'">
            <xsl:variable name="total">
                <xsl:value-of select="//cite:request//cite:count"/>
            </xsl:variable>
            <xsl:variable name="offset">
                <xsl:value-of select="//cite:request//cite:offset"/>
            </xsl:variable>
            <xsl:variable name="limit">
                <xsl:value-of select="//cite:request//cite:limit"/>
            </xsl:variable>

            <div class="cite_pagedNav cite_nav">
                
               
              
                
                <!-- Backwards Navigation -->
                <xsl:if test="(//cite:request/cite:prevOffset != '0') and (//cite:request/cite:prevOffset)"> 
                   <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="$getPagedValidReffUrl"/><xsl:value-of
                            select="//cite:request/cite:resolvedUrn"
                        />&amp;limit=<xsl:value-of
                            select="//cite:request/cite:prevLimit"
                        />&amp;offset=<xsl:value-of
                            select="//cite:request/cite:prevOffset"
                        /></xsl:attribute> previous <xsl:value-of
                            select="//cite:request/cite:nextLimit"/></xsl:element></xsl:if>
                
                | 
                
                <!-- Forwards Navigation -->
                <xsl:if test="(//cite:request/cite:nextOffset != '0') and (//cite:request/cite:nextOffset)"> 
                <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="$getPagedValidReffUrl"/><xsl:value-of
                            select="//cite:request/cite:resolvedUrn"
                        />&amp;limit=<xsl:value-of
                            select="//cite:request/cite:nextLimit"
                        />&amp;offset=<xsl:value-of
                            select="//cite:request/cite:nextOffset"
                        /></xsl:attribute> next <xsl:value-of
                            select="//cite:request/cite:nextLimit"/>
                </xsl:element>
                    </xsl:if>
                
                (out of <xsl:value-of select="$total"/>) </div>

        </xsl:if>
    </xsl:template>
    
    <xsl:template name="cite_object">
        
        <div class="cite_object">
            <h2><xsl:value-of select="//cite:reply//cite:citeObject/@urn"/></h2>
            <table class="cite_objectTable">
                <tr>
                    <th>Property</th>
                    <th>Type</th>
                    <th>Value</th>
                </tr>
                <xsl:for-each select="//cite:reply/cite:citeObject/cite:citeProperty">
                    <tr>
                        <td class="cite_propertyNameCell"><xsl:value-of select="current()/@name"/><span class="cite_propertyLabelSpan"><xsl:value-of select="current()/@label"/></span></td>
                        <td class="cite_propertyTypeCell"><xsl:value-of select="current()/@type"/></td>
                        <xsl:element name="td">
                            <xsl:attribute name="class">cite_propertyValueCell cite_<xsl:value-of select="current()/@type"/></xsl:attribute>
                            <xsl:apply-templates select="current()"/>
                        </xsl:element>
                      
                    </tr>
                </xsl:for-each>
            </table>
            
        </div>
        
    </xsl:template>



</xsl:stylesheet>
