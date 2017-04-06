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
            <ul>
                <li>
                <xsl:element name="a">
                    <xsl:attribute name="href">@cchome@</xsl:attribute>
                    Collections Home </xsl:element>
                 </li>

            </ul>

        </nav>
    </xsl:template>

    <xsl:template name="cite_pagedNav">
        <xsl:param name="requestUrl"/>
        <xsl:if test="(//cite:request/cite:request = 'GetPagedValidReff') or (//cite:request/cite:request = 'GetPaged')">
           <xsl:variable name="total">
                <xsl:value-of select="//cite:request//cite:count"/>
            </xsl:variable>
            <!-- <xsl:variable name="offset">
                <xsl:value-of select="//cite:request//cite:offset"/>
            </xsl:variable>
            <xsl:variable name="limit">
                <xsl:value-of select="//cite:request//cite:limit"/>
            </xsl:variable>-->

            <div class="cite_pagedNav cite_nav">




                <!-- Backwards Navigation -->
                <xsl:if test="(//cite:request/cite:prevOffset != '0') and (//cite:request/cite:prevOffset)">
                   <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="$requestUrl"/><xsl:value-of
                            select="//cite:request/cite:resolvedUrn"
                        />&amp;limit=<xsl:value-of
                            select="//cite:request/cite:prevLimit"
                        />&amp;offset=<xsl:value-of
                            select="//cite:request/cite:prevOffset"
                        /></xsl:attribute> previous <xsl:value-of
                            select="//cite:request/cite:prevLimit"/></xsl:element></xsl:if>

                |

                <!-- Forwards Navigation -->
                <xsl:if test="(//cite:request/cite:nextOffset != '0') and (//cite:request/cite:nextOffset)">
                <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="$requestUrl"/><xsl:value-of
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
        <xsl:param name="class">single_cite_object</xsl:param>


        <div class="cite_object">
            <h2><xsl:value-of select="//cite:reply//cite:citeObject/@urn"/></h2>

                <xsl:element name="table">
                    <xsl:attribute name="class">
                        cite_objectTable
                        <xsl:value-of select="$class"/>
                    </xsl:attribute>

                <tr>
                    <th>Property</th>
                    <th>Type</th>
                    <th>Value</th>
                </tr>
                <xsl:for-each select="current()/cite:citeProperty">
                    <tr>
                        <td class="cite_propertyNameCell"><xsl:value-of select="current()/@name"/><span class="cite_propertyLabelSpan"><xsl:value-of select="current()/@label"/></span></td>
                        <td class="cite_propertyTypeCell"><xsl:value-of select="current()/@type"/></td>
                        <xsl:element name="td">
                            <xsl:attribute name="class">cite_propertyValueCell cite_<xsl:value-of select="current()/@type"/></xsl:attribute>
                            <xsl:choose>
                                <xsl:when test="current()/@type = 'CtsUrn'">
                                    <xsl:element name="a">
                                        <xsl:attribute name="href">
                                            <xsl:value-of select="$ctsUrl"/><xsl:value-of select="current()"/>
                                        </xsl:attribute>
                                        <xsl:apply-templates select="current()"/>
                                    </xsl:element>
                                </xsl:when>
                                <xsl:when test="current()/@type = 'CiteUrn'">
                                    <xsl:element name="a">
                                        <xsl:attribute name="href">
                                            <xsl:value-of select="$getObjectUrl"/><xsl:value-of select="current()"/>
                                        </xsl:attribute>
                                        <xsl:apply-templates select="current()"/>
                                    </xsl:element>
                                </xsl:when>
                                <xsl:when test="current()/@type = 'Cite2Urn'">
																	<xsl:choose>
																		<xsl:when test="contains(current()/@extendedBy, 'CiteImage')">
																			<xsl:value-of select="current()"/><br/>
					                            <xsl:element name="a">
					                                <xsl:attribute name="href"><xsl:value-of select="$imgLinkUrl"/><xsl:value-of select="current()"/></xsl:attribute>

					                            <xsl:element name="img">
					                                <xsl:attribute name="src"><xsl:value-of select="$imgThumbUrl"/><xsl:value-of select="current()"/></xsl:attribute>
					                            </xsl:element>
					                            </xsl:element>
					                            <br/>
					                            <xsl:element name="a">
					                                <xsl:attribute name="href"><xsl:value-of select="$imgICTUrl"/><xsl:value-of select="current()"/></xsl:attribute>
					                                Cite and quote this image.
					                            </xsl:element>
																		</xsl:when>
																		<xsl:otherwise>
	                                    <xsl:element name="a">
	                                        <xsl:attribute name="href">
	                                            <xsl:value-of select="$getObjectUrl"/><xsl:value-of select="current()"/>
	                                        </xsl:attribute>
	                                        <xsl:apply-templates select="current()"/>
	                                    </xsl:element>
																	</xsl:otherwise>
																		</xsl:choose>
                                </xsl:when>
                                <xsl:otherwise> <xsl:apply-templates select="current()"/></xsl:otherwise>
                            </xsl:choose>

                        </xsl:element>

                    </tr>
                </xsl:for-each>

                <!-- IF THIS OBJECT IS AN IMAGE -->
                <xsl:if test="//cite:extension = 'http://www.homermultitext.org/cite/rdf/CiteImage'">
                    <tr>
                        <td>Extension</td>
                        <td>

                          Cite Image
                        </td>
                        <td>
                            <xsl:element name="a">
                                <xsl:attribute name="href"><xsl:value-of select="$imgLinkUrl"/><xsl:value-of select="current()/@urn"/></xsl:attribute>

                            <xsl:element name="img">
                                <xsl:attribute name="src"><xsl:value-of select="$imgThumbUrl"/><xsl:value-of select="current()/@urn"/></xsl:attribute>
                            </xsl:element>
                            </xsl:element>
                            <br/>
                            <xsl:element name="a">
                                <xsl:attribute name="href"><xsl:value-of select="$imgICTUrl"/><xsl:value-of select="current()/@urn"/></xsl:attribute>
                                Cite and quote this image.
                            </xsl:element>
                        </td>
                    </tr>
                </xsl:if>
                </xsl:element>

        </div>

    </xsl:template>




</xsl:stylesheet>
