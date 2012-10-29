/**
 * 
 */
package org.nexyu.nexyuAndroid.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.nexyu.nexyuAndroid.utils.StringUtils;

import android.util.Log;

/**
 * @author Paul Ecoffet
 *
 */
public class FingerPrintTrustManager implements X509TrustManager
{
	String fingerprint = null;

	/**
	 * @param fingerprint
	 */
	public FingerPrintTrustManager(String fingerprint)
	{
		super();
		this.fingerprint = fingerprint;
	}
	
	protected FingerPrintTrustManager()
	{
		super();
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String)
	 */
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException
	{
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String)
	 */
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException
	{
		for (X509Certificate cert : chain)
		{
			MessageDigest md;
			try
			{
				md = MessageDigest.getInstance("SHA-1");
			}
			catch (NoSuchAlgorithmException e)
			{
				throw new CertificateException();
			}
	    	byte[] der = cert.getEncoded();
	    	md.update(der);
	    	byte[] digest = md.digest();
	    	if(!StringUtils.getHex(digest).equals(fingerprint))
	    		throw new CertificateException();
	    	else
	    		Log.i("fingerprint", "fingerprints match");
		}
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return null;
	}

}
