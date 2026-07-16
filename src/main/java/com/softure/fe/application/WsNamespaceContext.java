package com.softure.fe.application;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

public class WsNamespaceContext implements NamespaceContext {

	@Override
	public String getNamespaceURI(String prefix) {
		switch (prefix) {
		case "soap":
			return "http://www.w3.org/2003/05/soap-envelope";
		case "wsse":
			return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		case "wsu":
			return "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
		case "wsa":
			return "http://www.w3.org/2005/08/addressing";
		default:
			return null;
		}
	}

	@Override
	public String getPrefix(String uri) {
		return null;
	}

	@Override
	public Iterator getPrefixes(String uri) {
		return null;
	}
}
