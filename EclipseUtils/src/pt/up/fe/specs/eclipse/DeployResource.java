/**
 * Copyright 2013 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.eclipse;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * @author Joao Bispo
 *
 */
public enum DeployResource implements ResourceProvider {

    DEPLOY_JAR_IN_JAR_TEMPLATE("deploy.xml.template"),
    DEPLOY_REPACK_TEMPLATE("deploy/deploy_repack.xml.template"),
    DEPLOY_SUBFOLDER_TEMPLATE("deploy/deploy_subfolder.xml.template"),
    IVY_RESOLVE_TEMPLATE("resolveIvy.xml.template"),
    SFTP_TEMPLATE("sftp.xml.template"),
    FTP_TEMPLATE("ftp.xml.template"),
    JAR_IN_JAR_LOADER("jar-in-jar-loader.zip");

    private final String resource;

    private DeployResource(String resource) {
        this.resource = resource;
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.Interfaces.ResourceProvider#getResource()
     */
    @Override
    public String getResource() {
        return resource;
    }

}
