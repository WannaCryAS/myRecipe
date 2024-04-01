package com.wannacry.myrecipe.adapter;

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class XMLParser {

    fun parseXML(xmlData: InputStream) {
        var parserFactory: XmlPullParserFactory
        var parser: XmlPullParser

//        parser.setInput(xmlData,null)

        try {
            parserFactory = XmlPullParserFactory.newInstance()
            parser = parserFactory.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false)
            parser.setInput(xmlData,null)
        } catch (e: XmlPullParserException) {

        }
    }
}
