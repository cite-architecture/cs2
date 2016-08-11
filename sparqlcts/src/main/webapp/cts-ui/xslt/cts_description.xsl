<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:cts="http://chs.harvard.edu/xmlns/cts" xmlns:dc="http://purl.org/dc/elements/1.1" xmlns:tei="http://www.tei-c.org/ns/1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output omit-xml-declaration="yes" method="html" encoding="UTF-8"/>
    <xsl:include href="header.xsl"/>
    <xsl:include href="chs_tei_to_html5.xsl"/>
	<xsl:include href="alternates.xsl"/>
    
    <xsl:variable name="homeUrl">@homeUrl@</xsl:variable>
    <xsl:variable name="formsUrl">@queryforms@</xsl:variable>
	
   
	<!-- Framework for main body of document -->
	<xsl:template match="/">
		<!-- can some of the reply contents in xsl variables
			for convenient use in different parts of the output -->
		<xsl:variable name="urnString">
			<xsl:value-of select="//cts:request/cts:urn"/>
		</xsl:variable>
		
		
		
		<html>
			<head>
				
				
				<link
						href="css/graph.css"
					rel="stylesheet"
					title="CSS for CTS"
					type="text/css"/>
			<link
						href="@coreCss@"
					rel="stylesheet"
					title="CSS for CTS"
					type="text/css"/>
			    <link rel="stylesheet" href="css/cite_common.css"></link>
				<xsl:choose>
					<xsl:when
						test="/cts:CTSError">
						<title>Error</title>
					</xsl:when>
					<xsl:otherwise>
						<title><xsl:value-of
							select="//cts:reply/cts:description/cts:groupname"/>, <xsl:value-of
								select="//cts:reply/cts:description/cts:title"/> (<xsl:value-of
									select="//cts:reply/cts:description/cts:label"/>): 
							<xsl:call-template name="urnPsg">
								<xsl:with-param name="urnStr">
									<xsl:value-of select="$urnString"/>
								</xsl:with-param>
							</xsl:call-template>
						</title>
					</xsl:otherwise>
				</xsl:choose>
			</head>
			<body>
				
				
			    <header>
			        <xsl:call-template name="header"/>
			    </header>
				<nav>
					<p>  @projectlabel@:
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
					<xsl:choose>
						<xsl:when
							test="/cts:CTSError">
							<xsl:apply-templates
								select="cts:CTSError"/>
						</xsl:when>
						<xsl:otherwise>
							<h2>Description</h2>
							
								<ul class="cite_caption">
									<li>Group Name:  <xsl:value-of select="//cts:reply/cts:description/cts:groupname"/></li>
									<li>Work Name: <xsl:value-of select="//cts:reply/cts:description/cts:title"/></li>
									<li>Version: <xsl:value-of select="//cts:reply/cts:description/cts:label"/></li>
									<li>URN: <xsl:value-of select="$urnString"/></li>
								</ul>
								
								
						
						</xsl:otherwise>
					</xsl:choose>
					
				</article>
			    <footer>
			        <xsl:call-template name="footer"/>
			    </footer>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="urnPsg">
		<xsl:param name="urnStr"/>
		<xsl:choose>
			<xsl:when test="contains($urnStr,':')">
				<xsl:call-template name="urnPsg">
					<xsl:with-param name="urnStr">
						<xsl:value-of select="substring-after($urnStr,':')"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$urnStr"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- Default: replicate unrecognized markup -->
	<xsl:template match="@*|node()" priority="-1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
