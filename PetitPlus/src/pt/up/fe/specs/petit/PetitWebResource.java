package pt.up.fe.specs.petit;

import pt.up.fe.specs.util.providers.WebResourceProvider;

public interface PetitWebResource {
    static WebResourceProvider create(String resourceUrl, String version) {
        return WebResourceProvider.newInstance("https://specs.fe.up.pt/resources/clava_api/", resourceUrl, version);
    }

    static WebResourceProvider create(String resourceUrl) {
        return WebResourceProvider.newInstance("https://specs.fe.up.pt/resources/clava_api/", resourceUrl);
    }

    WebResourceProvider PETIT_UBUNTU = create("linux_ubuntu_14/petit");
    WebResourceProvider PETIT_CENTOS6 = create("centos6/petit");

}
