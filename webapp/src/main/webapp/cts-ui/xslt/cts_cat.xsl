<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:cts="http://chs.harvard.edu/xmlns/cts"
	xmlns:dc="http://purl.org/dc/elements/1.1" xmlns:tei="http://www.tei-c.org/ns/1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output encoding="UTF-8" method="html" omit-xml-declaration="yes"/>
	<xsl:include href="cts_header.xsl"/>
	
	<xsl:variable name="homeUrl">home</xsl:variable>
	<xsl:variable name="formsUrl">home</xsl:variable>
	
	<!-- Framework for main body of document -->
	
	<xsl:template match="/">
		<!-- can some of the reply contents in xsl variables
			for convenient use in different parts of the output -->
		<xsl:variable name="urnString">
			<xsl:value-of select="//cts:request/cts:requestUrn"/>
		</xsl:variable>
		<xsl:variable name="catVar">api?request=<xsl:value-of select="//cts:request/cts:requestName"/>&amp;stylesheet=cts_passage&amp;urn=<xsl:value-of
			select="normalize-space(//cts:reply/cts:urn)"/></xsl:variable>
		<html>
			<head>
				<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
				<!--<link href="css/jquery-ui.css" rel="stylesheet"/>-->
				<link href="cts-ui/css/cts-core.css" rel="stylesheet" title="CSS for CTS" type="text/css"/>
				<link href="cts-ui/css/cts.css" rel="stylesheet"/>
				<link href="cts-ui/css/chs-tei.css" rel="stylesheet"/>
				<link href="cts-ui/css/cat.css" rel="stylesheet"/>
				<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css"/>
				<script src="cts-ui/js/jquery.min.js"></script>
				<script src="cts-ui/js/selector.js"></script>
				<xsl:choose>
					<xsl:when test="/cts:CTSError">
						<title>Error</title>
					</xsl:when>
					<xsl:otherwise>
						<title>CTS Citation Alignment Tool: <xsl:value-of select="//cts:reply/cts:label/urn"/>
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
							/></xsl:attribute> home </xsl:element> | 
						
						<xsl:element name="a">
							<xsl:attribute name="href">
								<xsl:value-of select="$catVar"/>
							</xsl:attribute> browse view </xsl:element>
					</p>
				</nav>
				<article>
					<xsl:choose>
						<xsl:when test="/cts:CTSError">
							<xsl:apply-templates select="cts:CTSError"/>
						</xsl:when>
						<xsl:otherwise>
							<div id="clipBoard" class="hide">
								<textarea rows="25" cols="80" wrap="soft" class=""/>
								<i id="closeClipboard" class='fa fa-close'></i>
							</div>
							<div id="addNote" class="hide">
								<h2>Add Note:</h2>
								<p id="noteUrn">urn:cts:greekLit…</p>
								<input type="text" placeholder=":input"/>
								<p><span id="cancelNoteButton">Cancel</span><span id="addNoteButton">Add</span></p>
							</div>
							<h1>Citation Alignment Tool</h1>
							<p>Drag in the text-passage to create CTS-URNs with extended references. The [+] button will add a selected URN to a list, which can be annotated and saved.</p>
							<h2><xsl:value-of select="//cts:reply/cts:label"/>
							</h2>
							<p>
								<xsl:value-of select="//cts:reply/cts:urn"/>
							</p>
							<div id="selectedUrn">
								<i class="fa fa-plus-square hide"></i>
								<p>- a selected urn will go here -</p>
							</div>
							<div id="textDisplay">
									<xsl:apply-templates select="//cts:reply"/>
									
									<div style="clear:both"></div>
							</div>
							<div id="urnListDiv">
								<h3>Saved URNs <i class="fa fa-clipboard"></i></h3>
								<ul id="urnList">
									
								</ul>
								
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
		<xsl:element name="div">
			<xsl:attribute name="lang">
				<xsl:value-of select="//passage/@xml:lang"/>
			</xsl:attribute>
			<xsl:attribute name="id">cts-textReply</xsl:attribute>
			<!-- This is where we will catch TEI markup -->
			<xsl:apply-templates/>
			<!-- ====================================== -->
		</xsl:element>
	</xsl:template>
	<xsl:template match="cts:urn"/>
	<xsl:template match="cts:groupname"/>
	<xsl:template match="cts:title"/>
	<xsl:template match="cts:label"/>
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
	
	<xsl:template match="cts:prevnext">
		
			
			
				<div class="prevnext">
					<span class="prv">
						<xsl:if test="normalize-space(cts:prev) != ''">
							
									<xsl:variable name="prvVar"
										>api?request=<xsl:value-of select="//cts:request/cts:requestName"/>&amp;stylesheet=cts_passage&amp;urn=<xsl:value-of
											select="normalize-space(cts:prev)"/></xsl:variable>
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="$prvVar"/>
										</xsl:attribute> prev </xsl:element>
								
							
						</xsl:if>
						<xsl:if test="normalize-space(cts:prev) = ''"> (at the beginning of the text) </xsl:if>
					</span> | <span class="nxt">
						<xsl:if test="normalize-space(cts:next) != ''">
			
								
									<xsl:variable name="nxtVar"
										>api?request=<xsl:value-of select="//cts:request/cts:requestName"/>&amp;stylesheet=cts_passage&amp;urn=<xsl:value-of
											select="normalize-space(cts:next)"/></xsl:variable>
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="$nxtVar"/>
										</xsl:attribute> next </xsl:element>
								
							
						</xsl:if>
						<xsl:if test="normalize-space(cts:next) = ''"> (at end of the text) </xsl:if>
					</span>
				</div>
			
		
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
	
	<!-- Special!! Identify leaf-nodes -->
	<xsl:template match="cts:node">		
		<xsl:element name="mark">
			<xsl:choose>
				
				<xsl:when test="tei:w">
					<xsl:attribute name="class">cts_node cts_inline</xsl:attribute>
				</xsl:when>
				
				<xsl:otherwise>
					<xsl:attribute name="class">cts_node cts_block</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:attribute name="data-ctsurn">
				<xsl:value-of select="@urn"/>
			</xsl:attribute>
			<xsl:attribute name="data-ctspassage">
				<xsl:value-of select="@passage"/>
			</xsl:attribute>
			<xsl:apply-templates/><xsl:text> </xsl:text>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="tei:milestone[@type='left']">
		<mark class=" bracket left-bracket" id="1"/>
	</xsl:template>
	<xsl:template match="tei:milestone[@type='right']">
		<mark class="bracket right-bracket" id="2"/>
	</xsl:template>

	<!-- Default: replicate unrecognized markup -->
	<!--<xsl:template match="@*|node()" priority="-1">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>-->
</xsl:stylesheet>
