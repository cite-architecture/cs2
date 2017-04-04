package edu.holycross.shot.sparqlcc

import static org.junit.Assert.*
import org.junit.Test
import org.custommonkey.xmlunit.*

import edu.holycross.shot.sparqlcc.CcGraph
import edu.harvard.chs.cite.Cite2Urn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*


class TestReplyGetObjectIntegr extends GroovyTestCase {


  String baseUrl = "http://localhost:8080/fuseki/cc/query"
  Sparql sparql = new Sparql(baseUrl)
  CcGraph cc = new CcGraph(sparql)


  @Test
  void testTest(){
    assert true
  }

  @Test
  void testGetObjectOrdered1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:5")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
		<GetObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
			<cite:request>
					<requestUrn>urn:cite2:hmt:venAsign.v1:5</requestUrn>
					<request>GetObject</request>
					<resolvedUrn>urn:cite2:hmt:venAsign.v1:5</resolvedUrn>
			</cite:request>
			<cite:reply>
				<citeObject urn="urn:cite2:hmt:venAsign.v1:5">
					<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:5</citeProperty>
					<citeProperty name="Label" label="Label" type="string">Sign 5.v1</citeProperty>
					<citeProperty name="Sequence" label="Sequence" type="number">5</citeProperty>
					<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
					<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.12</citeProperty>
					<prevUrn>urn:cite2:hmt:venAsign.v1:4</prevUrn>
					<nextUrn>urn:cite2:hmt:venAsign.v1:6</nextUrn>
				</citeObject>
			</cite:reply>
		</GetObject>
		"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetObjectOrdered2(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:6")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
	<cite:request>
	    <requestUrn>urn:cite2:hmt:venAsign.v1:6</requestUrn>
	    <request>GetObject</request>
	    <resolvedUrn>urn:cite2:hmt:venAsign.v1:6</resolvedUrn>
	</cite:request>
	<cite:reply>
		<citeObject urn="urn:cite2:hmt:venAsign.v1:6">
			<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:6</citeProperty>
			<citeProperty name="Label" label="Label" type="string">Sign 6.v1</citeProperty>
			<citeProperty name="Sequence" label="Sequence" type="number">6</citeProperty>
			<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
			<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.13</citeProperty>
			<prevUrn>urn:cite2:hmt:venAsign.v1:5</prevUrn>
			<nextUrn>urn:cite2:hmt:venAsign.v1:7</nextUrn>
		</citeObject>
	</cite:reply>
</GetObject>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetObjectOrdered3(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:7")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
	<cite:request>
	    <requestUrn>urn:cite2:hmt:venAsign.v1:7</requestUrn>
	    <request>GetObject</request>
	    <resolvedUrn>urn:cite2:hmt:venAsign.v1:7</resolvedUrn>
	</cite:request>
	<cite:reply>
		<citeObject urn="urn:cite2:hmt:venAsign.v1:7">
			<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:7</citeProperty>
			<citeProperty name="Label" label="Label" type="string">Sign 7.v1</citeProperty>
			<citeProperty name="Sequence" label="Sequence" type="number">7</citeProperty>
			<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
			<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.14</citeProperty>
			<prevUrn>urn:cite2:hmt:venAsign.v1:6</prevUrn>
			<nextUrn>urn:cite2:hmt:venAsign.v1:8</nextUrn>
		</citeObject>
	</cite:reply>
</GetObject>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetObjectOrdered4(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:venAsign.v1:8")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
	<cite:request>
	    <requestUrn>urn:cite2:hmt:venAsign.v1:8</requestUrn>
	    <request>GetObject</request>
	    <resolvedUrn>urn:cite2:hmt:venAsign.v1:8</resolvedUrn>
	</cite:request>
	<cite:reply>
		<citeObject urn="urn:cite2:hmt:venAsign.v1:8">
			<citeProperty name="OccurrenceUrn" label="The URN for this occurrence of a critical sign" type="Cite2Urn">urn:cite2:hmt:venAsign.v1:8</citeProperty>
			<citeProperty name="Label" label="Label" type="string">Sign 8.v1</citeProperty>
			<citeProperty name="Sequence" label="Sequence" type="number">8</citeProperty>
			<citeProperty name="CriticalSign" label="A URN identifying the kind of critical sign" type="Cite2Urn">urn:cite2:hmt:critsigns:asterisk</citeProperty>
			<citeProperty name="TextPassage" label="The Iliadic passage that the sign marks." type="ctsurn">urn:cts:greekLit:tlg0012.tlg001.msA:1.15</citeProperty>
			<prevUrn>urn:cite2:hmt:venAsign.v1:7</prevUrn>
			<nextUrn>urn:cite2:hmt:venAsign.v1:9</nextUrn>
		</citeObject>
	</cite:reply>
</GetObject>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }


  @Test
  void testGetObjectUnordered1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:vaimg:VA082RN_0083")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:vaimg:VA082RN_0083</requestUrn>
    <request>GetObject</request>
    <resolvedUrn>urn:cite2:hmt:vaimg.v1:VA082RN_0083</resolvedUrn>
</cite:request>
<cite:reply>
    <citeObject urn="urn:cite2:hmt:vaimg.v1:VA082RN_0083">
        <citeProperty name="Image" label="Image URN" type="Cite2Urn">urn:cite2:hmt:vaimg.v1:VA082RN_0083</citeProperty>
        <citeProperty name="Label" label="Caption" type="string">Venetus A: Marcianus Graecus Z. 454 (= 822).  Photograph in natural light, folio 82, recto.</citeProperty>
        <citeProperty name="Rights" label="Rights" type="string">This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ÔøΩ2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.</citeProperty>
        <extension>http://www.homermultitext.org/cite/rdf/CiteImage</extension>
    </citeObject>
</cite:reply>
</GetObject>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }


  @Test
  void testGetObjectUnordered2(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:hmt:vaimg:VA094RN_0095")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

    String expectedXml = """
<GetObject xmlns="http://chs.harvard.edu/xmlns/cite" xmlns:cite="http://chs.harvard.edu/xmlns/cite">
<cite:request>
    <requestUrn>urn:cite2:hmt:vaimg:VA094RN_0095</requestUrn>
    <request>GetObject</request>
    <resolvedUrn>urn:cite2:hmt:vaimg.v1:VA094RN_0095</resolvedUrn>
</cite:request>
<cite:reply>
    <citeObject urn="urn:cite2:hmt:vaimg.v1:VA094RN_0095">
        <citeProperty name="Image" label="Image URN" type="Cite2Urn">urn:cite2:hmt:vaimg.v1:VA094RN_0095</citeProperty>
        <citeProperty name="Label" label="Caption" type="string">Venetus A: Marcianus Graecus Z. 454 (= 822).  Photograph in natural light, folio 94, recto.</citeProperty>
        <citeProperty name="Rights" label="Rights" type="string">This image was derived from an original ©2007, Biblioteca Nazionale Marciana, Venezie, Italia. The derivative image is ÔøΩ2010, Center for Hellenic Studies. Original and derivative are licensed under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 License. The CHS/Marciana Imaging Project was directed by David Jacobs of the British Library.</citeProperty>
        <extension>http://www.homermultitext.org/cite/rdf/CiteImage</extension>
    </citeObject>
</cite:reply>
</GetObject>
"""

		  Diff xmlDiff = new Diff(expectedXml, replyString)
		  assert xmlDiff.identical()
  }

  @Test
  void testGetOrcaObject1(){
    // set up XMLUnit
		XMLUnit.setNormalizeWhitespace(true)
		//XMLUnit.setIgnoreWhitespace(true)

    //Set up params
    String reqString = "GetObject"
    Cite2Urn reqUrn = new Cite2Urn("urn:cite2:fufolio:PlutPericles_SyntaxTokens_ORCA.v1:4")


    def reqParams = [:]
    reqParams['urn'] = reqUrn.toString()
    reqParams['request'] = reqString

    String replyString =  cc.formatXmlReply(reqString,reqUrn,reqParams)
    System.err.println("----")
    System.err.println(replyString)
    System.err.println("----")

		assert false

	}


}
