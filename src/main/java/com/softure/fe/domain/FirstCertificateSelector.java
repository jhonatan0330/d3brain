package com.softure.fe.domain;

import java.util.List;

import xades4j.providers.impl.KeyStoreKeyingDataProvider.SigningCertificateSelector;

public class FirstCertificateSelector implements SigningCertificateSelector
{
	@Override
	public Entry selectCertificate(List<Entry> availableCertificates) {
		return availableCertificates.get(0);
	}
}
