package com.softure.fe.domain;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStore.ProtectionParameter;
import java.security.Provider;
import java.security.cert.X509Certificate;

import xades4j.providers.impl.KeyStoreKeyingDataProvider;
import xades4j.providers.impl.PKCS11KeyStoreKeyingDataProvider;

public class KeyStoreDataProvider extends KeyStoreKeyingDataProvider {

	public static Builder builder(String keyStoreType, File keyStoreFile,
			SigningCertificateSelector certificateSelector) {
		return new Builder(keyStoreType, keyStoreFile, certificateSelector);
	}

	private KeyStoreDataProvider(Builder builder) {
		super(new KeyStoreBuilderCreator() {
			@Override
			public KeyStore.Builder getBuilder(ProtectionParameter loadProtection) {

				return KeyStore.Builder.newInstance(builder.keyStoreType, builder.provider, builder.keyStoreFile,
						loadProtection);
			}
		}, builder.certificateSelector, builder.storePasswordProvider, builder.entryPasswordProvider,
				builder.fullChain);
	}

	@Override
	protected KeyStore.ProtectionParameter getKeyProtection(String entryAlias, X509Certificate entryCert,
			KeyEntryPasswordProvider entryPasswordProvider) {
		if (null == entryPasswordProvider) {
			return null;
		}

		return new KeyStore.PasswordProtection(entryPasswordProvider.getPassword(entryAlias, entryCert));
	}

	public static final class Builder {
		private final String keyStoreType;
		private final File keyStoreFile;
		private final SigningCertificateSelector certificateSelector;
		private KeyStorePasswordProvider storePasswordProvider;
		private KeyEntryPasswordProvider entryPasswordProvider;
		private boolean fullChain;
		private Provider provider;

		private Builder(String keyStoreType, File keyStoreFile, SigningCertificateSelector certificateSelector) {
			this.keyStoreType = keyStoreType;
			this.keyStoreFile = keyStoreFile;
			this.certificateSelector = certificateSelector;
			this.fullChain = false;
		}

		/**
		 * Create a new {@link PKCS11KeyStoreKeyingDataProvider} based on the current
		 * configuration.
		 *
		 * @return the provider
		 */
		public KeyStoreDataProvider build() {
			return new KeyStoreDataProvider(this);
		}

		/**
		 * Sets the provider of the keystore loading password.
		 *
		 * @param storePasswordProvider keystore password provider
		 * @return the current instance
		 */
		public Builder storePassword(KeyStorePasswordProvider storePasswordProvider) {
			this.storePasswordProvider = storePasswordProvider;
			return this;
		}

		/**
		 * Sets the provider of entry passwords
		 *
		 * @param entryPasswordProvider entry password provider
		 * @return the current instance
		 */
		public Builder entryPassword(KeyEntryPasswordProvider entryPasswordProvider) {
			this.entryPasswordProvider = entryPasswordProvider;
			return this;
		}

		/**
		 * Sets whether the full certificate chain should be returned, if available.
		 *
		 * @param fullChain {@code true} to return the full certificate chain, false
		 *                  otherwise
		 * @return the current instance
		 */
		public Builder fullChain(boolean fullChain) {
			this.fullChain = fullChain;
			return this;
		}

		/**
		 * Sets the provider from which the KeyStore is to be instantiated.
		 *
		 * @param provider the provider
		 * @return the current instance
		 */
		public Builder provider(Provider provider) {
			this.provider = provider;
			return this;
		}
	}

}
