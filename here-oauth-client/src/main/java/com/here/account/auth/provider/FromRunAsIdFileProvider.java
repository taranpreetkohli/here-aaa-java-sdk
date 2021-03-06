/*
 * Copyright (c) 2018 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.here.account.auth.provider;

import com.here.account.http.HttpConstants.HttpMethods;
import com.here.account.http.HttpProvider;
import com.here.account.http.HttpProvider.HttpRequest;
import com.here.account.http.HttpProvider.HttpRequestAuthorizer;
import com.here.account.identity.bo.IdentityTokenRequest;
import com.here.account.oauth2.AccessTokenRequest;
import com.here.account.oauth2.ClientAuthorizationRequestProvider;
import com.here.account.util.Clock;
import com.here.account.util.SettableSystemClock;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Gets authorization Access Tokens from an identity access token file.
 * A mechanism for runtimes using Run As Id to receive access tokens.
 *
 * @author kmccrack
 */
public class FromRunAsIdFileProvider
        extends AbstractClientAuthorizationRequestProvider
        implements ClientAuthorizationRequestProvider {

    /**
     * The HERE Access Token URL.
     */
    private static final String FILE_ACCESS_TOKEN_ENDPOINT_URL = 
            "file:///dev/shm/identity/access-token";

    private final String tokenUrl;

    public FromRunAsIdFileProvider() {
        this(new SettableSystemClock());
    }

    public FromRunAsIdFileProvider(Clock clock) {
        this(clock, FILE_ACCESS_TOKEN_ENDPOINT_URL);
    }
    
    public FromRunAsIdFileProvider(Clock clock, String tokenUrl) {
        super(clock);
        this.tokenUrl = tokenUrl;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTokenEndpointUrl() {
        return tokenUrl;
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HttpRequestAuthorizer getClientAuthorizer() {
        return getAuthorizer();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AccessTokenRequest getNewAccessTokenRequest() {
        return getRequest();
    }

    protected void verifyFileIsReadable() {
        File file;
        try {
            URL url = new URL(getTokenEndpointUrl());
            file = Paths.get(url.toURI()).toFile();
        } catch (Exception e) {
            throw new RequestProviderException("Unable to get HERE Access Token; unable to convert url to file "
                    + tokenUrl
                   + ".  If you were trying to use runAsId, contact HERE support.");
        }
        if (!file.exists()) {
            throw new RequestProviderException("Unable to get HERE Access Token.  File "
                    + tokenUrl
                    + " does not exist.  If you were trying to use runAsId, contact HERE support.");
        }
        if (!file.canRead()) {
            throw new RequestProviderException("Unable to get HERE Access Token.  File "
                    + tokenUrl
                    + " is not readable.  If you were trying to use runAsId, contact HERE support.");
        }
    }

    protected HttpProvider.HttpRequestAuthorizer getAuthorizer() {
        verifyFileIsReadable();

        return ((HttpRequest httpRequest, String method, String url,
                Map<String, List<String>> formParams) -> {});
    }
    
    protected IdentityTokenRequest getRequest() {
        // scope for pipeline access tokens will come from the file
        return new IdentityTokenRequest();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public HttpMethods getHttpMethod() {
        return HttpMethods.GET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getScope() {
        return null;
    }
}
