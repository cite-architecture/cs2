package edu.holycross.shot.sparqlimg

import static org.junit.Assert.*
import org.junit.Test

import edu.holycross.shot.sparqlimg.CiteImage
import edu.harvard.chs.cite.CiteUrn
import edu.harvard.chs.cite.CtsUrn
import edu.holycross.shot.prestochango.*

class TestRequestsIntegr extends GroovyTestCase {
    // use default fuseki settings to test
    String serverUrl = "http://localhost:3030/ds/"
    String iipsrv = "http://beta.hpcc.uh.edu/fcgi-bin/iipsrv.fcgi"

    groovy.xml.Namespace img = new groovy.xml.Namespace("http://chs.harvard.edu/xmlns/citeimg")

    String tstImg = "urn:cite:ecod:codbod8.cb-0008_001r"
    CiteUrn tstUrn = new CiteUrn(tstImg)

    void testCaption() {
        CiteImage chsi = new CiteImage(serverUrl, iipsrv)

        String expectedCaption = "Cologny, Fondation Martin Bodmer, Cod. Bodmer 8, folio 1 recto."
        def root = new XmlParser().parseText(chsi.getCaptionReply(tstImg))
        String actualCaption
        System.err.println "img reply = " + root
        root[img.reply][img.caption].each { c ->
            System.err.println "c.text() = " + c.text()
            actualCaption = c.text().replaceAll(/^[ ]+/,"")
            actualCaption = actualCaption.replaceAll(/[ ]+$/,"")
        }
        assert actualCaption == expectedCaption
    }

    void testRights() {
        CiteImage chsi = new CiteImage(serverUrl, iipsrv)

        String expectedRights = "This image is available under the terms of a Creative Commons license (CC BY-NC 3.0).  Any reuse must include the following attribution: Cologny, Fondation Martin Bodmer, Cod. Bodmer 8, f. 1r (www.e-codices.unifr.ch)."

        def root = new XmlParser().parseText(chsi.getRightsReply(tstImg))
        String actualRights
        root[img.reply][img.rights].each { rts ->
            actualRights = rts.text().replaceAll(/^[ ]+/,"")
            actualRights = actualRights.replaceAll(/[ ]+$/,"")
        }
        assert actualRights == expectedRights
    }

}
