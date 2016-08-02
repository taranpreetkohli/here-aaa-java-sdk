/*
 * Copyright 2016 HERE Global B.V.
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
package com.here.account.oauth2;


import org.junit.Before;


public abstract class AbstractCredentialTezt {

    String url;
    String accessKeyId;
    String accessKeySecret;
    
    @Before
    public void setUp() throws Exception {
        url = System.getProperty("urlStart") + "/oauth2/token";
        accessKeyId = System.getProperty("clientId");
        accessKeySecret = System.getProperty("clientSecret");
    }

}
