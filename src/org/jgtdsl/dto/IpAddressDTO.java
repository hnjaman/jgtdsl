package org.jgtdsl.dto;

public class IpAddressDTO {
	private String xForward;
    private String via;
    private String remoteAddress;

    public String getxForward() {
        return xForward;
    }

    public void setxForward(String xForward) {
        this.xForward = xForward;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}
