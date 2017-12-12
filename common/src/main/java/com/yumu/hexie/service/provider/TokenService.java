package com.yumu.hexie.service.provider;

import com.yumu.hexie.integration.provider.ilohas.entity.ProviderLoginer;

public interface TokenService {

	String getToken(ProviderLoginer loginer);
}
	